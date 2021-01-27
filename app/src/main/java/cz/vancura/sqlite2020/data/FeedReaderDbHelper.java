package cz.vancura.sqlite2020.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    private static String TAG = "myTAG-FeedReaderDbHelper";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    // contructor
    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create dB
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate db");
        db.execSQL("CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)");
    }

    // update dB structure, not data
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME);
        onCreate(db);
    }
}
