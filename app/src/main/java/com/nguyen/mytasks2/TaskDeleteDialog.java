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
public class TaskDeleteDialog extends DialogFragment {
   interface TaskDeleteListener {
      void onTaskDeleteOK();
   }

   // empty constructor is required for DialogFragment
   public TaskDeleteDialog() {
   }

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle("Delete Task");
      builder.setMessage("Are you sure you want to delete this task?");
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
            TaskDeleteListener listener = (TaskDeleteListener)getActivity();
            // MainActivity activity = (MainActivity)getActivity();
            // TasksAdapter adapter = activity.getAdapter();
            listener.onTaskDeleteOK();
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
