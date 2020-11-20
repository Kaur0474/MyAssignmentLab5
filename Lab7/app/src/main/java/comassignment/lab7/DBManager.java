package comassignment.lab7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public int versions (){
        return database.getVersion();
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String mess, Boolean sent) {
        int a = 0;
        if(sent){
a= 1;
        }
        else {
            a = 0;
        }

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.message, mess);
        contentValue.put(DatabaseHelper.sentorreceive, a);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.message, DatabaseHelper.sentorreceive };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }


    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public void printCursor(Cursor c, int version){

        Log.d("LAB5","Version :- "+version+"");
        int col_count = c.getColumnCount();
        Log.d("LAB5" , "Column Count:- "+col_count);

        String arr [] = c.getColumnNames();
        Log.d("LAB5" , "Column Names"+Arrays.toString(arr));
        int no_of_rows = 0;
        if (c.moveToFirst()){

            while(!c.isAfterLast()){
                long id = Long.parseLong(c.getString(c.getColumnIndex(DatabaseHelper._ID)));
                String message = c.getString(c.getColumnIndex(DatabaseHelper.message));
                boolean sent = Boolean.parseBoolean(c.getString(c.getColumnIndex(DatabaseHelper.sentorreceive)));

                Log.d("LAB5","ROWS DATA"+id+"..."+message+"......"+sent+"");
                no_of_rows++;
                 c.moveToNext();
            }
        }
        Log.d("LAB5" , "ROWS COUNT :- "+no_of_rows);


    }

}
