package org.itstep.pd011.jsonparserexample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.itstep.pd011.jsonparserexample.R;
import org.itstep.pd011.jsonparserexample.models.Product;

import java.util.List;
import java.util.Locale;

// адаптер для вывода товара в ListView
public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;               // пример использования Context в адаптере
    private LayoutInflater layoutInflater; // загрузчик разметки
    private int layout;                      // ид разметки
    private List<Product> productList;         // ссылка на коллекцию данных

    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> productList) {
        super(context, resource, productList);

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.layout = resource;
        this.productList = productList;
    } // GoodsAdapter

    // Запись данных в элемент разметки
    // convertView - старое состояние элемента разметки
    // parent      - ссылка на ListView
    public View getView(int position, View convertView, ViewGroup parent) {
        /* Оптимазация. Использование ViewHolder */
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView, position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.position = position;
        } // if

        // получить ссылку на элемент коллекции
        Product product = productList.get(position);

        // вывести данные в элементы разметки view
        viewHolder.txvName.setText(product.getName());
        viewHolder.txvAmount.setText(String.format(Locale.UK, "%d шт.", product.getAmount()));
        viewHolder.txvPrice.setText(String.format(Locale.UK, "%d.00 руб.", product.getPrice()));

        // вернуть сформированное представление
        return convertView;
    } // getView

    // внутренний класс - для хранения ссылок на элементы разметки
    // исключаем повторные операции поиска элементов в разметке по id
    private class ViewHolder {
        final TextView txvName;
        final TextView txvAmount;
        final TextView txvPrice;

        private int position;

        public ViewHolder(View view, int position) {
            // получить элементы отображения
            txvName = view.findViewById(R.id.txvName);
            txvPrice = view.findViewById(R.id.txvPrice);
            txvAmount = view.findViewById(R.id.txvAmount);

            this.position = position;
        } // ViewHolder
    } // class ViewHolder

} // class ProductAdapter
