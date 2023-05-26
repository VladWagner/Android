package com.step.home_work.infrastructure;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.step.home_work.R;
import com.step.home_work.models.Animal;
import com.step.home_work.models.ship.CargoType;
import com.step.home_work.models.ship.Ship;
import com.step.home_work.models.ship.ShipType;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    //Генерация значений
    private static Random rand = new Random();

    //Формат вывода чисел
    public static DecimalFormat numbersFormatter = new DecimalFormat("###,###,###");

    //Формат вывода даты
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);

    public Utils() {
    }

    public static Integer tryParseInt(String strValue) {

        Integer parsed = null;
        try {
            parsed = Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            return null;
        }

        return parsed;
    }

    //Получение случайных значений
    public static double getRandom(double lo, double hi) {
        return lo + rand.nextDouble() * (hi - lo);
    }

    public static int getRandom(int lo, int hi) {
        return lo + rand.nextInt(hi - lo);
    }

    public static long getRandom(long lo, long hi) {
        return lo + rand.nextLong() * (hi - lo);
    }

    //ФИО владельцев
    private static final String[] owners = new String[]{
            "Юрковский П.С.",
            "Якубовская В.А.",
            "Шапиро В.П.",
            "Вожжаев В.Б.",
            "Хроменко Е.М.",
    };

    public static String getOwnerSnp() {
        return owners[getRandom(0, owners.length)];
    }


    //Породы животных
    public static final List<SimpleTuple<String, String>> breeds = new ArrayList<>(List.of(
            new SimpleTuple<>("мейн-кун", "maine coon.jpeg"),
            new SimpleTuple<>("скоттиш-фолд", "scottish fold.jpg"),
            new SimpleTuple<>("бенгальская", "bengal.jpg"),
            new SimpleTuple<>("абиссинская", "abyssinian.jpg")));

    public static SimpleTuple<String, String> getBreed() {
        return breeds.get(getRandom(0, breeds.size()));
    }

    //Клички
    private static final String[] animalsNames = new String[]{
            "Фрида",
            "Шерри",
            "Вальдо",
            "Данте",
            "Зефир",
            "Клеон",
    };

    public static String getAnimalName() {
        return animalsNames[getRandom(0, animalsNames.length)];
    }

    //Список животных
    public static List<Animal> generateAnimalsList(int count) {

        return Stream.generate(Animal::factory).limit(count).collect(Collectors.toList());
    }

    //Типы кораблей вместе с id
    public static final List<ShipType> shipTypes = new ArrayList<>(List.of(
            new ShipType("Балкер", Parameters.BULK_ID),
            new ShipType("Контейнеровоз", Parameters.CONTAINERS_CARRIER_ID)));

    public static ShipType getShipType() {
        return shipTypes.get(getRandom(0, shipTypes.size()));
    }

    //Названия кораблей
    private static final List<String> shipNames = Arrays.asList(
            "Essen",
            "Smart",
            "Glory",
            "Ever given",
            "Discovery",
            "Singan"
    );

    public static String getShipName() {
        return shipNames.get(getRandom(0, shipNames.size()));
    }

    //id типов кораблей
    public static Map<String, Integer> shipTypesId = new HashMap<>(Map.of(
            "Балкер",Parameters.BULK_ID,
            "Контейнеровоз",Parameters.CONTAINERS_CARRIER_ID
    ));

    public static int getShipTypeId(String shipType){

        int id;
        try {
            id = shipTypesId.entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(shipType))
                    .map(Map.Entry::getValue).findFirst()
                    .get();
        } catch (Exception e) {
            //throw new RuntimeException(e);
            return 0;
        }

        return id;
    }

    //Пункты назначений
    private static final List<String> destinations = Arrays.asList(
            "порт Шанхая",
            "порт Роттердама",
            "порт Пусан",
            "порт Генуя",
            "порт Балтимор"
    );

    public static String getDestination() {
        return destinations.get(getRandom(0, destinations.size()));
    }

    //Типы грузов для балкера
    private static final List<CargoType> cargoTypesBulk = new ArrayList<>(
            List.of(new CargoType("уголь", 110),
                    new CargoType("железная руда", 350),
                    new CargoType("нефтепродукты", 980),
                    new CargoType("зерно", 75))
    );

    public static CargoType getCargoTypeBulk() {
        return cargoTypesBulk.get(getRandom(0, cargoTypesBulk.size()));
    }

    //Типы грузов для контейнеровоза
    private static final List<CargoType> cargoTypesContainerCarrier = new ArrayList<>(
            List.of(new CargoType("каучук", 600),
                    new CargoType("шерсть", 1_000),
                    new CargoType("продукты питания", 2_000))
    );

    public static CargoType getCargoTypeContainerCarrier() {
        return cargoTypesContainerCarrier.get(getRandom(0, cargoTypesContainerCarrier.size()));
    }

    //Получить груз в зависимости от типа корабля
    public static CargoType getSpecificCargo(int shipType) {
        CargoType cargoType = null;

        switch (shipType) {
            case Parameters.BULK_ID:
                cargoType = getCargoTypeBulk();
                break;
            case Parameters.CONTAINERS_CARRIER_ID:
                cargoType = getCargoTypeContainerCarrier();
                break;
            default:
                cargoType = new CargoType("неизвестно", 1);
                break;
        }//switch

        return cargoType;
    }


    //Получить список грузов в зависимости от типа корабля
    public static List<String> getCargoList(int type) {
        switch (type) {

            //Балкер
            case Parameters.BULK_ID:
                return cargoTypesBulk.stream().map(c -> c.type).collect(Collectors.toList());

            //Контейнеровоз
            case Parameters.CONTAINERS_CARRIER_ID:
                return cargoTypesContainerCarrier.stream().map(c -> c.type).collect(Collectors.toList());
        }

        return cargoTypesBulk.stream().map(c -> c.type).collect(Collectors.toList());
    }

    //Список суден
    public static List<Ship> generateShipsList(int count) {


        /*return new ArrayList<>(List.of(
                Ship.factory(),
                Ship.factory(),
                Ship.factory(),
                Ship.factory(),
                Ship.factory(),
                Ship.factory()
        ));*/
        return Stream.generate(Ship::factory).limit(count).collect(Collectors.toList());
    }

    public static List<String> shipsStorage;

    //Записать изображение в imageView
    public static void setImageView(String fileName, ImageView imgView, Context appContext) throws Exception {


        Drawable drawable = null;
        //Окрыть поток чтения из assets
        try (InputStream is = appContext.getAssets().open(fileName)) {
            drawable = Drawable.createFromStream(is, null);
        } catch (IOException e) {

            //Попытка открыть стандартную картинку
            try (InputStream is = appContext.getAssets().open("none.jpg")) {
                drawable = Drawable.createFromStream(is, null);
            } catch (Exception ex) {

                throw new Exception(ex);
            }
        }//try catch


        imgView.setImageDrawable(drawable);
    }

    //Заполнить dropDown
    public static void setSpinner(Context context, Spinner spinner, List<String> collection, int itemLayoutId, int dropDownLayoutId) {

        //Создать адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                itemLayoutId,
                collection
        );

        adapter.setDropDownViewResource(dropDownLayoutId);
        spinner.setAdapter(adapter);
    }

    //Выбрать определённый элемент списка
    public static void setSelected(Spinner spinner, String value) {
        Adapter adapter = spinner.getAdapter();

        //Пройти по всему списку
        for (int i = 0; i < adapter.getCount(); i++) {

            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                return;
            }
        }

    }

    //Вывести сообщение под полем ввода
    public static void showErrorMessage(EditText editText, Context context, String message) {
        editText.setError(message);
        editText.getBackground()
                .setColorFilter(context.getResources().getColor(R.color.error),
                        PorterDuff.Mode.SRC_ATOP);
    }


    //Метод валидациии
    public static boolean isValidEditText(EditText editText, Context context, String message, Predicate<String> predicate) {
        if (!predicate.test(editText.getText().toString())) {
            showErrorMessage(editText, context, message);
            return false;
        }
        editText.setError(null);
        editText.getBackground()
                .setColorFilter(context.getResources().getColor(R.color.default_field),
                        PorterDuff.Mode.SRC_ATOP);
        return true;
    }


    //Вывести snackBar
    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Закрыть", (v) -> {
        });

        snackbar.show();
    }

}//Utils
