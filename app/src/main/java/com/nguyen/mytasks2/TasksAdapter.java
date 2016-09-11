package com.nguyen.mytasks2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by My on 9/4/2016.
 */
public class TasksAdapter extends ArrayAdapter<Task> {
   static final int REQUEST_CODE = 100;
   int mPosition;
   Task mTaskToDelete;
   Context mContext;

   static class ViewHolder {
      TextView name;
      TextView date;
      TextView priority;
      ImageButton edit;
      ImageButton delete;
   }

   public TasksAdapter(Context context, List<Task> tasks) {
      super(context, 0, tasks);
      mContext = context;
   }

   @Override
   public View getView(final int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
         LayoutInflater inflater = LayoutInflater.from(mContext);
         convertView = inflater.inflate(R.layout.item_task, parent, false);
         viewHolder = new ViewHolder();
         // save all the Views from calls to findViewById(), which is expensive
         viewHolder.name = (TextView)convertView.findViewById(R.id.name);
         viewHolder.date = (TextView)convertView.findViewById(R.id.date);
         viewHolder.priority = (TextView)convertView.findViewById(R.id.priority);
         viewHolder.edit = (ImageButton) convertView.findViewById(R.id.edit);
         viewHolder.delete = (ImageButton)convertView.findViewById(R.id.delete);
         convertView.setTag(viewHolder);
      } else {
         viewHolder = (ViewHolder)convertView.getTag();
      }

      // fetch the selected Task
      final Task task = getItem(position);

      // display the Task name
      viewHolder.name.setText(task.name);

      // set up color codes for 3 types of Tasks: expired Tasks in red, upcoming Tasks in Orange,
      // and future Tasks in Green
      long diff = System.currentTimeMillis() - task.date.getTime();
      int textColor;
      if (diff > 0) {
         // task that expired
         textColor = Color.parseColor("#CC0000");
      } else if (diff >= -86400000) {
         // task scheduled within one day
         textColor = Color.parseColor("#FFD700");
      } else {
         // task beyond one day
         textColor = Color.parseColor("#009900");
      }
      viewHolder.date.setTextColor(textColor);
      // format the Date and Time of Task
      String date = Utils.getShortDateFromDate(task.date);
      String time = Utils.getTimeFromDate(task.date);
      // display the Date and Time in the coded color
      viewHolder.date.setText(date + " at " + time);

      // display the Priority, also in the coded color
      String[] priorities = { "High", "Medium", "Low" };
      viewHolder.priority.setTextColor(textColor);
      viewHolder.priority.setText(priorities[task.priority]);

      // set up the Edit button
      viewHolder.edit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            // save the position of the current Task, so onActivityResult() can update the right one
            mPosition = position;
            // transition to DetailActivity
            Intent i = DetailActivity.newIntent(mContext, task);
            ((Activity)mContext).startActivityForResult(i, REQUEST_CODE);
         }
      });

      // set up the Delete button
      viewHolder.delete.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            // mark this task for delete
            mTaskToDelete = task;
            // TaskDeleteDialog, which is a DialogFragment, requires
            // android.support.v4.app.FragmentManager and not android.app.FragmentManager
            // the former is obtained via getSupportFragmentManager() which is from MainActivity
            // the latter is obtained via getFragmentManager() which is from Activity
            TaskDeleteDialog dialog = new TaskDeleteDialog();
            FragmentManager manager = ((MainActivity)mContext).getSupportFragmentManager();
            dialog.show(manager, "TASK_DELETE_DIALOG");
         }
      });

      return convertView;
   }

   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == REQUEST_CODE) {
         if (resultCode == ((Activity)mContext).RESULT_OK) {
            // extract the Task
            Task updatedTask = (Task)data.getSerializableExtra("TASK_OUT");
            // select the currently selected Task
            Task currentTask = getItem(mPosition);
            // update the Task in memory
            currentTask.update(updatedTask);
            // update the Task in local database
            TaskDatabase.instance(mContext).update(updatedTask);
            Toast.makeText(mContext, "The selected task has been updated", Toast.LENGTH_SHORT).show();
         }
      }
   }

   // on the Task Delete dialog, when user presses OK, since the dialog is a fragment, it could
   // only communicate back to the Activity invoking it, which is MainActivity, and not
   // TasksAdapter. so MainActivity must implement the communication callback, which calls this
   // method in order to do the actual removal of the Task record.
   public void onDeleteOK() {
      // remove the Task in memory
      remove(mTaskToDelete);
      // remove the Task from local database
      TaskDatabase.instance(mContext).delete(mTaskToDelete);
   }
}
