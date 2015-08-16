package com.bin.superswiperefreshlayout;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bin.superswiperefreshlayout.library.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private ListView listView;
    private List<String> strs;
    private SuperSwipeRefreshLayout superSwipeRefreshLayout;
    private ArrayAdapter adapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            strs.add("item_new");
            adapter.notifyDataSetChanged();
            superSwipeRefreshLayout.setLoading(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.listView);
        superSwipeRefreshLayout=(SuperSwipeRefreshLayout)findViewById(R.id.superSwipeRefreshLayout);
        strs=new ArrayList<>();
        for(int i=0;i<20;i++){
            String str="item:"+i;
            strs.add(str);
        }
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, strs);
        superSwipeRefreshLayout.setMode(SuperSwipeRefreshLayout.Mode.BOTH);
        listView.setAdapter(adapter);
        superSwipeRefreshLayout.setOnLoadListener(new SuperSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                handler.sendEmptyMessageDelayed(1,2000);
            }
        });
        superSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                superSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
