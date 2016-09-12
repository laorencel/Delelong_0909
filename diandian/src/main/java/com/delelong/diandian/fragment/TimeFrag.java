package com.delelong.diandian.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delelong.diandian.MainActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.view.CustomNumberPicker;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/4.
 */
public class TimeFrag extends Fragment implements NumberPicker.OnValueChangeListener, View.OnClickListener {

    private static final String TAG = "BAIDUMAPFORTEST";
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_timepicker, container, false);

        initTimePicker();

        return view;
    }

    SimpleDateFormat dateFormat;
    String[] date, hour, min, nothing, newHour, newMin;
    String timeNow;
    CustomNumberPicker hourPicker, minutePicker, datePicker;
    TextView time_cancel, time_confirm;
    RelativeLayout time_picker;
    String mDay, mHour, mMin;

    /**
     * 加载时间选择器
     */
    private void initTimePicker() {
        //时间选择模块
        time_picker = (RelativeLayout) view.findViewById(R.id.time_picker);

        datePicker = (CustomNumberPicker) view.findViewById(R.id.datepicker);
        hourPicker = (CustomNumberPicker) view.findViewById(R.id.hourpicker);
        minutePicker = (CustomNumberPicker) view.findViewById(R.id.minutepicker);

        time_cancel = (TextView) view.findViewById(R.id.time_cancel);
        time_confirm = (TextView) view.findViewById(R.id.time_confirm);
        time_cancel.setOnClickListener(this);
        time_confirm.setOnClickListener(this);

        datePicker.setNumberPickerDividerColor(datePicker, Color.GRAY);
        hourPicker.setNumberPickerDividerColor(hourPicker, Color.GRAY);
        minutePicker.setNumberPickerDividerColor(minutePicker, Color.GRAY);

        date = new String[]{"现在", "今天", "明天", "后天"};
        hour = new String[]{"0点", "1点", "2点", "3点", "4点", "5点", "6点", "7点", "8点", "9点", "10点",
                "11点", "12点", "13点", "14点", "15点", "16点", "17点", "18点", "19点", "20点", "21点", "22点", "23点"};
        min = new String[]{"0分", "10分", "20分", "30分", "40分", "50分"};
        nothing = new String[]{"--"};
        //获取当前时间
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        timeNow = dateFormat.format(new Date());
        String hourNow = timeNow.substring(11, 13);
        String minNow = timeNow.substring(14, 16);
        //根据时间获取新的数据源
        newHour = getTimeList(Integer.parseInt(hourNow), hour);
        newMin = getTimeList(Integer.parseInt(minNow), min);
        //初始化时间
        mDay = date[1];
        mHour = newHour[0];
        mMin = newMin[0];

        hourPicker.setDisplayedValues(nothing);
        hourPicker.setOnValueChangedListener(this);

        minutePicker.setDisplayedValues(nothing);
        minutePicker.setOnValueChangedListener(this);

        datePicker.setDisplayedValues(date);
        datePicker.setMinValue(0);
        datePicker.setMaxValue(date.length - 1);
        datePicker.setOnValueChangedListener(this);
//        mHour = newHour[0];
//        mMin = newMin[0];
        orderedTime = "现在";
    }

    private TimeFrag mTimeFrag;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_cancel:
                orderedTime = "现在";
                if (mTimeFrag == null) {
                    mTimeFrag = (TimeFrag) activity.getFragmentManager().findFragmentByTag("TimeFrag");
                }
                timePickerListener.getTime(orderedTime);
                activity.getFragmentManager().beginTransaction().hide(mTimeFrag).commit();
                break;
            case R.id.time_confirm:
                //时间隐藏，确定时间
                if (mTimeFrag == null) {
                    mTimeFrag = (TimeFrag) activity.getFragmentManager().findFragmentByTag("TimeFrag");
                }
                timePickerListener.getTime(orderedTime);
                activity.getFragmentManager().beginTransaction().hide(mTimeFrag).commit();
                break;
        }

    }

    TimePickerListener timePickerListener;

    public void getTimePickerListener(TimePickerListener timePickerListener) {
        this.timePickerListener = timePickerListener;
    }

    public interface TimePickerListener {
        void getTime(String time);
    }

    private String orderedTime;//最终时间
    int mDateChoice = 0;

    DateTime dateTime;

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.datepicker:
                switch (newVal) {
                    case 0:
                        mDateChoice = 0;
//                        mHour = newHour[0];
//                        mMin = newMin[0];
                        changeDataSetWheel(nothing, nothing, hourPicker, minutePicker);
                        break;
                    case 1:
                        mDateChoice = 1;
                        mHour = newHour[0];
                        mMin = newMin[0];
                        if (oldVal - newVal > 0) {
                            changeDataSetWheel(newHour, newMin, hourPicker, minutePicker);
                        } else {
                            changeData(newHour, newMin, hourPicker, minutePicker);
                        }
                        break;
                    case 2:
                        mDateChoice = 2;
                        mHour = hour[0];
                        mMin = hour[0];
                        if (oldVal - newVal > 0) {
                            changeDataSetWheel(hour, min, hourPicker, minutePicker);
                        } else {
                            changeData(hour, min, hourPicker, minutePicker);
                        }
                        break;
                    case 3:
                        mDateChoice = 2;
                        mHour = hour[0];
                        mMin = hour[0];
                        if (oldVal - newVal > 0) {
                            changeDataSetWheel(hour, min, hourPicker, minutePicker);
                        } else {
                            changeData(hour, min, hourPicker, minutePicker);
                        }
                        break;
                }
                mDay = date[newVal];
                break;
            case R.id.hourpicker:
                switch (mDateChoice) {
                    case 0:
                        break;
                    case 1:
                        mHour = newHour[newVal];
                        break;
                    case 2:
                        mHour = hour[newVal];
                        break;
                }
                break;
            case R.id.minutepicker:
                switch (mDateChoice) {
                    case 0:
                        break;
                    case 1:
                        mMin = newMin[newVal];
                        break;
                    case 2:
                        mMin = min[newVal];
                        break;
                }
                break;
        }
