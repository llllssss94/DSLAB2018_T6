package Junit;

import static org.junit.Assert.*;

import org.junit.Test;

import ATM.Account;
import OFFER.Offer;

public class Filetest {

	@Test
	public void testCheckValid() {
		Offer test = new Offer(0);
		Account account = new Account();
		account.setBank("국민은행");
		account.setAccountNumber("23456789");
		assertEquals(true, test.checkValid(account));
	}

	@Test
	public void testUpdateDatabase() {
		Offer test = new Offer(0);
		Account testaccount = new Account();
		testaccount.setBank("국민은행");
		testaccount.setAccountNumber("23456789");
		testaccount.setBalance(222080);
		testaccount.setIsLocked(false);
		testaccount.setName("권성완");
		testaccount.setPassword(4752);
		testaccount.setRate(3);
		testaccount.setLog("");
		assertEquals(true, test.updateDatabase(testaccount));
	}

	@Test
	public void testReadDatabase() {
		Account testaccount = new Account();
		testaccount.setBank("국민은행");
		testaccount.setAccountNumber("23456789");
		testaccount.setBalance(222080);
		testaccount.setIsLocked(false);
		testaccount.setName("권성완");
		testaccount.setPassword(4752);
		testaccount.setRate(3);
		Account account = new Account();
		Offer test = new Offer(0);
		account.setBank("국민은행");
		account.setAccountNumber("23456789");
		test.readDatabase(account);
		assertEquals(testaccount.getBalance(), account.getBalance());
	}

}
