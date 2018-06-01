package OFFER;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import ATM.Account;

public class Offer {
	String company;
	private BufferedReader input;
	private PrintWriter output;
	
	public Offer(int company) {
		String[] list_bank = {"국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드"};
		this.company = list_bank[company];
		this.input = null;
		this.output = null;
	}
	
	public boolean checkValid(Account account) {
		try {
			String direc = "./회사/" + account.getBank() + "/" +account.getAccountNumber() + "/개인정보.txt";
			this.input = new BufferedReader(new FileReader(direc));			
			this.input.close();
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean updateDatabase(Account newAccount) {
		try {
			String direc;
			boolean card = newAccount.getBank().substring(2, 4).equals("카드");
			
			direc = "./회사/" + newAccount.getBank() + "/" +newAccount.getAccountNumber() + "/로그.txt";
			this.output = new PrintWriter(direc);
			this.output.print(newAccount.getLog());
			this.output.flush();
			this.output.close();
			direc = "./회사/" + newAccount.getBank() + "/" +newAccount.getAccountNumber() + "/개인정보.txt";
			this.output = new PrintWriter(direc);
			this.output.println(newAccount.getIsLocked());
			this.output.println(newAccount.getName());
			this.output.println(newAccount.getPassword());
			this.output.println(newAccount.getRate());
			if(card) {
				this.output.println(newAccount.getLimit());
				this.output.flush();
				this.output.close();
				direc = "./회사/" + newAccount.getBank() + "/" +newAccount.getAccountNumber() + "/잔액.txt";
				this.output = new PrintWriter(direc);
				this.output.println(newAccount.getDept());
				this.output.flush();
				this.output.close();
			} else {
				this.output.flush();
				this.output.close();
				direc = "./회사/" + newAccount.getBank() + "/" +newAccount.getAccountNumber() + "/잔액.txt";
				this.output = new PrintWriter(direc);
				this.output.println(newAccount.getBalance());
				this.output.flush();
				this.output.close();
			}			
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public int readDatabase(Account currentAccount) {
		try {
			String direc;
			boolean card = currentAccount.getBank().substring(2, 4).equals("카드");
			direc = "./회사/" + currentAccount.getBank() + "/" +currentAccount.getAccountNumber() + "/개인정보.txt";
			this.input = new BufferedReader(new FileReader(direc));
			if(input.readLine().equals("true")) {
				currentAccount.setIsLocked(true);
				return 5;
			}			
			currentAccount.setName(this.input.readLine());
			currentAccount.setPassword(Integer.parseInt(this.input.readLine()));
			currentAccount.setRate(Integer.parseInt(this.input.readLine()));
			if(card) {
				currentAccount.setLimit(this.input.readLine());
				this.input.close();
				direc = "./회사/" + currentAccount.getBank() + "/" +currentAccount.getAccountNumber() + "/잔액.txt";
				this.input = new BufferedReader(new FileReader(direc));
				currentAccount.setDept(input.readLine());
			} else {
				this.input.close();
				direc = "./회사/" + currentAccount.getBank() + "/" +currentAccount.getAccountNumber() + "/잔액.txt";
				this.input = new BufferedReader(new FileReader(direc));
				currentAccount.setBalance(input.readLine());
			}
			
			direc = "./회사/" + currentAccount.getBank() + "/" +currentAccount.getAccountNumber() + "/로그.txt";
			this.input = new BufferedReader(new FileReader(direc));
			String log = "";
			String tempLog;
			while((tempLog = this.input.readLine()) != null) {
				log = log + tempLog + "\n";	
			}
			currentAccount.setLog(log);
			this.input.close();		
		} catch(Exception e) {
			return 1;
		}
		return 0;
	}
}
