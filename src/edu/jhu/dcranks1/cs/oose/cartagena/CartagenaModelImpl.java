

package edu.jhu.dcranks1.cs.oose.cartagena;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaIllegalMoveEvent;
import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModelListener;
import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaMoveEvent;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Location;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Player;
import edu.jhu.cs.oose.fall2011.cartagena.iface.SpaceType;

/**
 * Implements the game logic of Cartagena game
 * @author Daniel Crankshaw
 */

public class CartagenaModelImpl implements CartagenaModel {


	/** The number of game pieces each player has. */
	private static final int NUM_PIECES_PER_PLAYER = 6;
	
	/** The number of spaces in each segment of the board. No segment can have two pieces of the same type. */
	private static final int NUM_LOCATIONS_PER_SEGMENT = 6;
	/** The number of cards each player starts the game with. */
	private static final int STARTING_HAND_SIZE = 6;
	/** Keeps track of all listeners to publish state changes to. */
	private Collection<CartagenaModelListener> allListeners;
	/** The number of moves left in the current player's turn. */
	private int movesLeftInTurn;
	
	/** Tracks the set of pieces in the board of the current game */
	private List<BoardPiece> board;
	
	/** The cards in each player's hand currently */
	private Map<Player, Collection<SpaceType>> cardHands;
	
	/** The player whose turn it is */
	private Player currentPlayer;
	
	/** The winning player, set once the game is over */
	private Player winner;

	/** Starts a new game by randomly creating a new board and assigning hands. */
	public CartagenaModelImpl() {
		movesLeftInTurn = 3;
		board = createNewBoard();
		cardHands = assignStartingHands();
		currentPlayer = Player.PLAYER_1;
		winner = null;

	}

	/** 
	 * Draws STARTING_HAND_SIZE cards randomly for each player. Every SpaceType has an equal probability of being drawn.
	 * @return A map between each player and their hand
	 */
	private Map<Player, Collection<SpaceType>> assignStartingHands() {

		allListeners = new LinkedList<CartagenaModelListener>();
		SpaceType[] possibleValues = SpaceType.values();
		Map<Player, Collection<SpaceType>> startHands = new HashMap<Player, Collection<SpaceType>>();
		Random rand = new Random();
		for (Player currentPlayer : Player.values()) {
			Collection<SpaceType> playerCards = new ArrayList<SpaceType>();
			for (int i = 0; i < STARTING_HAND_SIZE; i++) {
				int currentCard = rand.nextInt(possibleValues.length);
				playerCards.add(possibleValues[currentCard]);
			}
			startHands.put(currentPlayer, playerCards);
		}

		return startHands;
	}

