package viber2;

import static org.junit.jupiter.api.Assertions.*;

import org.avy.viber2.Login.Login;
import org.junit.Test;

public class TestPasswords {
    @Test
    public void TestPasswordValidity() {
	Login L = new Login();

	// Тук съвпадат парола-хеш (паролите са подсоелни!)
	String CorrectPass = "uncommon";
	String VerifyHash = L.HashNewPassword(CorrectPass);

	// Тук не съвпадат парола-хеш
	String FalsePass = "blabla", FalseHash = L.HashNewPassword(CorrectPass);

	// Трябва да върне TRUE
	boolean CheckTrue = L.VerifyPass(VerifyHash, CorrectPass.toCharArray());
	assertEquals(true, CheckTrue);

	// Трябва да върне FALSE
	boolean CheckFalse = L.VerifyPass(FalseHash, FalsePass.toCharArray());
	assertEquals(false, CheckFalse);
    }
}
