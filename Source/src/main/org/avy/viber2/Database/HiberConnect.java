package org.avy.viber2.Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

//@TODO: нещо смислено от вързката
public class HiberConnect {
    private SessionFactory factory;

    public void Setup() {
	StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
	Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

	factory = meta.getSessionFactoryBuilder().build();
	Session session = factory.openSession();

	//User usr = new User();
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
