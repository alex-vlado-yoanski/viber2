package org.avy.viber2.data;

import java.util.ArrayList;
import java.util.List;

import org.avy.viber2.tables.mapping.*;

public class UserSearch extends AdditionalData {

    private String searchingPhrase;
    private User searchingUser;
    private List<User> users;

    UserSearch() {
	this.searchingPhrase = "";
	this.searchingUser = new User();
	this.users = new ArrayList<User>();
    }

    public String getSearchingPhrase() {
	return searchingPhrase;
    }

    public void setSearchingPhrase(String searchingPhrase) {
	this.searchingPhrase = searchingPhrase;
    }

    public User getSearchingUser() {
	return searchingUser;
    }

    public void setSearchingUser(User searchUser) {
	this.searchingUser = searchUser;
    }

    public List<User> getUsers() {
	return users;
    }

    public void setUsers(List<User> users) {
	this.users = users;
    }

    public void setUsers(User user) {
	this.users.add(user);
    }

}
