package org.avy.viber2.DataManagers;

import org.avy.viber2.Database.DatabaseConnection;

public class Login extends DatabaseConnection {
    // private HiberConnect HC;

    /**
     * Автентикира потребителя
     * 
     * @param UName име на потребителя
     * @param Pass  паролата на потребителя
     * @return true Успешно валидирани данни, false Невалидни данни
     * @throws Exception
     */
    public boolean AuthenticateUser(String UName, String Pass) throws Exception {
	throw new UnsupportedOperationException();
	// Ако нишката е там
	/*
	 * if (HC.isAlive()) { // Ако има такова име if (HC.SelectItem("name",
	 * "U.Name").contains(UName)) { // String Hash = HC.SelectItem("password",
	 * "U.Name"); char[] pw = Pass.toCharArray(); // Виж дали има паролата е вярна
	 * if (false) { return true; } } } return false;
	 */
    }

    /**
     * Хешира нова парола.
     * 
     * @param Парола за хеширане
     * @return Хеша на паролата.
     */
    public String HashNewPassword(String Pass) {
	throw new UnsupportedOperationException();
    }

    /**
     * Хешира нова парола.
     * 
     * @param Hash Хеш за сравнение
     * @param Pass Парола за сравнение
     * 
     * @return true при вярна парола, false при грешна парола
     */
    public boolean VerifyPass(String Hash, String Pass) {
	throw new UnsupportedOperationException();
    }
}
