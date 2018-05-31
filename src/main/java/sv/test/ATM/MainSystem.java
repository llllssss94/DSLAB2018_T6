package ATM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import OFFER.Offer;

public class MainSystem {
	private Account account;
	public String card;
	public Offer[] offer;
	public int bank;
	private int exchangeRate;
	private int wrongPasswordTimes;
	private int errorType;

	public MainSystem() {
		account = new Account();
		card = null;
		offer = new Offer[10];
		for (int i = 0; i < 10; i++)
			offer[i] = new Offer(i);
		exchangeRate = 0;
		wrongPasswordTimes = 0;
		errorType = 0;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	// 체크카드 0, 통장 1, 신용카드 2, 지로용지3 // 은행 // 계좌정보
	public void insert(String str) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		this.card = str;
		this.account.setBank(list_bank[Integer.parseInt(card.charAt(0) + "")]);
		this.account.setAccountNumber(card.substring(1));
	}

	public int lockAccount() {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for(bank = 0; bank < list_bank.length; bank++) {
			if(list_bank[bank].equals(account.getBank()))
				break;
		}
		if ((this.errorType = offer[bank].readDatabase(account)) == 0) {
			account.setIsLocked(true);
			if (offer[bank].updateDatabase(account)) {
				return 0;
			} else
				return 2;
		} else
			return 2;
	}

