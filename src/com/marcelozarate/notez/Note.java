package com.marcelozarate.notez;

public class Note {

	long id;
    String title;
	String date;
	String content;

	public Note (){
		
	}
	
	public Note(int id, String title, String date, String content) {
		this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
    }
	
	public Note(String title, String date, String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }
 
 
    // Setters
    public void setId(long id) {
        this.id = id;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDate(String date) {
    	this.date = date;
    }
    
    public void setContent(String content) {
    	this.content = content;
    }
 
    // Getters
    public long getId() {
        return this.id;
    }
 
    public String getTitle() {
        return this.title;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public String getContent() {
    	return this.content;
    }
	
}
