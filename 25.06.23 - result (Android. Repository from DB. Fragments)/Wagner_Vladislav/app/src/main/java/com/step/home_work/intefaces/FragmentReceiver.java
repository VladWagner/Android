package com.step.home_work.intefaces;

import com.step.home_work.models.entities.Appointment;
import com.step.home_work.models.entities.Doctor;
import com.step.home_work.models.entities.Patient;
import com.step.home_work.models.queries.Query5;
import com.step.home_work.models.queries.Query6;
import com.step.home_work.models.queries.Query7;

import java.util.List;

public interface FragmentReceiver {

    void query1(List<Patient> patients,String title);

    void query2(List<Doctor> doctors,String title);

    void query3(List<Appointment> appointments,String title);

    void query4(List<Doctor> doctors,String title);

    void query5(List<Query5> query5,String title);

    void query6(List<Query6> query6,String title);

    void query7(List<Query7> query7,String title);

}
