package com.sqsong.datepicker.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class WheelDateUtils {

    public static final String DATE_MODE = "date_mode";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String START_TIME_MILLIS = "start_time_millis";
    public static final String END_TIME_MILLIS = "end_time_millis";
    public static final String CURRENT_TIME_MILLIS = "current_time_millis";

    public static final int MODE_YEAR = 0; // year
    public static final int MODE_YM = 1; // year-month
    public static final int MODE_YMD = 2; // year-month-day
    public static final int MODE_YMDHM = 3; // year-month-day-hour-minute
    public static final int MODE_YMDHMS = 4; // year-month-day-hour-minute-second


    public static List<String> getInitYear(Calendar startCalendar, Calendar endCalendar) {
        List<String> yearList = new ArrayList<>();
        int startYear = startCalendar.get(Calendar.YEAR);
        int endYear = endCalendar.get(Calendar.YEAR);
        for (int i = startYear; i <= endYear; i++) {
            yearList.add(String.valueOf(i));
        }
        return yearList;
    }

    public static List<String> getMonthList(Calendar startCalendar, Calendar endCalendar, Calendar currentCalendar) {
        List<String> monthList = new ArrayList<>();
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);

        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonth = endCalendar.get(Calendar.MONTH);

        int currentYear = currentCalendar.get(Calendar.YEAR);

        if (currentYear == startYear && currentYear == endYear) {
            for (int i = startMonth; i <= endMonth; i++) {
                int month = i + 1;
                monthList.add(formatDate(month));
            }
        } else if (currentYear == startYear) {
            for (int i = startMonth; i < 12; i++) {
                int month = i + 1;
                monthList.add(formatDate(month));
            }
        } else if (currentYear == endYear) {
            for (int i = 0; i <= endMonth; i++) {
                int month = i + 1;
                monthList.add(formatDate(month));
            }
        } else {
            for (int i = 0; i < 12; i++) {
                int month = i + 1;
                monthList.add(formatDate(month));
            }
        }
        return monthList;
    }

    public static List<String> getDayList(Calendar startCalendar, Calendar endCalendar, Calendar currentCalendar) {
        List<String> dayList = new ArrayList<>();
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);
        int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonth = endCalendar.get(Calendar.MONTH);
        int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH);

        GregorianCalendar gregorianCalendar = new GregorianCalendar(currentYear, currentMonth, 1);
        int days = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (currentYear == startYear && currentMonth == startMonth &&
                currentYear == endYear && currentMonth == endMonth) {
            for (int i = startDay; i <= endDay; i++) {
                dayList.add(formatDate(i));
            }
        } else if (currentYear == startYear && currentMonth == startMonth) {
            for (int i = startDay; i <= days; i++) {
                dayList.add(formatDate(i));
            }
        } else if (currentYear == endYear && currentMonth == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                dayList.add(formatDate(i));
            }
        } else {
            for (int i = 1; i <= days; i++) {
                dayList.add(formatDate(i));
            }
        }

        return dayList;
    }

    public static List<String> getHourList(Calendar startCalendar, Calendar endCalendar, Calendar currentCalendar) {
        List<String> hourList = new ArrayList<>();
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);
        int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);

        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonth = endCalendar.get(Calendar.MONTH);
        int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);

        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

        if (currentYear == startYear && currentMonth == startMonth &&
                currentYear == endYear && currentMonth == endMonth &&
                currentDay == startDay && currentDay == endDay) {
            for (int i = startHour; i <= endHour; i++) {
                hourList.add(formatDate(i));
            }
        } else if (currentYear == startYear && currentMonth == startMonth && currentDay == startDay) {
            for (int i = startHour; i <= 23; i++) {
                hourList.add(formatDate(i));
            }
        } else if (currentYear == endYear && currentMonth == endMonth && currentDay == endDay) {
            for (int i = 0; i <= endHour; i++) {
                hourList.add(formatDate(i));
            }
        } else {
            for (int i = 0; i <= 23; i++) {
                hourList.add(formatDate(i));
            }
        }
        return hourList;
    }

    public static List<String> getMinuteList(Calendar startCalendar, Calendar endCalendar, Calendar currentCalendar) {
        List<String> minuteList = new ArrayList<>();
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);
        int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = startCalendar.get(Calendar.MINUTE);

        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonth = endCalendar.get(Calendar.MONTH);
        int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        int endMinute = endCalendar.get(Calendar.MINUTE);

        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);

        if (currentYear == startYear && currentMonth == startMonth &&
                currentYear == endYear && currentMonth == endMonth &&
                currentDay == startDay && currentDay == endDay &&
                currentHour == startHour && currentHour == endHour) {
            for (int i = startMinute; i <= endMinute; i++) {
                minuteList.add(formatDate(i));
            }
        } else if (currentYear == startYear && currentMonth == startMonth &&
                currentDay == startDay && currentHour == startHour) {
            for (int i = startMinute; i <= 59; i++) {
                minuteList.add(formatDate(i));
            }
        } else if (currentYear == endYear && currentMonth == endMonth &&
                currentDay == endDay && currentHour == endHour) {
            for (int i = 0; i <= endMinute; i++) {
                minuteList.add(formatDate(i));
            }
        } else {
            for (int i = 0; i <= 59; i++) {
                minuteList.add(formatDate(i));
            }
        }
        return minuteList;
    }

    public static List<String> getSecondList(Calendar startCalendar, Calendar endCalendar, Calendar currentCalendar) {
        List<String> secondList = new ArrayList<>();
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);
        int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = startCalendar.get(Calendar.MINUTE);
        int startSecond = startCalendar.get(Calendar.SECOND);

        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonth = endCalendar.get(Calendar.MONTH);
        int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        int endMinute = endCalendar.get(Calendar.MINUTE);
        int endSecond = endCalendar.get(Calendar.SECOND);

        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentCalendar.get(Calendar.MINUTE);

        if (currentYear == startYear && currentMonth == startMonth &&
                currentYear == endYear && currentMonth == endMonth &&
                currentDay == startDay && currentDay == endDay &&
                currentHour == startHour && currentHour == endHour &&
                currentMinute == startMinute && currentMinute == endMinute) {
            for (int i = startSecond; i <= endSecond; i++) {
                secondList.add(formatDate(i));
            }
        } else if (currentYear == startYear && currentMonth == startMonth &&
                currentDay == startDay && currentHour == startHour &&
                currentMinute == startMinute) {
            for (int i = startSecond; i <= 59; i++) {
                secondList.add(formatDate(i));
            }
        } else if (currentYear == endYear && currentMonth == endMonth &&
                currentDay == endDay && currentHour == endHour &&
                currentMinute == endMinute) {
            for (int i = 0; i <= endSecond; i++) {
                secondList.add(formatDate(i));
            }
        } else {
            for (int i = 0; i <= 59; i++) {
                secondList.add(formatDate(i));
            }
        }
        return secondList;
    }

    public static String formatDate(int date) {
        return date < 10 ? "0" + date : String.valueOf(date);
    }
}
