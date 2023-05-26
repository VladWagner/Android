package org.itstep.vpu911.interactionbetweenfragments;

// Фрагменты не могут напрямую взаимодействовать между собой.
// Для этого надо обращаться к контексту, в качестве которого
// выступает класс Activity. Для обращения к activity, как правило,
// создается вложенный интерфейс
interface OnSendDataListener {
    void onSendData(String data);
} // OnSendDataListener
