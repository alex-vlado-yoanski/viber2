package org.avy.viber2.tables.mapping;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private int ID;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_chats", joinColumns = @JoinColumn(name = "chat_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public int getID() {
	return ID;
    }

    public List<User> getUsers() {
	return users;
    }

    public void setUser(User user) {
	users.add(user);
    }

    public Chat() {
	this.ID = 0;
	this.users = new ArrayList<User>();
	this.messages = new ArrayList<Message>();
    }
}
