package com.jcc.weatheralarm;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        AlarmAdapter alarmAdapter = new AlarmAdapter(MainActivity.this);
        recyclerView.setAdapter(alarmAdapter);
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

    public class AlarmAdapter extends RecyclerView.Adapter {
        Context mContext;

        public AlarmAdapter(Context context) {
            mContext = context;
        }

        @Override
        public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(mContext, R.layout.alarm_item, parent);
            return new AlarmViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            AlarmViewHolder viewHolder = (AlarmViewHolder) holder;

            viewHolder.mAlarmTime.setText("Time:" + position);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView mAlarmTime;
        public ImageView mAlarmTheme;

        public AlarmViewHolder(View itemView) {
            super(itemView);

            mAlarmTime = itemView.findViewById(R.id.alarm_text);
            mAlarmTheme = itemView.findViewById(R.id.alarm_theme);
        }
    }

    public class AlarmItem {
        private long mAlarmTime;
        private int mTheme;
        private int mRegularDay;

        public AlarmItem(long time, int theme, int days) {
            mAlarmTime = time;
            mTheme = theme;
            mRegularDay = days;
        }

        public long getAlarmTime() {
            return mAlarmTime;
        }

        public int getTheme() {
            return mTheme;
        }

        public int getRegularDay() {
            return mRegularDay;
        }
    }
}
