package OFFER;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import ATM.Account;

public class Offer {
	private String company;		
	private BufferedReader input;
	private PrintWriter output;
	
	public Offer(int company) {
		String[] list_bank = {"��������", "�������", "��������", "��������", "��Ƽ����", "�츮����", "�ѱ�����", "�Ｚī��", "����ī��", "�Ե�ī��"};
		this.company = list_bank[company];
		this.input = null;
		this.output = null;
	}
	
	public boolean checkValid(Account account) {
		try {
			String direc = "./ȸ��/" + company + "/" +account.getAccountNumber() + "/��������.txt";
			System.out.println(direc);
			this.input = new BufferedReader(new FileReader(direc));			
			this.input.close();
			System.out.println("valid input");
		} catch(Exception e) {
			System.out.println("invalid input");
			return false;
		}
		return true;
	}
	
	public boolean updateDatabase(Account newAccount) {
		try {
			String direc;
			boolean card = newAccount.getBank().substring(2, 4).equals("ī��");
			
			direc = "./ȸ��/" + company + "/" +newAccount.getAccountNumber() + "/�α�.txt";
			this.output = new PrintWriter(direc);
			this.output.print(newAccount.getLog());
			this.output.flush();
			this.output.close();
			direc = "./ȸ��/" + company + "/" +newAccount.getAccountNumber() + "/��������.txt";
			this.output = new PrintWriter(direc);
			this.output.println(newAccount.getIsLocked());
			this.output.println(newAccount.getName());
			this.output.println(newAccount.getPassword());
			this.output.println(newAccount.getRate());
			if(card) {
				this.output.println(newAccount.getLimit());
				this.output.flush();
				this.output.close();
				direc = "./ȸ��/" + company + "/" +newAccount.getAccountNumber() + "/�ܾ�.txt";
				this.output = new PrintWriter(direc);
				this.output.println(newAccount.getDept());
				this.output.flush();
				this.output.close();
			} else {
				this.output.flush();
				this.output.close();
				direc = "./ȸ��/" + company + "/" +newAccount.getAccountNumber() + "/�ܾ�.txt";
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
			boolean card = currentAccount.getBank().substring(2, 4).equals("ī��");
			direc = "./ȸ��/" + company + "/" +currentAccount.getAccountNumber() + "/��������.txt";
			this.input = new BufferedReader(new FileReader(direc));
			if(input.readLine().equals("true")) {
				currentAccount.setIsLocked(true);
				System.out.println("error 2 : [requestInformationFromServer] the account is locked");
				return 2;
			}
			currentAccount.setName(this.input.readLine());
			currentAccount.setPassword(Integer.parseInt(this.input.readLine()));
			currentAccount.setRate(Integer.parseInt(this.input.readLine()));
			if(card) {
				currentAccount.setLimit(Integer.parseInt(this.input.readLine()));
				this.input.close();
				direc = "./ȸ��/" + company + "/" +currentAccount.getAccountNumber() + "/�ܾ�.txt";
				this.input = new BufferedReader(new FileReader(direc));
				currentAccount.setDept(Integer.parseInt(input.readLine()));
			} else {
				this.input.close();
				direc = "./ȸ��/" + company + "/" +currentAccount.getAccountNumber() + "/�ܾ�.txt";
				this.input = new BufferedReader(new FileReader(direc));
				currentAccount.setBalance(Integer.parseInt(input.readLine()));
			}
			
			direc = "./ȸ��/" + company + "/" +currentAccount.getAccountNumber() + "/�α�.txt";
			this.input = new BufferedReader(new FileReader(direc));
			String log = "";
			String tempLog;
			while((tempLog = this.input.readLine()) != null) {
				log = log + tempLog + "\n";	
			}
			currentAccount.setLog(log);
			this.input.close();		
		} catch(Exception e) {
			System.out.println("error 1 : [requestInformationFromServer] load information failed");
			return 1;
		}
		System.out.println("success : [requestInformationFromServer] end");
		return 0;
	}
}