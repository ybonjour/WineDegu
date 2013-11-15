package ch.yvu.winedegu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ch.yvu.winedegu.data.WineStore;
import ch.yvu.winedegu.data.WineStore.Degu;

public class NewDegu extends Activity {
	private EditText _txtName;
	private EditText _txtDescription;
	private Button _btnSave;
	private WineStore _store;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_degu_detail);
		
		_store = WineStore.create(getApplicationContext());
		
		_txtName = (EditText) this.findViewById(R.id.deguName);
		_txtDescription = (EditText) this.findViewById(R.id.deguDescription);
		
		_btnSave = (Button) this.findViewById(R.id.addDegu);
		_btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addDegu();
			}
		});
	}
	
	private void addDegu(){		
		Degu degu = _store.new Degu(
					_txtName.getText().toString(),
					_txtDescription.getText().toString()
				);
		
		_store.addDegu(degu);
		finish();
	}
}
