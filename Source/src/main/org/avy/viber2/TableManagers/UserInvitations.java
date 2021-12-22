package org.avy.viber2.TableManagers;

import javax.persistence.*;

//import org.hibernate.type.DateType;

/*
 * STATUSES:
 * 0 - WAITING
 * 1 - REJECTED
 * 2 - ACCEPTED 
 */

@Entity
@Table(name = "user_invitations")
public class UserInvitations {
    
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    
    @Column(name = "status", nullable = false)
    private int status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name = "fk_user_invitations_sender_id"))
    private int senderID;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", foreignKey = @ForeignKey(name = "fk_user_invitations_receiver_id"))
    private int recieverID;
    
    //@Column(name = "create_date", nullable = false)
    //private DateType create_date;

    public int getID() {
	return ID;
    }

    public void setId(int ID) {
	this.ID = ID;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public int getSenderID() {
	return senderID;
    }

    public void setSenderID(int senderID) {
	this.senderID = senderID;
    }

    public int getRecieverID() {
	return recieverID;
    }

    public void setRecieverID(int recieverID) {
	this.recieverID = recieverID;
    }
/*
    public DateType getCreateDate() {
	return createDate;
    }

    public void setCreateDate(DateType createDate) {
	this.createDate = createDate;
    }
*/
    public UserInvitations() {

    }

}
