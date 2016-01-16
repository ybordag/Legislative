package com.bordag.legislative;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Vijji on 1/9/2016.
 */
public class BillDbAdapter {
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static int KEY_LIKED;
    public static int KEY_DISLIKED;
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "BillDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table bills (_id integer primary key autoincrement, "
                    + "title text not null, body text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "bills";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS bills");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public BillDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the bills database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public BillDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new bill using the title and body provided. If the bill is
     * successfully created return the new rowId for that bill, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the bill
     * @param body the body of the bill
     * <@param numLiked the number liked of the bill
     * <@param numDisliked the number disliked of the bill
     * @return rowId or -1 if failed
     */
    public long createbill(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        //initialValues.put(KEY_LIKED, numLiked);
        //initialValues.put(KEY_DISLIKED, numDisliked);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the bill with the given rowId
     *
     * @param rowId id of bill to delete
     * @return true if deleted, false otherwise
     */
    public boolean deletebill(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all bills in the database
     *
     * @return Cursor over all bills
     */
    public Cursor fetchAllbills() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, /**KEY_LIKED, KEY_DISLIKED**/}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the bill that matches the given rowId
     *
     * @param rowId id of bill to retrieve
     * @return Cursor positioned to matching bill, if found
     * @throws SQLException if bill could not be found/retrieved
     */
    public Cursor fetchbill(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY, /**KEY_LIKED, KEY_DISLIKED**/}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the bill using the details provided. The bill to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of bill to update
     * @param title value to set bill title to
     * @param body value to set bill body to
     * <@param numLiked value to set bill liked to
     * <@param numDisliked value to set bill disliked to
     * @return true if the bill was successfully updated, false otherwise
     */
    public boolean updatebill(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        //args.put(KEY_LIKED, numLiked);
        //args.put(KEY_DISLIKED, numDisliked);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}

