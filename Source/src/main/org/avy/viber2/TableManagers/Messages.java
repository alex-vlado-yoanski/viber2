package org.avy.viber2.TableManagers;

import java.sql.Timestamp;

public class Messages {
    private int id;
    private String text, file_path;
    private Timestamp create_date;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getFile_path() {
	return file_path;
    }

    public void setFile_path(String file_path) {
	this.file_path = file_path;
    }

    public Timestamp getCreate_date() {
	return create_date;
    }

    public void setCreate_date(Timestamp create_date) {
	this.create_date = create_date;
    }

    public Messages() {

    }
}
