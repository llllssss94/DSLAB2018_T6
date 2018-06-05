package ATM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import OFFER.Offer;

public class MainSystem {
	private Account account;
	public String card;
	public Offer[] offer;
	public String bank;
	public String b5, b1;
	public int errorType, ch;

	public MainSystem() {
		account = new Account();
		card = null;
		offer = new Offer[10];
		for (int i = 0; i < 10; i++)
			offer[i] = new Offer(i);
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
		for (bank = 0; bank < list_bank.length; bank++) {
			if (list_bank[bank].equals(account.getBank()))
				break;
		}
		if ((this.errorType = offer[bank].readDatabase(account)) == 0) {
			account.setIsLocked(true);
			if (offer[bank].updateDatabase(account)) {
				return 0;
			} else
				return 2;
		} else
			return 3;
	}

	public int deposit(String money) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for (bank = 0; bank < list_bank.length; bank++) {
			if (list_bank[bank].equals(account.getBank()))
				break;
		}
		if (this.account.getBank().substring(2, 4).equals("카드")) {
			if ((this.errorType = offer[bank].readDatabase(account)) == 0) {
				if (isBig(money, account.getDept())) {
					return 6;
				} else {
					account.setDept(minus(account.getDept(), money));
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
				return 3;
			}
		} else {
			if ((this.errorType = offer[bank].readDatabase(account)) == 0) {
				account.setBalance(plus(account.getBalance(), money));
				String newLog = new Date() + " " + money + " 입금 \t( 잔액 : " + account.getBalance() + " )\n";
				newLog = newLog + account.getLog();
				account.setLog(newLog);
				if (offer[bank].updateDatabase(account)) {
					return 0;
				} else {
					return 2;
				}
			} else {
				return 3;
			}
		}
	}

