package ca.javajeff.tsepochka;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ScrollingActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    ImageView img_view;
    TextView title_view;
    TextView text_view;
    TextView date_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(this);

        img_view = (ImageView) findViewById(R.id.image_item);
        text_view = (TextView) findViewById(R.id.text_item);
        title_view = (TextView) findViewById(R.id.title_scroll);
        date_view = (TextView) findViewById(R.id.data_scroll);

        CollapsingToolbarLayout toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);




        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("text");
        Log.i("text", text);
        String source = bundle.getString("source");
        String date = bundle.getString("date");
        String image = bundle.getString("image");
        String title = bundle.getString("title");

//        toolbar_layout.setTitle("Новости");
//        toolbar_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.cardview_dark_background));
//        toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf(getResources().getColor(R.color.cardview_dark_background)));
//        toolbar_layout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
//        toolbar_layout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        Picasso.with(ScrollingActivity.this).load(image).resize(500, 300).centerCrop().into(img_view);

        text_view.setText(text);
        date_view.setText(date);
        title_view.setText(title);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = true;
            }
        }
    }

}
