package com.nguyen.mytasks2;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by My on 9/4/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
   interface TimePickerListener {
      void onFinishTime(int hour, int minute);
   }

   // convenient static method to pass data into this Fragment
   public static TimePickerFragment newInstance(Date date)
   {
      Bundle bundle = new Bundle();
      bundle.putSerializable("DATE_IN", date);
      TimePickerFragment fragment = new TimePickerFragment();
      fragment.setArguments(bundle);
      return fragment;
   }

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      // extract the Date object
      Date date = (Date)getArguments().getSerializable("DATE_IN");
      // convert the Date object to a Calendar object, to extract the day and minute, for
      // displaying them in the DatePickerDialog
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      Log.d("TRUONG", "date:" + date + ", calendar: " + calendar);
      int hour = calendar.get(Calendar.HOUR_OF_DAY);
      int minute = calendar.get(Calendar.MINUTE);
      // display the actual TimePickerDialog
      return new TimePickerDialog(getActivity(), this, hour, minute, false);
   }

   // callback from TimePickerDialog: pass on the hour and minute to the calling Activity
   @Override
   public void onTimeSet(TimePicker timePicker, int hour, int minute) {
      TimePickerListener listener = (TimePickerListener)getActivity();
      listener.onFinishTime(hour, minute);
   }
}
