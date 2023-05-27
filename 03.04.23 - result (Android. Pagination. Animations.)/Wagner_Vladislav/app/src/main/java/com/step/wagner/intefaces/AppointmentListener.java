package com.step.wagner.intefaces;

import java.util.Date;

public interface AppointmentListener {

    void onOkClickListener(int id, Date appointmentDate, int doctorId, int patientId);

}
