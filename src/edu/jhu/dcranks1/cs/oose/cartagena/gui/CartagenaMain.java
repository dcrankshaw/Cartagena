package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import javax.swing.JFrame;

public class CartagenaMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CartagenaModelFactory modelFactory = new CartagenaModelFactory();
		try
		{
		CartagenaFrame frame = new CartagenaFrame(modelFactory);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		
	}

}
