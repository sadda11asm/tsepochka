package ca.javajeff.tsepochka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static ca.javajeff.tsepochka.CategoriesActivity.CATEGORY;
import static ca.javajeff.tsepochka.CategoriesActivity.getPreferences;
import static ca.javajeff.tsepochka.CategoriesActivity.loadCategories;

/**
 * Created by Саддам on 13.03.2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder>{
    private ArrayList<Category> mCategoryData = new ArrayList<>();
    Context context;


    private final MyAdapter.MyAdapterOnClickHandler mClickHandler;


    public interface MyAdapterOnClickHandler {
        void onClick(String id);
    }


    public MyAdapter(MyAdapter.MyAdapterOnClickHandler ClickHandler) {
        mClickHandler= ClickHandler;
    }



    public class MyAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView categoryView;
        public final CheckBox checkBox;


        public MyAdapterViewHolder(View view) {
            super(view);
            context = view.getContext();
            categoryView = (TextView) view.findViewById(R.id.category_item);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public MyAdapter.MyAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.my_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttach = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttach);
        return new MyAdapter.MyAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyAdapterViewHolder holder, final int position) {
        String category = mCategoryData.get(position).getCategory();
        boolean check = mCategoryData.get(position).isSelected();
        holder.categoryView.setText(category);
        holder.checkBox.setChecked(check);
        holder.checkBox.setVisibility(View.INVISIBLE);
        /*holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mCategoryData.get(position).setChosen(isChecked);
                Set<String> categories = new HashSet<String>();
                for (int i=0;i<mCategoryData.size();i++) {
                    if (mCategoryData.get(i).isSelected()) {
                        categories.add(mCategoryData.get(i).getCategory());
                    }
                }
                categories.add("ICO");
                categories.add("Познавательные статьи");
                Context context = buttonView.getContext();
                SharedPreferences pref = context.getSharedPreferences(CategoriesActivity.TAG, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putStringSet(CATEGORY, categories);
                editor.commit();
                loadCategories((MyAdapterOnClickHandler) context);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        if (null == mCategoryData) return 0;
        return mCategoryData.size();
    }

    public void setInfoData(ArrayList<Category> categories) {
        for (int i=0;i<categories.size();i++) {
            mCategoryData.add(categories.get(i));
        }
        notifyDataSetChanged();
        //Log.v("array",mInfoData.toString());
    }
}
