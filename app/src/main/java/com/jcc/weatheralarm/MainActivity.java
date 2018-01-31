package com.jcc.weatheralarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jcc.weatheralarm.database.AlarmDAO;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private AlarmAdapter mAdapter;

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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                openTimePickerDialog(true);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AlarmAdapter(MainActivity.this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.refresh();
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

    private void openTimePickerDialog(boolean is24r){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Log.e("CCC", "hourOfDay::" + hourOfDay);
        Log.e("CCC", "minute::" + minute);

        AlarmDAO.getInstance(MainActivity.this).insert(new AlarmDAO.AlarmItem(hourOfDay, minute));
        mAdapter.refresh();
    }

    public class AlarmAdapter extends RecyclerView.Adapter {
        Context mContext;
        ArrayList<AlarmDAO.AlarmItem> mAdapterItems = new ArrayList<>();

        public AlarmAdapter(Context context) {
            mContext = context;
        }

        public void refresh() {
            mAdapterItems.clear();
            mAdapterItems.addAll(AlarmDAO.getInstance(mContext).getAllAlarms());
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.alarm_item, parent, false);
            return new AlarmViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            AlarmViewHolder viewHolder = (AlarmViewHolder) holder;

            final AlarmDAO.AlarmItem item = getItem(position);
            viewHolder.mAlarmTime.setText(item.toTimeString());
            viewHolder.mAlarmSwitch.setChecked(item.getAlarmState());
            viewHolder.mAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    item.setOnOff(b);
                    AlarmDAO.getInstance(mContext).update(item);
//                    if (b) {
//                        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//                        Intent intent = new Intent(mContext, AlarmReceiver.class);
//                        int code = Integer.parseInt(item.getAlarmId().substring(item.getAlarmId().length() - 4));
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, code, intent, PendingIntent.FLAG_ONE_SHOT);
//
//                        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, , pendingIntent);
//                    }
                }
            });
        }

        public AlarmDAO.AlarmItem getItem(int position) {
            return mAdapterItems.get(position);
        }

        @Override
        public int getItemCount() {
            return mAdapterItems.size();
        }
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView mAlarmTime;
        public ImageView mAlarmTheme;
        public Switch mAlarmSwitch;

        public AlarmViewHolder(View itemView) {
            super(itemView);

            mAlarmTime = itemView.findViewById(R.id.alarm_text);
            mAlarmTheme = itemView.findViewById(R.id.alarm_theme);
            mAlarmSwitch = itemView.findViewById(R.id.alarm_switch);
        }
    }
}
