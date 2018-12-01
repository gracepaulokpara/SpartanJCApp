package app.my.SpartanJCApp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "contact_manager";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME_CONTACT = "contacts";
    public static final String TABLE_NAME_CONTACT_DATA = "contact_data";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CONTACT_FIRST_NAME = "first_name";
    public static final String COLUMN_CONTACT_LAST_NAME = "last_name";
    public static final String COLUMN_CONTACT_ADDRESS = "address";

    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_DATA_CATEGORY = "data_category";
    public static final String COLUMN_DATA_TYPE = "data_type";
    public static final String COLUMN_DATA_VALUE = "data_value";

    // these are the data categories, this will be put in the contact data table's data category column
    // to set the data category
    public static final String DATA_CATEGORY_PHONE = "phone";
    public static final String DATA_CATEGORY_EMAIL = "email";
    public static final String DATA_CATEGORY_SOCIAL = "social";
    public static final String DATA_CATEGORY_NOTE = "note";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String QUERY_TABLE_CONTACT = "CREATE TABLE " + TABLE_NAME_CONTACT + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CONTACT_FIRST_NAME + " TEXT NOT NULL, " +
                COLUMN_CONTACT_LAST_NAME + " TEXT NOT NULL, " +
                COLUMN_CONTACT_ADDRESS + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(QUERY_TABLE_CONTACT);

        String QUERY_TABLE_CONTACT_DATA = "CREATE TABLE " + TABLE_NAME_CONTACT_DATA + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CONTACT_ID + " TEXT NOT NULL, " +
                COLUMN_DATA_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_DATA_TYPE + " TEXT NOT NULL, " +
                COLUMN_DATA_VALUE + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(QUERY_TABLE_CONTACT_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
