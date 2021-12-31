package org.avy.viber2.data;

public class ResponseType {

    public static String createErrorResponse(int errorCode) {
	String errorMessage = "Internal Server Error."; // errorCode : 500
	
	// Формиране на описание на грешката според кода на грешка
	switch(errorCode) {
	    case 400: {
		errorMessage = "Bad request.";
		break;
	    }
	    case 470: {
		errorMessage += "Bad request. Invalid request type.";
		break;
	    }
	}
	
	// Формиране на отговора за грешка
	String response = "{\"errorCode\" : \"" + errorCode + "\", \"errorMessage\" : \"" + errorMessage + "\"}";
	
	return response;
    }
    
}
