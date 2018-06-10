package ATM;

import java.util.Date;

import OFFER.Offer;

//TODO 1: �ߺ��Ǵ� �ڵ� ����
//TODO 2: üũī��0, ����1, �ſ�ī�� 2... �̷� magic number�� enum���� ����
//TODO 3: if-else ����
//TODO 4: if�� ���ǽ� ����. ex)if (isBig(String.valueOf((won = Integer.parseInt(money) * exchangeRate[i])), this.account.getBalance())) {...
//TODO 5: GUI��-  switch�� default ���� ����.
//TODO 6: �ּ��߰�.
//���������� �����ߴٰ� �� ������.

public class MainSystem
{
    private Account account;
    public String card;
    public Offer[] offer;
    public String bank;
    public String b5, b1;
    public int errorType, ch;

    //-----------------------------------------------------------------
    // String[] list_bank: �޼ҵ帶�� �� �ִ��� global�� ��.
    String[] list_bank = {"��������", "�������", "��������", "��������", "��Ƽ����", "�츮����", "�ѱ�����", "�Ｚī��", "����ī��", "�Ե�ī��"};

    // �ڵ帶�� return 2; return 3; return 1; �̷��� ���� ���˾Ƹ԰ھ �������� �ڵ��� ��Ȱ�뼺�� ���� ����� ���� ��.
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

    private int findWhichBank(Account account)
    {
        //����: �޼ҵ� �߰�.
        //� ����/ī������� ã�� ���������� int�� ����.
        int bank;
        for (bank = 0; bank < list_bank.length; bank++)
        {
            if (list_bank[bank].equals(account.getBank()))
                break;
        }
        return bank;
    }


    private int checkUpdateDB(Offer offer, Account account)
    {   //update Database �������� ����.
        if (offer.updateDatabase(account)) return NO_ERROR;
        else return SERVER_NOT_RESPONSE;
    }

    // üũī�� 0, ���� 1, �ſ�ī�� 2, ���ο���3 // ���� // ��������
    public void insert(String str)
    {
        //���¹�ȣ�� insert����. GUI���� ���.
        this.card = str;
        this.account.setBank(list_bank[Integer.parseInt(card.charAt(0) + "")]);
        this.account.setAccountNumber(card.substring(1));
    }

    public boolean isUnderBalance(String money)
    { //�ܾ��� ������� Ȯ���ϴ� �޼ҵ�

        //��������
        if ((account.getBank().equals("��������") && isBig(money, account.getBalance()))) return true;
        if (!account.getBank().equals("��������") && isBig(plus(money, "1300"), account.getBalance())) return true;
        return false;
    }

    public boolean isWrongAccount(int bankIndex, Account account)
    {   //�߸��� ����: true ����.
        this.errorType = offer[bankIndex].readDatabase(account);
        boolean readWrongDB = (this.errorType != 0);
        return readWrongDB;
    }





    public int lockAccount()
    {   //account�� lock�ϴ� �޼ҵ�.

        //��� �������� ã�´�.
        int bank;
        bank = findWhichBank(account);

        //���°� ���������� �������� Ȯ��.
        if (isWrongAccount(bank,account)) return WRONG_ACCOUNT;

        //lock�� �Ǵ�.
        account.setIsLocked(true);

        //DB�� update
        return checkUpdateDB(offer[bank], account);

    }


    public int deposit(String money)
    {   //�Ա� ���. return�ϴ� �������� GUI���� frame���� �� ���δ�.

        int bank;
        bank = findWhichBank(account);
        if (isWrongAccount(bank,account)) return WRONG_ACCOUNT;

        //�Է��� ���°� ī���ȣ�ΰ�� �� ��ȯ.
        if (this.account.getBank().substring(2, 4).equals("ī��"))
        {
            return deposit_CARD(money, this.account, bank);
        } else
        { //�Է��� ���°� ���� ������ ���.
            return deposit_BANK(money, this.account, bank);
        }
    }

    private int deposit_BANK(String money, Account account, int bank)
    {   //�������� �Ա��ϱ�. �Ա�+�α�����+checkUpdateDB����.

        account.setBalance(plus(account.getBalance(), money));
        String newLog = new Date() + " " + money + " �Ա� \t( �ܾ� : " + account.getBalance() + " )\n";
        newLog = newLog + account.getLog();
        account.setLog(newLog);
        return checkUpdateDB(offer[bank],account );
    }

