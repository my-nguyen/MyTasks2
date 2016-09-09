package com.nguyen.mytasks2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by My on 9/4/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
   interface DatePickerListener {
      void onFinishDate(int year, int month, int day);
   }

   // convenient static method to pass data into this Fragment
   public static DatePickerFragment newInstance(Date date)
   {
      Bundle bundle = new Bundle();
      bundle.putSerializable("DATE_IN", date);
      DatePickerFragment fragment = new DatePickerFragment();
      fragment.setArguments(bundle);
      return fragment;
   }

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      // receive a Date object
      Date date = (Date)getArguments().getSerializable("DATE_IN");
      // convert the Date object to a Calendar object, to extract the year, month and day, for
      // displaying them in the DatePickerDialog
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      int year = calendar.get(Calendar.YEAR);
      int month = calendar.get(Calendar.MONTH);
      int day = calendar.get(Calendar.DAY_OF_MONTH);
      // display the actual DatePickerDialog
      return new DatePickerDialog(getActivity(), this, year, month, day);
   }

   // callback from DatePickerDialog: pass on the year, month, and day to the calling Activity
   @Override
   public void onDateSet(DatePicker datePicker, int year, int month, int day) {
      DatePickerListener listener = (DatePickerListener)getActivity();
      listener.onFinishDate(year, month, day);
   }
}
