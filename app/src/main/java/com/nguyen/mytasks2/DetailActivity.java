package com.nguyen.mytasks2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity
      implements DatePickerFragment.DatePickerListener, TimePickerFragment.TimePickerListener {
   Task mTask;
   TextView mDueDate;
   TextView mDueTime;

   // convenient static method to pass data into DetailActivity
   public static Intent newIntent(Context context, Task task) {
      Intent i = new Intent(context, DetailActivity.class);
      i.putExtra("TASK_IN", task);
      return i;
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);

      // extract all the view ids
      final EditText taskName = (EditText)findViewById(R.id.task_name);
      mDueDate = (TextView)findViewById(R.id.due_date);
      ImageButton datePicker = (ImageButton)findViewById(R.id.date_picker);
      mDueTime = (TextView)findViewById(R.id.due_time);
      ImageButton timePicker = (ImageButton)findViewById(R.id.time_picker);
      Spinner prioritySpinner = (Spinner)findViewById(R.id.priority_spinner);
      final EditText noteText = (EditText)findViewById(R.id.note_text);
      Button save = (Button)findViewById(R.id.save);
      Button cancel = (Button)findViewById(R.id.cancel);

      // extract the Task passed in (from either MainActivity or TasksAdapter)
      mTask = (Task)getIntent().getSerializableExtra("TASK_IN");
      if (mTask == null) {
         // create a new Task if there isn't one already. the Task constructor will create a Date
         // object that includes the date and time necessary for this Activity
         mTask = new Task();
         getSupportActionBar().setTitle("New Task");
      } else {
         getSupportActionBar().setTitle("Edit Task");
      }

      // set the Task name
      taskName.setText(mTask.name);

      // set the Task date and time
      final Date date = mTask.date;
      mDueDate.setText(Utils.getLongDateFromDate(date));
      mDueTime.setText(Utils.getTimeFromDate(date));

      // set up the DatePickerFragment
      datePicker.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            DialogFragment fragment = DatePickerFragment.newInstance(date);
            fragment.show(getSupportFragmentManager(), "DATE_PICKER");
         }
      });

      // set up the TimePickerFragment
      timePicker.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            DialogFragment fragment = TimePickerFragment.newInstance(date);
            fragment.show(getSupportFragmentManager(), "TIME_PICKER");
         }
      });

      // set up the Priority spinner
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.priority_array, android.R.layout.simple_spinner_item);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      prioritySpinner.setAdapter(adapter);
      // default priority is fed from Task
      prioritySpinner.setSelection(mTask.priority);
      prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mTask.priority = position;
         }
         @Override
         public void onNothingSelected(AdapterView<?> parent) {
         }
      });

      // set up the Note
      noteText.setText(mTask.note);

      // set up the Save button
      save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            // save name and note; the date, time and priority have all been saved already
            mTask.name = taskName.getText().toString();
            mTask.note = noteText.getText().toString();

            // send the data back to MainActivity
            Intent i = new Intent();
            i.putExtra("TASK_OUT", mTask);
            setResult(RESULT_OK, i);

            // dismiss this Activity
            finish();
         }
      });

      // set up the Cancel button
      cancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            finish();
         }
      });
   }

   // callback method from DatePickerFragment
   @Override
   public void onFinishDate(int year, int month, int day) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(mTask.date);
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.DAY_OF_MONTH, day);
      // save the year, month and day in Task
      mTask.date = calendar.getTime();
      // update the Due Date TextView
      mDueDate.setText(Utils.getLongDateFromDate(mTask.date));
   }

   // callback method from TimePickerFragment
   @Override
   public void onFinishTime(int hour, int minute) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(mTask.date);
      calendar.set(Calendar.HOUR_OF_DAY, hour);
      calendar.set(Calendar.MINUTE, minute);
      // save the hour and minute in Task
      mTask.date = calendar.getTime();
      // update the Due Time TextView
      mDueTime.setText(Utils.getTimeFromDate(mTask.date));
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // get the MenuItem with ShareActionProvider
      getMenuInflater().inflate(R.menu.menu_detail, menu);
      MenuItem item = menu.findItem(R.id.share_action);

      // get reference to the ShareActionProvider
      ShareActionProvider provider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

      // create and invoke implicit intent with ACTION_SEND
      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.setType("text/plain");
      intent.putExtra(Intent.EXTRA_TEXT, mTask.toString());
      provider.setShareIntent(intent);

      return super.onCreateOptionsMenu(menu);
   }
}
