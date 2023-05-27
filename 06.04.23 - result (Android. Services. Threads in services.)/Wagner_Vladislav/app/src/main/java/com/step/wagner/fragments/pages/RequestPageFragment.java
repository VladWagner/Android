package com.step.wagner.fragments.pages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.step.wagner.MainActivity;
import com.step.wagner.R;
import com.step.wagner.adapters.AlbumAdapter;
import com.step.wagner.adapters.CommentAdapter;
import com.step.wagner.adapters.PostAdapter;
import com.step.wagner.adapters.UserAdapter;
import com.step.wagner.fragments.ContainerFragment;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.models.Album;
import com.step.wagner.models.Comment;
import com.step.wagner.models.Post;
import com.step.wagner.models.user.User;
import com.step.wagner.services.RequestsService;

import java.util.List;

public class RequestPageFragment extends Fragment {

    //Номер страницы
    private int pageIndex;

    //Фрагмент для вывода таблиц
    private ContainerFragment containerFragment;

    private BroadcastReceiver broadcastReceiver;

    private MainActivity activity;

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

        containerFragment = (ContainerFragment) getChildFragmentManager().findFragmentById(R.id.resultFragment);

        activity = (MainActivity) getActivity();
        View parentsView = activity.findViewById(android.R.id.content);

        //Создать объект получателя
        broadcastReceiver = createReceiver();

        //Зарегистрировать получателя
        //activity.registerReceiver(broadcastReceiver, new IntentFilter(Parameters.ACTIONS[pageIndex]));

        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, new IntentFilter(Parameters.ACTIONS[pageIndex]));

        //Запуск сервиса для запроса
        Intent intent = new Intent(activity.getApplicationContext(), RequestsService.class);

        //Задать номер запроса
        intent.putExtra(Parameters.REQUEST_NUMBER_PARAM, pageIndex + 1);

        activity.startService(intent);

        return view;
    }


    //Снять регистрацию получателя
    @Override
    public void onDestroy() {
        super.onDestroy();

        //activity.unregisterReceiver(broadcastReceiver);

        LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcastReceiver);
    }

    //Создать получателя
    private BroadcastReceiver createReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int requestNumber = intent.getIntExtra(Parameters.REQUEST_NUMBER_PARAM, 1);

                switch (requestNumber) {

                    //Альбомы
                    case 1:

                        List<Album> albums = intent.getParcelableArrayListExtra(Album.class.getCanonicalName());

                        containerFragment.getRecyclerView().setAdapter(new AlbumAdapter(activity.getApplicationContext(), albums,getParentFragmentManager()));
                        containerFragment.getTextView().setText("Коллекция альбомов");

                        break;

                    //Комментарии
                    case 2:

                        List<Comment> comments = intent.getParcelableArrayListExtra(Comment.class.getCanonicalName());

                        containerFragment.getRecyclerView().setAdapter(new CommentAdapter(activity.getApplicationContext(), comments));
                        containerFragment.getTextView().setText("Коллекция комментариев");

                        break;
                    case 3:

                        List<Post> posts = intent.getParcelableArrayListExtra(Post.class.getCanonicalName());

                        containerFragment.getRecyclerView().setAdapter(new PostAdapter(activity.getApplicationContext(), posts));
                        containerFragment.getTextView().setText("Коллекция постов");

                        break;
                    case 4:
                        //List<User> users = intent.getParcelableArrayListExtra(User.class.getCanonicalName());
                        List<User> users = intent.getParcelableArrayListExtra(User.class.getCanonicalName());

                        containerFragment.getRecyclerView().setAdapter(new UserAdapter(activity.getApplicationContext(), users));
                        containerFragment.getTextView().setText("Коллекция пользователей");
                        break;
                }

            }
        };//BroadcastReceiver
    }
}