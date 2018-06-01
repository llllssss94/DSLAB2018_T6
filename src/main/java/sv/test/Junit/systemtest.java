package Junit;

import static org.junit.Assert.*;

import org.junit.Test;

import ATM.Account;
import ATM.MainSystem;

public class systemtest {
	MainSystem main = new MainSystem();

	@Test
	public void testGetAccount() {
		main.insert("023456789");
		main.offer[0].readDatabase(main.getAccount());
		assertEquals(main.getAccount().getAccountNumber(), "23456789");
	}

	@Test
	public void testSetAccount() {
		Account account = new Account();
		account.setAccountNumber("23456789");
		account.setBank("±ππŒ¿∫«‡");
		main.offer[0].readDatabase(account);
		main.setAccount(account);
		assertEquals(account.getBalance(), main.getAccount().getBalance());
	}

	@Test
	public void testInsert() {
		main.insert("023456789");
		main.setAccount(main.getAccount());
		assertEquals(main.getAccount().getAccountNumber(), "23456789");
	}

	@Test
	public void testLockAccount() {
		main.insert("023456789");
		assertEquals(main.lockAccount(), 2);
	}

	@Test
	public void testDeposit() {
		main.insert("023456789");
		assertEquals(main.deposit("30000"), 2);
	}

	@Test
	public void testWithdraw() {
		main.insert("023456789");
		assertEquals(main.withdraw("30000"), 2);
	}

	@Test
	public void testTransfer() {
		main.insert("023456789");
		Account account = new Account();
		assertEquals(main.transfer("30000", account), 2);		
	}

	@Test
	public void testExchange() {
		main.insert("023456789");
		assertEquals(main.exchange("30000", "USD"), 1);
	}

	@Test
	public void testLoan() {
		main.insert("023456789");
		assertEquals(main.loan("30000"), 7);
	}

	@Test
	public void testTakeCharge() {
		main.insert("023456789");
		main.offer[0].readDatabase(main.getAccount());
		main.takeCharge(main.getAccount());
		assertEquals(main.getAccount().getAccountNumber(), "23456789");
	}

	@Test
	public void testDepositWithoutBank() {
		main.insert("023456789");		
		assertEquals(main.depositWithoutBank("30000"), 2);
	}

	@Test
	public void testCheckBalance() {
		main.insert("023456789");
		main.offer[0].readDatabase(main.getAccount());
		assertEquals(main.checkBalance(), main.getAccount().getLog());
	}

	@Test
	public void testPayUtilityBill() {
		main.insert("023456789");
		main.offer[0].readDatabase(main.getAccount());
		Account account = new Account();
		main.offer[0].readDatabase(account);
		assertEquals(main.payUtilityBill(account),2);
	}

}
