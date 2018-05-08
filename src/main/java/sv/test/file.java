package OFFER;

// 카드번호 예시 X-XXXX-XXXX

import java.io.FileOutputStream;
import java.io.FileInputStream;

public class file {
	private String bank;
	private String account;
	private String data;
    private String[] list_bank = {"신한은행", "국민은행", "하나은행", "우리은행", "기업은행", "농협은행", "씨티은행"};
    FileInputStream input;
    FileOutputStream output;
    
	public file() {
		bank = null;
		account = null;
		data = null;
	}
	
	public boolean ReadInput(String input) {
		if(Character.getNumericValue(input.charAt(0)) > 6) {
			System.out.println("error : input error");
			return false;
		}
		bank = list_bank[Character.getNumericValue(input.charAt(0))];
		account = input.substring(1);
		return true;
	}
	
	public void EndBank() {
		bank = null;
		account = null;
		data = null;
	}
	
	
	public boolean CheckValidation(String input) {
		if(!ReadInput(input))
			return false;
		System.out.println("account = " + account + " bank = " + bank);
		try {
			String direc = "./은행/" + bank + "/" + account + "/잔액.txt";
			this.input = new FileInputStream(direc);
			this.input.close();
			return true;
		} catch(Exception e) {
			System.out.println("error : invalid input");
			return false;
		}
	}
	
	public boolean Deposit(int money) {
		int sum;
		try {
			String direc = "./은행/" + bank + "/" + account + "/잔액.txt";
			input = new FileInputStream(direc);
			sum = input.read();
			System.out.println(sum);
			sum += money;
			input.close();			
			output = new FileOutputStream(direc);
			output.write(sum);			
			output.close();
			EndBank();
		} catch(Exception e) {
			System.out.println("error : deposit");
			return false;
		}
		return true;
	}
}
