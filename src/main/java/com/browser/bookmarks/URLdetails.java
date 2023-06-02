package com.browser.bookmarks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class URLdetails {
	private	StringProperty title;
	private	StringProperty type;
	private	StringProperty url;
	private	StringProperty Tip;
	public URLdetails(String title, String type, String url, String Tip){
		this.title = new SimpleStringProperty(title);
		this.type = new SimpleStringProperty(type);
		this.url = new SimpleStringProperty(url);
		this.Tip = new SimpleStringProperty(Tip);
	}
	public String getTitle() {
		return title.get();
	}
	public String getUrl() {
		return url.get();
	}
	public String getTip() {
		return Tip.get();
	}		
	public String getType() {
		return type.get();
	}
	public void setTitle(String title){
		this.title = new SimpleStringProperty(title);
	}
	public void setType(String type){
		this.type = new SimpleStringProperty(type);
	}
	public void setUrl(String url){
		this.url = new SimpleStringProperty(url);
	}
	public void setTip(String tip){
		this.Tip =new SimpleStringProperty(tip);
	}
	@Override
	public String toString(){
		return this.Tip.toString();
	}
}