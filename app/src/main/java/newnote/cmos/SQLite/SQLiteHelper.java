package newnote.cmos.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JIN on 1/19/2016.
 */
public class SQLiteHelper<T> extends SQLiteOpenHelper {

    public static String DB_PATH = "/data/data/ice.tea09.sqlitedemo/databases/";
    public static String DB_NAME = "Cmos.sqlite";
    public static final int DB_VERSION = 1;
    private List<T> lst;
    private T clazz;
    private Class<T> BaseClazz;
    public static String TABLE_NAME = "Users";

    private SQLiteDatabase myDB;
    private Context context;

    public SQLiteHelper(Context context, Class<T> baseClazz) {

        super(context, DB_NAME, null, DB_VERSION);
        BaseClazz = baseClazz;

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.context = context;
        try {
            clazz = BaseClazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        TABLE_NAME = clazz.getClass().getSimpleName();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public synchronized void close() {
        if (myDB != null) {
            myDB.close();
        }
        super.close();
    }

    public List<T> getAll() {
        List<T> lst = new ArrayList<T>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (c == null) return null;

            String name;
            c.moveToFirst();
            T newIntance = BaseClazz.newInstance();
            do {
                for (Field field : newIntance.getClass().getFields()
                        ) {
                    switch (field.getType().getSimpleName()) {
                        case "int":
                            field.set(newIntance, c.getInt(c.getColumnIndex(field.getName())));
                            break;
                        case "String":
                            field.set(newIntance, c.getString(c.getColumnIndex(field.getName())));
                            break;
                        case "Double":
                            field.set(newIntance, c.getDouble(c.getColumnIndex(field.getName())));
                            break;
                        case "byte[]":

                            field.set(newIntance,c.getBlob(c.getColumnIndex(field.getName())));
                    }
                }

                //c.getColumnNames();
                lst.add(newIntance);
                newIntance = BaseClazz.newInstance();
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }


        db.close();

        return lst;
    }


    /***
     * Open database
     *
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /***
     * Copy database from source code assets to device
     *
     * @throws IOException
     */
    public void copyDataBase() throws IOException {
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("tle99 - copyDatabase", e.getMessage());
        }

    }

    /***
     * Check if the database doesn't exist on device, create new one
     *
     * @throws IOException
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
             copyDataBase();
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("tle99 - create", e.getMessage());
            }
        }
    }

    // ---------------------------------------------
    // PRIVATE METHODS
    // ---------------------------------------------

    /***
     * Check if the database is exist on device or not
     *
     * @return
     */
    private boolean checkDataBase() {
        SQLiteDatabase tempDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            Log.e("tle99 - check", e.getMessage());
        }
        if (tempDB != null)
            tempDB.close();
        return tempDB != null ? true : false;
    }


}