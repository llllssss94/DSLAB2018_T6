package ATM;

public class Account {
	private String name;
	private String bank;
	private String accountNumber;
	private String log;
	private String limit;
	private String dept;
	private boolean isLocked;
	private int password;
	private int rate;
	private String balance;
	
	public Account() {
		this.setName(null);
		this.setBank(null);
		this.setAccountNumber(null);
		this.setLog(null);
		this.setLimit(null);
		this.setDept(null);
		this.setIsLocked(false);
		this.setPassword(0000);
		this.setRate(0);
		this.setBalance(null);		
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
	
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public String getBalance() {
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
	
	public void setLimit(String limit) {
		this.limit = limit;
	}
	
	public String getLimit() {
		return this.limit;
	}
	
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public String getDept() {
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
