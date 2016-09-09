package com.nguyen.mytasks2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by My on 9/6/2016.
 */
public class TaskDatabase {
   static TaskDatabase sInstance;
   Context mContext;
   SQLiteDatabase mDatabase;

   public static TaskDatabase instance(Context context) {
      if (sInstance == null) {
         sInstance = new TaskDatabase(context);
      }
      return sInstance;
   }

   private TaskDatabase(Context context) {
      // mContext = context.getApplicationContext();
      mContext = context;
      TaskDbHelper dbHelper = new TaskDbHelper(context);
      mDatabase = dbHelper.getWritableDatabase();
   }

   public List<Task> query() {
      List<Task> tasks = new ArrayList<>();
      Cursor cursor = mDatabase.query(TaskContract.TaskEntry.TABLE_NAME, null, null, null, null, null, null);
      try {
         cursor.moveToFirst();
         while (!cursor.isAfterLast()) {
            // retrieve all data worth of one Task from the database
            String uuid = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_UUID));
            String name = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
            long date = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DATE));
            int priority = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY));
            String note = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NOTE));

            // construct a Task in memory based on the database Task
            Task task = new Task(uuid, name, new Date(date), priority, note);
            tasks.add(task);
            cursor.moveToNext();
         }
      } finally {
         cursor.close();
      }

      return tasks;
   }

   public void insert(Task task) {
      ContentValues values = getContentValues(task);
      mDatabase.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
   }

   public void update(Task task) {
      ContentValues values = getContentValues(task);
      String whereClause = TaskContract.TaskEntry.COLUMN_UUID + " = ?";
      String[] whereArgs = { task.uuid };
      mDatabase.update(TaskContract.TaskEntry.TABLE_NAME, values, whereClause, whereArgs);
   }

   public void delete(Task task) {
      String whereClause = TaskContract.TaskEntry.COLUMN_UUID + " = ?";
      String[] whereArgs = { task.uuid };
      mDatabase.delete(TaskContract.TaskEntry.TABLE_NAME, whereClause, whereArgs);
   }

   private ContentValues getContentValues(Task task) {
      ContentValues values = new ContentValues();
      values.put(TaskContract.TaskEntry.COLUMN_UUID, task.uuid);
      values.put(TaskContract.TaskEntry.COLUMN_NAME, task.name);
      values.put(TaskContract.TaskEntry.COLUMN_DATE, task.date.getTime());
      values.put(TaskContract.TaskEntry.COLUMN_PRIORITY, task.priority);
      values.put(TaskContract.TaskEntry.COLUMN_NOTE, task.note);
      return values;
   }
}
