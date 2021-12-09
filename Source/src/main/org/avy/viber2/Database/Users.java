package org.avy.viber2.Database;

public class Users {
    private long id;
    private String uname;
    private String passw;

    public long getId() {
	return id;
    }

    public void setId(long I) {
	this.id = I;
    }

    public String getUname() {
	return uname;
    }

    public void setUname(String N) {
	this.uname = N;
    }

    public String getPassw() {
	return passw;
    }

    public void setPassw(String P) {
	this.passw = P;
    }

    public Users() {

    }
}
