package uk.ac.aston.cs3mdd.weatherroute.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "RoutesLibrary.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "my_routes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LABEL = "route_title";
    private static final String START_POINT = "start_point";
    private static final String END_POINT = "end_point";
    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //to create the table in the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_LABEL + " TEXT, " +
                        START_POINT + " TEXT, " +
                        END_POINT + " TEXT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    //method to add routes information in the database
    public void addRoute(String label, String sPoint, String ePoint){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();

        data.put(COLUMN_LABEL, label);
        data.put(START_POINT, sPoint);
        data.put(END_POINT, ePoint);
        Log.d("Database debug", "add route method called");
        long result = db.insert(TABLE_NAME, null, data);
        if (result == -1) {
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully added the data!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readDate (){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null) {
            c = db.rawQuery(query, null);
        }
        return c;
    }

    //method to delete the data by
    public void deleteItembyId(String columnID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{columnID});
        db.close();
    }

    //method to update the data when the user edits/updates it on homefragment
    public void updateData(String routeId, String updatedRouteName, String updatedStartPoint, String updatedEndPoint) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_LABEL, updatedRouteName);
        values.put(START_POINT, updatedStartPoint);
        values.put(END_POINT, updatedEndPoint);

        int updatedRows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{routeId});
        Log.d("MyDatabase", "Updated rows: " + updatedRows);

        db.close();
    }

}
