package ch.yvu.winedegu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ch.yvu.winedegu.data.WineStore;
import ch.yvu.winedegu.data.WineStore.Degu;

public class WineList extends Activity {
	public static final String DEGU_KEY = "Degu";
	private static final int RC_NEW_WINE = 1;
	private static final int RC_EDIT_WINE = 2;
	private ListView _list;
	private WineStore _store;
	private long _deguId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wine_list);

		_store = WineStore.create(getApplicationContext());
		_deguId = getIntent().getExtras().getLong(DEGU_KEY);
		Degu degu = _store.getDegu(_deguId);
		String title = degu.getName() != null ? degu.getName() : "-";
		setTitle(title);
		
		_list = (ListView) this.findViewById(R.id.wineList);
		updateListAdapter();
		_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				editWine(id);
			}
		});
		SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        _list,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                            	for(int position : reverseSortedPositions) {
                            		long id = _list.getAdapter().getItemId(position);
                            		_store.deleteWine(id);
                            	}
                            	updateListAdapter();
                            }
                        });
		_list.setOnTouchListener(touchListener);
		_list.setOnScrollListener(touchListener.makeScrollListener());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		if(item.getItemId() == R.id.menuAddWine) {
			newWine();
		}
		return true;
	}

	private void newWine(){
		Intent wineDetail = new Intent(getBaseContext(), NewWine.class);
		wineDetail.putExtra(NewWine.DEGU_KEY, _deguId);
        startActivityForResult(wineDetail, RC_NEW_WINE);
	}
	
	private void editWine(long id){
		Intent wineDetail = new Intent(getBaseContext(), WineDetail.class);
		wineDetail.putExtra(WineDetail.WINE_ID_KEY, id);
        startActivityForResult(wineDetail, RC_EDIT_WINE);
	}
	
	private void updateListAdapter() {
        Cursor cursor = WineStore.create(getApplicationContext()).allWines(_deguId);
        WineListAdapter adapter = new WineListAdapter(this, cursor);
        _list.setAdapter(adapter);
        _list.refreshDrawableState();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		updateListAdapter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wine_list, menu);
		return true;
	}
}