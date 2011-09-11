/*


The game is played on a board with thirty-eight consecutive spaces: a start, an end, and thirty-six intermediate spaces.
Each intermediate space has one of six types. The thirty-six intermediate spaces are divided into six space segments, each of which must contain one space of each type.
Each player starts with six pieces on the start space and six cards.
Players alternate turns. Each turn consists of up to three moves.
Each move may either involve playing a card to move a piece forward or moving a piece backward to draw more cards.
Pieces may never leave the end space once they reach it. Pieces may not return to the start space once they leave it.
The first player to reach the end space with all of his or her pieces is the winner; explicit feedback for this event is required.

 */

package edu.jhu.dcranks1.cs.oose.cartagena;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

public class CartagenaModelImpl implements CartagenaModel {
	private Collection<CartagenaModelListener> allListeners;

	private static final int NUM_PIECES_PER_PLAYER = 6;
	private static final int NUM_LOCATIONS_PER_SEGMENT = 6;
	private static final int MAX_CARDS_PER_PLAYER = 6;
	private int movesLeftInTurn;
	private List<BoardPiece> board;
	private Map<Player, Collection<SpaceType>> cardHands;
	private Player currentPlayer;
	private Player winner;

	public CartagenaModelImpl() {
		movesLeftInTurn = 3;
		board = createNewBoard();
		cardHands = assignStartingHands();
		currentPlayer = Player.PLAYER_1;
		winner = null;

	}

	private Map<Player, Collection<SpaceType>> assignStartingHands() {
		SpaceType[] possibleValues = SpaceType.values();
		Map<Player, Collection<SpaceType>> startHands = new HashMap<Player, Collection<SpaceType>>();
		Random rand = new Random();
		for (Player currentPlayer : Player.values()) {
			Collection<SpaceType> playerCards = new ArrayList<SpaceType>();
			for (int i = 0; i < MAX_CARDS_PER_PLAYER; i++) {
				int currentCard = rand.nextInt(possibleValues.length);
				playerCards.add(possibleValues[currentCard]);
			}
			startHands.put(currentPlayer, playerCards);
		}

		return startHands;
	}

	private List<BoardPiece> createNewBoard() {
		List<BoardPiece> newBoard = new ArrayList<BoardPiece>();
		Collection<Player> playerPieces = new ArrayList<Player>();
		for (int j = 0; j < NUM_PIECES_PER_PLAYER; j++) {

			playerPieces.add(Player.PLAYER_1);
			playerPieces.add(Player.PLAYER_2);

			// create collection with six pieces for each player in it, then add
			// that to the first BoardPiece
		}
		newBoard.add(new BoardPiece(new Location(
				Location.INITIAL_LOCATION_SPACE_NUMBER), playerPieces, null));

		for (int i = Location.INITIAL_LOCATION_SPACE_NUMBER + 1; i <= Location.MAXIMUM_SPACE_NUMBER; i++) {
			newBoard.add(new BoardPiece(new Location(i)));
		}

		int currentLocationNumber = 1;
		List<SpaceType> allTypes = new ArrayList<SpaceType>();
		for (SpaceType type : SpaceType.values()) {
			allTypes.add(type);
		}
		for (int i = 1; i <= Location.MAXIMUM_SPACE_NUMBER; i++) {

			// every sixth space reshuffle the order of the space types
			if (i % NUM_LOCATIONS_PER_SEGMENT == 0) {
				Collections.shuffle(allTypes);
			}
			BoardPiece current = newBoard.get(currentLocationNumber);
			current.setSpaceType(allTypes.get(i % NUM_LOCATIONS_PER_SEGMENT));
		}
		return newBoard;

	}

	@Override
	public void addListener(CartagenaModelListener listener) {
		allListeners.add(listener);
	}

	@Override
	public void endTurnEarly() {
		switchTurn();
		publishEvent(new CartagenaMoveEvent(true, false));

	}

	@Override
	public Collection<SpaceType> getCards(Player player) {
		return Collections.unmodifiableCollection(cardHands.get(player));
	}

	@Override
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	@Override
	public Collection<Player> getPieces(Location location) {
		BoardPiece current = board.get(location.getSpaceNumber());
		return Collections.unmodifiableCollection(current.getCurrentPieces());
	}

	@Override
	public SpaceType getSpaceType(Location location) {
		BoardPiece current = board.get(location.getSpaceNumber());
		return current.getSpaceType();
	}

