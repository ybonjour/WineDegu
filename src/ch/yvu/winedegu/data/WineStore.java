package ch.yvu.winedegu.data;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import ch.yvu.winedegu.data.WineDeguOpenHelper.DeguTable;
import ch.yvu.winedegu.data.WineDeguOpenHelper.WineTable;


public class WineStore {
	
	public class Degu {
		private final String name;
		private final String description;
		
		public Degu(String name, String description) {
			this.name = name;
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}
		
		public ContentValues getContentValues(){
			ContentValues values = new ContentValues();
			values.put(DeguTable.NAME, this.name);
			values.put(DeguTable.DESCRIPTION, this.description);
			
			return values;
		}
	}
	
	public class Wine {
		private final String name;
		private final int price;
		private final Bitmap picture;
		private final int ratingColor;
		private final int ratingNose;
		private final int ratingPalate;
		private final int ratingOverall;
		private final long degu;
		
		public Wine(String name, int price, Bitmap picture, int ratingColor, int ratingNose, int ratingPalate, int ratingOverall, long degu) {
			this.name = name;
			this.price = price;
			this.picture = picture;
			this.ratingColor = ratingColor;
			this.ratingNose = ratingNose;
			this.ratingPalate = ratingPalate;
			this.ratingOverall = ratingOverall;
			this.degu = degu;
		}
		
		public String getName() {
			return name;
		}

		public int getPrice() {
			return price;
		}

		public Bitmap getPicture() {
			return picture;
		}

		public int getRatingColor() {
			return ratingColor;
		}

		public int getRatingNose() {
			return ratingNose;
		}

		public int getRatingPalate() {
			return ratingPalate;
		}

		public int getRatingOverall() {
			return ratingOverall;
		}
		
		public long getDegu() {
			return degu;
		}
		
		public ContentValues getContentValues(){
			ContentValues values = new ContentValues();
			values.put(WineTable.NAME, this.name);
			values.put(WineTable.PRICE, this.price);
			values.put(WineTable.PICTURE, getBitmapAsByteArray(this.picture));
			values.put(WineTable.COLOR, this.ratingColor);
			values.put(WineTable.NOSE, this.ratingNose);
			values.put(WineTable.PALATE, this.ratingPalate);
			values.put(WineTable.OVERALL, this.ratingOverall);
			values.put(WineTable.DEGU, this.degu);
			
			return values;
		}
		
	}
	
	private WineDeguOpenHelper dbHelper;
	
	private WineStore(WineDeguOpenHelper dbHelper){
		if(dbHelper == null) throw new IllegalArgumentException("dbHelper");
		
		this.dbHelper = dbHelper;
	}
	
	public static WineStore create(Context context)
	{
		WineDeguOpenHelper helper = new WineDeguOpenHelper(context);
		return new WineStore(helper);
	}
	
	public Degu getDegu(long id) {
		String strQuery = "SELECT * FROM " + DeguTable.TABLE + " WHERE " + DeguTable.ID + "=?";
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery(strQuery, new String[]{Long.toString(id)});
		
		if (c.getCount() != 1 || !c.moveToFirst()) return null;
		
		return new Degu(c.getString(c.getColumnIndex(DeguTable.NAME)), c.getString(c.getColumnIndex(DeguTable.DESCRIPTION)));
	}
	
	public void addDegu(Degu degu) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.insert(DeguTable.TABLE, null, degu.getContentValues());
	}
	
	public void updateDegu(long id, Degu degu) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.update(DeguTable.TABLE, degu.getContentValues(), DeguTable.ID + "=?", new String[]{Long.toString(id)});
	}
	
	public void deleteDegu(long id) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.delete(WineTable.TABLE, WineTable.DEGU + "=?", new String[]{Long.toString(id)});
		db.delete(DeguTable.TABLE, DeguTable.ID + "=?", new String[]{Long.toString(id)});
	}	
	
	public Cursor allDegus(){
		String strQuery = "SELECT * FROM " + DeguTable.TABLE;
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.rawQuery(strQuery, null);
	}
	
	public void addWine(Wine wine){
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.insert(WineTable.TABLE, null, wine.getContentValues());
	}
	
	public void updateWine(long id, Wine wine) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.update(WineTable.TABLE, wine.getContentValues(), WineTable.ID + "=?", new String[]{Long.toString(id)});
	}
	
	public void deleteWine(long id) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.delete(WineTable.TABLE, WineTable.ID + "=?", new String[]{Long.toString(id)});
	}	
	
	public Cursor allWines(long degu){
		String strQuery = "SELECT * FROM " + WineTable.TABLE
				+ " WHERE " + WineTable.DEGU + "=?"
				+ " ORDER BY (" + WineTable.COLOR + "+"
				+ WineTable.NOSE + "+" + WineTable.PALATE
				+ "+" + WineTable.OVERALL + ") / (1.0*" + WineTable.PRICE + ") DESC;";
		
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.rawQuery(strQuery, new String[]{Long.toString(degu)});
	}
	
	public Wine getWine(long id){
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		String[] selectionArgs = new String[]{ Long.toString(id) };
		String strQuery = "SELECT * FROM " + WineTable.TABLE + " WHERE " + WineTable.ID + "=?;";
		Cursor c = db.rawQuery(strQuery, selectionArgs);
		
		if (c.getCount() != 1 || !c.moveToFirst()) return null;
		
		byte[] dataPicture = c.getBlob(c.getColumnIndex(WineTable.PICTURE));
		Bitmap bitmapPicture = BitmapFactory.decodeByteArray(dataPicture, 0, dataPicture.length);
		
		Wine wine = new Wine(c.getString(c.getColumnIndex(WineTable.NAME)),
				c.getInt(c.getColumnIndex(WineTable.PRICE)),
				bitmapPicture,
				c.getInt(c.getColumnIndex(WineTable.COLOR)),
				c.getInt(c.getColumnIndex(WineTable.NOSE)),
				c.getInt(c.getColumnIndex(WineTable.PALATE)),
				c.getInt(c.getColumnIndex(WineTable.OVERALL)),
				c.getLong(c.getColumnIndex(WineTable.DEGU)));
		
		c.close();
		return wine;
	}
	
	public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.PNG, 0, outputStream);       
	    return outputStream.toByteArray();
	}
	
}