package org.avy.viber2.tables.mapping;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message extends AdditionalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private long ID;

    @ManyToOne(fetch = FetchType.LAZY) // == fetch when needed
    @JoinColumn(name = "chat_id", foreignKey = @ForeignKey(name = "fk_messages_chat_id"))
    private Chat chat;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_by", foreignKey = @ForeignKey(name = "fk_messages_sent_by"))
    private User sentBy;

    public long getID() {
	return ID;
    }

    public void setID(long id) {
	this.ID = id;
    }

    public Chat getChat() {
	return chat;
    }

    public void setChat(Chat chat) {
	this.chat = chat;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getFilePath() {
	return filePath;
    }

    public void setFilePath(String filePath) {
	this.filePath = filePath;
    }

    public Timestamp getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
	this.createDate = createDate;
    }

    public User getSentBy() {
	return sentBy;
    }

    public Message() {
	this.ID = 0;
	this.chat = new Chat();
	this.text = "";
	this.filePath = "";
	this.createDate = new Timestamp(0);
	this.sentBy = new User();
    }
}
