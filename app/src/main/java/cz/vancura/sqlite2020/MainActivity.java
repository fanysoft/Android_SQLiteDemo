package cz.vancura.sqlite2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.vancura.sqlite2020.data.FeedReaderContract;
import cz.vancura.sqlite2020.data.FeedReaderDbHelper;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "myTAG";
    private static SQLiteDatabase db;
    TextView textView;
    EditText editText;
    Button buttonRead, buttonDelete, buttonInsert, buttonUpdate;
    int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "actvity started");

        textView = findViewById(R.id.textview);
        editText = findViewById(R.id.editTextId);
        buttonInsert= findViewById(R.id.buttonInsert);
        buttonRead = findViewById(R.id.buttonRead);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // dB instance
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);

        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase();


        // dB - insert
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "dB insert");
                textView.setText("dB insert\n");

                if (editText.getText().length() > 0) {
                    number = Integer.parseInt(editText.getText().toString());

                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "title " + number);
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "subtitle " + number);

                    // Insert the new row, returning the primary key value of the new row
                    long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

                    textView.append("Inserted id=" + newRowId + " title" + number);

                }else{
                    textView.setText("insert number");
                }

            }
        });


        // db - read
        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "dB read");
                textView.setText("dB read\n");

                // Define a projection that specifies which columns from the database you will actually use after this query.
                String[] projection = {
                        BaseColumns._ID,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
                };

                // Filter results WHERE "title" = 'My Title'
                //String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
                //String[] selectionArgs = { "My Title" };

                // How you want the results sorted in the resulting Cursor
                String sortOrder = FeedReaderContract.FeedEntry._ID;

                Cursor cursor = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                        projection,             // The array of columns to return (pass null to get all)
                        null,              // The columns for the WHERE clause
                        null,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        sortOrder               // The sort order
                );

                if (cursor!= null) {
                    int colCount = cursor.getColumnCount();
                    int rowCount =  cursor.getCount();
                    Log.d(TAG, "Cursor has " + colCount + " collumns and " + rowCount + " rows");
                    String[] columnNames = cursor.getColumnNames();
                    int loopCol=0;
                    for (String s: columnNames) {
                        Log.d(TAG, "Cursor column name = " + columnNames[loopCol]);
                        loopCol++;
                    }


                    while(cursor.moveToNext()) {
                        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                        String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                        //String itemsubtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));
                        String all = "id="+itemId + " title=" + itemTitle + "\n";

                        Log.d(TAG, all);
                        textView.append(all);
                    }
                    cursor.close();

                }else{
                    Log.d(TAG, "Cursor is null");
                    textView.append("No data so far");
                }

            }
        });

        // dB - delete
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "dB delete");
                textView.setText("dB delete\n");

                if (editText.getText().length() > 0) {
                    number = Integer.parseInt(editText.getText().toString());

                    // Define 'where' part of query.
                    String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
                    // Specify arguments in placeholder order.
                    String what = "title " + number;
                    String[] selectionArgs = { what };
                    // Issue SQL statement
                    int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

                    textView.append("Deleting rows with title " + number + " - deleted rows=" + deletedRows);

                }else{
                    textView.setText("insert number");
                }

            }
        });

        // dB - update
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "dB update");
                textView.setText("dB update\n");

                if (editText.getText().length() > 0) {
                    number = Integer.parseInt(editText.getText().toString());

                    // New value for one column
                    String title = "title " + number + " NEW";
                    ContentValues values = new ContentValues();
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);

                    // Which row to update, based on the title
                    String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
                    String what = "title " + number;
                    String[] selectionArgs = { what };

                    int count = db.update(
                            FeedReaderContract.FeedEntry.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs);

                    textView.append("Updating items with title " + number + " to title " + number + " NEW - updated rows=" + count);

                }else{
                    textView.setText("insert number");
                }

            }
        });


    }
}