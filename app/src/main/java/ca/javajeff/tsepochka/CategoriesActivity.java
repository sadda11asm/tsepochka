package ca.javajeff.tsepochka;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Саддам on 13.03.2018.
 */

public class CategoriesActivity extends AppCompatActivity implements MyAdapter.MyAdapterOnClickHandler {
    private static RecyclerView myCategoriesView;
    private static RecyclerView notMyCategoriesView;
    private static MyAdapter myAdapter;
    private static MyAdapter notMyAdapter;

    public static String[] ALL = {"ICO", "Банки", "Биржи", "Биткойн", "Китайский привет", "Криптокошельки", "Майнинг", "Мнения", "Познавательные статьи", "Регулирования", "Технологии", "Хардфорк", "Эфир"};
    final static String TAG = "preferences";
    final static String CATEGORY = "preferred_categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        toolbar.setScrollBarSize(40);
        toolbar.setTitle("Мои категории");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        myCategoriesView = (RecyclerView) findViewById(R.id.my_categories);
        notMyCategoriesView = (RecyclerView) findViewById(R.id.not_my_categories);

        loadCategories(this);
    }

    public static void loadCategories(MyAdapter.MyAdapterOnClickHandler context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager((Context) context, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager((Context) context, LinearLayoutManager.VERTICAL, false);
        myCategoriesView.setLayoutManager(layoutManager);
        notMyCategoriesView.setLayoutManager(layoutManager1);
        myCategoriesView.setHasFixedSize(true);
        notMyCategoriesView.setHasFixedSize(true);

        myAdapter = new MyAdapter(context);
        notMyAdapter = new MyAdapter(context);

        myCategoriesView.setAdapter(myAdapter);
        notMyCategoriesView.setAdapter(notMyAdapter);
        getPreferences((Context) context);
    }


    public static void getPreferences(Context context) {
        Set<String> categories = new HashSet();
        ArrayList<Category> myCategories = new ArrayList<>();
        ArrayList<Category> notMyCategories = new ArrayList<>();
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        pref.getStringSet(CATEGORY, categories);
        for (int i=0;i<ALL.length;i++) {
            /*if (categories.contains(ALL[i])) {
                myCategories.add(new Category(ALL[i]));
            } else {
                Category category = new Category(ALL[i]);
                category.setChosen(false);
                notMyCategories.add(category);
            }*/
            myCategories.add(new Category(ALL[i]));
        }
        myAdapter.setInfoData(myCategories);
        notMyAdapter.setInfoData(notMyCategories);

    }



    @Override
    public void onClick(String id) {

    }

}
