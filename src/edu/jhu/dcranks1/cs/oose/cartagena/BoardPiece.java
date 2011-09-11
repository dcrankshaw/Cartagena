package edu.jhu.dcranks1.cs.oose.cartagena;

import java.util.ArrayList;
import java.util.Collection;

import edu.jhu.cs.oose.fall2011.cartagena.iface.Location;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Player;
import edu.jhu.cs.oose.fall2011.cartagena.iface.SpaceType;


public class BoardPiece {
	
	private Location location;
	private Collection<Player> currentPieces;
	private SpaceType spaceType;
	
	public BoardPiece(Location _myLocation)
	{
		location = _myLocation;
		currentPieces = new ArrayList<Player>();
		spaceType = SpaceType.BLUE; //Default spacetype, will eventually get changed
									//TODO: Could this cause problems? May add a verification check to ensure Board
									//obeys all rules at end of constructor in CartagenaModelImpl
	}
	
	public BoardPiece(Location _myLocation, SpaceType _mySpaceType)
	{
		location = _myLocation;
		currentPieces = new ArrayList<Player>();
		spaceType = _mySpaceType;
	}
	
	public BoardPiece(Location _mylocation, Collection<Player> _currentPieces, SpaceType _mySpaceType)
	{
		location = _mylocation;
		currentPieces = _currentPieces;
		spaceType = _mySpaceType;
		
		
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Collection<Player> getCurrentPieces() {
		return currentPieces;
	}

	public void setCurrentPieces(Collection<Player> currentPieces) {
		this.currentPieces = currentPieces;
	}

	public SpaceType getSpaceType() {
		return spaceType;
	}

	public void setSpaceType(SpaceType spaceType) {
		this.spaceType = spaceType;
	}

}
