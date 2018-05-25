package ATM;

public class Account {
	private String name;
	private String bank;
	private String accountNumber;
	private String log;
	private int limit;
	private int dept;
	private boolean isLocked;
	private int password;
	private int rate;
	private int balance;
	
	public Account() {
		this.setName(null);
		this.setBank(null);
		this.setAccountNumber(null);
		this.setLog(null);
		this.setLimit(0);
		this.setDept(0);
		this.setIsLocked(false);
		this.setPassword(0000);
		this.setRate(0);
		this.setBalance(0);		
	}
	
	public boolean checkPassword(int password) {
		if(this.password == password)
			return true;
		return false;
	}
	
	public void setLog(String log) {
		this.log = log;
	}
	
	public String getLog() {
		return this.log;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getBalance() {
		return this.balance;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	public String getBank() {
		return this.bank;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public int getLimit() {
		return this.limit;
	}
	
	public void setDept(int dept) {
		this.dept = dept;
	}
	
	public int getDept() {
		return this.dept;
	}
	
	public void setIsLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	
	public boolean getIsLocked() {
		return this.isLocked;
	}
	
	public void setPassword(int password) {
		this.password = password;
	}
	
	public int getPassword() {
		return this.password;
	}
	
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public int getRate() {
		return this.rate;
	}
}
