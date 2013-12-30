package com.marcelozarate.notez;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ActionBarActivity implements OnItemClickListener {

	public static final int ADD = 1;
	public static final int EDIT = 2;
	
	Menu menu;
	ListView list;
	NotesSQLiteHelper db;
	NoteArrayAdapter noteadapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = (ListView)findViewById(R.id.listView1);	
		db = new NotesSQLiteHelper(this);
		List<Note> notes = db.getAllNotes();	
		noteadapter = new NoteArrayAdapter(this, R.id.listView1, notes);		
		list.setAdapter(noteadapter);		
		list.setOnItemClickListener(this);
		db.close();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			
		if(item.getItemId()==R.id.action_add){
			//Toast.makeText(this.getApplicationContext(), "AGREGAR", Toast.LENGTH_LONG).show();
			
			Intent intent = new Intent(this.getApplicationContext(),AddNoteActivity.class);
			this.startActivityForResult(intent, MainActivity.ADD);
						
			/*
			if(menu != null){
			     MenuItem item_share = menu.findItem(R.id.action_share);
			     item_share.setVisible(false);
			}
			*/
			
		}
		
		
		
/*		if(item.getItemId()==R.id.action_share){
			Toast.makeText(this.getApplicationContext(), "COMPARTIR", Toast.LENGTH_LONG).show();
		}*/
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Log.e("Hello", "Clic en elemento");
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == ActionBarActivity.RESULT_OK){
			
			long id=0;	
			
			if(requestCode==MainActivity.ADD){
				id = data.getExtras().getLong("id");
				this.db = new NotesSQLiteHelper(this);
				
				Note note = this.db.getNote(id);
				noteadapter.add(note);	
				db.closeDB();
			}
					
			
		}
	}		

}