	public int deposit(int money) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for(bank = 0; bank < list_bank.length; bank++) {
			if(list_bank[bank].equals(account.getBank()))
				break;
		}
		if (this.account.getBank().substring(2, 4).equals("카드")) {			
			if ((this.errorType = offer[bank].readDatabase(account)) == 0) {
				if (account.getDept() < money) {
					return 6;
				} else {
					account.setDept(account.getDept() - money);
					String newLog = new Date() + " " + money + " 상환 \t( 대출금 : " + account.getDept() + " )\n";
					newLog = newLog + account.getLog();
					account.setLog(newLog);
					if (offer[bank].updateDatabase(account)) {
						return 0;
					} else {
						return 2;
					}
				}
			} else {
				return 2;
			}
		} else {
			if ((this.errorType = offer[bank].readDatabase(account)) == 0) {
				account.setBalance(account.getBalance() + money);
				String newLog = new Date() + " " + money + " 입금 \t( 잔액 : " + account.getBalance() + " )\n";
				newLog = newLog + account.getLog();
				account.setLog(newLog);
				if (offer[bank].updateDatabase(account)) {
					return 0;
				} else {
					return 2;
				}
			} else {
				return 2;
			}
		}
	}

	public int withdraw(int money) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for(bank = 0; bank < list_bank.length; bank++) {
			if(list_bank[bank].equals(account.getBank()))
				break;
		}
		if ((this.errorType = offer[bank].readDatabase(account)) == 0) {
			if ((account.getBank().equals("신한은행") && account.getBalance() < money)
					|| (!account.getBank().equals("신한은행") && account.getBalance() < money + 1300)) {
				return 1;
			} else {
				String newLog;
				if (!account.getBank().equals("신한은행")) {
					this.takeCharge(account);					
				}
				account.setBalance(account.getBalance() - money);
				newLog = new Date() + " " + money + " 출금 \t( 잔액 : " + account.getBalance() + " )\n" + account.getLog();
				account.setLog(newLog);
				
				if (offer[bank].updateDatabase(account)) {
					return 0;
				} else {
					return 2;
				}
			}
		} else {
			return 2;
		}
	}

	public int transfer(int money, Account newAccount) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for(bank = 0; bank < list_bank.length; bank++) {
			if(list_bank[bank].equals(account.getBank()))
				break;
		}		
		if (offer[bank].checkValid(account)) {
			if ((this.errorType = offer[bank].readDatabase(account)) == 0) {
				int temp_bank = 20;
				for (int i = 0; i < 10; i++) {
					if (newAccount.getBank().equals(list_bank[i])) {
						temp_bank = i;
						break;
					}
				}
				if (temp_bank == 20) {
					return 3;
				}
				if ((account.getBank().equals("신한은행") && account.getBalance() < money)
						|| (!account.getBank().equals("신한은행") && account.getBalance() < money + 1300)) {
					return 1;
				} else {
					if (!account.getBank().equals("신한은행"))
						this.takeCharge(account);
					if (newAccount.getBank().substring(2, 4).equals("카드")) {			
						if ((this.errorType = offer[bank].readDatabase(newAccount)) == 0) {
							if (newAccount.getDept() < money) {
								return 6;
							} else {
								newAccount.setDept(newAccount.getDept() - money);
								String newLog = new Date() + " " + money + " 상환 \t( 대출금 : " + newAccount.getDept() + " )\n";
								newLog = newLog + newAccount.getLog();
								newAccount.setLog(newLog);
								if (!offer[bank].updateDatabase(newAccount)) {
									return 2;
								}
								offer[bank].readDatabase(account);
								if(account.getBank() != "신한은행")
									takeCharge(account);
								account.setBalance(account.getBalance() - money);
								newLog = new Date() + " " + money + " 송금 " + newAccount.getBank() + " "
										+ newAccount.getAccountNumber() + "\t( 잔액 : " + account.getBalance() + " )\n";
								newLog = newLog + account.getLog();
								account.setLog(newLog);					
								if (offer[bank].updateDatabase(account)) {
									return 0;
								} else {
									newAccount.setDept(newAccount.getDept() + money);
									newLog = new Date() + " " + money + " 기존거래 취소 \t( 대출금 : " + newAccount.getDept() + " )\n";
									newLog = newLog + newAccount.getLog();
									newAccount.setLog(newLog);
									if (!offer[bank].updateDatabase(newAccount)) {
										return 2;
									}
									return 2;
								}								
							}
						} else {
							return 2;
						}
					}
					newAccount.setBalance(newAccount.getBalance() + money);
					String tempLog = new Date() + " " + money + " 입금 " + account.getBank() + " "
							+ account.getAccountNumber() + "\t( 잔액 : " + newAccount.getBalance() + " )\n";
					System.out.println(tempLog);
					tempLog = tempLog + newAccount.getLog();
					newAccount.setLog(tempLog);
					if (!offer[temp_bank].updateDatabase(newAccount)) {
						return 2;
					}
					offer[bank].readDatabase(account);
					account.setBalance(account.getBalance() - money);
					String newLog = new Date() + " " + money + " 송금 " + newAccount.getBank() + " "
							+ newAccount.getAccountNumber() + "\t( 잔액 : " + account.getBalance() + " )\n";
					newLog = newLog + account.getLog();
					account.setLog(newLog);					
					if (offer[bank].updateDatabase(account)) {
						return 0;
					} else {
						newAccount.setBalance(newAccount.getBalance() + money);
						tempLog = new Date() + " " + money + " 기존거래 취소 " + account.getBank() + " "
								+ account.getAccountNumber() + "\t( 잔액 : " + newAccount.getBalance() + " )\n";
						System.out.println(tempLog);
						tempLog = tempLog + newAccount.getLog();
						newAccount.setLog(tempLog);
						if (!offer[temp_bank].updateDatabase(newAccount)) {
							return 2;
						}
						return 2;
					}
				}
			} else {
				return 2;
			}
		}
		return 2;
	}

	public int exchange(int money, String str) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for(bank = 0; bank < list_bank.length; bank++) {
			if(list_bank[bank].equals(account.getBank()))
				break;
		}
		int[] exchangeRate = { 1100, 1000, 1280, 200 };
		String[] country = { "USD", "JPY", "EUR", "CNY" };
		int i;
		int won;
		for (i = 0; i < 4; i++) {
			if (str.equals(country[i]))
				break;
		}
		if (this.account.getBalance() < (won = money * exchangeRate[i])) {
			return 1;
		} else {
			account.setBalance(account.getBalance() - won);
			String newLog = new Date() + " " + won + "원 을 " + money + str + "로 환전 " + "\t( 잔액 : " + account.getBalance()
					+ " )\n";
			newLog = newLog + account.getLog();
			account.setLog(newLog);
			if (offer[bank].updateDatabase(account)) {
				return 0;
			} else {
				return 2;
			}
		}
	}

	public int loan(int money) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for(bank = 0; bank < list_bank.length; bank++) {
			if(list_bank[bank].equals(account.getBank()))
				break;
		}
		if (account.getLimit() < account.getDept() + money) {
			return 7;
		} else {
			offer[bank].readDatabase(account);
			account.setDept(account.getDept() + money);
			String newLog = new Date() + " " + money + " 대출 " + "\t( 남은 한도 : "
					+ (account.getLimit() - account.getDept()) + " )\n";
			newLog = newLog + account.getLog();
			account.setLog(newLog);
			if (offer[bank].updateDatabase(account)) {
				return 0;
			} else {
				return 2;
			}
		}
	}

	public void takeCharge(Account account) {
		account.setBalance(account.getBalance() - 1300);
		String newLog = new Date() + " 1300" + " 수수료 \t( 잔액 : " + account.getBalance() + " )\n" + account.getLog();
		account.setLog(newLog);
	}

	public int depositWithoutBank(int money) {
		return deposit(money);
	}

	public String checkBalance() {
		return this.account.getLog();
	}

	public int payUtilityBill(Account newAccount) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for(bank = 0; bank < list_bank.length; bank++) {
			if(list_bank[bank].equals(account.getBank()))
				break;
		}
		if (offer[bank].checkValid(account)) {
			int money = newAccount.getBalance();
			if ((this.errorType = offer[bank].readDatabase(account)) != 0) {
				return 2;
			}
			if ((account.getBank().equals("신한은행") && account.getBalance() < money)
					|| (!account.getBank().equals("신한은행") && account.getBalance() < money + 1300)) {
				return 1;
			} else if(newAccount.getBalance() == 0) {
				return 9;
			} else {				
				newAccount.setBalance(newAccount.getBalance() - money);
				String tempLog = new Date() + " " + money + " 공과금납부 " + account.getBank() + " "
						+ account.getAccountNumber() + "\t( 잔액 : " + newAccount.getBalance() + " )\n";
				tempLog = tempLog + newAccount.getLog();
				newAccount.setLog(tempLog);
				if (!offer[6].updateDatabase(newAccount)) {
					return 2;
				}
				if (!account.getBank().equals("신한은행"))
					this.takeCharge(account);
				offer[bank].readDatabase(account);
				account.setBalance(account.getBalance() - money);
				String newLog = new Date() + " " + money + " 공과금납부 " + newAccount.getBank() + " "
						+ newAccount.getAccountNumber() + "\t( 잔액 : " + account.getBalance() + " )\n";
				newLog = newLog + account.getLog();
				account.setLog(newLog);				
				if (offer[bank].updateDatabase(account)) {
					return 0;
				} else {
					return 2;
				}
			}
		} else {
			return 2;
		}
	}
}
