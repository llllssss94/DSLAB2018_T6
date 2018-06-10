package ATM;

import java.util.Date;

import OFFER.Offer;

//TODO 1: 중복되는 코드 정리
//TODO 2: 체크카드0, 통장1, 신용카드 2... 이런 magic number들 enum으로 정리
//TODO 3: if-else 정리
//TODO 4: if문 조건식 정리. ex)if (isBig(String.valueOf((won = Integer.parseInt(money) * exchangeRate[i])), this.account.getBalance())) {...
//TODO 5: GUI문-  switch에 default 문이 없음.
//TODO 6: 주석추가.
//수정했으면 수정했다고 써 놓겠음.

public class MainSystem
{
    private Account account;
    public String card;
    public Offer[] offer;
    public String bank;
    public String b5, b1;
    public int errorType, ch;

    //-----------------------------------------------------------------
    // String[] list_bank: 메소드마다 들어가 있던거 global로 뺌.
    String[] list_bank = {"국민은행", "기업은행", "농협은행", "신한은행", "씨티은행", "우리은행", "한국은행", "삼성카드", "현대카드", "롯데카드"};

    // 코드마다 return 2; return 3; return 1; 이런거 뭔지 못알아먹겠어서 가독성과 코드의 재활용성을 위해 상수로 따로 뺌.
    static final int NO_ERROR = 0;
    static final int NOT_ENOUGH_BALANCE = 1;
    static final int SERVER_NOT_RESPONSE = 2;
    static final int WRONG_ACCOUNT = 3;
    static final int WRONG_PASSWORD_OVER_3 = 4;
    static final int ACCOUNT_LOCKED = 5;
    static final int DEPOSIT_OVER_DEPT = 6;
    static final int OVER_LIMIT = 7;
    static final int SAME_SENDER_RECEIVER = 8;
    static final int UB_ALREADY_PAYED = 9;


    public MainSystem()
    {
        //main system. system initial.
        account = new Account();
        card = null;
        offer = new Offer[10];
        for (int i = 0; i < 10; i++)
            offer[i] = new Offer(i);
    }


