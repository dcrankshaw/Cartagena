package edu.jhu.dcranks1.cs.oose.cartagena;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.jhu.cs.oose.fall2011.cartagena.iface.Location;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Player;
import edu.jhu.cs.oose.fall2011.cartagena.iface.SpaceType;

/**
 * This class represents a single spot on the game board
 * @author Daniel Crankshaw
 */

public class BoardPiece {
	
	/** The location of this spot. */
	private Location location;
	/** All of the game pieces on this spot. */
	private Collection<Player> currentPieces;
	/** The spacetype of this spot */
	private SpaceType spaceType;
	
	/**
	 * Creates a new spot at the given location with no pieces and a default color on it
	 * @param _myLocation the location of the spot
	 */
	public BoardPiece(Location _myLocation)
	{
		location = _myLocation;
		currentPieces = new ArrayList<Player>();
		spaceType = SpaceType.BLUE; //Default spacetype, will eventually get changed
									//TODO: Could this cause problems? May add a verification check to ensure Board
									//obeys all rules at end of constructor in CartagenaModelImpl
	}
	
	/**
	 * Creates a new spot with the given location and type and no players on it
	 * @param _myLocation the location of the spot
	 * @param _mySpaceType the type of the spot
	 */
	public BoardPiece(Location _myLocation, SpaceType _mySpaceType)
	{
		location = _myLocation;
		currentPieces = new ArrayList<Player>();
		spaceType = _mySpaceType;
	}
	
	/**
	 * Creates a new spot with the given location, game pieces, and space type
	 * @param _mylocation the location of the spot
	 * @param _currentPieces the pieces on the spot
	 * @param _mySpaceType the type of the spot
	 */
	public BoardPiece(Location _mylocation, Collection<Player> _currentPieces, SpaceType _mySpaceType)
	{
		location = _mylocation;
		currentPieces = _currentPieces;
		spaceType = _mySpaceType;
		
	}

	/**
	 * get the location of the spot
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/** 
	 * Set the location of the spot
	 * @param location the new location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * get the pieces on the spot
	 * @return all the pieces
	 */
	public Collection<Player> getCurrentPieces() {
		return currentPieces;
	}

	/**
	 * set the current pieces 
	 * @param currentPieces the new pieces for the spot
	 */
	public void setCurrentPieces(Collection<Player> currentPieces) {
		this.currentPieces = currentPieces;
	}

	/**
	 * Get the type of spot
	 * @return the type
	 */
	public SpaceType getSpaceType() {
		return spaceType;
	}

	/**
	 * set the type of the spot
	 * @param spaceType the new type for the spot
	 */
	public void setSpaceType(SpaceType spaceType) {
		this.spaceType = spaceType;
	}
}
