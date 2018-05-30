package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import ATM.Account;
import ATM.MainSystem;

public class inputPasswordFrame extends JFrame implements ActionListener, KeyListener {

	int count, menu, wrongtime;
	String password;
	JLabel[] l;
	JLabel state;
	JButton[] b;
	JButton bb, reset;
	MainSystem main;
	Account account;
	GridBagLayout gbl;
	GridBagConstraints gbc;

	public inputPasswordFrame(int menu, MainSystem main) {
		super("password");
		this.menu = menu;
		this.main = main;
		this.count = 0;
		this.password = null;
		this.wrongtime = 0;
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		l = new JLabel[4];
		for (int i = 0; i < 4; i++) {
			l[i] = new JLabel("__");
			l[i].addKeyListener(this);
			add(l[i], i, 1, 1, 1);
		}
		state = new JLabel("Input Password");
		add(state, 0, 0, 3, 1);
		b = new JButton[10];
		for (int i = 0; i < 10; i++) {
			b[i] = new JButton(String.valueOf(i));
			b[i].addActionListener(this);
			b[i].addKeyListener(this);
		}
		bb = new JButton("<-");
		bb.addActionListener(this);
		reset = new JButton("Reset");
		reset.addActionListener(this);

		state.addKeyListener(this);
		bb.addKeyListener(this);
		reset.addKeyListener(this);

		add(b[1], 0, 3, 1, 1);
		add(b[2], 1, 3, 1, 1);
		add(b[3], 2, 3, 1, 1);
		add(b[4], 0, 4, 1, 1);
		add(b[5], 1, 4, 1, 1);
		add(b[6], 2, 4, 1, 1);
		add(b[7], 0, 5, 1, 1);
		add(b[8], 1, 5, 1, 1);
		add(b[9], 2, 5, 1, 1);
		add(b[0], 1, 6, 1, 1);
		add(bb, 0, 6, 1, 1);
		add(reset, 2, 6, 1, 1);
		this.setSize(500, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public inputPasswordFrame(int menu, MainSystem main, Account account) {
		super("password");
		this.menu = menu;
		this.main = main;
		this.count = 0;
		this.password = null;
		this.wrongtime = 0;
		this.account = account;
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		l = new JLabel[4];
		for (int i = 0; i < 4; i++) {
			l[i] = new JLabel("__");
			l[i].addKeyListener(this);
			add(l[i], i, 1, 1, 1);
		}
		state = new JLabel("Input Password");
		add(state, 0, 0, 3, 1);
		b = new JButton[10];
		for (int i = 0; i < 10; i++) {
			b[i] = new JButton(String.valueOf(i));
			b[i].addActionListener(this);
			b[i].addKeyListener(this);
		}
		bb = new JButton("<-");
		bb.addActionListener(this);
		reset = new JButton("Reset");
		reset.addActionListener(this);
		state.addKeyListener(this);
		bb.addKeyListener(this);
		reset.addKeyListener(this);
		add(b[1], 0, 3, 1, 1);
		add(b[2], 1, 3, 1, 1);
		add(b[3], 2, 3, 1, 1);
		add(b[4], 0, 4, 1, 1);
		add(b[5], 1, 4, 1, 1);
		add(b[6], 2, 4, 1, 1);
		add(b[7], 0, 5, 1, 1);
		add(b[8], 1, 5, 1, 1);
		add(b[9], 2, 5, 1, 1);
		add(b[0], 1, 6, 1, 1);
		add(bb, 0, 6, 1, 1);
		add(reset, 2, 6, 1, 1);
		this.setSize(500, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == reset) {
			for (int i = 0; i < 4; i++) {
				l[i].setText("__");
			}
			count = 0;
			password = null;
		} else if (e.getSource() == bb && count > 0) {
			password = password.substring(0, password.length() - 1);
			l[--count].setText("__");
		} else if (e.getSource() == b[0]) {
			if (count == 0) {
				password = "0";
			} else {
				password = password + "0";
			}
			l[count++].setText("*");
			checkPassword();
		} else if (e.getSource() == b[1]) {
			if (count == 0) {
				password = "1";
			} else {
				password = password + "1";
			}
			l[count++].setText("*");
			checkPassword();
		} else if (e.getSource() == b[2]) {
			if (count == 0) {
				password = "2";
			} else {
				password = password + "2";
			}
			l[count++].setText("*");
			checkPassword();
		} else if (e.getSource() == b[3]) {
			if (count == 0) {
				password = "3";
			} else {
				password = password + "3";
			}
			l[count++].setText("*");
			checkPassword();
		} else if (e.getSource() == b[4]) {
			if (count == 0) {
				password = "4";
			} else {
				password = password + "4";
			}
			l[count++].setText("*");
			checkPassword();
		} else if (e.getSource() == b[5]) {
			if (count == 0) {
				password = "5";
			} else {
				password = password + "5";
			}
			l[count++].setText("*");
			checkPassword();
		} else if (e.getSource() == b[6]) {
			if (count == 0) {
				password = "6";
			} else {
				password = password + "6";
			}
			l[count++].setText("*");
			checkPassword();
		} else if (e.getSource() == b[7]) {
			if (count == 0) {
				password = "7";
			} else {
				password = password + "7";
			}
			l[count++].setText("*");
			checkPassword();
		} else if (e.getSource() == b[8]) {
			if (count == 0) {
				password = "8";
			} else {
				password = password + "8";
			}
			l[count++].setText("*");
			checkPassword();
		} else if (e.getSource() == b[9]) {
			if (count == 0) {
				password = "9";
			} else {
				password = password + "9";
			}
			l[count++].setText("*");
			checkPassword();
		}
	}

	public void selectmenu() {
		switch (menu) {
		case 0: // withdraw
			new inputMoneyFrame(3, main);
			this.dispose();
			break;
		case 1: // exchange
			new selectCountry(main);
			this.dispose();
			break;
		case 2: // loan
			new inputMoneyFrame(4, main);
			this.dispose();
			break;
		case 3: // checkbalance
			new printLogFrame(main);
			this.dispose();
			break;
		case 4: // transfer
			new inputMoneyFrame(2, main, account);
			this.dispose();
			break;
		case 5: // payub
			int error;
			if ((error = main.payUtilityBill(account)) == 0) {
				new ReceiptFrame(main);
				this.dispose();
			} else {
				new errorPrintFrame(error);
				this.dispose();
			}
			break;

		}
	}

	public void checkPassword() {
		if (count == 4) {
			if (main.getAccount().checkPassword(Integer.parseInt(password))) {
				selectmenu();
				return;
			} else {
				wrongtime++;
				state.setText("비밀번호 " + wrongtime + "회 틀렸습니다. (3회이상 오류시 거래정지)");
				state.setOpaque(true);
				state.setBackground(Color.LIGHT_GRAY);
				for (int i = 0; i < 4; i++) {
					l[i].setText("__");
				}
				count = 0;
				password = null;
			}
			if (wrongtime > 2) {
				main.lockAccount();
				new errorPrintFrame(4);
				this.dispose();
			}
		}
	}

	private void add(Component c, int x, int y, int w, int h) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbl.setConstraints(c, gbc);
		add(c);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		char key = e.getKeyChar();
		switch (key) {
		case '0':
			if (count == 0) {
				password = "0";
			} else {
				password = password + "0";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '1':
			if (count == 0) {
				password = "1";
			} else {
				password = password + "1";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '2':
			if (count == 0) {
				password = "2";
			} else {
				password = password + "2";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '3':
			if (count == 0) {
				password = "3";
			} else {
				password = password + "3";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '4':
			if (count == 0) {
				password = "4";
			} else {
				password = password + "4";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '5':
			if (count == 0) {
				password = "5";
			} else {
				password = password + "5";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '6':
			if (count == 0) {
				password = "6";
			} else {
				password = password + "6";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '7':
			if (count == 0) {
				password = "7";
			} else {
				password = password + "7";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '8':
			if (count == 0) {
				password = "8";
			} else {
				password = password + "8";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '9':
			if (count == 0) {
				password = "9";
			} else {
				password = password + "9";
			}
			l[count++].setText("*");
			checkPassword();
			break;
		case '\b':
			if (count > 0) {
				password = password.substring(0, password.length() - 1);
				l[--count].setText("__");
			}
			break;
		case 'r':
			for (int i = 0; i < 4; i++) {
				l[i].setText("__");
			}
			count = 0;
			password = null;
			break;
		}
	}
}
