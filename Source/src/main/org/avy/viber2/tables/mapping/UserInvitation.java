package org.avy.viber2.tables.mapping;

import javax.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_invitations")
public class UserInvitation extends AdditionalData {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    @Column(name = "status", nullable = false)
    private int status; // UserInvitationStatus.java

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name = "fk_user_invitations_sender_id"))
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", foreignKey = @ForeignKey(name = "fk_user_invitations_receiver_id"))
    private User receiver;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    public long getID() {
	return ID;
    }
    
    public void setID(long id) {
	this.ID = id;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public User getSender() {
	return sender;
    }

    public void setSender(User sender) {
	this.sender = sender;
    }

    public User getReceiver() {
	return receiver;
    }

    public void setReceiver(User receiver) {
	this.receiver = receiver;
    }

    public Timestamp getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
	this.createDate = createDate;
    }

    public UserInvitation() {
	this.ID = 0;
	this.status = 0;
	this.sender = new User();
	this.receiver = new User();
	this.createDate = new Timestamp(0);
    }

}
