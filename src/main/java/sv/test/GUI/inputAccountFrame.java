package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ATM.Account;
import ATM.MainSystem;

public class inputAccountFrame extends insertFrame{
	public MainSystem main, temp;

	public inputAccountFrame(int menu) {
		super(menu);
		ok.removeActionListener(this);
		ok.addActionListener(new myActionListener());
		if(menu == 0) {
			combo.setSelectedIndex(3);
			combo.setEnabled(false);
		}
		// TODO Auto-generated constructor stub
	}
	
	public inputAccountFrame(int menu ,MainSystem main) {
		super(menu);
		this.main = main;
		temp = new MainSystem();
		ok.removeActionListener(this);
		ok.addActionListener(new myActionListener());		
	}
	
	class myActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			temp.insert(String.valueOf(combo.getSelectedIndex()) + tf.getText());
			if(temp.offer[combo.getSelectedIndex()].checkValid(temp.getAccount())) {
				temp.offer[combo.getSelectedIndex()].readDatabase(temp.getAccount());
				if(menu == 0) {	// 무통장
					new inputMoneyFrame(1, main);
					dispose();
				} else if (menu == 1) {	// 이체
					new inputPasswordFrame(4, main, temp.getAccount());
					dispose();
				} else if (menu == 2) { // 지로
					new insertFrame(6, main);
					dispose();
				}
			}
		}
		
	}
}
