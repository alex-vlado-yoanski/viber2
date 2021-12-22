package org.avy.viber2.tables.mapping;

import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Messages {
    
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    
    @Column(name = "text", nullable = false)
    private String text;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    public int getID() {
	return ID;
    }

    public void setID(int ID) {
	this.ID = ID;
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
/*
    public Timestamp getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
	this.createDate = createDate;
    }
*/
    public Messages() {

    }
}
