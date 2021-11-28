package org.avy.viber2.Login;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
//import de.mkammerer.argon2.Argon2Helper;

public class Login {
    // Argon2d хеш алгоритъм; 16 байта сол, 32 байта хеш
    Argon2 Ar = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2d, 16, 32);

    /**
     * Намира оптимален брой итерации.
     * 
     * @return Брой итерации за 1 секунда, при използвани 64Mb и 2 нишки.
     */
    private int ArgonFindOptimalIters() {
	throw new UnsupportedOperationException("Not implemented yet");
	// return Argon2Helper.findIterations(Ar, 1000, 65536, 2);
    }

    /**
     * Хешира нова парола.
     * 
     * @param Pass Парола за хеширане
     * @return Хеша на паролата.
     */
    public String HashNewPassword(String Pass) {
	// Хешираме паролата - 24 "врътки", ползваме 64Mb, 2 нишки, подаваме паролата
	String HashedPass = Ar.hash(24, 65536, 2, Pass.toCharArray());
	return HashedPass;
    }

    /**
     * Хешира нова парола.
     * 
     * @param Hash Хеш за сравнение
     * @param Pass Парола за сравнение
     */
    public void VerifyPass(String Hash, char[] Pass) {
	// Хешираме паролата - 24 "врътки", ползваме 64Mb, 2 нишки, подаваме паролата
	try {
	    if (Ar.verify(Hash, Pass)) {
		System.out.println("Hashes match!");
	    } else {
		System.out.println("No hash match!");
	    }
	} finally {
	    Ar.wipeArray(Pass);
	}
    }
}
