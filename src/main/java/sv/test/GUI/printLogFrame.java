package GUI;

import java.awt.*;
import javax.swing.*;

import ATM.MainSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class printLogFrame extends JFrame implements ActionListener{
	JTextArea txtLog;
	JButton ok;
	JScrollPane scrollPane;
	GridBagLayout gbl;
	GridBagConstraints gbc;
	
	public printLogFrame(MainSystem main) {
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		txtLog = new JTextArea();
		scrollPane = new JScrollPane(txtLog);
		add(scrollPane, 0, 0 , 5, 5);
		txtLog.setText(main.checkBalance());
		
		ok = new JButton("»Æ¿Œ");
		ok.addActionListener(this);
		add(ok, 2, 6, 1, 1);
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
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		new mainFrame();
		this.dispose();
	}

}
