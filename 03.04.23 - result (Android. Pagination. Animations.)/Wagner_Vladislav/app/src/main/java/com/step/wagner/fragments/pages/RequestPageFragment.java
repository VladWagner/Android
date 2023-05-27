package com.step.wagner.fragments.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.step.wagner.R;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.controllers.WebRequestsController;
import com.step.wagner.fragments.RequestsFragment;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.Receiver;
import com.step.wagner.models.Album;

import java.util.List;

public class RequestPageFragment extends Fragment {

    //Номер страницы
    private int pageIndex;

    //Фрагмент для вывода таблиц
    private RequestsFragment requestsFragment;

    public RequestPageFragment() {
    }


    //Фабричный метод
    public static RequestPageFragment newInstance(int pageIndex) {
        RequestPageFragment requestPageFragment = new RequestPageFragment();

        //Передать индекс страницы
        Bundle params = new Bundle();
        params.putInt("index", pageIndex);

        requestPageFragment.setArguments(params);

        return requestPageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        pageIndex = bundle != null ? bundle.getInt("index") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_page, container, false);

        requestsFragment = (RequestsFragment) getChildFragmentManager().findFragmentById(R.id.resultFragment);

        View parentsView = getActivity().findViewById(android.R.id.content);

        //Создать контроллер
        WebRequestsController controller = new WebRequestsController();

        CommonTask task = new CommonTask();

        //Выполнить запрос и задать коллекцию во фрагмент вывода
        switch (pageIndex) {
            case 0:

                task.execute(() -> {
                    List<Album> albums;

                    //Выполнение синхронного запроса на поучение коллекции
                    try {

                        albums = controller.getAllAlbums();

                    } catch (Exception e) {

                        //Вывести сообщение в snackBar в onPostExecute
                        return () -> {
                            Utils.showSnackBar(view, "Получить коллекцию альбомов не удалось!");
                            return null;
                        };

                    }//try-catch

                    return () -> {
                        requestsFragment.setCollection(albums, 1);
                        return null;
                    };
                });//execute

                break;
            case 2:
                requestsFragment.setTxvValue("Запрос в разработке!");
                break;
            case 3:
                requestsFragment.setTxvValue("Запрос в разработке!");
                break;
        }

        return view;
    }
}