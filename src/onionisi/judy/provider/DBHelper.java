package onionisi.judy.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "judy.db";
	private static final int DATABASE_VERSION = 2;
	public static final String TABLES_TABLE_NAME = "TableTbl";
	public static final String TABLES_TABLE_NAME2 = "MenuTbl";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
       /* 
		db.execSQL("CREATE TABLE " + TABLES_TABLE_NAME + " ("
                + Tables._ID + " INTEGER PRIMARY KEY,"
                + Tables.NUM + " TEXT,"
                + Tables.DESCRIPTION + " TEXT"
                + ");");
        */
		db.execSQL("CREATE TABLE " + TABLES_TABLE_NAME2 + " ("
				+ Menus._ID + " INTEGER PRIMARY KEY, "
				+ Menus.TYPE_ID + " INTEGER,"
				+ Menus.NAME + " TEXT,"
				+ Menus.PRICE + " INTEGER,"
				+ Menus.PIC + " TEXT,"
				+ Menus.REMARK + " TEXT"
				+ ");");
	}

	// updating new version call this
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP TABLE IF EXISTS TableTbl");
		db.execSQL("DROP TABLE IF EXISTS MenuTbl");
		onCreate(db);
	}
}
