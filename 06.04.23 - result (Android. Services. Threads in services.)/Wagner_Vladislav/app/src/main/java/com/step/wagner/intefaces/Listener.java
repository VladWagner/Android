package com.step.wagner.intefaces;

//Интерфейс для обработки клика на positive button диалоге.
public interface Listener<T> {

    void onOkClickListener(T...params);

}
