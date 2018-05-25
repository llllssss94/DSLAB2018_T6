package GUI;

import ATM.MainSystem;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class selectCountry extends JFrame implements ActionListener{
	MainSystem main;
	JButton[] country;
	GridBagLayout gbl;
	GridBagConstraints gbc;
	
	public selectCountry(MainSystem main) {
		super("selectCountry");			
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		this.main = main;
		country = new JButton[4];
		country[0] = new JButton("USD");
		country[1] = new JButton("JPY");
		country[2] = new JButton("EUR");
		country[3] = new JButton("CNY");
		for(int i = 0; i < 4; i++) {
			country[i].addActionListener(this);
		}
		add(new JLabel("Select Country"), 0, 0, 3, 1);
		add(country[0], 0, 1, 1, 1);
		add(country[1], 2, 1, 1, 1);
		add(country[2], 0, 2, 1, 1);
		add(country[3], 2, 2, 1, 1);
		this.setSize(500, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == country[0]) {
			new inputUSDFrame(main, country[0].getText());
			this.dispose();
		}else if(e.getSource() == country[1]) {
			new inputUSDFrame(main, country[1].getText());
			this.dispose();
		}else if(e.getSource() == country[2]) {
			new inputUSDFrame(main, country[2].getText());
		}else if(e.getSource() == country[3]) {
			new inputUSDFrame(main, country[3].getText());
			this.dispose();
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
}
