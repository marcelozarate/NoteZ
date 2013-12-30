package com.marcelozarate.notez;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;


public class AddNoteActivity extends ActionBarActivity {
	private EditText noteText;
	private String text;
	private NotesSQLiteHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_note);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_note, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    case R.id.action_save:
	    	addNote();
	    	return true;
	    case R.id.action_delete:
	    	NavUtils.navigateUpFromSameTask(this);
	    	return true;
	    case R.id.action_share:
	    	shareContent();
	    	return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	public void shareContent(){
		
		this.noteText = (EditText)findViewById(R.id.editTextNote);
		text = noteText.getText().toString();
		
		Intent sharingIntent = new Intent(Intent.ACTION_SEND); 
        sharingIntent.setType("text/plain");
        String shareBody = text;
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
	
	public void addNote(){
		
		this.noteText = (EditText)findViewById(R.id.editTextNote);
		text = noteText.getText().toString();
		
		if(text.trim().length()>0){
			Time rightNow = new Time(Time.getCurrentTimezone());
			rightNow.setToNow();
			String date = rightNow.format("%d.%m.%Y %H:%M");			
			
			String title = text.substring(0, 15);
			
			Note nota = new Note(title, date, text);
			
			db = new NotesSQLiteHelper(this);

			long id= db.createNote(nota);

			if(id != 0){				
				Intent result = new Intent();
				result.putExtra("id", id);
				setResult(Activity.RESULT_OK, result);
				finish();
			}else{
				Toast.makeText(this,"Error al guardar la nota", Toast.LENGTH_SHORT).show();
			}
			db.close();
		}else{
			Toast.makeText(this,"No se ha podido obtener el texto", Toast.LENGTH_SHORT).show();
		}
	}
}
