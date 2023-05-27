package org.itstep.pd011.androiddialogpart4;

// Интерфейс - для передачи данных из диалога в активность
public interface Datable {
    // для удаления объекта типа String из активности
    void remove(String phone);

    // для добавления объектов типа String и Customer в активность
    void add(String phone, Customer customer);
} // interface Datable