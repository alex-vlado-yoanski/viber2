package org.avy.viber2.Login;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private int id;
    private String uname, passw;

    public User() {
    }

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
