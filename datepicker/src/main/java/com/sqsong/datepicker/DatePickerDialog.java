package com.sqsong.datepicker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.sqsong.datepicker.utils.DensityUtils;
import com.sqsong.datepicker.utils.WheelDateUtils;
import com.sqsong.datepicker.wheel.WheelPicker;

import java.util.Calendar;
import java.util.List;

public class DatePickerDialog extends DialogFragment implements WheelPicker.OnItemSelectedListener {

    private WheelPicker yearWheel;
    private LinearLayout monthLl;
    private WheelPicker monthWheel;
    private LinearLayout dayLl;
    private WheelPicker dayWheel;
    private LinearLayout hourLl;
    private WheelPicker hourWheel;
    private LinearLayout minuteLl;
    private WheelPicker minuteWheel;
    private LinearLayout secondLl;
    private WheelPicker secondWheel;

    private int mMode;
    private int mDayIndex;
    private int mYearIndex;
    private int mHourIndex;
    private int mMinuteIndex;
    private int mSecondIndex;
    private String mDayStr;
    private String mHourStr;
    private String mMinuteStr;
    private String mSecondStr;
    private int mExtraType;
    private int mMonthIndex;
    private long mEndMillis;
    private String mYearStr;
    private String mMonthStr;
    private long mStartMillis;
    private long mCurrentMillis;
    private Calendar mEndCalendar;
    private Calendar mStartCalendar;
    private Calendar mCurrentCalendar;
    private OnDateActionListener mListener;

    public static DatePickerDialog newInstance(long startMillis, long endMillis, long currentMillis, int mode, int extraType) {
        DatePickerDialog dialog = new DatePickerDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(WheelDateUtils.START_TIME_MILLIS, startMillis);
        bundle.putLong(WheelDateUtils.END_TIME_MILLIS, endMillis);
        bundle.putLong(WheelDateUtils.CURRENT_TIME_MILLIS, currentMillis);
        bundle.putInt(WheelDateUtils.DATE_MODE, mode);
        bundle.putInt(WheelDateUtils.EXTRA_TYPE, extraType);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDateActionListener) {
            mListener = (OnDateActionListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mStartMillis = bundle.getLong(WheelDateUtils.START_TIME_MILLIS);
            mEndMillis = bundle.getLong(WheelDateUtils.END_TIME_MILLIS);
            mCurrentMillis = bundle.getLong(WheelDateUtils.CURRENT_TIME_MILLIS);
            mMode = bundle.getInt(WheelDateUtils.DATE_MODE);
            mExtraType = bundle.getInt(WheelDateUtils.EXTRA_TYPE);
        }
        if (mStartMillis > mEndMillis || mCurrentMillis > mEndMillis || mCurrentMillis < mStartMillis)
            throw new IllegalArgumentException("The start millis or current millis cannot greater than end millis.");
        if (mMode != WheelDateUtils.MODE_YEAR && mMode != WheelDateUtils.MODE_YM && mMode != WheelDateUtils.MODE_YMD
                && mMode != WheelDateUtils.MODE_YMDHM && mMode != WheelDateUtils.MODE_YMDHMS)
            throw new IllegalArgumentException("The mode must belong to 0,1,2,3,4");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.dialog_date_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initViewVisibleState();
        initEvent();
    }

    private void initView(View view) {
        yearWheel = view.findViewById(R.id.year_wheel);
        monthLl = view.findViewById(R.id.month_ll);
        monthWheel = view.findViewById(R.id.month_wheel);
        dayLl = view.findViewById(R.id.day_ll);
        dayWheel = view.findViewById(R.id.day_wheel);
        hourLl = view.findViewById(R.id.hour_ll);
        hourWheel = view.findViewById(R.id.hour_wheel);
        minuteLl = view.findViewById(R.id.minute_ll);
        minuteWheel = view.findViewById(R.id.minute_wheel);
        secondLl = view.findViewById(R.id.second_ll);
        secondWheel = view.findViewById(R.id.second_wheel);

        view.findViewById(R.id.cancel_tv).setOnClickListener((v) -> dismiss());
        view.findViewById(R.id.confirm_tv).setOnClickListener((v) -> confirmYear());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = DensityUtils.getScreenWidth();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.windowAnimations = R.style.BottomShowUpDialogStyle;
    }

    private void confirmYear() {
        checkParams();
        Fragment fragment = getTargetFragment();
        if (fragment instanceof OnDateActionListener) {
            ((OnDateActionListener) fragment).onDateArrived(mYearStr, mMonthStr, mDayStr, mHourStr, mMinuteStr, mSecondStr, mExtraType);
        }
        if (mListener != null) {
            mListener.onDateArrived(mYearStr, mMonthStr, mDayStr, mHourStr, mMinuteStr, mSecondStr, mExtraType);
        }
        dismiss();
    }

