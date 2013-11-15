package ch.yvu.winedegu;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.yvu.winedegu.data.WineDeguOpenHelper.WineTable;


public class WineListAdapter extends CursorAdapter {
	
	public WineListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor c) {
		updateView(view, context, c);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout listItem = (LinearLayout) inflater.inflate(R.layout.wine_item, parent, false);
        updateView(listItem, context, c);
		return listItem;
	}

	private void updateView(View listItem, Context context, Cursor c){
		int idxPicture = c.getColumnIndex(WineTable.PICTURE);
		byte[] dataPicture = c.getBlob(idxPicture);
		Bitmap picture = BitmapFactory.decodeByteArray(dataPicture, 0, dataPicture.length);
		ImageView viewPicture = (ImageView) listItem.findViewById(R.id.wineItemPicture);
		viewPicture.setImageBitmap(picture);
		viewPicture.refreshDrawableState();
		
		int idxName = c.getColumnIndex(WineTable.NAME);
		TextView viewName = (TextView) listItem.findViewById(R.id.wineItemName);
		viewName.setText(c.getString(idxName));
		
		int idxPrice = c.getColumnIndex(WineTable.PRICE);
		int idxRatingColor = c.getColumnIndex(WineTable.COLOR);
		int idxRatingNose = c.getColumnIndex(WineTable.NOSE);
		int idxRatingPalate = c.getColumnIndex(WineTable.PALATE);
		int idxRatingOverall = c.getColumnIndex(WineTable.OVERALL);
		
		float price = c.getInt(idxPrice) / 100f;
		int rating = c.getInt(idxRatingColor) + c.getInt(idxRatingNose) + c.getInt(idxRatingPalate) + c.getInt(idxRatingOverall);
		float score = (float) rating /  (float) price;
		float scoreRounded = Math.round(score * 100) / 100f; 
		
		TextView viewScore = (TextView) listItem.findViewById(R.id.wineItemDescription);
		viewScore.setText("Points: " + rating + ", Price: " + price + ", Score: " + Float.toString(scoreRounded));
	}
}
