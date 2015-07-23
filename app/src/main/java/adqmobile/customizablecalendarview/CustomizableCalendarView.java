package adqmobile.customizablecalendarview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Date;

/**
 * Created by alan on 22/07/15.
 */
public class CustomizableCalendarView extends GridView implements AdapterView.OnItemClickListener {

    private Date mCurrentDate;
    private CalendarAdapter mAdapter;
    private OnDateClickListener mOnDateClickListener;

    private boolean mShowWeekends = true;
    private boolean mShowNameOfWeekDays = true;
    private boolean mFillHeight = true;

    public CustomizableCalendarView(Context context) {
        super(context);
        init(null);
    }

    public CustomizableCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomizableCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mCurrentDate = new Date();
        mAdapter = new CalendarAdapter();
        mAdapter.setDate(mCurrentDate);

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CustomizableCalendarView, 0, 0);
            try {
                mShowNameOfWeekDays = a.getBoolean(R.styleable.CustomizableCalendarView_showNameOfWeekDays, true);
                mShowWeekends = a.getBoolean(R.styleable.CustomizableCalendarView_showWeekends, true);
                mFillHeight = a.getBoolean(R.styleable.CustomizableCalendarView_fillHeight, true);
            } finally {
                a.recycle();
            }
        }
        setAdapter(mAdapter);
        setOnItemClickListener(this);
    }

    public final void setAdapter(CalendarAdapter adapter) {
        if (adapter instanceof CalendarAdapter) {
            mAdapter = adapter;
            mAdapter.setDate(mCurrentDate);
            super.setAdapter(mAdapter);
            setShowNameOfWeekDays(mShowNameOfWeekDays);
            setShowWeekends(mShowWeekends);
            setFillHeight(mFillHeight);
        }
    }

    public final CalendarAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public final void setNumColumns(int number) {
        if (mShowWeekends) {
            super.setNumColumns(7);
        } else {
            super.setNumColumns(5);
        }
    }

    public void setDate(Date date) {
        mCurrentDate = date;
        mAdapter.setDate(mCurrentDate);
    }

    public void setShowNameOfWeekDays(boolean showNameOfWeekDays) {
        mShowNameOfWeekDays = showNameOfWeekDays;
        mAdapter.setShowNameOfWeekDays(mShowNameOfWeekDays);
    }

    public void setShowWeekends(boolean showWeekends) {
        mShowWeekends = showWeekends;
        setNumColumns(5);
        mAdapter.setShowWeekends(mShowWeekends);
    }

    public void setFillHeight(boolean fillHeight) {
        mFillHeight = fillHeight;
        mAdapter.setFillHeight(fillHeight);
    }

    protected final void setHeightOfCells() {
        if (!mFillHeight) {
            return;
        }
        int heightOfEachDayCell = CustomizableCalendarView.this.getHeight();

        if (mShowNameOfWeekDays) {
            heightOfEachDayCell -= mAdapter.getHeightOfDayNamesView() + 1;
        }
        heightOfEachDayCell -= getVerticalSpacing() * 6;
        heightOfEachDayCell = (int) Math.floor(heightOfEachDayCell / 6.0f);
        mAdapter.setHeightOfCells(heightOfEachDayCell);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnDateClickListener == null) {
            super.getOnItemClickListener();
            return;
        }

        CalendarAdapter.CalendarHelper dateHelper = mAdapter.getItem(position);
        mOnDateClickListener.onDateClick(parent, view, dateHelper.date);
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        mOnDateClickListener = onDateClickListener;
    }

    public interface OnDateClickListener {
        public void onDateClick(AdapterView<?> parent, View view, Date date);
    }
}
