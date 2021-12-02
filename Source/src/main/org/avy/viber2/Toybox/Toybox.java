/*
 * Изтрии ме
 * */

package org.avy.viber2.Toybox;

import org.avy.viber2.Database.HiberConnect;

public class Toybox {
    public void VladoHibernate() throws java.sql.SQLException {
	System.out.println("*** TEST Hibernate ***");
	HiberConnect HC = new HiberConnect();
	System.out.println("Opening DB");
	HC.Setup();

	System.out.println("Closing DB");
	HC.CloseDB();
    }
}
