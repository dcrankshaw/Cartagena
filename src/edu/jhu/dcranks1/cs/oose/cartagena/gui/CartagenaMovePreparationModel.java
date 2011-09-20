package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import edu.jhu.cs.oose.fall2011.cartagena.iface.Location;
import edu.jhu.cs.oose.fall2011.cartagena.iface.SpaceType;

public interface CartagenaMovePreparationModel {
	
	public void setStartLocation(Location l);
	
	public Location getStartLocation();
	
	public void selectCard(SpaceType type);
	
	public void setEndLocation(Location l);
	
	public void addListener(CartagenaMovePreparationModelListener listener);
	
	public void removeListener(CartagenaMovePreparationModelListener listener);
	
	public SpaceType getCardSelected();
	
	public void resetModel();
	
	public void setNewGame();
	
	public Location getEndLocation();
	
	
}
