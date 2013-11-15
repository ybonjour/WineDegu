package ch.yvu.winedegu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import ch.yvu.winedegu.data.WineDeguOpenHelper.DeguTable;
import ch.yvu.winedegu.data.WineStore;

public class DeguList extends Activity {

	private static final int RC_NEW_DEGU = 1;
	private static final int RC_EDIT_DEGU = 2;
	private static final int RC_WINE_LIST = 3;
	private static final int CONTEXTMENU_EDIT = 1;
	private ListView _list;
	private WineStore _store;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_degu_list);
		
		_store = WineStore.create(getApplicationContext());
		_list = (ListView) this.findViewById(R.id.deguList);
		updateListAdapter();
		_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent wineList = new Intent(getBaseContext(), WineList.class);
				wineList.putExtra(WineList.DEGU_KEY, id);
		        startActivityForResult(wineList, RC_WINE_LIST);
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
                            		_store.deleteDegu(id);
                            	}
                            	updateListAdapter();
                            }
                        });
		_list.setOnTouchListener(touchListener);
		_list.setOnScrollListener(touchListener.makeScrollListener());
		registerForContextMenu(_list);
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            
            menu.add(0, CONTEXTMENU_EDIT, 0, R.string.edit);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            switch(item.getItemId())
            {
	            case CONTEXTMENU_EDIT:
	                    editDegu(info.id);
	                    return true;
            }
            return super.onContextItemSelected(item);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		if(item.getItemId() == R.id.menuAddDegu) {
			newDegu();
		}
		return true;
	}

	private void newDegu(){
		Intent deguDetail = new Intent(getBaseContext(), NewDegu.class);
        startActivityForResult(deguDetail, RC_NEW_DEGU);
	}
	
	private void editDegu(long id){
		Intent wineDetail = new Intent(getBaseContext(), DeguDetail.class);
		wineDetail.putExtra(DeguDetail.DEGU_ID_KEY, id);
        startActivityForResult(wineDetail, RC_EDIT_DEGU);
	}
	
	private void updateListAdapter() {
        Cursor cursor = WineStore.create(getApplicationContext()).allDegus();
        String[] columns = new String[] { DeguTable.NAME, DeguTable.DESCRIPTION };
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, columns, to);

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
		getMenuInflater().inflate(R.menu.degu_list, menu);
		return true;
	}
	
}
