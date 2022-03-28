package com.eng.ashm.buschool.ui.activity.management;

import static com.eng.ashm.buschool.ui.activity.management.CreateTripActivity.CAR_REQUEST;
import static com.eng.ashm.buschool.ui.activity.management.CreateTripActivity.DRIVER_REQUEST;
import static com.eng.ashm.buschool.ui.activity.management.CreateTripActivity.SEARCH_REQUEST_KEY;
import static com.eng.ashm.buschool.ui.activity.management.CreateTripActivity.SEARCH_RESULT_KEY;
import static com.eng.ashm.buschool.ui.activity.management.CreateTripActivity.STUDENT_REQUEST;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.SearchDataModel;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.databinding.SearchActivityBinding;
import com.eng.ashm.buschool.ui.adapter.SearchAdapter;
import com.eng.ashm.buschool.ui.viewmodel.CarViewModel;
import com.eng.ashm.buschool.ui.viewmodel.DriverViewModel;
import com.eng.ashm.buschool.ui.viewmodel.StudentViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    //search requests
    public final static int DRIVER_REQUEST_CODE = 1001;
    public final static int STUDENT_REQUEST_CODE = 1002;
    public final static int CAR_REQUEST_CODE = 1003;
    //fields
    DriverViewModel driverViewModel = null;
    StudentViewModel studentViewModel = null;
    CarViewModel carViewModel = null;
    SearchActivityBinding binding = null;
    SearchAdapter searchAdapter = null;
    private int requestCode = -1;
    // search result lists
    private List<? extends IDataModel> searchList = null;
    private Driver tripDriver = null;
    private Car tripBus = null;
    private ArrayList<Student> studentList = null;
    private boolean observerFinished = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binding
        binding = SearchActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        //handle search intent
        Intent intent = getIntent();
        setIntent(intent);
        handleIntent(intent);
        displayList();
    }
    private void handleIntent(Intent intent){
        String request = intent.getStringExtra(SEARCH_REQUEST_KEY);
        if (request == null)
            return ;
        switch(request){
            case DRIVER_REQUEST:
                requestCode = DRIVER_REQUEST_CODE;
                break;
            case STUDENT_REQUEST:
                requestCode = STUDENT_REQUEST_CODE;
                break;
            case CAR_REQUEST:
                requestCode = CAR_REQUEST_CODE;
                break;
            default:
                requestCode = -1;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleSearchIntent(intent);
    }
    private void initView(){
        // buttons
        binding.cancelAddTrip.setOnClickListener(v->finish());
        binding.addToTrip.setOnClickListener(v->{
            ArrayList<? extends IDataModel> resultList = getSelectedData();
            if (requestCode == DRIVER_REQUEST_CODE){
                if (resultList.size()>1)
                {
                    Toast.makeText(this, "يجب اختيار سائق واحد فقط", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if (requestCode == CAR_REQUEST_CODE){
                if (resultList.size()>1)
                {
                    Toast.makeText(this, "يجب اختيار حافلة واحدة فقط", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Intent intent = new Intent();
            intent.putExtra(SEARCH_RESULT_KEY, resultList);
            setResult(RESULT_OK, intent);
            finish();
        });

        // searchView
        SearchManager searchManager = getSystemService(SearchManager.class);
        binding.searchListRv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(binding.searchListRv.getContext(), LinearLayoutManager.VERTICAL);
        binding.searchListRv.addItemDecoration(decoration);
        binding.searchview.requestFocus();
        binding.searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // adapters
        searchAdapter = new SearchAdapter();
        binding.searchListRv.setAdapter(searchAdapter);
        // view model
        driverViewModel = new ViewModelProvider(this).get(DriverViewModel.class);
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);

       setSupportActionBar( binding.searchToolbar);
       getSupportActionBar().setTitle("ابحث");
    }
    /**
     *
     */
    private ArrayList<? extends IDataModel> getSelectedData(){
        List<SearchDataModel> selectedList = searchAdapter.getUpdatedSearchList();

        if (requestCode == DRIVER_REQUEST_CODE){
            ArrayList<Driver> resultArrayList = new ArrayList<>();
            for (int index = 0 ;index < selectedList.size() ;index++){
                if (selectedList.get(index).isSelected)
                    resultArrayList.add((Driver) searchList.get(index));
            }
            return resultArrayList;
        }
        else if (requestCode == STUDENT_REQUEST_CODE){
            ArrayList<Student> resultArrayList = new ArrayList<>();
           for(int index = 0 ; index < selectedList.size();index++) {
               if (selectedList.get(index).isSelected)
                   resultArrayList.add((Student) searchList.get(index));
           }
            return resultArrayList;
        }
        else if (requestCode == CAR_REQUEST_CODE){
            ArrayList<Car> resultArrayList = new ArrayList<>();
            for (int index = 0 ;index < selectedList.size() ;index++){
                if (selectedList.get(index).isSelected)
                    resultArrayList.add((Car)searchList.get(index));
            }
            return resultArrayList;
        }
        return null;
    }

    /**
     *
     * @param intent
     */
    private void handleSearchIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (requestCode == DRIVER_REQUEST_CODE){
                driverViewModel.searchDriverByName(query);
                driverViewModel.searchDriverResult.observe(this,driverSearchObserver);
            }
            else if (requestCode == STUDENT_REQUEST_CODE){
                studentViewModel.searchStudentByName(query);
                studentViewModel.searchStudentResult.observe(this, studentSearchObserver);
            }
            else if (requestCode == CAR_REQUEST_CODE) {
                carViewModel.searchCar(query);
                carViewModel.searchCarResult.observe(this, carSearchObserver);
            }
            else {
                Toast.makeText(this, "خطأ فى تحديد القسم الذى تبحث عنه", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     *
     */
    private void displayList() {
        if (requestCode == DRIVER_REQUEST_CODE) {
            driverViewModel.getAllDrivers();
            // display result
            driverViewModel.requestDriverListResult.observe(this,driverListObserver);
        } else if (requestCode == STUDENT_REQUEST_CODE) {
            studentViewModel.getAllStudents();
            studentViewModel.requestStudentListResult.observe(this, studentListObserver);
        } else if (requestCode == CAR_REQUEST_CODE) {
            carViewModel.getAllCars();
            carViewModel.requestCarListResult.observe(this,carListObserver);
        } else {
            Toast.makeText(this, "خطأ فى تحديد القسم الذى تبحث عنه", Toast.LENGTH_SHORT).show();
        }
    }

    // search observers
    Observer<List<Driver>> driverListObserver = drivers -> {
        if (drivers == null || drivers.isEmpty())
            Toast.makeText(SearchActivity.this, "لا يوجد سائقين", Toast.LENGTH_SHORT).show();
        else {
            searchList = drivers;
            ArrayList<SearchDataModel> list = new ArrayList<>();

            for (Driver driver : drivers) {
                SearchDataModel dataModel = new SearchDataModel();
                dataModel.firstField = driver.name;
                dataModel.secondField = driver.phone;
                list.add(dataModel);
            }
            searchAdapter.updateSearchList(list);
        }
    };
    Observer<List<Student>> studentListObserver = students -> {
        if (students != null || !students.isEmpty()) {
            searchList = students;
            ArrayList<SearchDataModel> list = new ArrayList<>();
           // SearchDataModel dataModel = new SearchDataModel();
            for (Student student : students) {
                SearchDataModel dataModel = new SearchDataModel();
                dataModel.firstField = student.name;
                dataModel.secondField = student.yearOFStudy + "";
                list.add(dataModel);
            }
            searchAdapter.updateSearchList(list);
        } else {
            Toast.makeText(SearchActivity.this, "لا يوجد طلبة", Toast.LENGTH_SHORT).show();
        }
    };
    Observer<List<Car>> carListObserver =  buses -> {
        if (buses == null || buses.isEmpty()) {
            Toast.makeText(SearchActivity.this, "لا توجد باصات", Toast.LENGTH_SHORT).show();
            return;
        }
        searchList = buses;
        ArrayList<SearchDataModel> list = new ArrayList<>();
        //SearchDataModel dataModel = new SearchDataModel();
        for (Car car : buses) {
            SearchDataModel dataModel = new SearchDataModel();
            dataModel.firstField = car.carNum;
            dataModel.secondField = car.color + "";
            list.add(dataModel);
        }
        searchAdapter.updateSearchList(list);
    };
    // search list observers
    Observer<List<Driver>> driverSearchObserver = drivers -> {
        if(drivers == null || drivers.isEmpty())
            Toast.makeText(SearchActivity.this, "لا يوجد سائقين", Toast.LENGTH_SHORT).show();
        else{
            searchList =  drivers;
            ArrayList<SearchDataModel> list = new ArrayList<>();
            SearchDataModel dataModel = new SearchDataModel();
            for(Driver driver: drivers){
                dataModel.firstField = driver.name;
                dataModel.secondField = driver.phone;
                list.add(dataModel);
            }
            searchAdapter.updateSearchList(list);
        }
    };

    Observer<List<Student>> studentSearchObserver = students -> {

        if (students != null || !students.isEmpty()){
            searchList = students;
            ArrayList<SearchDataModel> list = new ArrayList<>();
            SearchDataModel dataModel = new SearchDataModel();
            for (Student student : students) {
                dataModel.firstField = student.name;
                dataModel.secondField = student.yearOFStudy + "";
                list.add(dataModel);
            }
            searchAdapter.updateSearchList(list);
        }
        else {
            Toast.makeText(SearchActivity.this, "لا يوجد طلبة", Toast.LENGTH_SHORT).show();
        }
    };
    Observer<List<Car>> carSearchObserver = buses -> {
        if(buses == null || buses.isEmpty()){
            Toast.makeText(SearchActivity.this, "لا توجد باصات", Toast.LENGTH_SHORT).show();
            return ;
        }
        searchList = buses;
        ArrayList<SearchDataModel> list = new ArrayList<>();
        SearchDataModel dataModel = new SearchDataModel();
        for(Car car: buses){
            dataModel.firstField = car.carNum;
            dataModel.secondField = car.color + "";
            list.add(dataModel);
        }
        searchAdapter.updateSearchList(list);
    };
    /**
     *
     */
    private void cleanUp(){

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
