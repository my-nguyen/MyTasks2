package com.nguyen.mytasks2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by My on 9/4/2016.
 */
public class Task implements Serializable {
   // UUID is used to identify the single record to update in the database
   public String uuid;
   public String name;
   // use Date instead of Calendar here because Calendar is a global object, so adding or subtracting
   // days on a Calendar object will have its global effect. whereas Date is local, so such
   // modification has no effect.
   public Date date;
   public int priority;
   public String note;

   public Task() {
      init(UUID.randomUUID().toString(), "", new Date(), 0, "");
   }

   public Task(String uuid, String name, Date date, int priority, String note) {
      init(uuid, name, date, priority, note);
   }

   public void update(Task rhs) {
      name = rhs.name;
      date = rhs.date;
      priority = rhs.priority;
      note = rhs.note;
   }

   @Override
   public String toString() {
      final String SEPARATOR = "::";
      StringBuilder builder = new StringBuilder();
      builder.append(name).append(SEPARATOR);
      SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy h:m a");
      String dateString = format.format(date);
      builder.append(dateString).append(SEPARATOR);
      builder.append(priority).append(SEPARATOR);
      builder.append(note);
      return builder.toString();
   }

   private void init(String uuid, String name, Date date, int priority, String note) {
      this.uuid = uuid;
      this.name = name;
      this.date = date;
      this.priority = priority;
      this.note = note;
   }
}
