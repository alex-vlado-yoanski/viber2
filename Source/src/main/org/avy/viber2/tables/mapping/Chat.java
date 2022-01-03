package org.avy.viber2.tables.mapping;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "chats")
public class Chat extends AdditionalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private long ID;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_chats", joinColumns = @JoinColumn(name = "chat_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public long getID() {
	return ID;
    }

    public void setID(long id) {
	this.ID = id;
    }

    public List<User> getUsers() {
	return users;
    }
    
    public void setUsers(List<User> users) {
	this.users = users;
    }

    public void setUser(User user) {
	users.add(user);
    }

    public List<Message> getMessages() {
	return messages;
    }

    public void setMessages(List<Message> messages) {
	this.messages = messages;
    }

    public Chat() {
	this.ID = 0;
	this.users = new ArrayList<User>();
	this.messages = new ArrayList<Message>();
    }
}
