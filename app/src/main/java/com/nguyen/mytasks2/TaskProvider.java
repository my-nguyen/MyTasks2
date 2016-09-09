package com.nguyen.mytasks2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by My on 9/8/2016.
 */
public class TaskProvider extends ContentProvider {
   // Use an int for each URI we will run, this represents the different queries
   private static final int TASK = 100;
   private static final int TASK_ID = 101;
   private static final UriMatcher sUriMatcher = buildUriMatcher();
   private TaskDbHelper dbHelper;

   // this method builds a UriMatcher that is used to determine which database request is being made
   public static UriMatcher buildUriMatcher(){
      String content = TaskContract.CONTENT_AUTHORITY;

      // All paths to the UriMatcher have a corresponding code to return when a match is found
      // (the ints above).
      UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
      matcher.addURI(content, TaskContract.PATH_TASK, TASK);
      matcher.addURI(content, TaskContract.PATH_TASK + "/#", TASK_ID);

      return matcher;
   }

   @Override
   public boolean onCreate() {
      dbHelper = new TaskDbHelper(getContext());
      return true;
   }

   // this method is used to find the MIME type of the results, either a directory of multiple
   // results, or an individual item
   @Nullable
   @Override
   public String getType(Uri uri) {
      switch (sUriMatcher.match(uri)) {
         case TASK:
            return TaskContract.TaskEntry.CONTENT_TYPE;
         case TASK_ID:
            return TaskContract.TaskEntry.CONTENT_ITEM_TYPE;
         default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
   }

   /*
   * this method takes in five parameters:
   * 1. uri: The URI (or table) that should be queried.
   * 2. projection: A string array of columns that will be returned in the result set.
   * 3. selection: A string defining the criteria for results to be returned.
   * 4. selectionArgs: Arguments to the above criteria that rows will be checked against.
   * 5. sortOrder: A string of the column(s) and order to sort the result set by.
   */
   @Nullable
   @Override
   public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
      final SQLiteDatabase db = dbHelper.getWritableDatabase();
      Cursor cursor;
      switch (sUriMatcher.match(uri)) {
         case TASK:
            cursor = db.query(TaskContract.TaskEntry.TABLE_NAME, projection,
                  selection, selectionArgs, null, null, sortOrder);
            break;
         case TASK_ID:
            long id = ContentUris.parseId(uri);
            selection = TaskContract.TaskEntry._ID + " = ?";
            selectionArgs = new String[]{ String.valueOf(id) };
            cursor = db.query(TaskContract.TaskEntry.TABLE_NAME, projection,
                  selection, selectionArgs, null, null, sortOrder);
            break;
         default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
      }

      // Set the notification URI for the cursor to the one passed into the function. This causes
      // the cursor to register a content observer to watch for changes that happen to this URI and
      // any of it's descendants. By descendants, we mean any URI that begins with this path.
      cursor.setNotificationUri(getContext().getContentResolver(), uri);
      return cursor;
   }

   // this method takes in a ContentValues object, which is a key value pair of column names and
   // values to be inserted
   @Nullable
   @Override
   public Uri insert(Uri uri, ContentValues values) {
      final SQLiteDatabase db = dbHelper.getWritableDatabase();
      long id;
      Uri returnUri;

      switch(sUriMatcher.match(uri)) {
         case TASK:
            id = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
            if (id > 0) {
               returnUri = TaskContract.TaskEntry.buildTaskUri(id);
            } else {
               throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
            }
            break;
         default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
      }

      // Use this on the URI passed into the function to notify any observers that the uri has changed
      getContext().getContentResolver().notifyChange(uri, null);
      return returnUri;
   }

   // this method takes in a selection string and arguments to define which rows should be deleted
   @Override
   public int delete(Uri uri, String selection, String[] selectionArgs) {
      final SQLiteDatabase db = dbHelper.getWritableDatabase();
      int rows; // Number of rows effected

      switch(sUriMatcher.match(uri)) {
         case TASK:
            rows = db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
            break;
         default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
      }

      // Because null could delete all rows:
      if(selection == null || rows != 0){
         getContext().getContentResolver().notifyChange(uri, null);
      }
      return rows;
   }

   // this method takes in a selection string and arguments to define which rows should be updated
   @Override
   public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
      final SQLiteDatabase db = dbHelper.getWritableDatabase();
      int rows;

      switch(sUriMatcher.match(uri)){
         case TASK:
            rows = db.update(TaskContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);
            break;
         default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
      }

      if(rows != 0){
         getContext().getContentResolver().notifyChange(uri, null);
      }
      return rows;
   }
}
