package ca.javajeff.tsepochka;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Саддам on 03.02.2018.
 */

public class ApiAdapter extends RecyclerView.Adapter<ApiAdapter.ApiAdapterViewHolder> {

    private ArrayList<String> mInfoData = new ArrayList<>();
    private ArrayList<String> mDateData = new ArrayList<>();
    private ArrayList<String> mImageData = new ArrayList<>();
    private ArrayList<String> mTextData = new ArrayList<>();
    private ArrayList<String> mSourceData = new ArrayList<>();
    Context context;


    private final ApiAdapterOnClickHandler mClickHandler;

    public interface ApiAdapterOnClickHandler {
        void onClick(String id);
    }


    public ApiAdapter(ApiAdapterOnClickHandler ClickHandler) {
        mClickHandler= ClickHandler;
    }



    public class ApiAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView infoView;
        public final ImageView imageView;
        public final TextView dateView;
//        public final TextView sourceView;

        public ApiAdapterViewHolder(View view) {
            super(view);
            context = view.getContext();
            infoView = (TextView) view.findViewById(R.id.text_item);
            imageView = (ImageView) view.findViewById(R.id.image_item);
            dateView = (TextView) view.findViewById(R.id.date_item);
            //           sourceView = (TextView) view.findViewById();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            int adapterPosition = getAdapterPosition();
            final Intent intent;
            intent = new Intent(context, ScrollingActivity.class);
            intent.putExtra("text",mTextData.get(adapterPosition));
            intent.putExtra("date", mDateData.get(adapterPosition));
            intent.putExtra("title", mInfoData.get(adapterPosition));
            intent.putExtra("image", mImageData.get(adapterPosition));
            intent.putExtra("source", mSourceData.get(adapterPosition));
            context.startActivity(intent);
            mClickHandler.onClick(String.valueOf(adapterPosition));
        }
    }

    @Override
    public ApiAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.info_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttach = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttach);
        return new ApiAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApiAdapterViewHolder holder, final int position) {
        String info= mInfoData.get(position);
        String date = mDateData.get(position);
        String image = mImageData.get(position);
        Log.v("element", info);
        holder.infoView.setText(info);
        holder.infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                int adapterPosition = position;
                final Intent intent;
                intent = new Intent(context, ScrollingActivity.class);
                intent.putExtra("text",mTextData.get(adapterPosition));
                intent.putExtra("date", mDateData.get(adapterPosition));
                intent.putExtra("title", mInfoData.get(adapterPosition));
                intent.putExtra("image", mImageData.get(adapterPosition));
                intent.putExtra("source", mSourceData.get(adapterPosition));
                context.startActivity(intent);
                mClickHandler.onClick(String.valueOf(adapterPosition));
            }
        });
        holder.dateView.setText(date);

        Picasso.with(context).load(image).resize(342,180).centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (null == mInfoData) return 0;
        return mInfoData.size();
    }

    public void setInfoData(ArrayList<MainActivity.DataNews> data) {
        for (int i=0;i<data.size();i++) {
            mInfoData.add(data.get(i).getTitles());
            mImageData.add(data.get(i).getImages());
            mDateData.add(data.get(i).getDates());
            mTextData.add(data.get(i).getTexts());
            mSourceData.add(data.get(i).getSources());
        }
        notifyDataSetChanged();
        //Log.v("array",mInfoData.toString());
    }
}
