package ch.yvu.winedegu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import ch.yvu.winedegu.data.WineStore;
import ch.yvu.winedegu.data.WineStore.Wine;

public class WineDetail extends Activity {

	public static final String WINE_ID_KEY = "WineID";
	
	private static int RC_TAKE_PICTURE = 1;
	private ImageView _viewPicture;
	private ImageButton	_btnTakePicture;
	private Button _btnSave;
	private EditText _txtName;
	private EditText _txtPrice;
	private NumberPicker _ratingColor;
	private NumberPicker _ratingNose;
	private NumberPicker _ratingPalate;
	private NumberPicker _ratingOverall;
	private WineStore _store;
	private Wine _wine;
	private long _wineId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wine_detail);
		
		_btnSave = (Button) this.findViewById(R.id.addWine);
		_btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveWine();
			}
		});
		
		_btnTakePicture = (ImageButton) this.findViewById(R.id.takePicture);
		_btnTakePicture.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				takePicture();
			}
		});
		
		_store = WineStore.create(getApplicationContext());
		
		Bundle extras = getIntent().getExtras();
		_wineId = extras.getLong(WINE_ID_KEY);
		_wine = _store.getWine(_wineId);
		if(_wine == null) {
			_btnSave.setEnabled(false);
			_btnTakePicture.setEnabled(false);
			return;
		}
		
		_txtName = (EditText) this.findViewById(R.id.name);
		_txtName.setText(_wine.getName());
		
		_txtPrice = (EditText) this.findViewById(R.id.price);
		_txtPrice.setText(Float.toString(_wine.getPrice() / 100f));
		
		_ratingColor = (NumberPicker) this.findViewById(R.id.ratingColor);
		_ratingColor.setMinValue(1);
		_ratingColor.setMaxValue(3);
		_ratingColor.setValue(_wine.getRatingColor());
		
		_ratingNose = (NumberPicker) this.findViewById(R.id.ratingNose);
		_ratingNose.setMinValue(1);
		_ratingNose.setMaxValue(7);
		_ratingNose.setValue(_wine.getRatingNose());
		
		_ratingPalate = (NumberPicker) this.findViewById(R.id.ratingPalate);
		_ratingPalate.setMinValue(1);
		_ratingPalate.setMaxValue(7);
		_ratingPalate.setValue(_wine.getRatingPalate());
		
		_ratingOverall = (NumberPicker) this.findViewById(R.id.ratingOverall);
		_ratingOverall.setMinValue(1);
		_ratingOverall.setMaxValue(3);
		_ratingOverall.setValue(_wine.getRatingOverall());
		
		_viewPicture = (ImageView) this.findViewById(R.id.winePicture);
		_viewPicture.setImageBitmap(_wine.getPicture());
		_viewPicture.refreshDrawableState();
		_viewPicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
				Drawable pictureDrawable = _viewPicture.getDrawable();
				if(pictureDrawable == null) return;
				Bitmap picture = ((BitmapDrawable) pictureDrawable).getBitmap();
				byte[] bytesPicture = WineStore.getBitmapAsByteArray(picture);
				
				Bundle arguments = new Bundle();
				arguments.putByteArray(PictureDialog.PICTURE_KEY, bytesPicture);
				
				PictureDialog dialog = new PictureDialog();
				dialog.setArguments(arguments);
				dialog.show(getFragmentManager(), "PictureDialogFragment");
			}
		});
	}
	
	private void saveWine(){
		int priceInt = 0;
		try{
			double price = Float.parseFloat(_txtPrice.getText().toString());
			priceInt = (int) Math.round(price*100);
		} catch(NumberFormatException e) {
			// do nothing
		}
		
		Bitmap picture = null;
		Drawable pictureDrawable = _viewPicture.getDrawable();
		if(pictureDrawable != null) {
			picture = ((BitmapDrawable) pictureDrawable).getBitmap();
		}
		
		
		Wine wine = _store.new Wine(
					_txtName.getText().toString(),
					priceInt,
					picture,
					_ratingColor.getValue(),
					_ratingNose.getValue(),
					_ratingPalate.getValue(),
					_ratingOverall.getValue(),
					_wine.getDegu()
				);
		
		_store.updateWine(_wineId, wine);
		finish();
	}

	private void takePicture(){
		Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCamera, RC_TAKE_PICTURE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if(requestCode == RC_TAKE_PICTURE && resultCode == RESULT_OK){
			Bitmap picture = (Bitmap) data.getExtras().get("data");
			_viewPicture.setImageBitmap(picture);
			_viewPicture.refreshDrawableState();
		}
	}
}
