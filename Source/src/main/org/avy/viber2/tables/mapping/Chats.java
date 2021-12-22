package org.avy.viber2.tables.mapping;

import javax.persistence.*;

@Entity
@Table(name = "chats")
public class Chats {
    
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_chats_message_id"))
    private int messageID;

    public int getID() {
	return ID;
    }

    public void setID(int ID) {
	this.ID = ID;
    }

    public int getMessage_id() {
	return messageID;
    }

    public void setMessageID(int messageID) {
	this.messageID = messageID;
    }

    public Chats() {

    }
}
