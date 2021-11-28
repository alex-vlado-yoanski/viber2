package org.avy.viber2;

import org.avy.viber2.Login.Login;
import org.avy.viber2.HibernateConnect.HiberConnect;

public class Main {
    private static void VladoToybox() {
	System.out.println("*** TEST Argon2 ***");
	Login L = new Login();

	String HP = L.HashNewPassword("common"),
		CMP_HASH = "$argon2d$v=19$m=65536,t=24,p=2$8sxtVVVcM/xkxmD8UuwnLQ$6H0NF2470zg6upt61Hu//RzGpcvjgO9bM6PQ4g2P+G8";

	System.out.println("Dis be of workings? If yes, here ur hash:\n" + HP);

	L.VerifyPass(CMP_HASH, "common".toCharArray());
	L.VerifyPass(CMP_HASH, "AlaBalaKonskaPanica".toCharArray());
	
	System.out.println("*** TEST Hibernate ***");
	HiberConnect HC = new HiberConnect();
	
    }

    public static void main(String[] args) {
	VladoToybox();
    }
}
