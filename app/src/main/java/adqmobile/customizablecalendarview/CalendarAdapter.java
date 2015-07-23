package adqmobile.customizablecalendarview;

import android.content.Context;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alan on 22/07/15.
 */
public class CalendarAdapter extends BaseAdapter {

    private boolean mShowNameOfWeekDays = true;
    private boolean mShowWeekends = true;
    private CalendarHelper[] mDates;
    private Date mCurrentDate;
    private boolean mFillHeight;
    private int mHeightOfDayNamesView = -1;
    private int mHeightOfCells;

    public final void setDate(Date date) {
        mCurrentDate = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        MonthDisplayHelper monthHelper = new MonthDisplayHelper(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));

        cal.set(Calendar.DAY_OF_MONTH, monthHelper.getDayAt(0, 0));

        if (monthHelper.getDayAt(0, 0) > 1) {
            cal.add(Calendar.MONTH, -1);
        }

        mDates = new CalendarHelper[6 * getAmountDaysInWeek()];

        int j = 0;
        for (int i = 0; i < 6; i++) {
            int n[] = monthHelper.getDigitsForRow(i);

            for (int d = 0; d < n.length; d++) {
                CalendarHelper tmp = new CalendarHelper(cal.getTime(), monthHelper.isWithinCurrentMonth(i, d), i == 0 || i == 6);

                if (mShowWeekends || (!mShowWeekends && (d > 0 && d < 6))) {
                    mDates[j] = tmp;
                    j++;
                }
                cal.add(Calendar.DATE, 1);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public final int getCount() {
        if (mShowNameOfWeekDays) {
            return getAmountDaysInWeek() + mDates.length;
        }
        return mDates.length;
    }

    @Override
    public final CalendarHelper getItem(int position) {
        int daysInWeek = getAmountDaysInWeek();

        if (mShowNameOfWeekDays) {
            if (position < daysInWeek) {
                return null;
            }
            return mDates[position - daysInWeek];
        }
        return mDates[position];
    }

    private int getAmountDaysInWeek() {
        return (mShowWeekends ? 7 : 5);
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (mShowNameOfWeekDays && getItem(position) == null) {
            if (!mShowWeekends) {
                if (position < getAmountDaysInWeek()) {
                    position++;
                }
            }
            convertView = getNameOfWeekDaysView(position, convertView, parent);
            convertView.getViewTreeObserver().addOnGlobalLayoutListener(new HeightListener(convertView));
            return convertView;
        }
        Date date = getItem(position).date;
        boolean isWithinCurrentMonth = getItem(position).thisMonth;
        boolean isWeekend = getItem(position).isWeekend;
        convertView = getDayView(date, isWithinCurrentMonth, isWeekend, convertView, parent);

        if (mFillHeight) {
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mHeightOfCells));
        }
        return convertView;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */

    /**
     * Return the view that is above the cell of days, with the name of week days. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     * @param dayOfWeek 0 to 6, being 0 = sunday, 1 = monday... and 6 = saturday
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    public View getNameOfWeekDaysView(int dayOfWeek, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.calendar_day_defaultview, null);
        TextView day = (TextView) convertView.findViewById(R.id.day);

        try {
            String[] array = day.getContext().getResources().getStringArray(R.array.days_of_week);
            day.setText(array[dayOfWeek]);
        } catch (Exception e) { }

        return convertView;
    }

    /**
     * Return the view that is above the cell of days, with the name of week days. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     * @param date the date that the cell corresponds to.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    public View getDayView(Date date, boolean isWithinCurrentMonth, boolean isWeekend, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.calendar_day_defaultview, null);
        TextView day = (TextView) convertView.findViewById(R.id.day);
        day.setText(String.valueOf(date.getDate()));
        return convertView;
    }

    protected final void setShowNameOfWeekDays(boolean showNameOfWeekDays) {
        mShowNameOfWeekDays = showNameOfWeekDays;
        setDate(mCurrentDate);
    }

    protected final void setShowWeekends(boolean showWeekends) {
        mShowWeekends = showWeekends;
        setDate(mCurrentDate);
    }

    protected final void setFillHeight(boolean fillHeight) {
        this.mFillHeight = fillHeight;
        setDate(mCurrentDate);
    }

    protected final int getHeightOfDayNamesView() {
        return mHeightOfDayNamesView;
    }

    protected final void setHeightOfCells(int heightOfCells) {
        this.mHeightOfCells = heightOfCells;
        setDate(mCurrentDate);
    }

    final class CalendarHelper {
        public Date date;
        public boolean thisMonth;
        public boolean isWeekend;

        public CalendarHelper(Date date, boolean isThisMonth, boolean isWeekend) {
            this.date = date;
            this.thisMonth = isThisMonth;
            this.isWeekend = isWeekend;
        }
    }

    private class HeightListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private final View thisView;

        public HeightListener(View view) {
            thisView = view;
        }

        @Override
        public void onGlobalLayout() {
            if (mHeightOfDayNamesView != -1) {
                return;
            }

            mHeightOfDayNamesView = thisView.getHeight();
            thisView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            ((CustomizableCalendarView) thisView.getParent()).setHeightOfCells();
        }
    }
}
