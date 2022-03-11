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
        // binding listeners
        binding.searchListRv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(binding.searchListRv.getContext(), LinearLayoutManager.VERTICAL);
        binding.searchListRv.addItemDecoration(decoration);
        binding.cancelAddTrip.setOnClickListener(v->finish());
        binding.searchview.requestFocus();
        SearchManager searchManager = getSystemService(SearchManager.class);
        binding.searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        binding.addToTrip.setOnClickListener(v->{
            Intent intent = getSelectedData();
            if (intent != null)
            setResult(RESULT_OK, intent);
            else
                setResult(RESULT_CANCELED);
            finish();
        });

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
    private Intent getSelectedData(){
        List<SearchDataModel> selectedList = searchAdapter.getUpdatedSearchList();
        Intent intent = new Intent();
        ArrayList<Parcelable> resultArrayList = new ArrayList<>();
        if (requestCode == DRIVER_REQUEST_CODE){
            for (int index = 0 ;index < selectedList.size() ;index++){
                if (selectedList.get(index).isSelected)
                    tripDriver = (Driver) searchList.get(index);
                break ;
            }
            resultArrayList.add(tripDriver);
            intent.putParcelableArrayListExtra(SEARCH_RESULT_KEY, resultArrayList);
        }
        else if (requestCode == STUDENT_REQUEST_CODE){
            studentList = new ArrayList<>();
           for(int index = 0 ; index < selectedList.size();index++) {
               if (selectedList.get(index).isSelected)
                   studentList.add((Student) searchList.get(index));
           }
           intent.putParcelableArrayListExtra(SEARCH_RESULT_KEY, studentList);
        }
        else if (requestCode == CAR_REQUEST_CODE){
            for (int index = 0 ;index < selectedList.size() ;index++){
                if (selectedList.get(index).isSelected)
                    tripBus = (Car) searchList.get(index);
                break ;
            }
            resultArrayList.add(tripBus);
            if (tripBus != null)
                Toast.makeText(this, "تم اضافة حافلة رقم : "+ tripBus.carNum, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "نتايج بحث 0", Toast.LENGTH_SHORT).show();

            intent.putParcelableArrayListExtra(SEARCH_RESULT_KEY,resultArrayList);
        }
        else {
            return null;
        }
        return intent;
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
            ArrayList<SearchDataModel> list = new ArrayList<>();
            SearchDataModel dataModel = new SearchDataModel();
            for (Driver driver : drivers) {
                dataModel.firstField = driver.name;
                dataModel.secondField = driver.phone;
                list.add(dataModel);
            }
            searchAdapter.updateSearchList(list);
        }
    };
    Observer<List<Student>> studentListObserver = students -> {
        if (students != null || !students.isEmpty()) {
            ArrayList<SearchDataModel> list = new ArrayList<>();
            SearchDataModel dataModel = new SearchDataModel();
            for (Student student : students) {
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
        ArrayList<SearchDataModel> list = new ArrayList<>();
        SearchDataModel dataModel = new SearchDataModel();
        for (Car car : buses) {
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
