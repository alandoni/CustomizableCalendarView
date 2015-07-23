package adqmobile.customizablecalendarview.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import adqmobile.customizablecalendarview.CalendarAdapter;
import adqmobile.customizablecalendarview.CustomizableCalendarView;
import adqmobile.customizablecalendarview.R;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final CustomizableCalendarView calendar = (CustomizableCalendarView) findViewById(R.id.calendar);

        calendar.setAdapter(new CustomCalendarAdapter());

        CheckBox fillHeight = (CheckBox) findViewById(R.id.fillHeight);
        fillHeight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calendar.setFillHeight(isChecked);
            }
        });

        CheckBox showNameOfDays = (CheckBox) findViewById(R.id.namesOfDays);
        showNameOfDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calendar.setShowNameOfWeekDays(isChecked);
            }
        });

        CheckBox showWeekends = (CheckBox) findViewById(R.id.weekends);
        showWeekends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calendar.setShowWeekends(isChecked);
            }
        });
    }

    private class CustomCalendarAdapter extends CalendarAdapter {

        @Override
        public View getDayView(Date date, boolean isWithinCurrentMonth, boolean isWeekend, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.calendar_day_whiteview, null);

            TextView day = (TextView) convertView.findViewById(R.id.day);
            day.setText(String.valueOf(date.getDate()));

            if (!isWithinCurrentMonth) {
                day.setTextColor(Color.parseColor("#aaaaaa"));
            }

            Date today = new Date();

            if (date.getDate() == today.getDate() && date.getMonth() == today.getMonth() && date.getYear() == today.getYear()) {
                day.setBackgroundResource(R.drawable.today_background);
                day.setTextColor(Color.parseColor("#ffffff"));
            }

            return convertView;
        }

        @Override
        public View getNameOfWeekDaysView(int dayOfWeek, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.calendar_day_whiteview, null);
            TextView day = (TextView) convertView.findViewById(R.id.day);
            String[] array = day.getContext().getResources().getStringArray(R.array.days_of_week);
            day.setText(array[dayOfWeek]);
            return convertView;
        }
    }
}
