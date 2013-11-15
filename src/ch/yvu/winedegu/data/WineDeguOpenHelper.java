package ch.yvu.winedegu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WineDeguOpenHelper extends SQLiteOpenHelper {

	public class WineTable {
		public static final String TABLE = "Wine";
		public static final String ID = "_id";
		public static final String NAME = "Name";
		public static final String PRICE = "Price";
		public static final String PICTURE = "Picture";
		public static final String COLOR = "Color";
		public static final String NOSE = "Nose";
		public static final String PALATE = "Palate";
		public static final String OVERALL = "Overall";
		public static final String DEGU = "Degu";
		
		public static final String CREATE = "CREATE TABLE " + TABLE + "("
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ NAME + " TEXT, "
				+ PRICE + " INTEGER, "
				+ PICTURE + " BLOB, "
				+ COLOR + " INTEGER, "
				+ NOSE + " INTEGER, "
				+ PALATE + " INTEGER, "
				+ OVERALL + " INTEGER, "
				+ DEGU + " INTEGER);";
	}
	
	public class DeguTable {
		public static final String TABLE = "Degu";
		public static final String ID = "_id";
		public static final String NAME = "Name";
		public static final String DESCRIPTION = "Description";
		
		public static final String CREATE = "CREATE TABLE " + TABLE + "("
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ NAME + " TEXT, "
				+ DESCRIPTION + " TEXT);";
	}
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "WineDegu";
	
	WineDeguOpenHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DeguTable.CREATE);
		db.execSQL(WineTable.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}