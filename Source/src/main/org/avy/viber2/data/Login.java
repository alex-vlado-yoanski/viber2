package org.avy.viber2.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.User;
import org.hibernate.Session;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Login", description = "Servlet handling logins from Android client", urlPatterns = { "/login" })
public class Login extends HttpServlet {
    private static Gson gson;
    private static final long serialVersionUID = 1L;

    private void SetHeaders(HttpServletResponse response) {
	// Setup HTTP headers
	// Verify with curl -I <URL>
	response.setHeader("Keep-Alive", "timeout=15, max=1000"); // Timeout after 15sec
	response.setHeader("Accept", "application/json"); // Accepts json
	response.setHeader("Content-Type", "application/json; charset=UTF-8"); // Sends json
	response.setHeader("X-Viber2-Identifier", "AM_VIBER2_SERVER"); // Identify ourselves in front of app; RFC6648 :(
    }

    private String GetFromDatabase() {
	// Hibernate test -- TO DO: Да се махне след сокетите
	Session session = DatabaseConnection.getSessionFactory().openSession();

	CriteriaBuilder builder = session.getCriteriaBuilder();
	CriteriaQuery<User> criteriaQuery2 = builder.createQuery(User.class);
	criteriaQuery2.from(User.class);

	List<User> asd = session.createQuery(criteriaQuery2).getResultList();

	User u = asd.get(0);

	// -> от тук
	gson = new Gson();
	String json = gson.toJson(u);

	return json;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	SetHeaders(response);
	String json = this.GetFromDatabase();

	PrintWriter PW = response.getWriter();
	PW.print(json);
	PW.flush();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	SetHeaders(response);
	String AppIdentifier = request.getHeader("X-Viber2-Identifier");

	if (AppIdentifier.equals("AM_VIBER2_CLIENT")) {
	    // TODO Да разреши логин
	    System.out.println("\n\nPOST: " + AppIdentifier + "\n");
	    BufferedReader PW = request.getReader();

	    String json = PW.readLine();
	    gson = new Gson();

	    User usr = gson.fromJson(json, User.class);

	    System.out
		    .println("\nID = " + usr.getID() + " Name = " + usr.getName() + " Password = " + usr.getPassword());
	} else {
	    PrintWriter PW = response.getWriter();
	    PW.print("NOT_VALID_APP");
	    PW.flush();
	}
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	SetHeaders(response);
	String AppIdentifier = request.getHeader("X-Viber2-Identifier");

	if (AppIdentifier.equals("AM_VIBER2_CLIENT")) {
	    // TODO Да разреши логин
	    System.out.println("\n\nPUT: " + AppIdentifier + "\n");
	    BufferedReader PW = request.getReader();

	    String json = PW.readLine();
	    gson = new Gson();

	    User usr = gson.fromJson(json, User.class);

	    System.out
		    .println("\nID = " + usr.getID() + " Name = " + usr.getName() + " Password = " + usr.getPassword());

	} else {
	    PrintWriter PW = response.getWriter();
	    PW.print("NOT_VALID_APP");
	    PW.flush();
	}
    }
}
