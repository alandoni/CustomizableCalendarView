# CustomizableCalendarView
An Android CalendarView totally customizable

This calendar is a GridView that receives an CalendarAdapter to be totally customizable

Use this code to insert the calendar in your xml layout
You can set width and height
> <view
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    class="adqmobile.customizablecalendarview.CustomizableCalendarView"
    android:id="@+id/calendar"
    custom:showNameOfWeekDays="true"
    custom:showWeekends="true"
    custom:fillHeight="true"/>

The default CalendarAdapter is not much pretty, so, let`s customize it

First of all, make a class like the following below

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