	public int withdraw(String money) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for (bank = 0; bank < list_bank.length; bank++) {
			if (list_bank[bank].equals(account.getBank()))
				break;
		}
		if ((this.errorType = offer[bank].readDatabase(account)) == 0) {
			if ((account.getBank().equals("신한은행") && isBig(money, account.getBalance()))
					|| (!account.getBank().equals("신한은행") && isBig(plus(money, "1300"), account.getBalance()))) {
				return 1;
			} else {
				String newLog;
				if (!account.getBank().equals("신한은행")) {
					this.takeCharge(account);
				}
				account.setBalance(minus(account.getBalance(), money));
				newLog = new Date() + " " + money + " 출금 \t( 잔액 : " + account.getBalance() + " )\n" + account.getLog();
				account.setLog(newLog);

				if (offer[bank].updateDatabase(account)) {
					return 0;
				} else {
					return 2;
				}
			}
		} else {
			return 3;
		}
	}

	public int transfer(String money, Account newAccount) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for (bank = 0; bank < list_bank.length; bank++) {
			if (list_bank[bank].equals(account.getBank()))
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
					return 2;
				}
				if ((account.getBank().equals("신한은행") && isBig(money, account.getBalance()))
						|| (!account.getBank().equals("신한은행") && isBig(plus(money, "1300"), account.getBalance()))) {
					return 1;
				} else {
					if (newAccount.getBank().substring(2, 4).equals("카드")) {
						if ((this.errorType = offer[bank].readDatabase(newAccount)) == 0) {
							if (isBig(money, newAccount.getDept())) {
								return 6;
							} else {
								newAccount.setDept(minus(newAccount.getDept(), money));
								String newLog = new Date() + " " + money + " 상환 \t( 대출금 : " + newAccount.getDept()
										+ " )\n";
								newLog = newLog + newAccount.getLog();
								newAccount.setLog(newLog);
								if (!offer[bank].updateDatabase(newAccount)) {
									return 2;
								}
								offer[bank].readDatabase(account);
								if (!account.getBank().equals("신한은행") || !newAccount.getBank().equals("신한은행"))
									takeCharge(account);
								account.setBalance(minus(account.getBalance(), money));
								newLog = new Date() + " " + money + " 송금 " + newAccount.getBank() + " "
										+ newAccount.getAccountNumber() + "\t( 잔액 : " + account.getBalance() + " )\n";
								newLog = newLog + account.getLog();
								account.setLog(newLog);
								if (offer[bank].updateDatabase(account)) {
									return 0;
								} else {
									newAccount.setDept(plus(newAccount.getDept(), money));
									newLog = new Date() + " " + money + " 기존거래 취소 \t( 대출금 : " + newAccount.getDept()
											+ " )\n";
									newLog = newLog + newAccount.getLog();
									newAccount.setLog(newLog);
									if (!offer[bank].updateDatabase(newAccount)) {
										return 2;
									}
									return 2;
								}
							}
						} else {
							return 3;
						}
					}
					newAccount.setBalance(plus(newAccount.getBalance(), money));
					String tempLog = new Date() + " " + money + " 입금 " + account.getBank() + " "
							+ account.getAccountNumber() + "\t( 잔액 : " + newAccount.getBalance() + " )\n";
					tempLog = tempLog + newAccount.getLog();
					newAccount.setLog(tempLog);
					if (!offer[temp_bank].updateDatabase(newAccount)) {
						return 2;
					}
					offer[bank].readDatabase(account);
					if (!account.getBank().equals("신한은행") || !newAccount.getBank().equals("신한은행"))
						takeCharge(account);
					account.setBalance(minus(account.getBalance(), money));
					String newLog = new Date() + " " + money + " 송금 " + newAccount.getBank() + " "
							+ newAccount.getAccountNumber() + "\t( 잔액 : " + account.getBalance() + " )\n";
					newLog = newLog + account.getLog();
					account.setLog(newLog);
					if (offer[bank].updateDatabase(account)) {
						return 0;
					} else {
						newAccount.setBalance(plus(newAccount.getBalance(), money));
						tempLog = new Date() + " " + money + " 기존거래 취소 " + account.getBank() + " "
								+ account.getAccountNumber() + "\t( 잔액 : " + newAccount.getBalance() + " )\n";
						tempLog = tempLog + newAccount.getLog();
						newAccount.setLog(tempLog);
						if (!offer[temp_bank].updateDatabase(newAccount)) {
							return 2;
						}
						return 2;
					}
				}
			} else {
				return 3;
			}
		}
		return 3;
	}

	public int exchange(String money, String str) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for (bank = 0; bank < list_bank.length; bank++) {
			if (list_bank[bank].equals(account.getBank()))
				break;
		}
		int[] exchangeRate = { 1100, 10, 1280, 200 };
		String[] country = { "USD", "JPY", "EUR", "CNY" };
		int i;
		int won;
		for (i = 0; i < 4; i++) {
			if (str.equals(country[i]))
				break;
		}
		if (isBig(String.valueOf((won = Integer.parseInt(money) * exchangeRate[i])), this.account.getBalance())) {
			return 1;
		} else {
			account.setBalance(minus(account.getBalance(), String.valueOf(won)));
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

	public int loan(String money) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for (bank = 0; bank < list_bank.length; bank++) {
			if (list_bank[bank].equals(account.getBank()))
				break;
		}
		if (isBig(plus(plus(account.getDept(), money), "1300"), account.getLimit())) {
			return 7;
		} else {
			offer[bank].readDatabase(account);
			takeCharge(account);
			account.setDept(plus(account.getDept(), money));
			String newLog = new Date() + " " + money + " 대출 " + "\t( 남은 한도 : "
					+ minus(account.getLimit(), account.getDept()) + " )\n";
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
		if (account.getRate() < 3)
			return;
		this.ch = 21;
		String newLog;
		if (account.getBank().substring(2, 4).equals("카드")) {
			account.setDept(plus(account.getDept(), "1300"));
			newLog = new Date() + "1300" + "수수료 \t( 한도 : " + minus(account.getLimit(), account.getDept()) + " )\n" + account.getLog();
		} else {
			account.setBalance(minus(account.getBalance(), "1300"));
			newLog = new Date() + " 1300" + " 수수료 \t( 잔액 : " + account.getBalance() + " )\n" + account.getLog();
		}		
		account.setLog(newLog);
	}

	public int depositWithoutBank(String money) {
		return deposit(money);
	}

	public String checkBalance() {
		return this.account.getLog();
	}

	public int payUtilityBill(Account newAccount) {
		String[] list_bank = { "국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드" };
		int bank;
		for (bank = 0; bank < list_bank.length; bank++) {
			if (list_bank[bank].equals(account.getBank()))
				break;
		}
		if (offer[bank].checkValid(account)) {
			String money = newAccount.getBalance();
			if ((this.errorType = offer[bank].readDatabase(account)) != 0) {
				return 2;
			}
			if ((account.getBank().equals("신한은행") && isBig(money, account.getBalance()))
					|| (!account.getBank().equals("신한은행") && isBig(plus(money, "1300"), account.getBalance()))) {
				return 1;
			} else if (newAccount.getBalance().equals("0")) {
				return 9;
			} else {
				newAccount.setBalance(minus(newAccount.getBalance(), money));
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
				account.setBalance(minus(account.getBalance(), money));
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
			return 3;
		}
	}

	public boolean isBig(String n1, String n2) {
		if (n1.length() > n2.length())
			return true;
		else if (n1.length() == n2.length()) {
			for (int i = 0; i < n1.length(); i++) {
				if (n1.charAt(i) == n2.charAt(i))
					continue;
				if (n1.charAt(i) < n2.charAt(i))
					return false;
				return true;
			}
		}
		return false;
	}

	public String minus(String n1, String n2) {
		if (n1.equals(n2))
			return "0";
		String sum = n1.substring(0, n1.length() - n2.length());
		for (int i = 0; i < n2.length(); i++) {
			char a = n1.charAt(n1.length() - n2.length() + i);
			char b = n2.charAt(i);
			if (a == b) {
				sum = sum + '0';
			} else if (isBig(a + "", b + "")) {
				sum = sum + String.valueOf(Integer.parseInt(a + "") - Integer.parseInt(b + ""));
			} else {
				int j, c;
				for (j = 0; sum.charAt(sum.length() - 1 - j) == '0'; j++)
					;
				if (sum.length() - 1 - j == 0) {
					if (sum.charAt(0) == '1') {
						sum = "";
						for (int k = 0; k < j; k++)
							sum = sum + '9';
					} else {
						c = Integer.parseInt(sum.charAt(0) + "") - 1;
						sum = String.valueOf(c);
						for (int k = 0; k < j; k++) {
							sum = sum + '9';
						}
					}
				}
				c = Integer.parseInt("1" + a) - Integer.parseInt("" + b);
				sum = sum + String.valueOf(c);
			}
		}
		if (sum.length() > 0) {
			while (sum.substring(0, 1).equals("0"))
				sum = sum.substring(1, sum.length());
		}
		return sum;
	}

	public String plus(String n1, String n2) {
		String sum = "";
		boolean up = false;
		if (n1.length() > n2.length()) {
			for (int i = 1; i <= n2.length(); i++) {
				char a = n1.charAt(n1.length() - i);
				char b = n2.charAt(n2.length() - i);
				if (!up) {
					if (Integer.parseInt(a + "") + Integer.parseInt(b + "") > 9) {
						sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "")).charAt(1) + sum;
						up = true;
					} else {
						sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "")) + sum;
						up = false;
					}
				} else {
					if (Integer.parseInt(a + "") + Integer.parseInt(b + "") > 8) {
						sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "") + 1).charAt(1) + sum;
						up = true;
					} else {
						sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "") + 1) + sum;
						up = false;
					}
				}
			}
			if (up) {
				for (int i = n1.length() - n2.length() - 1; i >= 0; i--) {
					char a = n1.charAt(i);
					if (a == '9') {
						sum = '0' + sum;
					} else {
						sum = n1.substring(0, i + 1) + sum;
						break;
					}
				}
				if (sum.charAt(0) == '0')
					sum = '1' + sum;
			} else {
				sum = n1.substring(0, n1.length() - n2.length()) + sum;
			}

		} else {
			for (int i = 1; i <= n1.length(); i++) {
				char a = n1.charAt(n1.length() - i);
				char b = n2.charAt(n2.length() - i);
				if (!up) {
					if (Integer.parseInt(a + "") + Integer.parseInt(b + "") > 9) {
						sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "")).charAt(1) + sum;
						up = true;
					} else {
						sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "")) + sum;
						up = false;
					}
				} else {
					if (Integer.parseInt(a + "") + Integer.parseInt(b + "") > 8) {
						sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "") + 1).charAt(1) + sum;
						up = true;
					} else {
						sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "") + 1) + sum;
						up = false;
					}
				}
			}
			if (up) {
				if (n2.length() == n1.length())
					sum = '1' + sum;
				else {
					for (int i = n2.length() - n1.length() - 1; i >= 0; i--) {
						char a = n2.charAt(i);
						if (a == '9') {
							sum = '0' + sum;
						} else {
							sum = n2.substring(0, i + 1) + sum;
							break;
						}
					}
					if (sum.charAt(0) == '0')
						sum = '1' + sum;
				}
			} else {
				sum = n2.substring(0, n2.length() - n1.length()) + sum;
			}
		}
		return sum;
	}
}
