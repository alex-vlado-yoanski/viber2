package org.avy.viber2.Database;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

//@TODO: нещо смислено от вързката
public class HiberConnect extends Thread {
    private static Configuration cfg = new Configuration().addClass(org.avy.viber2.Database.Users.class)
	    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
	    .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/viber2")
	    .setProperty("connection.username", "avy").setProperty("hibernate.connection.password", "uncommon")
	    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
    private static SessionFactory factory;
    private static Session session;

    // главна нишка
    public void run() {
	throw new UnsupportedOperationException("Database not threaded yet :(\n");
    }

    /**
     * Затваряме връзки и зачистваме
     * 
     * @throws HibernateException при проблем със затваряне сесии и зачистване
     */
    public void exit() throws HibernateException {
	factory.close();
	session.close();
    }

    /**
     * Инициализира базата данни
     */
    public HiberConnect() {
	factory = cfg.buildSessionFactory();
	session = factory.openSession();
    }

    /**
     * Запазва промените в базата данни
     * 
     * @param u - perstistance клас
     */
    public void CommitAndSaveSession(Users u) {
	session.beginTransaction();
	session.save(u);
	session.getTransaction().commit();
    }

    /**
     * UPDATE метод.
     * 
     * @throws Exception - TODO оправи ме
     */
    public List<?> UpdateItem() throws Exception {
	throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * SELECT X FROM Y WHERE Z метод.
     * 
     * @param What  - кой запис ще се взема запис
     * @param Where - коя таблица.колонка ще се взема запис
     * @param Is    - каква е търсената стойност
     * @throws Exception - Не може да е празна заявката!
     * @return списък от заявката
     */
    public List<?> SelectItem(String What, String Where, String Is) throws Exception {
	if ((What == null || What.isBlank()) || (Where == null || Where.isBlank()) || (Is == null || Is.isBlank())) {
	    throw new Exception("Empty SQL query");
	}
	Query<?> Qr = session.createQuery("SELECT " + What + " FROM " + Where + "WHERE " + Is);

	return Qr.getResultList();
    }

    /**
     * SELECT X FROM Y метод.
     * 
     * @param What  - кой запис ще се взема запис
     * @param Where - коя таблица.колонка ще се взема запис
     * @throws Exception Не може да е празна заявката!
     * @return списък от заявката
     */
    public List<?> SelectItem(String What, String Where) throws Exception {
	if ((What == null || What.isBlank()) || (Where == null || Where.isBlank())) {
	    throw new Exception("Empty SQL query");
	}
	Query<?> Qr = session.createQuery("SELECT " + What + " FROM " + Where);

	return Qr.getResultList();
    }

    /**
     * DELETE метод.
     * 
     * @param Where  - от коя таблица ще се изтрива запис
     * @param Equals - от коя колонка ще се изтрива запис
     * @param What   - каква е стойността за изтриване
     * @throws Exception - TODO оправи ме
     * @return брой изтрити елементи
     */
    public int DeleteItem(String Where, String Equals, String What) throws Exception {
	if ((Where == null || Where.isBlank()) || (Equals == null || Equals.isBlank())
		|| (What == null || What.isBlank())) {
	    throw new Exception("Empty SQL query");
	}
	Query<?> Qr = session.createQuery("DELETE FROM " + Where + " WHERE " + Equals + " =: " + What);

	return Qr.executeUpdate();
    }
}
