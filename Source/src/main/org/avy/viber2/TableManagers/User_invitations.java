package org.avy.viber2.TableManagers;

import org.hibernate.type.DateType;

public class User_invitations {
    private int id, status, sender_id, reciever_id;
    private DateType create_date;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public int getSender_id() {
	return sender_id;
    }

    public void setSender_id(int sender_id) {
	this.sender_id = sender_id;
    }

    public int getReciever_id() {
	return reciever_id;
    }

    public void setReciever_id(int reciever_id) {
	this.reciever_id = reciever_id;
    }

    public DateType getCreate_date() {
	return create_date;
    }

    public void setCreate_date(DateType create_date) {
	this.create_date = create_date;
    }

    public User_invitations() {

    }
}
