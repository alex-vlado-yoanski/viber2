package org.avy.viber2.tables.mapping;

import java.util.*;
import javax.persistence.*;
import org.avy.viber2.data.*;

@Entity
@Table(name = "users")
public class User extends AdditionalRequestData {

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

    @ManyToMany
    @JoinTable(name = "user_chats", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "chat_id"))
    List<Chat> chats;

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

    public User() {
	this.ID = 0;
	this.name = null;
	this.password = null;
	this.sendInvitations = new ArrayList<UserInvitation>();
    }
}
