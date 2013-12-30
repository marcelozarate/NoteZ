package com.marcelozarate.notez;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

public class NotesSQLiteHelper extends SQLiteOpenHelper {
	
	private static final String LOG = "NotesSQLiteHelper";
	
    private static final int DATABASE_VERSION = 1;
 
    private static final String DATABASE_NAME = "NoteZ.db";
	
    private static final String TABLE_NOTES = "notes";
    
    private static final String KEY_ID = "id";
    
    private static final String KEY_TITLE = "title";
    
    private static final String KEY_DATE = "date";
    
    private static final String KEY_CONTENT = "content";
    //Sentencia SQL para crear la tabla de Notas
    private static final String sqlCreate = "CREATE TABLE notes (id INTEGER PRIMARY KEY ASC, title TEXT, date TEXT, content TEXT)";
 
    private Cursor c;
    
    public NotesSQLiteHelper(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        // Debería hacer la migración pero ni ganas.
 
        db.execSQL("DROP TABLE IF EXISTS Notes");
 
        db.execSQL(sqlCreate);
    }
    
    // METODOS -----------------------------------------------------------------------
    
    /*
     * 	Creando una nota
     */
    public long createNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_DATE, getDateTime());
     
        // INSERT	
        long note_id = db.insert(TABLE_NOTES, null, values);
     
        return note_id;
    }
    
    public Note getNote(long id){
 	   SQLiteDatabase db = getReadableDatabase();
 	   Note note = null;
 	   try {
 		String[] args = new String[]{Long.toString(id)};
 		Cursor c = db.query(TABLE_NOTES, null, KEY_ID+"=?", 
 				args, null, null, KEY_ID);
 				
 		if (c.moveToNext()) {
 			String title = c.getString(1);
 			String content = c.getString(3);
 			
 			Time rightNow = new Time(Time.getCurrentTimezone());
			String date = rightNow.format("%d.%m.%Y %H:%M");
			
 			date = c.getString(2);
 			   
 			note = new Note(title, date, content);
 			note.setId(c.getLong(0));			
 		}
 		c.close();
 		
 	   } catch (SQLException e) {
 			Log.e("NoteSQLLiteHelper", "No se puede leer nota");
 		}
 	   db.close();
 	   return note;
 	   
    }
    
    /*
     * Obteniendo todas las notas
     * */
    public List<Note> getAllNotes() {

        List<Note> notes = new ArrayList<Note>();
        
        String selectQuery = "SELECT "+KEY_ID+", "+KEY_TITLE+", "+KEY_DATE+", "+KEY_CONTENT+" FROM " + TABLE_NOTES;
        
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();

        c = db.rawQuery(selectQuery, null);
        
        // Recorriendo y agregando
        if (c.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                note.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
                note.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                note.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));
     
                // Adding a note to the list
                notes.add(note);
            } while (c.moveToNext());
        }
     
        c.close();
        closeDB();
        return notes;
    }
    
    /*
     * Actualizando una nota
     * */
    public boolean updateNote(Note note){
    	String[] args = new String[]{Long.toString(note.getId())};
    	SQLiteDatabase db = getReadableDatabase();
    	boolean updatedone=false;
    	try {
    		ContentValues noteValues = new ContentValues();
    		noteValues.put(NotesSQLiteHelper.KEY_ID, Long.valueOf(note.getId()));
    		noteValues.put(NotesSQLiteHelper.KEY_CONTENT, note.getContent());
    		noteValues.put(NotesSQLiteHelper.KEY_DATE, note.getDate());
    		
    		int result=db.update(NotesSQLiteHelper.TABLE_NOTES, noteValues, NotesSQLiteHelper.KEY_ID+"=?", args);
    		if(result>0){
    			updatedone=true;
    		}
    	} catch (SQLException e) {
    		Log.e(NotesSQLiteHelper.class.getName(), "Error al Intentar Actualizar: " + e.getMessage());
    	}
 	   
    	return updatedone;
    }
    
    public  boolean delete(Note note) {
 		boolean ret=false;
 		
 		SQLiteDatabase db = getReadableDatabase();
 		if (db != null) {
 			try {		
 				
 				String[] args = new String[]{Long.toString(note.getId())};
 				int result=db.delete(NotesSQLiteHelper.TABLE_NOTES, NotesSQLiteHelper.KEY_ID+"=?", args);
 				if(result>0){
 					ret=true;
 				}
 				
 			} catch (SQLException e) {
 				Log.e(NotesSQLiteHelper.class.getName(), "Error al intentar borrar: " + e.getMessage());
 			}
 		}
 		return ret;
 	}
    
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    
}
