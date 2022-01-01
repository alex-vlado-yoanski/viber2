package org.avy.viber2.data;

// Видове заявки, които очакваме
public class RequestType {

    private RequestType() {}
    
    public static final int LOGIN_CREDENTIALS = 1;
    public static final int SIGN_IN = 2;
    public static final int USER_CHATS = 3;
    public static final int USER_INVITATIONS = 4;
    // дефиниране на номера на следващата заявка, която очакваме тук ^
    
}
