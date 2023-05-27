package org.itstep.pd011.retrofitwebdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// работаем с Jetpack, обработка DET-запросв от WEB-сервера
// прием организации слушателей событий на уровне активности
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView postsList;
    private TextView txvUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvUrl = findViewById(R.id.txvUrl);
        txvUrl.setText(Api.BASE_URL);

        postsList = findViewById(R.id.lsvPosts);

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        Api api = retrofit.create(Api.class);

        Call<List<Post>> call = api.getPosts();

        call.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                List<Post> posts = response.body();
                String[] postTitles = new String[posts.size()];
                int i = 0;

                for (Post post : posts) {
                    postTitles[i] =  post.getTitle();
                    i++;
                } // for post

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.item,
                    postTitles);
                postsList.setAdapter(adapter);
                postsList.setOnItemClickListener(MainActivity.this);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getClass().toString() + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    // Реализация одного из традиционных приемов программирвоанания под Android:
    // слушатель (обработчик) события на уровне активности (методом активности)
    // в обработчике клиика по элементу списка получаем строку из
    // View в разметке элемента по которому был клик
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Snackbar.make(view,  ((TextView) view).getText(),  Snackbar.LENGTH_INDEFINITE)
            .setAction("OK", v -> {})
            .show();
    } // onItemClick

    //region Работа с меню приложения
    @Override  // создание меню приложения
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu


    @Override // обработка выбора в меню приложения
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mniExit) {
            finish();
            return true;
        } // if
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
}
