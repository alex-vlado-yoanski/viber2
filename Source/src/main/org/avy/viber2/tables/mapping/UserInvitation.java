package org.avy.viber2.tables.mapping;

import javax.persistence.*;
import java.sql.Timestamp;

/*
 * STATUSES:
 * 0 - WAITING
 * 1 - REJECTED
 * 2 - ACCEPTED 
 */

@Entity
@Table(name = "user_invitations")
public class UserInvitation {
    
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    
    @Column(name = "status", nullable = false)
    private int status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name = "fk_user_invitations_sender_id"))
    private User sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", foreignKey = @ForeignKey(name = "fk_user_invitations_receiver_id"))
    private User receiver;
    
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    public int getID() {
	return ID;
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

    public User getReciever() {
	return receiver;
    }

    public void setReciever(User receiver) {
	this.receiver = receiver;
    }

    public Timestamp getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
	this.createDate = createDate;
    }

    public UserInvitation() {
	ID=0;
	status = 0;
	createDate = new Timestamp(0);
    }

}
