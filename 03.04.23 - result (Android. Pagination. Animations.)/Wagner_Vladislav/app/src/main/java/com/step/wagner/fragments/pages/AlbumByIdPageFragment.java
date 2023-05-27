package com.step.wagner.fragments.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.step.wagner.R;
import com.step.wagner.controllers.WebRequestsController;
import com.step.wagner.fragments.RequestsFragment;

public class AlbumByIdPageFragment extends Fragment {

    //Фрагмент для вывода таблиц
    private RequestsFragment requestsFragment;

    private View parentView;

    //Контроллер для выполнения запросов
    WebRequestsController controller;

    public AlbumByIdPageFragment() {
    }


    //Фабричный метод
    public static AlbumByIdPageFragment newInstance(){

        return new AlbumByIdPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album_by_id_page, container, false);

        //Фрагмент для вывода результата запроса
        requestsFragment = (RequestsFragment) getChildFragmentManager().findFragmentById(R.id.resultFragment);

        //Ссылка на view активности, в которой находится данный фрагмент
        parentView = getActivity().findViewById(android.R.id.content);

        (view.findViewById(R.id.btnSetAlbumId)).setOnClickListener(v -> setParameters());


        controller = new WebRequestsController();

        //controller.getAlbumById(requestsFragment.getRecyclerView(),requestsFragment.getTextView(),parentView,getChildFragmentManager(),requestsFragment.getContext());
        requestsFragment.setTxvValue("Получение информации об альбоме по id");

        return view;
    }

    private void setParameters(){

        //Запск диалога + заапрос
        controller.getAlbumById(requestsFragment.getRecyclerView(),
                requestsFragment.getTextView(),
                parentView,
                getChildFragmentManager(),
                requestsFragment.getContext()
        );
    }
}