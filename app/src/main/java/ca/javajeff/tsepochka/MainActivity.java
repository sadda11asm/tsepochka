package ca.javajeff.tsepochka;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import client.yalantis.com.foldingtabbar.FoldingTabBar;

public class MainActivity extends AppCompatActivity implements ApiAdapter.ApiAdapterOnClickHandler {

    private ApiAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorTextView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private Set<String> categories;
    final String TAG = "preferences";
    final String CATEGORY = "preferred_categories";
    final String preferred = "ALL";
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setScrollBarSize(40);
        //toolbar.setLogo(R.drawable.icon);
        categories  = new HashSet<>();

        FoldingTabBar tabBar = (FoldingTabBar) findViewById(R.id.folding_tab_bar);

        tabBar.setOnFoldingItemClickListener(new FoldingTabBar.OnFoldingItemSelectedListener() {
            @Override
            public boolean onFoldingItemSelected(@NotNull MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "Item", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        tabBar.setOnMainButtonClickListener(new FoldingTabBar.OnMainButtonClickedListener() {
            @Override
            public void onMainButtonClicked() {
                Toast.makeText(MainActivity.this, "Main", Toast.LENGTH_SHORT).show();
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_item);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshContent();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        mErrorTextView = (TextView) findViewById(R.id.error_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_main);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ApiAdapter(this);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setVisibility(View.INVISIBLE);
        getPreferences();
        loadNewsData();
    }

    private void getPreferences() {
        Context context = MainActivity.this;
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        categories = pref.getStringSet(CATEGORY, categories);
    }

    private void setPreferences() {
        Context context = MainActivity.this;
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(CATEGORY, categories);
        editor.commit();
    }

    private void refreshContent() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new ApiAdapter(MainActivity.this);
//                mAdapter.setInfoData(null);
                loadNewsData();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void loadNewsData() {
        fetchFirebaseData();
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void fetchFirebaseData() {
        final ArrayList<DataNews> data = new ArrayList<>(100);
        myRef.child("news").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!preferred.equals("ALL")) {
                    for(DataSnapshot dt: dataSnapshot.getChildren()) {
                        if (categories.contains(dt.getKey())) {
                            for (DataSnapshot dt2 : dt.getChildren()) {
                                DataNews element = new DataNews();
                                element.setTitles(dt2.child("title").getValue().toString());
                                element.setImages(dt2.child("img").getValue().toString());
                                element.setDates(dt2.child("data").getValue().toString() + " | " + dt.getKey() + " | " + dt2.child("source").getValue().toString());
                                element.setSources(dt2.child("source").getValue().toString());
                                try {
                                    element.setTexts(dt2.child("textHref").getValue().toString());
                                } catch (Exception e) {
                                    element.setTexts(dt2.child("text").getValue().toString());
                                }
                                data.add(element);
                            }
                        }
                    }
                } else {
                    for(DataSnapshot dt: dataSnapshot.getChildren()) {
                        for (DataSnapshot dt2 : dt.getChildren()) {
                            DataNews element = new DataNews();
                            element.setTitles(dt2.child("title").getValue().toString());
                            element.setImages(dt2.child("img").getValue().toString());
                            element.setDates(dt2.child("data").getValue().toString() + " | " + dt.getKey() + " | " + dt2.child("source").getValue().toString());
                            element.setSources(dt2.child("source").getValue().toString());
                            try {
                                element.setTexts(dt2.child("textHref").getValue().toString());
                            } catch (Exception e) {
                                element.setTexts(dt2.child("text").getValue().toString());
                            }
                            data.add(element);
                        }
                    }
                }
                Collections.sort(data, new DateComparator());
                Collections.reverse(data);
                mAdapter.setInfoData(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String id) {

    }

    public class DataNews {
        private String titles;
        private String dates;
        private String images;
        private String sources;
        private String texts;

        public DataNews() {
            titles = new String();
            dates = new String();
            images = new String();
            sources = new String();
            texts = new String();
        }

        public String getDates() {
            return dates;
        }

        public String getImages() {
            return images;
        }

        public String getSources() {
            return sources;
        }

        public String getTexts() {
            return texts;
        }

        public String getTitles() {
            return titles;
        }

        public void setDates(String dates) {
            this.dates = dates;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public void setSources(String sources) {
            this.sources = sources;
        }

        public void setTexts(String texts) {
            this.texts = texts;
        }

        public void setTitles(String titles) {
            this.titles = titles;
        }
    }

    private class DateComparator implements java.util.Comparator<DataNews> {
        @Override
        public int compare(DataNews o1, DataNews o2) {
            String s1 = o1.getDates();
            String s2 = o2.getDates();
            String[] ar1=s1.split(" ");
            String[] ar2 = s2.split(" ");
            String date1="";
            for (int i=0;i<ar1[1].length()-1;i++) {
                date1+=ar1[1].charAt(i);
            }
            String date2="";
            for (int i=0;i<ar2[1].length()-1;i++) {
                date2+=ar2[1].charAt(i);
            }
            Map<String, Integer> q = new HashMap();
            q.put("January", 1);
            q.put("February", 2);
            q.put("March",3);
            q.put("April", 4);
            q.put("May", 5);
            q.put("June", 6);
            q.put("July", 7);
            q.put("August", 8);
            q.put("September", 9);
            q.put("October", 10);
            q.put("November", 11);
            q.put("December", 12);
//            Log.i("sort_date", date1);
//            Log.i("sort_date2", date2);
//            Log.i("sort_value1", String.valueOf(q.get(ar1[0])));
//            Log.i("sort_value2", String.valueOf(q.get(ar2[0])));
            if (ar2[2].equals("0NaN")) return 1;
            if (ar1[2].equals("0NaN")) return -1;
            if (!ar1[2].equals(ar2[2])) {
                return Integer.valueOf(ar1[2]) - Integer.valueOf(ar2[2]);
            }
            if (!ar1[0].equals(ar2[0])) {
                if (q.containsKey(ar1[0]) && q.containsKey(ar2[0])) {
                    return (q.get(ar1[0]) - q.get(ar2[0]));
                }
                if (q.containsKey(ar1[0])) return 1;
                return -1;
            }
            return (Integer.valueOf(date1)-Integer.valueOf(date2));
        }
    }
}
