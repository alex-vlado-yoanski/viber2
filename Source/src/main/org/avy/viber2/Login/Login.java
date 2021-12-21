package org.avy.viber2.Login;

import org.avy.viber2.Database.HiberConnect;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Helper;

public class Login extends HiberConnect {
    private HiberConnect HC;
    private static int MAX_ITERS = 1;
    // Argon2d хеш алгоритъм; 16 байта сол, 32 байта хеш
    Argon2 Ar = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2d, 16, 32);

    public Login() {
	// Намира оптимален брой итерации.
	MAX_ITERS = Argon2Helper.findIterations(Ar, 1000, 65536, 2);
    }

    /**
     * Автентикира потребителя
     * 
     * @param UName име на потребителя
     * @param Pass  паролата на потребителя
     * @return true Успешно валидирани данни, false Невалидни данни
     * @throws Exception
     */
    public boolean AuthenticateUser(String UName, String Pass) throws Exception {
	// Ако нишката е там
	if (HC.isAlive()) {
	    // Ако има такова име
	    if (HC.SelectItem("name", "U.Name").contains(UName)) {
		String Hash = HC.SelectItem("password", "U.Name");
		char[] pw = Pass.toCharArray();
		// Виж дали има паролата е вярна
		if (VerifyPass(Pass, pw)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Хешира нова парола.
     * 
     * @param Парола за хеширане
     * @return Хеша на паролата.
     */
    public String HashNewPassword(String Pass) {
	// Хешираме паролата - 24 "врътки", ползваме 64Mb, 2 нишки, подаваме паролата
	char[] p = Pass.toCharArray();
	String HashedPass = Ar.hash(MAX_ITERS, 65536, 2, p);
	return HashedPass;
    }

    /**
     * Хешира нова парола.
     * 
     * @param Hash Хеш за сравнение
     * @param Pass Парола за сравнение
     * 
     * @return true при вярна парола, false при грешна парола
     */
    public boolean VerifyPass(String Hash, char[] Pass) {
	boolean IsValid = false;
	try {
	    if (Ar.verify(Hash, Pass)) {
		IsValid = true;
	    } else {
		IsValid = false;
	    }
	} finally {
	    Ar.wipeArray(Pass);
	}
	return IsValid;
    }
}
