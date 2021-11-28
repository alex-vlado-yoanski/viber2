package org.avy.viber2.HibernateConnect;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

//@TODO: Връзка с база данни
public class HiberConnect {
    // Умира тук :(
    StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

    SessionFactory factory = meta.getSessionFactoryBuilder().build();
    Session session = factory.openSession();
    Transaction t = session.beginTransaction();

    public HiberConnect() {
    }
}