	/**
	 * Creates a new board with a random permuatation of the spacetypes in each of the segments.
	 * @return a list of all the BoardPieces
	 */
	private List<BoardPiece> createNewBoard() {
		List<BoardPiece> newBoard = new ArrayList<BoardPiece>();
		Collection<Player> playerPieces = new ArrayList<Player>();

		for (int j = 0; j < NUM_PIECES_PER_PLAYER; j++) {

			playerPieces.add(Player.PLAYER_1);
			// create collection with six pieces for each player in it, then add
			// that to the first BoardPiece
		}
		for (int j = 0; j < NUM_PIECES_PER_PLAYER; j++) {
			
			playerPieces.add(Player.PLAYER_2);

			// create collection with six pieces for each player in it, then add
			// that to the first BoardPiece
		}
		newBoard.add(new BoardPiece(new Location(
				Location.INITIAL_LOCATION_SPACE_NUMBER), playerPieces, null));

		List<SpaceType> allTypes = new ArrayList<SpaceType>();
		for (SpaceType type : SpaceType.values()) {
			allTypes.add(type);
		}

		for (int i = Location.INITIAL_LOCATION_SPACE_NUMBER + 1; i <= Location.MAXIMUM_SPACE_NUMBER; i++) {

			if (((i - 1) % NUM_LOCATIONS_PER_SEGMENT) == 0) {
				Collections.shuffle(allTypes);
			}

			BoardPiece current = new BoardPiece(new Location(i), allTypes
					.get((i - 1) % NUM_LOCATIONS_PER_SEGMENT));

			newBoard.add(current);
		}

		return newBoard;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addListener(CartagenaModelListener listener) {
		allListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endTurnEarly() {
		switchTurn();
		publishEvent(new CartagenaMoveEvent(true, false));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SpaceType> getCards(Player player) {
		return Collections.unmodifiableCollection(cardHands.get(player));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Player> getPieces(Location location) {
		BoardPiece current = board.get(location.getSpaceNumber());
		return Collections.unmodifiableCollection(current.getCurrentPieces());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpaceType getSpaceType(Location location) {
		BoardPiece current = board.get(location.getSpaceNumber());
		return current.getSpaceType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTurnMovesLeft() {
		return movesLeftInTurn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player getWinner() {
		return winner;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void movePieceBackward(Location from, Location to) {
		BoardPiece start = board.get(from.getSpaceNumber());
		BoardPiece end = board.get(to.getSpaceNumber());
		if (to.getSpaceNumber() == Location.INITIAL_LOCATION_SPACE_NUMBER) {
			// cannot move back to start spot
			publishIllegalEvent(new CartagenaIllegalMoveEvent(
					"Cannot move backwards to initial position"));
		} else if (start.getCurrentPieces().contains(currentPlayer)) {
			if (end.getCurrentPieces().size() == 1
					|| end.getCurrentPieces().size() == 2) {
				drawCards(end.getCurrentPieces().size());
				start.getCurrentPieces().remove(currentPlayer);
				end.getCurrentPieces().add(currentPlayer);
				boolean turnOver = false;
				if (--movesLeftInTurn == 0) {
					turnOver = true;
					switchTurn();

				}

				// The game cannot be over if the player has moved backwards
				publishEvent(new CartagenaMoveEvent(turnOver, false));
			} else {
				String message = "Cannot move backwards to a spot with "
						+ end.getCurrentPieces().size() + " pieces on it";
				publishIllegalEvent(new CartagenaIllegalMoveEvent(message));
			}
		} else {
			String message = "You don't have any pieces on that spot";
			publishIllegalEvent(new CartagenaIllegalMoveEvent(message));
		}

	}

	/**
	 * Ends the current player's turn
	 */
	private void switchTurn() {
		if (currentPlayer == Player.PLAYER_1)
			currentPlayer = Player.PLAYER_2;
		else
			currentPlayer = Player.PLAYER_1;

		movesLeftInTurn = 3;
	}

	/**
	 * Randomly draws cards and places them in the hand of the current player
	 * @param numCards The number of cards to draw.
	 */
	private void drawCards(int numCards) {
		for (int i = 0; i < numCards; i++) {
			//It appears that you can have as big a hand as you want
			//if (cardHands.get(currentPlayer).size() < STARTING_HAND_SIZE) {
				Random rand = new Random();
				SpaceType[] types = SpaceType.values();
				SpaceType addType = types[rand.nextInt(types.length)];
				cardHands.get(currentPlayer).add(addType);
			/*} else
				break;*/
		}
	}

	/**
	 * Decides whether either player has gotten all of their pieces off the board.
	 * @return true if the game is over, false otherwise
	 */
	private boolean determineGameOver() {
		boolean player1Victory = true;
		boolean player2Victory = true;
		for (int i = Location.INITIAL_LOCATION_SPACE_NUMBER; i <= Location.MAXIMUM_SPACE_NUMBER; i++) {
			BoardPiece current = board.get(i);
			if (current.getCurrentPieces().contains(Player.PLAYER_1)) {
				player1Victory = false;
			}
			if (current.getCurrentPieces().contains(Player.PLAYER_2)) {
				player2Victory = false;
			}
			if (!player1Victory && !player2Victory) {
				// means the game is not over
				return false;
			}
		}

		if (player1Victory) {
			winner = Player.PLAYER_1;
			return true;
		} else if (player2Victory) {
			winner = Player.PLAYER_2;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Publishes the attempted illegal move to all of the model's listeners.
	 * @param move the illegal event being published
	 */
	private void publishIllegalEvent(CartagenaIllegalMoveEvent move) {
		for (CartagenaModelListener listener : allListeners) {
			listener.illegalMoveRejected(move);
		}
	}

	/**
	 * Publishes the event to all of the model's listeners.
	 * @param move the event being published
	 */
	private void publishEvent(CartagenaMoveEvent move) {
		for (CartagenaModelListener listener : allListeners) {
			listener.moveMade(move);
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void movePieceForward(Location location, SpaceType card) {
		Collection<SpaceType> oldHand = cardHands.remove(currentPlayer);
		if (oldHand.contains(card)) {
			oldHand.remove(card);
			cardHands.put(currentPlayer, oldHand);
			BoardPiece startLocation = board.get(location.getSpaceNumber());
			if (startLocation.getCurrentPieces().contains(currentPlayer)) {
				startLocation.getCurrentPieces().remove(currentPlayer);
				for (int i = location.getSpaceNumber() + 1; i <= Location.MAXIMUM_SPACE_NUMBER; i++) {
					BoardPiece endLocation = board.get(i);
					if (endLocation.getSpaceType() != null) {
						if (endLocation.getSpaceType().equals(card)
								&& endLocation.getCurrentPieces().size() == 0) {
							endLocation.getCurrentPieces().add(currentPlayer);
							break;
						}

					}

				}

				boolean turnOver = false;
				if (--movesLeftInTurn == 0) {
					turnOver = true;
					switchTurn();
				}
				boolean gameOver = determineGameOver();

				publishEvent(new CartagenaMoveEvent(turnOver, gameOver));

			} else {
				publishIllegalEvent(new CartagenaIllegalMoveEvent(
						"You do not have any pieces on that spot"));
			}
		} else {
			cardHands.put(currentPlayer, oldHand);
			publishIllegalEvent(new CartagenaIllegalMoveEvent(
					"You do not have any " + card.toString() + "cards"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeListener(CartagenaModelListener listener) {
		allListeners.remove(listener);

	}

}
