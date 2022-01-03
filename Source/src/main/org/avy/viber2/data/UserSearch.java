package org.avy.viber2.data;

import java.util.ArrayList;
import java.util.List;

import org.avy.viber2.tables.mapping.*;

public class UserSearch extends AdditionalData {

    private String searchPhrase;
    private List<User> users;

    UserSearch() {
	this.searchPhrase = "";
	this.users = new ArrayList<User>();
    }

    public String getSearchPhrase() {
	return searchPhrase;
    }

    public void setSearchPhrase(String searchPhrase) {
	this.searchPhrase = searchPhrase;
    }

    public List<User> getUsers() {
	return users;
    }

    public void setUsers(List<User> users) {
	this.users = users;
    }

}
