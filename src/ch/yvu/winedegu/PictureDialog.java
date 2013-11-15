package ch.yvu.winedegu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class PictureDialog extends DialogFragment {
	public static final String PICTURE_KEY = "Picture";
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = this.getArguments();
		byte[] bytesPicture = args.getByteArray(PICTURE_KEY);
		Bitmap picture = BitmapFactory.decodeByteArray(bytesPicture, 0, bytesPicture.length);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.picture_dialog, null);

	    ImageView pictureView = (ImageView) view.findViewById(R.id.wineDialogPicture);
	    pictureView.setImageBitmap(picture);
	    pictureView.refreshDrawableState();
	    pictureView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().cancel();
			}
		});
	    
	    builder.setView(view);
		
		return builder.create();
	}
}
