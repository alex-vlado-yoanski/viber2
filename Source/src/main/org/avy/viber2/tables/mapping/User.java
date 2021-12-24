package org.avy.viber2.tables.mapping;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User
{
    
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
    
    public long getID() {
	return ID;
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

    }
}
