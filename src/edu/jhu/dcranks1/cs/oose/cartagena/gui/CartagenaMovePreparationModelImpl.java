package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import java.util.ArrayList;
import java.util.List;

import edu.jhu.cs.oose.fall2011.cartagena.iface.Location;
import edu.jhu.cs.oose.fall2011.cartagena.iface.SpaceType;

public class CartagenaMovePreparationModelImpl implements CartagenaMovePreparationModel {

	private List<CartagenaMovePreparationModelListener> listeners;
	Location start;
	SpaceType card;
	Location end;
	boolean newGame;
	
	public CartagenaMovePreparationModelImpl()
	{
		listeners = new ArrayList<CartagenaMovePreparationModelListener>();
		start = null;
		end = null;
		card = null;
	}
	
	@Override
	public void addListener(CartagenaMovePreparationModelListener listener) {
		listeners.add(listener);
		
	}

	@Override
	public void removeListener(CartagenaMovePreparationModelListener listener) {
		listeners.remove(listener);
		
	}

	@Override
	public void selectCard(SpaceType type) {
		if(start != null) 
		{
			card = type;
			notifyMoveReady();
		}
		
	}

	@Override
	public void setEndLocation(Location location) {
		if(start != null)
		{
			end = location;
			notifyMoveReady();
		}
	}

	@Override
	public SpaceType getCardSelected()
	{
		return card;
	}
	
	@Override
	public Location getEndLocation()
	{
		return end;
	}
	
	@Override
	public void setStartLocation(Location location) {
		start = location;
		notifySpotSelected(location);
		
	}

	@Override
	public Location getStartLocation() {
		return start;
	}
	
	private void notifySpotSelected(Location location)
	{
		for(CartagenaMovePreparationModelListener l: listeners)
		{
			l.spotSelected(location.getSpaceNumber());
		}
	}
	
	private void notifyMoveReady()
	{
		for(CartagenaMovePreparationModelListener l: listeners)
		{
			l.makeMove();
		}
	}
	
	private void notifyNewGame()
	{
		
		for(CartagenaMovePreparationModelListener l: listeners)
		{
			l.newGame();
		}
	}

	
	/**
	 * This resets the model to the beginning of a turn
	 */
	@Override
	public void resetModel()
	{
		start = null;
		end = null;
		card = null;
		//listeners = new ArrayList<CartagenaMovePreparationModelListener>();
		
	}

	
	@Override
	public void setNewGame() {
		resetModel();
		notifyNewGame();
		
	}

}