    private void checkParams() {
        switch (mMode) {
            case WheelDateUtils.MODE_YEAR:
                mMonthStr = "01";
                mDayStr = "01";
                mHourStr = "00";
                mMinuteStr = "00";
                mSecondStr = "00";
                break;
            case WheelDateUtils.MODE_YM:
                mDayStr = "01";
                mHourStr = "00";
                mMinuteStr = "00";
                mSecondStr = "00";
                break;
            case WheelDateUtils.MODE_YMD:
                mHourStr = "00";
                mMinuteStr = "00";
                mSecondStr = "00";
                break;
            case WheelDateUtils.MODE_YMDHM:
                mSecondStr = "00";
                break;
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment fragment = getTargetFragment();
        if (fragment instanceof OnDateActionListener) {
            ((OnDateActionListener) fragment).onDialogDismiss(mExtraType);
        }
        if (mListener != null) {
            mListener.onDialogDismiss(mExtraType);
        }
    }

    private void initViewVisibleState() {
        if (mMode == WheelDateUtils.MODE_YEAR) {
            monthLl.setVisibility(View.GONE);
            dayLl.setVisibility(View.GONE);
            hourLl.setVisibility(View.GONE);
            minuteLl.setVisibility(View.GONE);
            secondLl.setVisibility(View.GONE);
        } else if (mMode == WheelDateUtils.MODE_YM) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) monthLl.getLayoutParams();
            params.leftMargin = DensityUtils.dip2px(30);
            dayLl.setVisibility(View.GONE);
            hourLl.setVisibility(View.GONE);
            minuteLl.setVisibility(View.GONE);
            secondLl.setVisibility(View.GONE);
        } else if (mMode == WheelDateUtils.MODE_YMD) {
            int margins = DensityUtils.dip2px(30);
            ((LinearLayout.LayoutParams) monthLl.getLayoutParams()).leftMargin = margins;
            ((LinearLayout.LayoutParams) dayLl.getLayoutParams()).leftMargin = margins;

            hourLl.setVisibility(View.GONE);
            minuteLl.setVisibility(View.GONE);
            secondLl.setVisibility(View.GONE);
        } else if (mMode == WheelDateUtils.MODE_YMDHM) {
            secondLl.setVisibility(View.GONE);
        }
    }

    private void initEvent() {
        yearWheel.setOnItemSelectedListener(this);
        monthWheel.setOnItemSelectedListener(this);
        dayWheel.setOnItemSelectedListener(this);

        mStartCalendar = Calendar.getInstance();
        mStartCalendar.setTimeInMillis(mStartMillis);

        mEndCalendar = Calendar.getInstance();
        mEndCalendar.setTimeInMillis(mEndMillis);

        mCurrentCalendar = Calendar.getInstance();
        mCurrentCalendar.setTimeInMillis(mCurrentMillis);
        int currentYear = mCurrentCalendar.get(Calendar.YEAR);
        int currentMonth = mCurrentCalendar.get(Calendar.MONTH) + 1;
        int currentDay = mCurrentCalendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = mCurrentCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = mCurrentCalendar.get(Calendar.MINUTE);
        int currentSecond = mCurrentCalendar.get(Calendar.SECOND);

        List<String> yearList = WheelDateUtils.getInitYear(mStartCalendar, mEndCalendar);
        List<String> monthList = WheelDateUtils.getMonthList(mStartCalendar, mEndCalendar, mCurrentCalendar);
        List<String> dayList = WheelDateUtils.getDayList(mStartCalendar, mEndCalendar, mCurrentCalendar);
        List<String> hourList = WheelDateUtils.getHourList(mStartCalendar, mEndCalendar, mCurrentCalendar);
        List<String> minuteList = WheelDateUtils.getMinuteList(mStartCalendar, mEndCalendar, mCurrentCalendar);
        List<String> secondList = WheelDateUtils.getSecondList(mStartCalendar, mEndCalendar, mCurrentCalendar);

        mYearIndex = yearList.indexOf(mYearStr = String.valueOf(currentYear));
        mMonthIndex = monthList.indexOf(mMonthStr = WheelDateUtils.formatDate(currentMonth));
        mDayIndex = dayList.indexOf(mDayStr = WheelDateUtils.formatDate(currentDay));
        mHourIndex = hourList.indexOf(mHourStr = WheelDateUtils.formatDate(currentHour));
        mMinuteIndex = minuteList.indexOf(mMinuteStr = WheelDateUtils.formatDate(currentMinute));
        mSecondIndex = secondList.indexOf(mSecondStr = WheelDateUtils.formatDate(currentSecond));

        yearWheel.setData(yearList);
        monthWheel.setData(monthList);
        dayWheel.setData(dayList);
        hourWheel.setData(hourList);
        minuteWheel.setData(minuteList);
        secondWheel.setData(secondList);
        yearWheel.post(() -> {
            yearWheel.setSelectedItemPosition(mYearIndex, false);
            monthWheel.setSelectedItemPosition(mMonthIndex, false);
            dayWheel.setSelectedItemPosition(mDayIndex, false);
            hourWheel.setSelectedItemPosition(mHourIndex, false);
            minuteWheel.setSelectedItemPosition(mMinuteIndex, false);
            secondWheel.setSelectedItemPosition(mSecondIndex, false);
        });
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        Log.e("songmao", "Position: " + position + ", Data: " + data);
        String time = (String) data;
        if (picker.getId() == R.id.year_wheel) {
            resetData(time, mMonthStr, mDayStr, mHourStr, mMinuteStr, mSecondStr);
        } else if (picker.getId() == R.id.month_wheel) {
            resetData(mYearStr, time, mDayStr, mHourStr, mMinuteStr, mSecondStr);
        } else if (picker.getId() == R.id.day_wheel) {
            resetData(mYearStr, mMonthStr, time, mHourStr, mMinuteStr, mSecondStr);
        } else if (picker.getId() == R.id.hour_wheel) {
            resetData(mYearStr, mMonthStr, mDayStr, time, mMinuteStr, mSecondStr);
        } else if (picker.getId() == R.id.minute_wheel) {
            resetData(mYearStr, mMonthStr, mDayStr, mHourStr, time, mSecondStr);
        } else if (picker.getId() == R.id.second_wheel) {
            resetData(mYearStr, mMonthStr, mDayStr, mHourStr, mMinuteStr, time);
        }
    }

    private void resetData(String year, String month, String day, String hour, String minute, String second) {
        mYearStr = year;
        mMonthStr = month;
        mDayStr = day;
        mHourStr = hour;
        mMinuteStr = minute;
        mSecondStr = second;

        mCurrentCalendar.set(Calendar.YEAR, Integer.parseInt(mYearStr));
        mCurrentCalendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        mCurrentCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        mCurrentCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        mCurrentCalendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        mCurrentCalendar.set(Calendar.SECOND, Integer.parseInt(second));

        List<String> yearList = WheelDateUtils.getInitYear(mStartCalendar, mEndCalendar);
        List<String> monthList = WheelDateUtils.getMonthList(mStartCalendar, mEndCalendar, mCurrentCalendar);
        List<String> dayList = WheelDateUtils.getDayList(mStartCalendar, mEndCalendar, mCurrentCalendar);
        List<String> hourList = WheelDateUtils.getHourList(mStartCalendar, mEndCalendar, mCurrentCalendar);
        List<String> minuteList = WheelDateUtils.getMinuteList(mStartCalendar, mEndCalendar, mCurrentCalendar);
        List<String> secondList = WheelDateUtils.getSecondList(mStartCalendar, mEndCalendar, mCurrentCalendar);

        yearWheel.setData(yearList);
        monthWheel.setData(monthList);
        dayWheel.setData(dayList);
        hourWheel.setData(hourList);
        minuteWheel.setData(minuteList);
        secondWheel.setData(secondList);

        mYearIndex = yearList.indexOf(mYearStr);
        if (mYearIndex == -1) {
            mYearIndex = yearList.size() - 1;
        }
        mMonthIndex = monthList.indexOf(mMonthStr);
        if (mMonthIndex == -1) {
            mMonthIndex = monthList.size() - 1;
        }
        mDayIndex = dayList.indexOf(mDayStr);
        if (mDayIndex == -1) {
            mDayIndex = dayList.size() - 1;
        }
        mHourIndex = hourList.indexOf(mHourStr);
        if (mHourIndex == -1) {
            mHourIndex = hourList.size() - 1;
        }
        mMinuteIndex = minuteList.indexOf(mMinuteStr);
        if (mMinuteIndex == -1) {
            mMinuteIndex = minuteList.size() - 1;
        }
        mSecondIndex = secondList.indexOf(mSecondStr);
        if (mSecondIndex == -1) {
            mSecondIndex = secondList.size() - 1;
        }
        mYearStr = yearList.get(mYearIndex);
        mMonthStr = monthList.get(mMonthIndex);
        mDayStr = dayList.get(mDayIndex);
        mHourStr = hourList.get(mHourIndex);
        mMinuteStr = minuteList.get(mMinuteIndex);
        mSecondStr = secondList.get(mSecondIndex);
        yearWheel.post(() -> {
            monthWheel.setSelectedItemPosition(mMonthIndex, false);
            dayWheel.setSelectedItemPosition(mDayIndex, false);
            hourWheel.setSelectedItemPosition(mHourIndex, false);
            minuteWheel.setSelectedItemPosition(mMinuteIndex, false);
            secondWheel.setSelectedItemPosition(mSecondIndex, false);
        });
    }

    public interface OnDateActionListener {
        void onDateArrived(String year, String month, String day, String hour,
                           String minute, String second, int extraType);

        void onDialogDismiss(int extraType);
    }

}
