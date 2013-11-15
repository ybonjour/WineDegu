package ch.yvu.winedegu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class RatingView extends View {

	private ImageView[] _buttons;
	private int _value;
	private final Drawable _activeDrawable;
	private final Drawable _inactiveDrawable;
	
	public RatingView(Context context, int numLevels, Drawable active, Drawable inactive) {
		super(context);
		_inactiveDrawable = inactive;
		_activeDrawable = active;
		
		_buttons = new ImageButton[numLevels];
		for(int i=0; i < numLevels; i++){
			ImageView btn = new ImageView(context);
			btn.setClickable(true);
			btn.setImageDrawable(_inactiveDrawable);
			final int x = i;
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonClicked(x);
				}
			});
			_buttons[i] = btn;
		}
		
		_value = 0;
	}
	
	public int getValue() {
		return _value;
	}
	
	private void buttonClicked(int idx) {
		_value = idx + 1;
		for(int i=0; i < _buttons.length; i++) {
			if(i <= idx) {
				_buttons[i].setImageDrawable(_activeDrawable);
				_buttons[i].refreshDrawableState();
			} else {
				_buttons[i].setImageDrawable(_activeDrawable);
				_buttons[i].refreshDrawableState();
			}
		}
	}
}
