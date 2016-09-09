package com.nguyen.mytasks2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by My on 9/4/2016.
 */
public class Utils {
   public static String getShortDateFromDate(Date date) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("MM/d/yy");
      return dateFormat.format(date);
   }

   public static String getLongDateFromDate(Date date) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
      return dateFormat.format(date);
   }

   public static String getTimeFromDate(Date date) {
      SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
      return timeFormat.format(date);
   }
}
