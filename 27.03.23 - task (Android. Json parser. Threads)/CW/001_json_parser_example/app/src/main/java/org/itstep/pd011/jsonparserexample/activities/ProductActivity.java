package org.itstep.pd011.jsonparserexample.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.itstep.pd011.jsonparserexample.R;
import org.itstep.pd011.jsonparserexample.adapters.ProductAdapter;
import org.itstep.pd011.jsonparserexample.models.Product;
import org.itstep.pd011.jsonparserexample.models.Store;
import org.itstep.pd011.jsonparserexample.utils.JsonHelper;

import java.util.List;

public class ProductActivity extends AppCompatActivity {

    // данные для сохранения/загрузки
    private Store store;

    // поля ввода для добавляемого в коллекцию товара
    private EditText edtName;
    private EditText edtPrice;
    private EditText edtAmount;

    // элемент отображения списка товаров и его адаптер
    ListView lsvGoodsList;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // создали коллекцию товаров для обработки, при создании
        // выполняется заполнение коллекции начальными значениями
        store = new Store();

        // получить ссылки на элементы управления
        // командные кнопки - назначаем обработчика клика
        findViewById(R.id.btnAddGoods1).setOnClickListener(v -> addGoods());
        findViewById(R.id.btnSave1).setOnClickListener(v -> saveStore());
        findViewById(R.id.btnLoad1).setOnClickListener(v -> loadStore());
        findViewById(R.id.btnClear1).setOnClickListener(v -> clearStore());

        edtName = findViewById(R.id.edtName1);
        edtPrice = findViewById(R.id.edtPrice1);
        edtAmount = findViewById(R.id.edtAmount1);

        // получить ссылку на элемент отображения списка товаров, адаптер этого списка
        lsvGoodsList = findViewById(R.id.lsvGoodsList1);
        productAdapter = new ProductAdapter(this, R.layout.product_item, store.getProductList());
        lsvGoodsList.setAdapter(productAdapter);
    } // onCreate


    // добавление товара в коллекцию
    private void addGoods() {
        // создать товар на основании полей ввода
        Product goods = new Product(
                edtName.getText().toString(),
                Integer.parseInt(edtPrice.getText().toString()),
                Integer.parseInt(edtAmount.getText().toString())
        );

        // добавить товар в коллекцию, уведомить адаптер для корректного
        // отображения коллекции
        store.getProductList().add(goods);
        productAdapter.notifyDataSetChanged();
    } // addGoods

    // сохранить коллекцию в JSON-файл
    private void saveStore() {
        boolean result = JsonHelper.exportToJSON(this, store.getProductList());
        String str = result?"Данные сохранены":"Не удалось сохранить данные";
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    } // saveStore

    // загрузить коллекцию из JSON-файла
    private void loadStore() {
        // чтение коллекции из JSON-файла
        List<Product> list = JsonHelper.importFromJSON(this);
        String str = "";

        if(list != null){
            store.setProductList(list);
            productAdapter = new ProductAdapter(this, R.layout.product_item, store.getProductList());
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

    //region Меню активности
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mniActivityBack) {
            // возврат из активности
            finish();
        } // if

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class ProductActivity