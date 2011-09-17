package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import javax.swing.JFrame;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.dcranks1.cs.oose.cartagena.model.CartagenaModelImpl;

public class CartagenaMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CartagenaModel model = new CartagenaModelImpl();
		try
		{
		CartagenaFrame frame = new CartagenaFrame(model);
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
