package org.itstep.pd011.jsonparserexample.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.itstep.pd011.jsonparserexample.R;
import org.itstep.pd011.jsonparserexample.models.Product;
import org.itstep.pd011.jsonparserexample.models.Store;
import org.itstep.pd011.jsonparserexample.utils.JsonHelper;

import java.util.List;

// пример работы с внешними библиотеками, на примере парсинга JSON
public class MainActivity extends AppCompatActivity {

    // данные для сохранения/загрузки
    private Store store;

    // поля ввода для добавляемого в коллекцию товара
    private EditText edtName;
    private EditText edtPrice;
    private EditText edtAmount;

    // элемент отображения списка товаров и его адаптер
    ListView lsvGoodsList;
    ArrayAdapter<Product> productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создали коллекцию товаров для обработки, при создании
        // выполняется заполнение коллекции начальными значениями
        store = new Store();

        // получить ссылки на элементы управления
        // командные кнопки - назначаем обработчика клика
        findViewById(R.id.btnAddGoods).setOnClickListener(v -> addGoods());
        findViewById(R.id.btnSave).setOnClickListener(v -> saveStore());
        findViewById(R.id.btnLoad).setOnClickListener(v -> loadStore());
        findViewById(R.id.btnClear).setOnClickListener(v -> clearStore());

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtAmount = findViewById(R.id.edtAmount);

        // получить ссылку на элемент отображения списка товаров, адаптер этого списка
        lsvGoodsList = findViewById(R.id.lsvGoodsList);
        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                store.getProductList());
        lsvGoodsList.setAdapter(productAdapter);
    } // onCreate


    // добавление товара в коллекцию
    private void addGoods() {
        // создать товар на основании полей ввода
        Product product = new Product(
                edtName.getText().toString(),
                Integer.parseInt(edtPrice.getText().toString()),
                Integer.parseInt(edtAmount.getText().toString())
        );

        // добавить товар в коллекцию, уведомить адаптер для корректного
        // отображения коллекции
        store.getProductList().add(product);
        productAdapter.notifyDataSetChanged();
    } // addGoods

    // сохранить коллекцию в JSON-файл
    private void saveStore() {
        boolean result = JsonHelper.exportToJSON(this, store.getProductList());
        String str = result?"Данные сохранены":"Не удалось сохранить данные";
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    } // saveStore

    // загрузить коллекцию из JSON-файла
    // TODO: обновлять коллекцию без создания адаптера
    private void loadStore() {
        // чтение коллекции из JSON-файла
        List<Product> list = JsonHelper.importFromJSON(this);
        String str = "";

        if(list != null){
            store.setProductList(list);
            productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, store.getProductList());
            lsvGoodsList.setAdapter(productAdapter);
            str = "Данные восстановлены";
        } else {
            str = "Не удалось восстановить данные";
        } //if

        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    } // loadStore

    // очистка коллекции
    private void clearStore() {
        // очистить коллекцию и уведомить адаптер
        store.getProductList().clear();
        productAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Коллекция очищена", Toast.LENGTH_LONG).show();
    } // clearStore

    //region Меню приложения
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            // выход из приложения
            case R.id.mniExit:
                finish();
                break;

            // вызов активности, демонстрирующей работу c JSON, ListView с адаптером
            case R.id.mniGoodsViewActivity:
                // примитивный вызов активности :(
                startActivity(new Intent(this, ProductActivity.class));
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion

} // class MainActivity