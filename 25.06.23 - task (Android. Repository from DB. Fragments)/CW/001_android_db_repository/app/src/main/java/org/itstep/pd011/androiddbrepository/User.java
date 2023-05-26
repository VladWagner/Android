package org.itstep.pd011.androiddbrepository;

// Модель данных - термин для обрабатываемого типа данных
// DAO - Data Access Object - класс, отображающий структуру таблицы
// ORM - Object Relation Mapping --> Hibernate --> Spring Data
// DTO - Data Transfer Object - класс для передачи из таблицы в модель
public class User {

    private long id;       // ид
    private String name;   // имя
    private int year;      // год регистрации или год рождения

    public User(long id, String name, int year){
        this.id = id;
        this.name = name;
        this.year = year;
    } // User

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("%s : %d", name, year);
    } // toString
} // class User