	@Override
	public int getTurnMovesLeft() {
		return movesLeftInTurn;
	}

	@Override
	public Player getWinner() {
		return winner;
	}

	@Override
	public void movePieceBackward(Location from, Location to) {
		BoardPiece start = board.get(from.getSpaceNumber());
		BoardPiece end = board.get(to.getSpaceNumber());
		if (to.getSpaceNumber() == Location.INITIAL_LOCATION_SPACE_NUMBER) {
			// cannot move back to start spot
			publishIllegalEvent(new CartagenaIllegalMoveEvent(
					"Cannot move backwards to initial position"));
		} else if (start.getCurrentPieces().contains(currentPlayer)) {
			if (start.getCurrentPieces().size() == 1
					|| start.getCurrentPieces().size() == 2) {
				start.getCurrentPieces().remove(currentPlayer);
				end.getCurrentPieces().add(currentPlayer);
				drawCards(start.getCurrentPieces().size());
				movesLeftInTurn--;
				boolean turnOver = false;
				if (movesLeftInTurn == 0) {
					turnOver = true;
					switchTurn();

				}

				// The game cannot be over if the player has moved backwards
				publishEvent(new CartagenaMoveEvent(turnOver, false));
			} else {
				String message = "Cannot move backwards to a spot with "
						+ start.getCurrentPieces().size() + " pieces on it";
				publishIllegalEvent(new CartagenaIllegalMoveEvent(message));
			}
		} else {
			String message = "You don't have any pieces on that spot";
			publishIllegalEvent(new CartagenaIllegalMoveEvent(message));
		}

	}

	private void switchTurn() {
		if (currentPlayer == Player.PLAYER_1)
			currentPlayer = Player.PLAYER_2;
		else
			currentPlayer = Player.PLAYER_1;

		movesLeftInTurn = 3;
	}

	private void drawCards(int numCards) {
		for (int i = 0; i < numCards; i++) {
			if (cardHands.get(currentPlayer).size() < MAX_CARDS_PER_PLAYER) {
				Random rand = new Random();
				SpaceType[] types = SpaceType.values();
				SpaceType addType = types[rand.nextInt(types.length)];
				cardHands.get(currentPlayer).add(addType);
			} else
				break;
		}
	}

	private boolean determineGameOver() {
		for (int i = Location.INITIAL_LOCATION_SPACE_NUMBER; i <= Location.MAXIMUM_SPACE_NUMBER; i++) {
			BoardPiece current = board.get(i);
			if (current.getCurrentPieces().contains(currentPlayer))
				return false;
		}
		return true;
	}

	private void publishIllegalEvent(CartagenaIllegalMoveEvent move) {
		for (CartagenaModelListener listener : allListeners) {
			listener.illegalMoveRejected(move);
		}
	}

	private void publishEvent(CartagenaMoveEvent move) {
		for (CartagenaModelListener listener : allListeners) {
			listener.moveMade(move);
		}
	}

	@Override
	public void movePieceForward(Location location, SpaceType card) {
		Collection<SpaceType> oldHand = cardHands.remove(currentPlayer);
		if (oldHand.contains(card)) {
			oldHand.remove(card);
			cardHands.put(currentPlayer, oldHand);
			BoardPiece startLocation = board.get(location.getSpaceNumber());
			if (startLocation.getCurrentPieces().contains(currentPlayer)) {
				startLocation.getCurrentPieces().remove(currentPlayer);
				for(int i = location.getSpaceNumber(); i <= Location.MAXIMUM_SPACE_NUMBER; i++)
				{
					BoardPiece endLocation = board.get(i);
					if(endLocation.getCurrentPieces().size() == 0)
					{
						endLocation.getCurrentPieces().add(currentPlayer);
						break;
					}
				}
				
				
				movesLeftInTurn--;
				boolean turnOver = false;
				if (movesLeftInTurn == 0) {
					turnOver = true;
					switchTurn();
				}
				boolean gameOver = determineGameOver();
				publishEvent(new CartagenaMoveEvent(turnOver, gameOver));

			}
			else {
				publishIllegalEvent(new CartagenaIllegalMoveEvent(
						"Player does not have any pieces on that spot"));
			}
		}
		else {
			cardHands.put(currentPlayer, oldHand);
			publishIllegalEvent(new CartagenaIllegalMoveEvent(
					"Player does have any " + card.toString() + "cards"));
		}
	}

	@Override
	public void removeListener(CartagenaModelListener listener) {
		allListeners.remove(listener);

	}

}
