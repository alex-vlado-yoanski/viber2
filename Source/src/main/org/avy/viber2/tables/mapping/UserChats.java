package org.avy.viber2.tables.mapping;

import javax.persistence.*;

@Entity
@Table(name = "user_chats")
public class UserChats {
    
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usesr_id", foreignKey = @ForeignKey(name = "fk_user_chats_user_id"))
    private int userID;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", foreignKey = @ForeignKey(name = "fk_user_chats_chat_id"))
    private int chatID;

    public int getId() {
	return ID;
    }

    public void setID(int ID) {
	this.ID = ID;
    }

    public int getUserID() {
	return userID;
    }

    public void ID(int userID) {
	this.userID = userID;
    }

    public int getChatID() {
	return chatID;
    }

    public void setChat_id(int chatID) {
	this.chatID = chatID;
    }

    public UserChats() {

    }
}
