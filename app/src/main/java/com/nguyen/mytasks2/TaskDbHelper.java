package com.nguyen.mytasks2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by My on 9/6/2016.
 */
public class TaskDbHelper extends SQLiteOpenHelper {
   private static final int DATABASE_VERSION = 1;
   static final String DATABASE_NAME = "task.db";

   public TaskDbHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase sqLiteDatabase) {
      final String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
            TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TaskContract.TaskEntry.COLUMN_UUID + " TEXT NOT NULL, " +
            TaskContract.TaskEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            TaskContract.TaskEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
            TaskContract.TaskEntry.COLUMN_PRIORITY + " INTEGER NOT NULL, " +
            TaskContract.TaskEntry.COLUMN_NOTE + " TEXT NOT NULL);";

      sqLiteDatabase.execSQL(SQL_CREATE_TASK_TABLE);
   }

   @Override
   public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
      sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
      onCreate(sqLiteDatabase);
   }
}