    private int deposit_CARD(String money, Account account, int bank)
    {   //ī��� �Ա��ϱ�. �Ա�+�α�����+checkUpdateDB����.

        // �Ա��ϴ� �� > ���̸� �ԱݺҰ�.
        if (isBig(money, account.getDept()))
        {
            return DEPOSIT_OVER_DEPT;
        } else
        {
            account.setDept(minus(account.getDept(), money));
            String newLog = new Date() + " " + money + " ��ȯ \t( ����� : " + account.getDept() + " )\n";
            newLog = newLog + account.getLog();
            account.setLog(newLog);
            return checkUpdateDB(offer[bank],account );
        }
    }


    public int withdraw(String money)
    {   //����Ѵ�.

        //�������� bank. ��� �������� �˱� ����.
        int bank;
        bank = findWhichBank(account);

        //account Ȯ��.
        if(isWrongAccount(bank, account)) return WRONG_ACCOUNT;

        //�ܾ� ������� Ȯ��
            if(isUnderBalance(money)) return NOT_ENOUGH_BALANCE;
            else
            {
                return withdraw_Money(money, offer[bank], account, "���");
            }
    }

    private int withdraw_Money(String money, Offer offer, Account account, String withdrawType)
    {
        //�� ��ݱ��(��������� ���o) + log������Ʈ +  checkUpdateDB

        String newLog;
        if (!account.getBank().equals("��������"))
        {
            this.takeCharge(account);
        }
        account.setBalance(minus(account.getBalance(), money));
        newLog = new Date() + " " + money + withdrawType+ "\t( �ܾ� : " + account.getBalance() + " )\n" + account.getLog();
        account.setLog(newLog);

        return checkUpdateDB(offer,account );
    }

    private int withdraw_Money(String money, Offer offer, Account account, String withdrawType, Account receiverAccount)
    {  //Overloading
        //�� ��ݱ��(��������� ���o) + log������Ʈ +  checkUpdateDB + ��������������.

        String newLog;
        if (!this.account.getBank().equals("��������"))
        {
            this.takeCharge(this.account);
        }
        this.account.setBalance(minus(this.account.getBalance(), money));
        newLog = new Date() + " " + money + withdrawType+ receiverAccount.getBank() + " " + receiverAccount.getAccountNumber() + "\t( �ܾ� : " + this.account.getBalance() + " )\n" + this.account.getLog();
        this.account.setLog(newLog);

        return checkUpdateDB(offer, account);
    }


    public int transfer(String money, Account receiveAccount)
    {   //�۱� �õ��ϰ� error code�� ����.

        int bank;
        bank = findWhichBank(this.account);

        //�۱��Ϸ��� account�� valid���� ������ ����.
        if (!offer[bank].checkValid(account)) return WRONG_ACCOUNT;
        if (isWrongAccount(bank, account)) return WRONG_ACCOUNT;
        offer[bank].readDatabase(account);


        //receiver�� bank ������ valid���� ������ ����.
        int receiver_bank;
        receiver_bank = findWhichBank(receiveAccount);
        if(isWrongAccount(receiver_bank, receiveAccount)) return WRONG_ACCOUNT;
        offer[receiver_bank].readDatabase(receiveAccount);


        //�۱ݰ����� �ܾ� Ȯ��
        if(isUnderBalance(money)) return NOT_ENOUGH_BALANCE;

        int receiverUpdateCheck;
        int senderUpdateCheck;

        if (receiveAccount.getBank().substring(2, 4).equals("ī��"))
        {   //�����ڰ� ī���� ���: ������ �� ��ȯ
            receiverUpdateCheck = deposit_CARD(money, receiveAccount, receiver_bank);
            //�۱��� �ܾ� ����
            senderUpdateCheck = withdraw_Money(money,offer[bank],account,"�۱� ", receiveAccount);

            if(receiverUpdateCheck ==2 || senderUpdateCheck ==2)
            {//updateDB�� ������ �� ��� �ŷ� ���.

                //������ �ŷ� ���
                receiveAccount.setDept(plus(receiveAccount.getDept(), money));
                String newLog = new Date() + " " + money + " �����ŷ� ��� \t( ����� : " + receiveAccount.getDept()
                        + " )\n";
                newLog = newLog + receiveAccount.getLog();
                receiveAccount.setLog(newLog);

                //�۽��� �ŷ� ���
                account.setBalance(plus(account.getBalance(), money));
                String tempLog = new Date() + " " + money + " �����ŷ� ��� " + account.getBank() + " "
                        + account.getAccountNumber() + "\t( �ܾ� : " + account.getBalance() + " )\n";
                tempLog = tempLog + account.getLog();
                account.setLog(tempLog);
                return SERVER_NOT_RESPONSE;
            }
            return NO_ERROR;
        }
        else
        {   //�����ڰ� �Ϲ� ���� ������ ���: �Ա�.
            receiverUpdateCheck = deposit_BANK(money, receiveAccount, receiver_bank);

            //�۽��ڿ��� ����.
            senderUpdateCheck = withdraw_Money(money,offer[bank],account,"�۱� ", receiveAccount);


            if (receiverUpdateCheck == 2 || senderUpdateCheck == 2)
            {
                //updateDB�� ������ �� ��� �ŷ� ���.

                //������ �ŷ� ���
                withdraw_Money(money, offer[receiver_bank], receiveAccount, "���� �ŷ� ���");
                //�۽��� �ŷ� ���
                withdraw_Money(money, offer[bank], account, "���� �ŷ� ���");

                return SERVER_NOT_RESPONSE;
            }
            return NO_ERROR;
        }
    }



