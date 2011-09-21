package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.omg.CORBA.INITIALIZE;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Location;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Player;
import edu.jhu.cs.oose.fall2011.cartagena.iface.SpaceType;

public class CartagenaBoardPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CartagenaModel model;
	private boolean[] selectedSpots;
	private static final int BOARD_WIDTH = 1050;
	private static final int BOARD_HEIGHT = 900;
	public static final float TRANSPARENCY_LEVEL = 0.7f;
	// private static final String basePath =
	// "/home/crank/git/Cartagena/images/drwhovillain";
	private static final String fileEnding = "trans.png";
	private static String path = "images/drwhovillain";
	private Map<SpaceType, BufferedImage> boardPictures;
	private static final int NUM_COLUMNS = 7;
	private static final int NUM_ROWS = 6;
	private static final int NUM_SPOTS_PER_COLUMN = 6;
	private static final int NUM_SPOTS_PER_ROW = 6;
	private static final int MAX_PIECES_PER_ROW = 4;
	private static final int MAX_ROWS_PER_SPOT = 6;
	private static final String backgroundFileName = "background2.png";
	private static final String tardisFileName = "tardis.png";
	private BufferedImage tardisPicture;

	private static final int NUM_PIECES_PER_PLAYER = 6;

	private BufferedImage backgroundLogo;

	private CartagenaMovePreparationModel movePrepModel;

	private static final double pictureCellPercentage = 0.85;

	private static final double SELECTION_BORDER_PERCENTAGE = 0.05;

	private static final double PATH_WIDTH_PERCENTAGE = 0.1;

	public CartagenaBoardPanel(CartagenaModel cartagenaModel,
			CartagenaMovePreparationModel movePrep)
			throws CartagenaGuiException {
		this.model = cartagenaModel;
		this.movePrepModel = movePrep;
		selectedSpots = new boolean[Location.MAXIMUM_SPACE_NUMBER
				- Location.MINIMUM_SPACE_NUMBER + 1];
		Arrays.fill(selectedSpots, false);

		boardPictures = new HashMap<SpaceType, BufferedImage>();

		movePrepModel.addListener(new CartagenaMovePreparationModelListener() {

			@Override
			public void spotSelected(int i) {
				Arrays.fill(selectedSpots, false);
				if (i >= 0) {
					selectedSpots[i] = true;
				}
				repaint();
			}
		});

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		int pictureNumber = 0;
		for (SpaceType current : SpaceType.values()) {
			try {
				BufferedImage img = ImageIO
						.read(classLoader.getResourceAsStream(path
								+ pictureNumber + fileEnding));
				boardPictures.put(current, img);
				pictureNumber++;
			} catch (Exception e) {
				throw new CartagenaGuiException(e.getMessage());
			}

		}

		try {
			backgroundLogo = ImageIO.read(classLoader.getResourceAsStream(path
					+ backgroundFileName));
			tardisPicture = ImageIO.read(classLoader.getResourceAsStream(path
					+ tardisFileName));
		} catch (Exception e) {
			throw new CartagenaGuiException(e.getMessage());
		}

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				final int cx = (int) ((double) p.getX() / (double) getWidth() * NUM_COLUMNS);
				final int cy = (int) ((double) p.getY() / (double) getHeight() * NUM_ROWS);
				
				 
				// means user clicked in the rightmost column below the initial
				// square
				if (cx == NUM_ROWS && cy != 0) {
					// if you click in the panel outside of a square, the
					// currently selected square gets unselected
					movePrepModel.setStartLocation(null);
				} else {
					Location selected = new Location(calculateSpacenumber(cx,
							cy));
					if (SwingUtilities.isRightMouseButton(e)) {
						movePrepModel.setEndLocation(selected);
					} else {
						movePrepModel.setStartLocation(selected);
					}
				}

			}

		});
	}

	private int calculateSpacenumber(int x, int y) {
		if (y % 2 == 0) {
			return ((NUM_SPOTS_PER_ROW * y) + (NUM_SPOTS_PER_ROW - x));
		} else {
			return (NUM_SPOTS_PER_ROW * y + x + 1);
		}

	}

	public Dimension getPreferredSize() {
		return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
	}

	// TODO paint first and last square - 1st and last square should be tardis's
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		Image scaledBackground = scalePicture(backgroundLogo, getWidth(),
				getHeight());

		int widthOffset = (getWidth() - scaledBackground.getWidth(null)) / 2;
		int heightOffset = (getHeight() - scaledBackground.getHeight(null)) / 2;

		g.drawImage(scaledBackground, widthOffset, heightOffset, null);

		int cellWidth = getWidth() / NUM_COLUMNS;
		int cellHeight = getHeight() / NUM_ROWS;

		// draw start spot

		drawBoardPiece(g, NUM_SPOTS_PER_ROW * cellWidth, 0, cellWidth,
				cellHeight, null, null, PathPosition.WEST,
				selectedSpots[Location.INITIAL_LOCATION_SPACE_NUMBER],
				Location.INITIAL_LOCATION_SPACE_NUMBER);

		// draw end spot

		drawBoardPiece(g, NUM_SPOTS_PER_ROW * cellWidth, (NUM_ROWS - 1) * cellHeight,
				cellWidth, cellHeight, null, null, PathPosition.WEST, false, -1);

		for (int spaceNumber = Location.INITIAL_LOCATION_SPACE_NUMBER; spaceNumber < Location.MAXIMUM_SPACE_NUMBER; spaceNumber++) {
			int y = spaceNumber / NUM_SPOTS_PER_ROW;
			int x = 0;
			if (y % 2 == 0) {
				x = (NUM_SPOTS_PER_ROW - (spaceNumber % NUM_SPOTS_PER_ROW)) - 1;
			} else {
				x = spaceNumber % NUM_SPOTS_PER_ROW;
			}

			PathPosition entrance = PathPosition.EAST;
			PathPosition exit = PathPosition.WEST;

			// TODO path position logic

			drawBoardPiece(g, x * cellWidth, y * cellHeight, cellWidth,
					cellHeight, model
							.getSpaceType(new Location(spaceNumber + 1)),
					entrance, exit, selectedSpots[spaceNumber + 1],
					spaceNumber + 1);

		}
	}

	public static Image scalePicture(BufferedImage original, int picWidth,
			int picHeight) {
		int origHeight = original.getHeight();
		int origWidth = original.getWidth();

		double optimalWidthScalePercentage = (double) picWidth
				/ (double) origWidth;
		double optimalHeightScalePercentage = (double) picHeight
				/ (double) origHeight;

		double scalePercentage = 1;

		// preserve aspect ratio by changing to the size demanded by the
		// dimension that can grow less (or has to shrink more)
		if (optimalWidthScalePercentage < optimalHeightScalePercentage) {
			scalePercentage = optimalWidthScalePercentage;
		} else {
			scalePercentage = optimalHeightScalePercentage;
		}

		int scaledWidth = (int) (origWidth * scalePercentage);
		int scaledHeight = (int) (origHeight * scalePercentage);

		return original.getScaledInstance(scaledWidth, scaledHeight,
				BufferedImage.SCALE_DEFAULT);

	}

	private void drawBoardPiece(Graphics g, int xTopLeft, int yTopLeft,
			int width, int height, SpaceType type, PathPosition pathEntrance,
			PathPosition pathExit, boolean selected, int spaceNumber) {
		// scale the picture
		int picWidth = (int) (width * pictureCellPercentage);
		int picHeight = (int) (height * pictureCellPercentage);
		int standardBorderWidth = (width - picWidth) / 2;
		int standardBorderHeight = (height - picHeight) / 2;

		Image scaledPic;
		if (type != null) {
			scaledPic = scalePicture(boardPictures.get(type), picWidth,
					picHeight);
		} else {
			scaledPic = scalePicture(tardisPicture, picWidth, picHeight);
		}

		picWidth = scaledPic.getWidth(null);
		picHeight = scaledPic.getHeight(null);
		int actualBorderWidth = (width - picWidth) / 2;
		int actualBorderHeight = (height - picHeight) / 2;

		if (selected) {
			g.setColor(Color.blue.brighter());
			g.fillRect(xTopLeft, yTopLeft, width, height);

			g.setColor(Color.BLACK);
			int innerX = (int) (xTopLeft + (width
					* (SELECTION_BORDER_PERCENTAGE) / 2));
			int innerY = (int) (yTopLeft + (height
					* (SELECTION_BORDER_PERCENTAGE) / 2));

			g.fillRect(innerX, innerY,
					(int) (width * (1 - SELECTION_BORDER_PERCENTAGE)),
					(int) (height * (1 - SELECTION_BORDER_PERCENTAGE)));
		}

		g.drawImage(scaledPic, xTopLeft + actualBorderWidth, yTopLeft
				+ actualBorderHeight, null);
		// last space is indicated by spaceNumber == -1
		if (spaceNumber >= 0) {
			int numPlayer1Pieces = Collections.frequency(model
					.getPieces(new Location(spaceNumber)), Player.PLAYER_1);
			int numPlayer2Pieces = Collections.frequency(model
					.getPieces(new Location(spaceNumber)), Player.PLAYER_2);
			
			drawGamePiece(g, xTopLeft, yTopLeft, width, height,
					numPlayer1Pieces, numPlayer2Pieces, spaceNumber);
		} else {
			int numPlayer1PiecesOnBoard = 0;
			int numPlayer2PiecesOnBoard = 0;

			for (int i = Location.MINIMUM_SPACE_NUMBER; i <= Location.MAXIMUM_SPACE_NUMBER; i++) {
				numPlayer1PiecesOnBoard += Collections.frequency(model
						.getPieces(new Location(i)), Player.PLAYER_1);
				numPlayer2PiecesOnBoard += Collections.frequency(model
						.getPieces(new Location(i)), Player.PLAYER_2);
			}

			drawGamePiece(g, xTopLeft, yTopLeft, width, height,
					NUM_PIECES_PER_PLAYER - numPlayer1PiecesOnBoard,
					NUM_PIECES_PER_PLAYER - numPlayer2PiecesOnBoard,
					spaceNumber);
		}
		// drawPath(g, xTopLeft, yTopLeft, width, height, pathEntrance);
		// drawPath(g, xTopLeft, yTopLeft, width, height, pathExit);

	}

	// TODO add in game pieces
	private void drawGamePiece(Graphics g, int xTopLeft, int yTopLeft,
			int width, int height, int numPlayer1Pieces, int numPlayer2Pieces,
			int spotNumber) {
		
		int picWidth = (int) (width * pictureCellPercentage);
		int picHeight = (int) (height * pictureCellPercentage);
		int borderWidth = (width - picWidth) / 2;
		int borderHeight = (height - picHeight) / 2;
		

		float pieceSpacingPercent = 0.1f;

		int pieceWidth = picWidth / MAX_PIECES_PER_ROW;
		int pieceHeight = picHeight / MAX_ROWS_PER_SPOT;

		int pieceNumber = 1;

		int x = xTopLeft + borderWidth;
		int y = yTopLeft + borderHeight - pieceHeight; //subtract pieceHeight because y will get incremented the first time through the for loop
		double radiusPercentage = 0.5;
		double borderExtra = 0.1;
		
		
		
		
		
		int radiusWidth = (int) (pieceWidth*radiusPercentage);
		int radiusHeight = (int) (pieceHeight*radiusPercentage);
		int pieceBorderWidth = (int) (radiusWidth*(1+borderExtra));
		int pieceBorderHeight = (int) (radiusHeight * (1+borderExtra));
		
		
		Player player = Player.PLAYER_1;
		for (int i = 0; i < numPlayer1Pieces; i++) {
			if (((pieceNumber - 1) % (MAX_PIECES_PER_ROW)) == 0) {
				y += pieceHeight;
			}

			
			// draw shape
			// set color to black
			// draw same shape but 90% of the size, inside, this will give
			// me a border
			
			int xPiece = (x + ((pieceNumber - 1) % MAX_PIECES_PER_ROW)
					* pieceWidth);
			
			int cx = xPiece + pieceWidth / 2;
			int cy = y + pieceHeight / 2;
			
			//g.setColor(Color.BLUE);
			//g.fillOval(cx, cy, pieceBorderWidth, pieceBorderHeight);
			//g.fillOval(cx, cy, pieceBorderWidth, pieceBorderWidth);
			
			g.setColor(Color.RED);
			g.fillOval(cx, cy, radiusWidth, radiusWidth);
			
			/*;
			g.fillRect(xPiece + (int) (pieceWidth * pieceSpacingPercent / 2), y
					+ (int) (pieceHeight * pieceSpacingPercent / 2),
					pieceWidth, pieceHeight);*/

			pieceNumber++;
		}
		player = Player.PLAYER_2;
		for (int i = 0; i < numPlayer2Pieces; i++) {
			if (((pieceNumber - 1) % (MAX_PIECES_PER_ROW)) == 0) {
				y += pieceHeight;
			}

			int xPiece = (x + ((pieceNumber - 1) % MAX_PIECES_PER_ROW)
					* pieceWidth);
			
			int cx = xPiece + pieceWidth / 2;
			int cy = y + pieceHeight / 2;
			
			//g.setColor(Color.BLUE);
			//g.fillOval(cx, cy, pieceBorderWidth, pieceBorderWidth);
			
			g.setColor(Color.GREEN);
			g.fillOval(cx, cy, radiusWidth, radiusWidth);
			
			
			
			//g.fillRect(xPiece + (int) (pieceWidth * pieceSpacingPercent / 2), y
				//	+ (int) (pieceHeight * pieceSpacingPercent / 2),
				//	pieceWidth, pieceHeight);

			pieceNumber++;
		}

	}

	private void drawPath(Graphics g, int xTopLeft, int yTopLeft, int width,
			int height, PathPosition position) {
		int picWidth = (int) (width * pictureCellPercentage);
		int picHeight = (int) (height * pictureCellPercentage);
		int borderWidth = (width - picWidth) / 2;
		int borderHeight = (height - picHeight) / 2;
		g.setColor(Color.WHITE);
		int verticalPathWidth = (int) (width * PATH_WIDTH_PERCENTAGE);
		int horizontalPathWidth = (int) (height * PATH_WIDTH_PERCENTAGE);
		int verticalPathOffset = (width - verticalPathWidth) / 2;
		int horizontalPathOffset = (height - horizontalPathWidth) / 2;
		switch (position) {
		case NORTH: {
			g.fillRect(xTopLeft + verticalPathOffset, yTopLeft,
					verticalPathWidth, borderHeight);
			break;
		}
		case SOUTH: {
			g.fillRect(xTopLeft + verticalPathOffset, yTopLeft + height
					- borderHeight, verticalPathWidth, borderHeight);
			break;
		}
		case EAST: {
			g.fillRect(xTopLeft + width - borderWidth, yTopLeft
					+ horizontalPathOffset, borderWidth, horizontalPathWidth);
			break;
		}
		case WEST: {
			g.fillRect(xTopLeft, yTopLeft + horizontalPathOffset, borderWidth,
					horizontalPathWidth);
			break;
		}
		default: // don't paint anything

		}
	}

	public void setCartagenaModel(CartagenaModel cartagenaModel) {
		this.model = cartagenaModel;
	}

}
