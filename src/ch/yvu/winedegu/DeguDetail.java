package ch.yvu.winedegu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ch.yvu.winedegu.data.WineStore;
import ch.yvu.winedegu.data.WineStore.Degu;

public class DeguDetail extends Activity {

	public static final String DEGU_ID_KEY = "DeguId";
	
	private Button _btnSave;
	private EditText _txtName;
	private EditText _txtDescription;
	private WineStore _store;
	private Degu _degu;
	private long _deguId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_degu_detail);
		
		_store = WineStore.create(getApplicationContext());
		
		_btnSave = (Button) this.findViewById(R.id.addDegu);
		_btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveDegu();
			}
		});
		
		Bundle extras = getIntent().getExtras();
		_deguId = extras.getLong(DEGU_ID_KEY);
		_degu = _store.getDegu(_deguId);
		if(_degu == null) {
			_btnSave.setEnabled(false);
			return;
		}
		
		_txtName = (EditText) this.findViewById(R.id.deguName);
		_txtName.setText(_degu.getName());
		
		_txtDescription = (EditText) this.findViewById(R.id.deguDescription);
		_txtDescription.setText(_degu.getDescription());
	}
	
	private void saveDegu(){
		Degu degu = _store.new Degu(
					_txtName.getText().toString(),
					_txtDescription.getText().toString()
				);
		
		_store.updateDegu(_deguId, degu);
		finish();
	}
}