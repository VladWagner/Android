package com.step.home_work.intefaces;

import java.util.List;

public interface FragmentReceiver {

    void queries(int queryNumber);

    void tables(String tableName);

    <T> void addInTable(T entity);

    <T> void setCollection(List<T> entities, Class<T> entityType);

}
