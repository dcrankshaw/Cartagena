package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import javax.swing.JFrame;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.dcranks1.cs.oose.cartagena.model.CartagenaModelImpl;

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
