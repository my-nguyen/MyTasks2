package com.nguyen.mytasks2;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by My on 9/6/2016.
 */
public class TaskContract {
   // The Content Authority is a name for the entire content provider, similar to the relationship
   // between a domain name and its website. A convenient string to use for content authority is
   // the package name for the app, since it is guaranteed to be unique on the device
   public static final String CONTENT_AUTHORITY = "com.nguyen.mytasks";
   // The content authority is used to create the base of all URIs which apps will use to
   // contact this content provider
   public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
   // A list of possible paths that will be appended to the base URI for each of the different tables
   public static final String PATH_TASK = "task";

   public static final class TaskEntry implements BaseColumns {
      // Define the table schema
      public static final String TABLE_NAME = "task";
      public static final String COLUMN_UUID = "uuid";
      public static final String COLUMN_NAME = "name";
      public static final String COLUMN_DATE = "date";
      public static final String COLUMN_PRIORITY = "priority";
      public static final String COLUMN_NOTE = "note";

      // Content URI represents the base location for the table
      public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();
      // special type prefixes that specify if a URI returns a list or a specific item
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_TASK;
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_TASK;

      // Define a function to build a URI to find a specific task by it's identifier
      public static Uri buildTaskUri(long id){
         return ContentUris.withAppendedId(CONTENT_URI, id);
      }
   }
}
