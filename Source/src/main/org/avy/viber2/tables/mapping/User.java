package org.avy.viber2.tables.mapping;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User extends AdditionalData {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInvitation> sendInvitations;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInvitation> receivedInvitations;

    @ManyToMany(mappedBy = "users")
    private List<Chat> chats;

    @OneToMany(mappedBy = "sentBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public long getID() {
	return ID;
    }

    public void setID(long id) {
	this.ID = id;
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

    public List<UserInvitation> getSendInvitations() {
	return this.sendInvitations;
    }

    public List<UserInvitation> getReceivedInvitations() {
	return this.receivedInvitations;
    }

    public List<Chat> getChats() {
	return chats;
    }

    public void setChat(Chat chat) {
	this.chats.add(chat);
    }

    public User() {
	this.ID = 0;
	this.name = "";
	this.password = "";
	this.sendInvitations = new ArrayList<UserInvitation>();
	this.receivedInvitations = new ArrayList<UserInvitation>();
	this.chats = new ArrayList<Chat>();
	this.messages = new ArrayList<Message>();
    }

}
