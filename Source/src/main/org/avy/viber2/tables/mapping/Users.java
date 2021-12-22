package org.avy.viber2.tables.mapping;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Users {
    
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "password", nullable = false)
    private String password;


    public long getID() {
	return ID;
    }

    public void setID(long ID) {
	this.ID = ID;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public Users() {

    }
}
