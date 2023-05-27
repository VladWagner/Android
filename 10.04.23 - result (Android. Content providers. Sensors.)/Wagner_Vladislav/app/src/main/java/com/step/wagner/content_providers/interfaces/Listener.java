package com.step.wagner.content_providers.interfaces;

//Интерфейс для обработки клика на positive button диалоге.
public interface Listener<T> {

    void onOkClickListener(T...params);

}
