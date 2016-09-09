package com.nguyen.mytasks2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by My on 9/6/2016.
 */
public class TasksGenerateDialog extends DialogFragment {
   interface TasksGenerateListener {
      void onTasksGenerateOK();
   }

   // empty constructor is required for DialogFragment
   public TasksGenerateDialog() {
   }

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle("Generate Tasks");
      builder.setMessage("Do you want to generate 40 random tasks?");
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
            TasksGenerateListener listener = (TasksGenerateListener)getActivity();
            listener.onTasksGenerateOK();
         }
      });
      builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
         }
      });

      return builder.create();
   }
}
