package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import ATM.MainSystem;

public class selectMoneyFrame extends JFrame implements ActionListener, KeyListener {
	MainSystem main;
	int menu;
	JLabel l1, l5, money;
	JButton up1, up2, down1, down2, ok;
	GridBagLayout gbl;
	GridBagConstraints gbc;

	public selectMoneyFrame(int menu, MainSystem main) {
		super("select");
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		this.menu = menu;
		this.main = main;
		l1 = new JLabel(String.valueOf(Integer.parseInt(main.bank) / 10000));
		l5 = new JLabel("0");
		money = new JLabel("금액 : " + main.bank);
		money.setOpaque(true);
		money.setBackground(Color.LIGHT_GRAY);
		up1 = new JButton("up");
		up1.addActionListener(this);
		up1.addKeyListener(this);
		up2 = new JButton("up");
		up2.addActionListener(this);
		up2.addKeyListener(this);
		down1 = new JButton("down");
		down1.addActionListener(this);
		down1.addKeyListener(this);
		down2 = new JButton("down");
		down2.addActionListener(this);
		down2.addKeyListener(this);
		ok = new JButton("확인");
		ok.addActionListener(this);
		ok.addKeyListener(this);
		JLabel aa = new JLabel("10000원권 :");
		aa.setOpaque(true);
		aa.setBackground(Color.LIGHT_GRAY);
		add(aa, 0, 0, 2, 2);
		add(l1, 2, 0, 2, 2);
		JLabel bb = new JLabel("50000원권 :");
		bb.setOpaque(true);
		bb.setBackground(Color.LIGHT_GRAY);
		add(bb, 0, 2, 2, 2);
		add(l5, 2, 2, 2, 2);
		add(up1, 4, 0, 1, 1);
		add(down1, 4, 1, 1, 1);
		add(up2, 4, 2, 1, 1);
		add(down2, 4, 3, 1, 1);
		add(money, 3, 5, 2, 1);
		add(ok, 0, 5, 2, 1);
		this.setSize(500, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		char key = e.getKeyChar();
		switch (key) {
		case '1':
			if (Integer.parseInt(l5.getText()) > 0) {
				l1.setText(String.valueOf(Integer.parseInt(l1.getText()) + 5));
				l5.setText(String.valueOf(Integer.parseInt(l5.getText()) - 1));
			}
			break;
		case '2':
			if (Integer.parseInt(l1.getText()) > 4) {
				l1.setText(String.valueOf(Integer.parseInt(l1.getText()) - 5));
				l5.setText(String.valueOf(Integer.parseInt(l5.getText()) + 1));
			}
			break;
		case '5':
			if (Integer.parseInt(l1.getText()) > 4) {
				l5.setText(String.valueOf(Integer.parseInt(l5.getText()) + 1));
				l1.setText(String.valueOf(Integer.parseInt(l1.getText()) - 5));
			}
			break;
		case '6':
			if (Integer.parseInt(l5.getText()) > 0) {
				l1.setText(String.valueOf(Integer.parseInt(l1.getText()) + 5));
				l5.setText(String.valueOf(Integer.parseInt(l5.getText()) - 1));
			}
			break;
		case '\n':
			main.b1 = l1.getText();
			main.b5 = l5.getText();			
			int error;
			switch(menu) {
			case 0:
				if ((error = main.withdraw(main.bank)) == 0) {
					main.errorType = 20;
					new ReceiptFrame(main);
				} else {
					new errorPrintFrame(error);
				}
				this.dispose();
				break;
			case 1:
				if ((error = main.loan(main.bank)) == 0) {
					main.errorType = 20;
					new ReceiptFrame(main);
				} else {
					new errorPrintFrame(error);
				}
				this.dispose();
				break;
			}
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == up1 && Integer.parseInt(l5.getText()) > 0) {
			l1.setText(String.valueOf(Integer.parseInt(l1.getText()) + 5));
			l5.setText(String.valueOf(Integer.parseInt(l5.getText()) - 1));
		} else if (e.getSource() == up2 && Integer.parseInt(l1.getText()) > 4) {
			l5.setText(String.valueOf(Integer.parseInt(l5.getText()) + 1));
			l1.setText(String.valueOf(Integer.parseInt(l1.getText()) - 5));
		} else if (e.getSource() == down1 && Integer.parseInt(l1.getText()) > 4) {
			l1.setText(String.valueOf(Integer.parseInt(l1.getText()) - 5));
			l5.setText(String.valueOf(Integer.parseInt(l5.getText()) + 1));
		} else if (e.getSource() == down2 && Integer.parseInt(l5.getText()) > 0) {
			l1.setText(String.valueOf(Integer.parseInt(l1.getText()) + 5));
			l5.setText(String.valueOf(Integer.parseInt(l5.getText()) - 1));
		} else if (e.getSource() == ok) {
			int error;
			main.b1 = l1.getText();
			main.b5 = l5.getText();
			switch(menu) {
			case 0:
				
				if ((error = main.withdraw(main.bank)) == 0) {
					main.errorType = 20;
					new ReceiptFrame(main);
				} else {
					new errorPrintFrame(error);
				}
				this.dispose();
				break;
			case 1:
				if ((error = main.loan(main.bank)) == 0) {
					main.errorType = 20;
					new ReceiptFrame(main);
				} else {
					new errorPrintFrame(error);
				}
				this.dispose();
				break;
			}
		}
	}
}