        public int exchange(String money, String str)
    {
        int bank;
        bank = findWhichBank(account);
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
            String newLog = new Date() + " " + won + "�� �� " + money + str + "�� ȯ�� " + "\t( �ܾ� : " + account.getBalance()
                    + " )\n";
            newLog = newLog + account.getLog();
            account.setLog(newLog);
            return checkUpdateDB(offer[bank],account );
        }
    }

    public int loan(String money)
    {
        int bank;
        bank = findWhichBank(account);
        if (isBig(plus(plus(account.getDept(), money), "1300"), account.getLimit()))
        {
            return 7;
        } else
        {
            offer[bank].readDatabase(account);
            takeCharge(account);
            account.setDept(plus(account.getDept(), money));
            String newLog = new Date() + " " + money + " ���� " + "\t( ���� �ѵ� : "
                    + minus(account.getLimit(), account.getDept()) + " )\n";
            newLog = newLog + account.getLog();
            account.setLog(newLog);
            return checkUpdateDB(offer[bank],account );
        }
    }

    public void takeCharge(Account account)
    {
        if (account.getRate() < 3)
            return;
        this.ch = 21;
        String newLog;
        if (account.getBank().substring(2, 4).equals("ī��"))
        {
            account.setDept(plus(account.getDept(), "1300"));
            newLog = new Date() + "1300" + "������ \t( �ѵ� : " + minus(account.getLimit(), account.getDept()) + " )\n" + account.getLog();
        } else
        {
            account.setBalance(minus(account.getBalance(), "1300"));
            newLog = new Date() + " 1300" + " ������ \t( �ܾ� : " + account.getBalance() + " )\n" + account.getLog();
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


        int bank = findWhichBank(account);

        if (offer[bank].checkValid(account))
        {
            String money = newAccount.getBalance();
            if ((this.errorType = offer[bank].readDatabase(account)) != 0)
            {
                return 2;
            }
            if ((account.getBank().equals("��������") && isBig(money, account.getBalance()))
                    || (!account.getBank().equals("��������") && isBig(plus(money, "1300"), account.getBalance())))
            {
                return 1;
            } else if (newAccount.getBalance().equals("0"))
            {
                return 9;
            } else
            {
                newAccount.setBalance(minus(newAccount.getBalance(), money));
                String tempLog = new Date() + " " + money + " �����ݳ��� " + account.getBank() + " "
                        + account.getAccountNumber() + "\t( �ܾ� : " + newAccount.getBalance() + " )\n";
                tempLog = tempLog + newAccount.getLog();
                newAccount.setLog(tempLog);
                if (!offer[6].updateDatabase(newAccount))
                {
                    return 2;
                }
                if (!account.getBank().equals("��������"))
                    this.takeCharge(account);
                offer[bank].readDatabase(account);
                account.setBalance(minus(account.getBalance(), money));
                String newLog = new Date() + " " + money + " �����ݳ��� " + newAccount.getBank() + " "
                        + newAccount.getAccountNumber() + "\t( �ܾ� : " + account.getBalance() + " )\n";
                newLog = newLog + account.getLog();
                account.setLog(newLog);
                return checkUpdateDB(offer[bank],account );
            }
        } else
        {
            return 3;
        }
    }

    public boolean isBig(String n1, String n2)
    {   //n1 > n2�� true ����.
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