    public Account getAccount()
    {
        return this.account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    private int findWhichBank()
    {
        //수정: 메소드 추가.
        //어떤 은행/카드사인지 찾고 은행종류를 int로 리턴.
        int bank;
        for (bank = 0; bank < list_bank.length; bank++)
        {
            if (list_bank[bank].equals(account.getBank()))
                break;
        }
        return bank;
    }


    private int checkUpdateDB(Offer offer)
    {   //update Database 성공여부 리턴.
        if (offer.updateDatabase(account)) return NO_ERROR;
        else return SERVER_NOT_RESPONSE;
    }

    // 체크카드 0, 통장 1, 신용카드 2, 지로용지3 // 은행 // 계좌정보
    public void insert(String str)
    {
        //계좌번호를 insert받음. GUI에서 사용.
        this.card = str;
        this.account.setBank(list_bank[Integer.parseInt(card.charAt(0) + "")]);
        this.account.setAccountNumber(card.substring(1));
    }

    public boolean isUnderBalance(String money)
    { //잔액이 충분한지 확인하는 메소드

        //신한은행
        if ((account.getBank().equals("신한은행") && isBig(money, account.getBalance()))) return true;
        if (!account.getBank().equals("신한은행") && isBig(plus(money, "1300"), account.getBalance())) return true;
        return false;
    }

    public boolean isWrongAccount(int bankIndex, Account account)
    {   //잘못된 계좌: true 리턴.
        this.errorType = offer[bankIndex].readDatabase(account);
        boolean readWrongDB = (this.errorType != 0);
        return readWrongDB;
    }





    public int lockAccount()
    {   //account를 lock하는 메소드.

        //어느 은행인지 찾는다.
        int bank;
        bank = findWhichBank();

        //계좌가 비정상적인 계좌인지 확인.
        if (isWrongAccount(bank,account)) return WRONG_ACCOUNT;

        //lock을 건다.
        account.setIsLocked(true);

        //DB에 update
        return checkUpdateDB(offer[bank]);

    }


    public int deposit(String money)
    {   //입금 기능. return하는 에러들은 GUI에서 frame만들 때 쓰인다.

        int bank;
        bank = findWhichBank();
        if (isWrongAccount(bank,account)) return WRONG_ACCOUNT;

        //입력한 게좌가 카드번호인경우 빚 상환.
        if (this.account.getBank().substring(2, 4).equals("카드"))
        {
            return deposit_CARD(money,account, bank);
        } else
        { //입력한 계좌가 은행 계좌일 경우.
            return deposit_BANK(money,account, offer[bank]);

        }
    }

    private int deposit_BANK(String money, Account account, Offer offer)
    {
        account.setBalance(plus(this.account.getBalance(), money));
        String newLog = new Date() + " " + money + " 입금 \t( 잔액 : " + account.getBalance() + " )\n";
        newLog = newLog + this.account.getLog();
        account.setLog(newLog);
        return checkUpdateDB(offer);
    }

    private int deposit_CARD(String money, Account account, int bank)
    {
        // 입금하는 돈 > 빚이면 입금불가.
        if (isBig(money, this.account.getDept()))
        {
            return DEPOSIT_OVER_DEPT;
        } else
        {
            this.account.setDept(minus(this.account.getDept(), money));
            String newLog = new Date() + " " + money + " 상환 \t( 대출금 : " + this.account.getDept() + " )\n";
            newLog = newLog + this.account.getLog();
            this.account.setLog(newLog);
            return checkUpdateDB(offer[bank]);
        }
    }


    public int withdraw(String money)
    {   //출금한다.
        int bank;
        bank = findWhichBank();

        //account 확인.
        if(isWrongAccount(bank, account)) return WRONG_ACCOUNT;

        //잔액 충분한지 확인
            if(isUnderBalance(money)) return NOT_ENOUGH_BALANCE;
            else
            {   //돈 출금
                String newLog;
                if (!account.getBank().equals("신한은행"))
                {
                    this.takeCharge(account);
                }
                account.setBalance(minus(account.getBalance(), money));
                newLog = new Date() + " " + money + " 출금 \t( 잔액 : " + account.getBalance() + " )\n" + account.getLog();
                account.setLog(newLog);

                return checkUpdateDB(offer[bank]);
            }
        }

    public int transfer(String money, Account receiveAccount)
    {   //송금 시도하고 error code를 리턴.
        int bank;
        bank = findWhichBank();

        if (! offer[bank].checkValid(account)) return WRONG_ACCOUNT;
        //{
            if (isWrongAccount(bank, account)) return WRONG_ACCOUNT;

            //receiverAccount가 존재하는지 확인.
            int temp_bank = 0;
            boolean new_account_exist = false;
            while(temp_bank < 10)
            {
                if (receiveAccount.getBank().equals(list_bank[temp_bank]))
                {
                    new_account_exist = true;
                    break;
                }
                temp_bank ++;
            }
            if(!new_account_exist) return SERVER_NOT_RESPONSE;

            //------
            //송금인 잔액확인
            if(isUnderBalance(money)) return NOT_ENOUGH_BALANCE;

            else
            {// 여기 else삭제해도 됨. 송금인 잔액이 충분하면.
                if (receiveAccount.getBank().substring(2, 4).equals("카드"))
                {   //수신자가 카드면?
                    if(isWrongAccount(bank, receiveAccount)) return WRONG_ACCOUNT;
                    //if ((this.errorType = offer[bank].readDatabase(newAccount)) != 0)
                    //{
                    //    return 3;
                    //}
                    else //여기 else삭제해도 됨.
                    {
                        if (isBig(money, receiveAccount.getDept()))
                        {
                            return DEPOSIT_OVER_DEPT;
                        } else
                        {   // 여기 else삭제해도 됨.그냥 여기 Else 놔두자.
                            receiveAccount.setDept(minus(receiveAccount.getDept(), money));
                            String newLog = new Date() + " " + money + " 상환 \t( 대출금 : " + receiveAccount.getDept()
                                    + " )\n";
                            newLog = newLog + receiveAccount.getLog();
                            receiveAccount.setLog(newLog);

                            //checkUpdateDB

                            //update db 안되면 2리턴.
                            // return checkUpdateDB(bank,newAccount)
                            if (!offer[bank].updateDatabase(receiveAccount))
                            {
                                return 2;
                            }


                            offer[bank].readDatabase(account);
                            if (!account.getBank().equals("신한은행") || !receiveAccount.getBank().equals("신한은행"))
                                takeCharge(account);
                            account.setBalance(minus(account.getBalance(), money));
                            newLog = new Date() + " " + money + " 송금 " + receiveAccount.getBank() + " "
                                    + receiveAccount.getAccountNumber() + "\t( 잔액 : " + account.getBalance() + " )\n";
                            newLog = newLog + account.getLog();
                            account.setLog(newLog);
                            if (offer[bank].updateDatabase(account))
                            {
                                return 0;
                            } else
                            {
                                receiveAccount.setDept(plus(receiveAccount.getDept(), money));
                                newLog = new Date() + " " + money + " 기존거래 취소 \t( 대출금 : " + receiveAccount.getDept()
                                        + " )\n";
                                newLog = newLog + receiveAccount.getLog();
                                receiveAccount.setLog(newLog);
                                if (!offer[bank].updateDatabase(receiveAccount))
                                {
                                    return 2;
                                }
                                return 2;
                            }
                        }
                    }
                }
                receiveAccount.setBalance(plus(receiveAccount.getBalance(), money));
                String tempLog = new Date() + " " + money + " 입금 " + account.getBank() + " "
                        + account.getAccountNumber() + "\t( 잔액 : " + receiveAccount.getBalance() + " )\n";
                tempLog = tempLog + receiveAccount.getLog();
                receiveAccount.setLog(tempLog);
                if (!offer[temp_bank].updateDatabase(receiveAccount))
                {
                    return 2;
                }
                offer[bank].readDatabase(account);
                if (!account.getBank().equals("신한은행") || !receiveAccount.getBank().equals("신한은행"))
                    takeCharge(account);
                account.setBalance(minus(account.getBalance(), money));
                String newLog = new Date() + " " + money + " 송금 " + receiveAccount.getBank() + " "
                        + receiveAccount.getAccountNumber() + "\t( 잔액 : " + account.getBalance() + " )\n";
                newLog = newLog + account.getLog();
                account.setLog(newLog);
                if (offer[bank].updateDatabase(account))
                {
                    return 0;
                } else
                {
                    receiveAccount.setBalance(plus(receiveAccount.getBalance(), money));
                    tempLog = new Date() + " " + money + " 기존거래 취소 " + account.getBank() + " "
                            + account.getAccountNumber() + "\t( 잔액 : " + receiveAccount.getBalance() + " )\n";
                    tempLog = tempLog + receiveAccount.getLog();
                    receiveAccount.setLog(tempLog);
                    if (!offer[temp_bank].updateDatabase(receiveAccount))
                    {
                        return 2;
                    }
                    return 2;
                }
            }
       // } else
        //{
        //    return WRONG_ACCOUNT;
       // }
    }


    public int exchange(String money, String str)
    {
        int bank;
        bank = findWhichBank();
        int[] exchangeRate = {1100, 10, 1280, 200};
        String[] country = {"USD", "JPY", "EUR", "CNY"};
        int i;
        int won;
        for (i = 0; i < 4; i++)
        {
            if (str.equals(country[i]))
                break;
        }
        if (isBig(String.valueOf((won = Integer.parseInt(money) * exchangeRate[i])), this.account.getBalance()))
        {
            return 1;
        } else
        {
            account.setBalance(minus(account.getBalance(), String.valueOf(won)));
            String newLog = new Date() + " " + won + "원 을 " + money + str + "로 환전 " + "\t( 잔액 : " + account.getBalance()
                    + " )\n";
            newLog = newLog + account.getLog();
            account.setLog(newLog);
            return checkUpdateDB(offer[bank]);
        }
    }

    public int loan(String money)
    {
        int bank;
        bank = findWhichBank();
        if (isBig(plus(plus(account.getDept(), money), "1300"), account.getLimit()))
        {
            return 7;
        } else
        {
            offer[bank].readDatabase(account);
            takeCharge(account);
            account.setDept(plus(account.getDept(), money));
            String newLog = new Date() + " " + money + " 대출 " + "\t( 남은 한도 : "
                    + minus(account.getLimit(), account.getDept()) + " )\n";
            newLog = newLog + account.getLog();
            account.setLog(newLog);
            return checkUpdateDB(offer[bank]);
        }
    }

    public void takeCharge(Account account)
    {
        if (account.getRate() < 3)
            return;
        this.ch = 21;
        String newLog;
        if (account.getBank().substring(2, 4).equals("카드"))
        {
            account.setDept(plus(account.getDept(), "1300"));
            newLog = new Date() + "1300" + "수수료 \t( 한도 : " + minus(account.getLimit(), account.getDept()) + " )\n" + account.getLog();
        } else
        {
            account.setBalance(minus(account.getBalance(), "1300"));
            newLog = new Date() + " 1300" + " 수수료 \t( 잔액 : " + account.getBalance() + " )\n" + account.getLog();
        }
        account.setLog(newLog);
    }

    public int depositWithoutBank(String money)
    {
        return deposit(money);
    }

    public String checkBalance()
    {
        return this.account.getLog();
    }

    public int payUtilityBill(Account newAccount)
    {


        int bank = findWhichBank();

        if (offer[bank].checkValid(account))
        {
            String money = newAccount.getBalance();
            if ((this.errorType = offer[bank].readDatabase(account)) != 0)
            {
                return 2;
            }
            if ((account.getBank().equals("신한은행") && isBig(money, account.getBalance()))
                    || (!account.getBank().equals("신한은행") && isBig(plus(money, "1300"), account.getBalance())))
            {
                return 1;
            } else if (newAccount.getBalance().equals("0"))
            {
                return 9;
            } else
            {
                newAccount.setBalance(minus(newAccount.getBalance(), money));
                String tempLog = new Date() + " " + money + " 공과금납부 " + account.getBank() + " "
                        + account.getAccountNumber() + "\t( 잔액 : " + newAccount.getBalance() + " )\n";
                tempLog = tempLog + newAccount.getLog();
                newAccount.setLog(tempLog);
                if (!offer[6].updateDatabase(newAccount))
                {
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
                return checkUpdateDB(offer[bank]);
            }
        } else
        {
            return 3;
        }
    }

    public boolean isBig(String n1, String n2)
    {   //n1 > n2면 true 리턴.
        if (n1.length() > n2.length())
            return true;
        else if (n1.length() == n2.length())
        {
            for (int i = 0; i < n1.length(); i++)
            {
                if (n1.charAt(i) == n2.charAt(i))
                    continue;
                if (n1.charAt(i) < n2.charAt(i))
                    return false;
                return true;
            }
        }
        return false;
    }

    public String minus(String n1, String n2)
    {
        if (n1.equals(n2))
            return "0";
        String sum = n1.substring(0, n1.length() - n2.length());
        for (int i = 0; i < n2.length(); i++)
        {
            char a = n1.charAt(n1.length() - n2.length() + i);
            char b = n2.charAt(i);
            if (a == b)
            {
                sum = sum + '0';
            } else if (isBig(a + "", b + ""))
            {
                sum = sum + String.valueOf(Integer.parseInt(a + "") - Integer.parseInt(b + ""));
            } else
            {
                int j, c;
                for (j = 0; sum.charAt(sum.length() - 1 - j) == '0'; j++)
                    ;
                if (sum.length() - 1 - j == 0)
                {
                    if (sum.charAt(0) == '1')
                    {
                        sum = "";
                        for (int k = 0; k < j; k++)
                            sum = sum + '9';
                    } else
                    {
                        c = Integer.parseInt(sum.charAt(0) + "") - 1;
                        sum = String.valueOf(c);
                        for (int k = 0; k < j; k++)
                        {
                            sum = sum + '9';
                        }
                    }
                }
                c = Integer.parseInt("1" + a) - Integer.parseInt("" + b);
                sum = sum + String.valueOf(c);
            }
        }
        if (sum.length() > 0)
        {
            while (sum.substring(0, 1).equals("0"))
                sum = sum.substring(1, sum.length());
        }
        return sum;
    }

    public String plus(String n1, String n2)
    {
        String sum = "";
        boolean up = false;
        if (n1.length() > n2.length())
        {
            for (int i = 1; i <= n2.length(); i++)
            {
                char a = n1.charAt(n1.length() - i);
                char b = n2.charAt(n2.length() - i);
                if (!up)
                {
                    if (Integer.parseInt(a + "") + Integer.parseInt(b + "") > 9)
                    {
                        sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "")).charAt(1) + sum;
                        up = true;
                    } else
                    {
                        sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "")) + sum;
                        up = false;
                    }
                } else
                {
                    if (Integer.parseInt(a + "") + Integer.parseInt(b + "") > 8)
                    {
                        sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "") + 1).charAt(1) + sum;
                        up = true;
                    } else
                    {
                        sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "") + 1) + sum;
                        up = false;
                    }
                }
            }
            if (up)
            {
                for (int i = n1.length() - n2.length() - 1; i >= 0; i--)
                {
                    char a = n1.charAt(i);
                    if (a == '9')
                    {
                        sum = '0' + sum;
                    } else
                    {
                        sum = n1.substring(0, i + 1) + sum;
                        break;
                    }
                }
                if (sum.charAt(0) == '0')
                    sum = '1' + sum;
            } else
            {
                sum = n1.substring(0, n1.length() - n2.length()) + sum;
            }

        } else
        {
            for (int i = 1; i <= n1.length(); i++)
            {
                char a = n1.charAt(n1.length() - i);
                char b = n2.charAt(n2.length() - i);
                if (!up)
                {
                    if (Integer.parseInt(a + "") + Integer.parseInt(b + "") > 9)
                    {
                        sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "")).charAt(1) + sum;
                        up = true;
                    } else
                    {
                        sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "")) + sum;
                        up = false;
                    }
                } else
                {
                    if (Integer.parseInt(a + "") + Integer.parseInt(b + "") > 8)
                    {
                        sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "") + 1).charAt(1) + sum;
                        up = true;
                    } else
                    {
                        sum = String.valueOf(Integer.parseInt(a + "") + Integer.parseInt(b + "") + 1) + sum;
                        up = false;
                    }
                }
            }
            if (up)
            {
                if (n2.length() == n1.length())
                    sum = '1' + sum;
                else
                {
                    for (int i = n2.length() - n1.length() - 1; i >= 0; i--)
                    {
                        char a = n2.charAt(i);
                        if (a == '9')
                        {
                            sum = '0' + sum;
                        } else
                        {
                            sum = n2.substring(0, i + 1) + sum;
                            break;
                        }
                    }
                    if (sum.charAt(0) == '0')
                        sum = '1' + sum;
                }
            } else
            {
                sum = n2.substring(0, n2.length() - n1.length()) + sum;
            }
        }
        return sum;
    }
}
