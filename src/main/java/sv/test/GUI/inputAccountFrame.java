package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ATM.Account;
import ATM.MainSystem;

public class inputAccountFrame extends insertFrame {
	public MainSystem main, temp;

	public inputAccountFrame(int menu) {
		super(menu);
		myKeyListener key = new myKeyListener();

		tf.removeKeyListener(this);
		tf.addKeyListener(key);
		ok.removeKeyListener(this);
		ok.addKeyListener(key);
		bb.removeKeyListener(this);
		bb.addKeyListener(key);
		reset.removeKeyListener(this);
		reset.addKeyListener(key);
		b0.removeKeyListener(this);
		b0.addKeyListener(key);
		b1.removeKeyListener(this);
		b1.addKeyListener(key);
		b2.removeKeyListener(this);
		b2.addKeyListener(key);
		b3.removeKeyListener(this);
		b3.addKeyListener(key);
		b4.removeKeyListener(this);
		b4.addKeyListener(key);
		b5.removeKeyListener(this);
		b5.addKeyListener(key);
		b6.removeKeyListener(this);
		b6.addKeyListener(key);
		b7.removeKeyListener(this);
		b7.addKeyListener(key);
		b8.removeKeyListener(this);
		b8.addKeyListener(key);
		b9.removeKeyListener(this);
		b9.addKeyListener(key);
		state.removeKeyListener(this);
		state.addKeyListener(key);
		combo.removeKeyListener(this);
		combo.addKeyListener(key);

		temp = new MainSystem();
		ok.removeActionListener(this);
		ok.addActionListener(new myActionListener());
		if (menu == 1) {
			state.setText("송금하실 계좌를 입력하십시오");
		}
		if (menu == 0) {
			combo.setSelectedIndex(3);
			combo.setEnabled(false);
			state.setText("송금하실 계좌를 입력하십시오");
		}
		if (menu == 2) {
			combo.setSelectedIndex(6);
			combo.setEnabled(false);
		}
		// TODO Auto-generated constructor stub
	}

	public inputAccountFrame(int menu, MainSystem main) {
		super(menu);
		this.main = main;
		temp = new MainSystem();
		ok.removeActionListener(this);
		ok.addActionListener(new myActionListener());
		myKeyListener key = new myKeyListener();
		tf.removeKeyListener(this);
		tf.addKeyListener(key);
		ok.removeKeyListener(this);
		ok.addKeyListener(key);
		bb.removeKeyListener(this);
		bb.addKeyListener(key);
		reset.removeKeyListener(this);
		reset.addKeyListener(key);
		b0.removeKeyListener(this);
		b0.addKeyListener(key);
		b1.removeKeyListener(this);
		b1.addKeyListener(key);
		b2.removeKeyListener(this);
		b2.addKeyListener(key);
		b3.removeKeyListener(this);
		b3.addKeyListener(key);
		b4.removeKeyListener(this);
		b4.addKeyListener(key);
		b5.removeKeyListener(this);
		b5.addKeyListener(key);
		b6.removeKeyListener(this);
		b6.addKeyListener(key);
		b7.removeKeyListener(this);
		b7.addKeyListener(key);
		b8.removeKeyListener(this);
		b8.addKeyListener(key);
		b9.removeKeyListener(this);
		b9.addKeyListener(key);
		state.removeKeyListener(this);
		state.addKeyListener(key);
		combo.removeKeyListener(this);
		combo.addKeyListener(key);
		if (menu == 1) {
			state.setText("송금하실 계좌를 입력하십시오");
		}
		if (menu == 0) {
			combo.setSelectedIndex(3);
			combo.setEnabled(false);
			state.setText("송금하실 계좌를 입력하십시오");
		}
		if (menu == 2) {
			combo.setSelectedIndex(6);
			combo.setEnabled(false);
		}
	}

	class myActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			temp.insert(String.valueOf(combo.getSelectedIndex()) + tf.getText());
			if (temp.offer[combo.getSelectedIndex()].checkValid(temp.getAccount())) {
				int error;
				if ((error = temp.offer[combo.getSelectedIndex()].readDatabase(temp.getAccount())) != 0) {
					new errorPrintFrame(error);
					dispose();
				} else {
					if (menu == 0) { // 무통장
						if (combo.getSelectedIndex() == 6) {
							state.setText("한국은행계좌로 거래를 진행하실 수 없습니다.");
							state.setOpaque(true);
							state.setBackground(Color.LIGHT_GRAY);
						} else {
							new inputMoneyFrame(1, temp);
							dispose();
						}
					} else if (menu == 1) { // 이체
						if (combo.getSelectedIndex() == 6) {
							state.setText("한국은행계좌로 거래를 진행하실 수 없습니다.");
							state.setOpaque(true);
							state.setBackground(Color.LIGHT_GRAY);
						} else {
							if (main.getAccount().getAccountNumber().equals(temp.getAccount().getAccountNumber())
									&& main.getAccount().getBank().equals(temp.getAccount().getBank())) {
								new errorPrintFrame(8);
								dispose();
							} else {
								new inputMoneyFrame(2, main, temp.getAccount());
								dispose();
							}
						}
					} else if (menu == 2) { // 지로
						new insertFrame(6, temp);
						dispose();
					}
				}
			} else {
				new errorPrintFrame(3);
				dispose();
			}
		}
	}

	class myKeyListener implements KeyListener {

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
			case '0':
				tf.setText(tf.getText() + "0");
				break;
			case '1':
				tf.setText(tf.getText() + "1");
				break;
			case '2':
				tf.setText(tf.getText() + "2");
				break;
			case '3':
				tf.setText(tf.getText() + "3");
				break;
			case '4':
				tf.setText(tf.getText() + "4");
				break;
			case '5':
				tf.setText(tf.getText() + "5");
				break;
			case '6':
				tf.setText(tf.getText() + "6");
				break;
			case '7':
				tf.setText(tf.getText() + "7");
				break;
			case '8':
				tf.setText(tf.getText() + "8");
				break;
			case '9':
				tf.setText(tf.getText() + "9");
				break;
			case 'r':
				tf.setText("");
				break;
			case '\b':
				if (tf.getText().length() > 0)
					tf.setText(tf.getText().substring(0, tf.getText().length() - 1));
				break;
			case '\n':
				temp.insert(String.valueOf(combo.getSelectedIndex()) + tf.getText());
				if (temp.offer[combo.getSelectedIndex()].checkValid(temp.getAccount())) {
					int error;
					if ((error = temp.offer[combo.getSelectedIndex()].readDatabase(temp.getAccount())) != 0) {
						new errorPrintFrame(error);
						dispose();
					} else {
						if (menu == 0) { // 무통장
							new inputMoneyFrame(1, temp);
							dispose();
						} else if (menu == 1) { // 이체
							if (main.getAccount().getAccountNumber().equals(temp.getAccount().getAccountNumber())
									&& main.getAccount().getBank().equals(temp.getAccount().getBank())) {
								new errorPrintFrame(8);
								dispose();
							} else {
								new inputMoneyFrame(2, main, temp.getAccount());
								dispose();
							}
						} else if (menu == 2) { // 지로
							new insertFrame(6, temp);
							dispose();
						}
					}
				} else {
					new errorPrintFrame(3);
					dispose();
				}
				break;
			}

		}

	}
}