//        Log.i(TAG, "callTime: //" + "mDay" + mDay + "mHour" + mHour + "mMin" + mMin);
        dateTime = new DateTime();
        if (mDateChoice == 0) {
            orderedTime = "现在";
        } else {
            if (mDay.equals("明天")) {
                dateTime = dateTime.plusDays(1);
            } else if (mDay.equals("后天")) {
                dateTime = dateTime.plusDays(2);
            }
            orderedTime = dateTime.toString("yyyy-MM-dd HH:mm:SS");
//            Log.i(TAG, "onValueChange: " + orderedTime);
            //格式化选择时间
            orderedTime = orderedTime.replace(orderedTime.substring(11, 13), Integer.parseInt(mHour.substring(0, mHour.length() - 1)) < 10 ? "0" + mHour.substring(0, mHour.length() - 1) : mHour.substring(0, mHour.length() - 1));
            orderedTime = orderedTime.replace(orderedTime.substring(14, 16), mMin.substring(0, mMin.length() - 1).equals("0") ? "00" : mMin.substring(0, mMin.length() - 1));
            orderedTime = orderedTime.replace(orderedTime.substring(17,orderedTime.length()),  "00");
//            Log.i(TAG, "onValueChange: " + orderedTime);
        }
    }


    public void changeData(String[] hour, String[] min, NumberPicker hourPicker, NumberPicker minutePicker) {
        hourPicker.setDisplayedValues(hour);
        minutePicker.setDisplayedValues(min);
        hourPicker.setMaxValue(hour.length - 1);
        minutePicker.setMaxValue(min.length - 1);
    }

    public void changeDataSetWheel(String[] hour, String[] min, NumberPicker hourPicker, NumberPicker minutePicker) {
        hourPicker.setMaxValue(hour.length - 1);
        minutePicker.setMaxValue(min.length - 1);
        setWrapSelectorWheel(hourPicker, minutePicker);
        hourPicker.setDisplayedValues(hour);
        minutePicker.setDisplayedValues(min);
    }

    public void setWrapSelectorWheel(NumberPicker hourPicker, NumberPicker minutePicker) {
    /* setWrapSelectorWheel
     * 必须放在setMaxValue后面，因为源码在更改此值的时候要求MaxValue-MinValue>=mSelectorIndices.length（等于3）
     * 所以如果MaxValue-MinValue更改了需要重新调用setWrapSelectorWheel
     */
        hourPicker.setWrapSelectorWheel(false);
        minutePicker.setWrapSelectorWheel(false);
    }

    /**
     * 根据时间获取新的数据源
     *
     * @param time
     * @param list
     * @return
     */
    public String[] getTimeList(int time, String[] list) {
        ArrayList<String> list1 = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            String temp;
            if (time > 50) {
                list1.add(list[i]);
            } else {
                if (list.length > 10) {
                    //hour
                    if (i < 10) {
                        temp = list[i].substring(0, 1);
                    } else {
                        temp = list[i].substring(0, 2);
                    }
                } else {
                    //min
                    if (i == 0) {
                        temp = list[i].substring(0, 1);
                    } else {
                        temp = list[i].substring(0, 2);
                    }
                }
                Integer.parseInt(temp);
                if (Integer.parseInt(temp) >= time) {
                    list1.add(list[i]);
                }
            }
        }
        String[] newlist = new String[list1.size()];
        for (int i = 0; i < list1.size(); i++) {
            newlist[i] = list1.get(i);
        }
        return newlist;
    }

    MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

}
