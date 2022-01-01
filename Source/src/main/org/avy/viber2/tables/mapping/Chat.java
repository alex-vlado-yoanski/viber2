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
    
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @ManyToMany(mappedBy = "chats")
    List<User> users;

    public int getID() {
	return ID;
    }

    public List<Message> getMessages() {
	return messages;
    }

    public void setMessages(List<Message> msgList) {
   	this.messages = msgList ;
    }
    
    public Chat() {
	this.ID = 0;
	this.messages = new ArrayList<Message>();
	this.users = new ArrayList<User>();
    }
}
