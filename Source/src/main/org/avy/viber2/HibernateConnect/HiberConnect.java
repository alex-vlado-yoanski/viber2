package org.avy.viber2.HibernateConnect;

import org.avy.viber2.Login.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

//@TODO: нещо смислено от вързката
public class HiberConnect {
    private SessionFactory factory;

    public void Setup() {
	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
	try {
	    factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	} catch (Throwable ex) {
	    System.err.println("Failed to create sessionFactory object." + ex);
	    StandardServiceRegistryBuilder.destroy(registry);
	    throw new ExceptionInInitializerError(ex);
	}
	User usr = new User();
    }

    public void TransactDB() {
	// t = session.beginTransaction();
    }

    public void CloseDB() {
	factory.close();
    }

    public HiberConnect() {
	;
    }
}
