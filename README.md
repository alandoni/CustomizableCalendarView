# CustomizableCalendarView
An Android CalendarView totally customizable

This calendar is a GridView that receives an CalendarAdapter that could be extended to show what you want in each cell...

Use this code to insert the calendar in your xml layout
You can set width and height

``` xml
<view
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    class="adqmobile.customizablecalendarview.CustomizableCalendarView"
    android:id="@+id/calendar"
    custom:showNameOfWeekDays="true"
    custom:showWeekends="true"
    custom:fillHeight="true"/>
````
    
The default CalendarAdapter is not much pretty, so, let`s customize it

First of all, make a class like the following below
``` java
private class CustomCalendarAdapter extends CalendarAdapter {

    @Override
    public View getDayView(Date date, boolean isWithinCurrentMonth, boolean isWeekend, 
            View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.calendar_day_whiteview, null);

        TextView day = (TextView) convertView.findViewById(R.id.day);
        day.setText(String.valueOf(date.getDate()));

        if (!isWithinCurrentMonth) {
            day.setTextColor(Color.parseColor("#aaaaaa"));
        }

        Date today = new Date();

        if (date.getDate() == today.getDate() && date.getMonth() == today.getMonth() 
                && date.getYear() == today.getYear()) {
            day.setBackgroundResource(R.drawable.today_background);
            day.setTextColor(Color.parseColor("#ffffff"));
        }

        return convertView;
    }

    @Override
    public View getNameOfWeekDaysView(int dayOfWeek, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.calendar_day_whiteview, null);
        TextView day = (TextView) convertView.findViewById(R.id.day);
        String[] array = day.getContext().getResources().getStringArray(R.array.days_of_week);
        day.setText(array[dayOfWeek]);
        return convertView;
    }
}
```

Add this new adapter to your CustomizableCalendarView:
``` java
CustomizableCalendarView calendar = (CustomizableCalendarView) findViewById(R.id.calendar);
calendar.setAdapter(new CustomCalendarAdapter());
````

And in calendar_day_whiteview.xml, a simple textview to show the date:
``` xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    >

    <TextView
        android:layout_width="40dp"
        android:layout_height="40dp"
        android:gravity="center"
        android:text="1"
        android:id="@+id/day"
        android:layout_gravity="center" />
</LinearLayout>
```

I've created a drawable to mark the current day, it is:
``` xml
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#ff2c7d81"/>
    <corners android:radius="80dip"/>
    <padding android:left="0dip" android:top="0dip" android:right="0dip" android:bottom="0dip" />
</shape>
```

Now, use your creativity to make whatever you want with this library!
