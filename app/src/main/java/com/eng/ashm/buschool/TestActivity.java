package com.eng.ashm.buschool;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.TestActivityBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TestActivity extends AppCompatActivity {
    MainRepository mainRepository = null;
    TestActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestActivityBinding.inflate(getLayoutInflater());
        mainRepository = MainRepository.getInstance(this);
        setContentView(binding.getRoot());
        binding.testData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mainRepository.mainSearchListObservable.addObserver(observer);
            }
        });
    }

    Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {

            List<? extends IDataModel> list = (List<? extends IDataModel>)arg;
            if ((list != null) || (!list.isEmpty()))
            {
                if (list.size() == 0){
                    Toast.makeText(TestActivity.this, "empty result", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(IDataModel dataModel : list){
                    Toast.makeText(TestActivity.this, "name: "+((Student)dataModel).name, Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(TestActivity.this, "empty", Toast.LENGTH_SHORT).show();

        }
    };
    /**
     *
     * @return
     */
    private Car createTestCar(){
        Car car = new Car();
        car.carNum = 1000 + "";
        car.model = "اكس 6";
        car.kind = "بى ام دبليو";
        car.color = "ازرق";
        car.noOfSeats = 30;
        return car;
    }
    /**
     *
     * @return
     */
    private Driver createTestDriver(){
        Driver driver = new Driver();
        driver.password = "1234";
        driver.username = "ahmed";
        driver.address = "address";
        driver.email = "driver_email@gmail.com";
        driver.phone = "013254";
        driver.age = 25;
        driver.name = "ahmed";
        return driver;
    }
    /**
     *
     * @return
     */
    private Parent createTestParent(){
        Parent parent = new Parent();
        parent.name = " mohamed";
        parent.phone = "05235";
        parent.email = "parent_email@gmail.com";
        parent.job = "engineer";
        parent.relation = "father";
        parent.password = "54545";
        parent.username = "dad_username";
        parent.age  = 35;
        parent.address = "address of parent";
       // parent.relatedStudents = new Student[]{createTestStudent()};
        return parent;
    }
    /**
     *
     * @return
     */
    private Student createTestStudent(){
        Student student = new Student();
        student.yearOFStudy = 5;
        student.phone = "055685";
        student.name = "khaled";
        student.parentName = "anas";
        student.address = "suadia";
        student.age = 15;
        student.email = "student_email@gmail.com";
        return student;
    }
    /**
     *
     * @return
     */
    private Trip createTestTrip() {
        Trip trip = new Trip();
        trip.tripNum = 12;
        trip.bus = createTestCar();
        trip.tripDriver = createTestDriver();
        trip.tripState = "finished";
        ArrayList<Student> students = new ArrayList<>();
        students.add(createTestStudent());
        trip.tripStudents = students;
        return trip;
    }
}
