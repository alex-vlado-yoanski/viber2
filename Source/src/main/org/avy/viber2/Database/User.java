package org.avy.viber2.Database;

public class User {
    private int id;
    private String uname, passw;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getUname() {
	return uname;
    }

    public void setUname(String uname) {
	this.uname = uname;
    }

    public String getPassw() {
	return passw;
    }

    public void setPassw(String passw) {
	this.passw = passw;
    }
}
