package org.avy.viber2.database;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

public class DatabaseConnection {
    private static SessionFactory factory;

    public static synchronized SessionFactory getSessionFactory(){
        if(factory == null)
        {
            Configuration hibernateConfiguration = new Configuration();
            hibernateConfiguration.configure("hibernate.cfg.xml");
            factory = hibernateConfiguration.buildSessionFactory();
        }
        
        return factory;
    }
}
