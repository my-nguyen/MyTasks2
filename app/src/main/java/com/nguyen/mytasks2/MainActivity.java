package com.nguyen.mytasks2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TaskDeleteDialog.TaskDeleteListener {
   static int REQUEST_CODE = 101;
   TasksAdapter mAdapter;
   // sort criteria, which is 0 (sort by Name) by default
   int mCriteria = 0;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // Stetho.initializeWithDefaults(this);

      /*
      List<Task> tasks = TaskDatabase.instance(this).query();
      TasksRecyclerAdapter adapter =   new TasksRecyclerAdapter(this, tasks);
      RecyclerView listView = (RecyclerView) findViewById(R.id.tasks);
      listView.setAdapter(adapter);
      listView.setLayoutManager(new LinearLayoutManager(this));
      */

      // read existing Task's from the local database
      List<Task> tasks = TaskDatabase.instance(this).query();
      // List<Task> tasks = generateTasks();
      // set up the adapter for the ListView
      mAdapter = new TasksAdapter(this, tasks);
      ListView listView = (ListView)findViewById(R.id.tasks);
      listView.setAdapter(mAdapter);

      // set up the spinner to sort by Date, Name, or Priority
      Spinner sortOrder = (Spinner)findViewById(R.id.sort_spinner);
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.sort_order_array, android.R.layout.simple_spinner_item);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      sortOrder.setAdapter(adapter);
      // the default criteria is sort by Date
      sortOrder.setSelection(adapter.getPosition("By Date"));
      sortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mCriteria = position;
            sortByCriteria();
         }
         @Override
         public void onNothingSelected(AdapterView<?> parent) {
         }
      });

      // set up FloatingActionButton so that upon a click, it will lead to DetailActivity to create
      // a new Task
      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent i = DetailActivity.newIntent(MainActivity.this, null);
            startActivityForResult(i, REQUEST_CODE);
         }
      });
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == REQUEST_CODE) {
         // request code matches: this data returned from DetailActivity, which was initiated by
         // FloatingActionButton, so add a new Task
         if (resultCode == RESULT_OK) {
            // extract the Task sent back from DetailActivity
            Task task = (Task)data.getSerializableExtra("TASK_OUT");
            // add the new Task into the current list in memory
            mAdapter.add(task);
            // save the new Task to local database
            TaskDatabase.instance(this).insert(task);
            // sort the list of Tasks and update the UI
            sortByCriteria();
            Toast.makeText(this, "A new task has been created", Toast.LENGTH_SHORT).show();
         }
      } else {
         // request code doesn't match: this data returned from DetailActivity, and was initiated by
         // TasksAdapter, so forward the data to TasksAdapter to update the list of Tasks in memory
         mAdapter.onActivityResult(requestCode, resultCode, data);
         // sort the list of Tasks and update the UI
         sortByCriteria();
      }
   }

   // callback from TaskDeleteDialog: forward the action to TasksAdapter
   @Override
   public void onTaskDeleteOK() {
      mAdapter.onDeleteOK();
   }

   // this method sorts the Tasks in the current adapter by a criteria (Date, Name or Priority),
   // then refresh the UI to reflect the sorted list
   private void sortByCriteria() {
      switch (mCriteria) {
         case 0:
            // sort by Date
            mAdapter.sort(new DateComparator());
            break;
         case 1:
            // sort by Name
            mAdapter.sort(new NameComparator());
            break;
         case 2:
            // sort by Priority
            mAdapter.sort(new PriorityComparator());
            break;
      }
      // cause the UI to refresh to show the updated list
      mAdapter.notifyDataSetChanged();
   }

   private List<Task> generateTasks() {
      // create a String[] instead of a List<String> for ease of declaration
      String[] strings = {
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Ein", "Zwei", "Drei", "Vier", "Funf", "Sechs", "Sieben", "Acht", "Neun", "Zehn",
            "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix",
            "Uno", "Dos", "Tres", "Quatro", "Cinco", "Seis", "Siete", "Ocho", "Nueve", "Diez"
      };
      // copy the contents of the array into a List
      List<String> names = new ArrayList<>();
      for (String string : strings) {
         names.add(string);
      }
      String string = "ut aliquid scire se gaudeant quod quidem iam fit etiam in academia ita relinquet duas de quibus etiam atque etiam consideret haec para doca illi nos admirabilia dicamus inscite autem medicinae et gubernationis ultimum cum ultimo sapientiae comparatur atqui reperies inquit in hoc quidem pertinacem sed quid attinet de rebus tam apertis plura requirere mihi quidem antiochum quem audis satis belle videris attendere primum in nostrane potestate est quid meminerimus";
      String[] notes = string.split(" ");
      // prepare the random generator
      Random random = new Random();
      List<Task> tasks = new ArrayList<>();

      while (names.size() != 0) {
         // pick a random UUID
         String uuid = UUID.randomUUID().toString();
         // pick a random name from the List
         int randomName = random.nextInt(names.size());
         // remove the name from the List when done
         String name = names.remove(randomName);
         // pick a random day from 5 days in the past until 5 days in the future
         Calendar calendar = Calendar.getInstance();
         int randomDay = random.nextInt(10) - 5;
         calendar.add(Calendar.DAY_OF_MONTH, randomDay);
         // pick a random hour
         int randomHour = random.nextInt(24);
         calendar.set(Calendar.HOUR_OF_DAY, randomHour);
         // pick a random minute
         int randomMinute = random.nextInt(60);
         calendar.set(Calendar.MINUTE, randomMinute);
         // pick a random priority (0 = High, 1 = Medium, and 2 = Low)
         int randomPriority = random.nextInt(3);
         // pick a random note word
         int index = random.nextInt(notes.length);
         String note = notes[index];
         // create a new Task with the random UUID, random name, random date, and random priority
         Task task = new Task(uuid, name, calendar.getTime(), randomPriority, note);
         tasks.add(task);
         // save to local database
         TaskDatabase.instance(this).insert(task);
      }
      return tasks;
   }

   class DateComparator implements Comparator<Task> {
      @Override
      public int compare(Task task1, Task task2) {
         return (int)(task1.date.getTime() - task2.date.getTime());
      }
   }

   class NameComparator implements Comparator<Task> {
      @Override
      public int compare(Task task1, Task task2) {
         return task1.name.compareTo(task2.name);
      }
   }

   class PriorityComparator implements Comparator<Task> {
      @Override
      public int compare(Task task1, Task task2) {
         return task1.priority - task2.priority;
      }
   }
}
