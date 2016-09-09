package com.nguyen.mytasks2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by My on 9/8/2016.
 */
public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.ViewHolder> {
   public static class ViewHolder extends RecyclerView.ViewHolder {
      TextView name;
      TextView date;
      TextView priority;
      ImageButton edit;
      ImageButton delete;

      public ViewHolder(View view) {
         super(view);
         name = (TextView)view.findViewById(R.id.name);
         date = (TextView)view.findViewById(R.id.date);
         priority = (TextView)view.findViewById(R.id.priority);
         edit = (ImageButton) view.findViewById(R.id.edit);
         delete = (ImageButton)view.findViewById(R.id.delete);
      }
   }

   static final int REQUEST_CODE = 100;
   List<Task> mTasks;
   Context mContext;
   int mPosition;
   Task mTaskToDelete;

   public TasksRecyclerAdapter(Context context, List<Task> tasks) {
      mTasks = tasks;
      mContext = context;
   }

   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      View view = inflater.inflate(R.layout.item_task, parent, false);

      ViewHolder viewHolder = new ViewHolder(view);
      return viewHolder;
   }

   @Override
   public void onBindViewHolder(ViewHolder viewHolder, final int position) {
      final Task task = mTasks.get(position);

      // display the Task name
      viewHolder.name.setText(task.name);

      // set up color codes for 3 types of Tasks: expired Tasks in red, upcoming Tasks in Orange,
      // and future Tasks in Green
      long diff = System.currentTimeMillis() - task.date.getTime();
      int textColor;
      if (diff > 0) {
         // task that expired
         textColor = Color.RED;
      } else if (diff >= -86400000) {
         // task scheduled within one day
         textColor = Color.parseColor("#FFD700");
      } else {
         // task beyond one day
         // textColor = Color.GREEN;
         textColor = Color.parseColor("#006400");
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
   }

   @Override
   public int getItemCount() {
      return mTasks.size();
   }
}
