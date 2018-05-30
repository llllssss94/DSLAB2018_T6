package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import ATM.MainSystem;

public class ReceiptFrame extends JFrame implements ActionListener, KeyListener {

	MainSystem main;
	GridBagLayout gbl;
	GridBagConstraints gbc;
	JButton ok;

	public ReceiptFrame(MainSystem main) {
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		this.main = main;
		ok = new JButton("확인");
		ok.addActionListener(this);
		ok.addKeyListener(this);
		JLabel cc = new JLabel("거래내역");
		cc.addKeyListener(this);
		add(cc, 0, 0, 5, 1);
		JLabel txtL = new JLabel(main.getAccount().getLog().substring(0, main.getAccount().getLog().indexOf('\n')));
		txtL.addKeyListener(this);
		txtL.setOpaque(true);
		txtL.setBackground(Color.LIGHT_GRAY);
		add(txtL, 0, 1, 5, 1);
		JLabel dd = new JLabel("이용해주셔서 감사합니다.");
		dd.addKeyListener(this);
		add(dd, 0, 2, 1, 1);
		add(ok, 0, 3, 1, 1);
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
