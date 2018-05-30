package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class errorPrintFrame extends JFrame implements ActionListener, KeyListener {
	GridBagLayout gbl;
	GridBagConstraints gbc;
	JButton ok;
	String[] error = { "잔액이 부족합니다.", "서버가 불안정합니다. 같은 에러 발생시 서비스 센터에 전화해주십시오.(1588-1588)", "계좌정보가 잘못되었습니다.",
			"비밀번호를 3회 이상 틀려 거래가 불가합니다.", "거래불가 계좌입니다.", "상환금이 대출금보다 많을 수 없습니다.", "한도초과입니다.",
			"입금계좌와 송금계좌가 동일하여 거래가 종료됩니다.", "공과금이 이미 납부되었습니다." };

	public errorPrintFrame(int errorcode) {
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		JLabel dd = new JLabel("Error : " + error[errorcode - 1]);
		dd.addKeyListener(this);
		add(dd, 0, 0, 5, 4);
		ok = new JButton("확인");
		ok.addKeyListener(this);
		ok.addActionListener(this);
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		new mainFrame();
		this.dispose();
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
		new mainFrame();
		this.dispose();
	}
}
