package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.cs.oose.fall2011.cartagena.impl.model.CartagenaModelImpl;
//import edu.jhu.dcranks1.cs.oose.cartagena.model.CartagenaModelImpl;

public class CartagenaModelFactory {
	
	public CartagenaModel createModel()
	{
		return new CartagenaModelImpl();
	}

}
