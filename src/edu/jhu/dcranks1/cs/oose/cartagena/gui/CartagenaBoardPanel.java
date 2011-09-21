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
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
	private static final int MAX_ROWS_PER_SPOT = 3;
	private static final String backgroundFileName = "background2.png";
	
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
		selectedSpots = new boolean[Location.MAXIMUM_SPACE_NUMBER - Location.MINIMUM_SPACE_NUMBER + 1];
		Arrays.fill(selectedSpots, false);

		boardPictures = new HashMap<SpaceType, BufferedImage>();

		movePrepModel.addListener(new CartagenaMovePreparationModelListener() {

			@Override
			public void spotSelected(int i) {
				Arrays.fill(selectedSpots, false);
				if(i >= 0)
				{
					selectedSpots[i] = true;
				}
				repaint();
			}
		});

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		int pictureNumber = 0;
		for (SpaceType current : SpaceType.values()) {
			try {
				BufferedImage img = ImageIO.read(classLoader.getResourceAsStream(path + pictureNumber + fileEnding));
				boardPictures.put(current, img);
				pictureNumber++;
			} catch (Exception e) {
				throw new CartagenaGuiException(e.getMessage());
			}

		}
		
		try {
			backgroundLogo = ImageIO.read(classLoader.getResourceAsStream(path + backgroundFileName));
		}
		catch (Exception e)
		{
			throw new CartagenaGuiException(e.getMessage());
		}

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				final int cx = (int) ((double) p.getX() / (double) getWidth() * NUM_COLUMNS);
				final int cy = (int) ((double) p.getY() / (double) getHeight() * NUM_ROWS);
				
				/*System.out.println("Pixel clicked: (" + p.getX() + "," + p.getY() + ")\tCalculated point: (" + cx + "," + cy + ")");
				System.out.println("Cell Dimensions: (" + ((double) getWidth()) / NUM_COLUMNS + "," + ((double) getHeight()) / NUM_ROWS + ")");*/
				//means user clicked in the rightmost column below the initial square
				if (cx == NUM_ROWS && cy != 0) {
					// if you click in the panel outside of a square, the
					// currently selected square gets unselected
					movePrepModel.setStartLocation(null);
				} else {
					Location selected = new Location(calculateSpacenumber(cx, cy));
					if (SwingUtilities.isRightMouseButton(e)) {
						movePrepModel.setEndLocation(selected);
					} else {
						movePrepModel.setStartLocation(selected);
					}
				}

			}

		});
	}

	private int calculateSpacenumber(int x, int y)
	{
		if(y%2 == 0)
		{
			return ((NUM_SPOTS_PER_ROW*y) + (NUM_SPOTS_PER_ROW-x));
		}
		else
		{
			return (NUM_SPOTS_PER_ROW*y + x + 1);
		}
		
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
	}

	//TODO paint first and last square - 1st and last square should be tardis's
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Image scaledBackground = scalePicture(backgroundLogo, getWidth(), getHeight());
		
		int widthOffset = (getWidth() - scaledBackground.getWidth(null)) / 2;
		int heightOffset = (getHeight() - scaledBackground.getHeight(null)) / 2;
		
		g.drawImage(scaledBackground, widthOffset, heightOffset, null);
		
		int cellWidth = getWidth() / NUM_COLUMNS;
		int cellHeight = getHeight() / NUM_ROWS;
		
		for(int spaceNumber = Location.INITIAL_LOCATION_SPACE_NUMBER; spaceNumber < Location.MAXIMUM_SPACE_NUMBER; spaceNumber++)
		{
			int y = spaceNumber / NUM_SPOTS_PER_ROW;
			int x = 0;
			if(y % 2 == 0)
			{
				x = (NUM_SPOTS_PER_ROW - (spaceNumber % NUM_SPOTS_PER_ROW)) - 1;
			}
			else
			{
				x = spaceNumber % NUM_SPOTS_PER_ROW;
			}
			
			PathPosition entrance = PathPosition.EAST;
			PathPosition exit = PathPosition.WEST;
			
			//TODO path position logic
			
			drawBoardPiece(g, x * cellWidth, y * cellHeight, cellWidth,
					cellHeight, model.getSpaceType(new Location(spaceNumber + 1)), entrance, exit,
					selectedSpots[spaceNumber + 1]);
			
		}
		
		
		/*for(int y = 0; y < NUM_ROWS; y++)
		{
			for(int x = 0; x < NUM_COLUMNS - 1; x++)
			{
				PathPosition entrance = PathPosition.EAST;
				PathPosition exit = PathPosition.WEST;
				
				//TODO path position logic
				
				drawBoardPiece(g, x * cellWidth, y * cellHeight, cellWidth,
						cellHeight, model.getSpaceType(new Location(x
								+ NUM_SPOTS_PER_ROW * y)), entrance, exit,
						selectedSpots[x + NUM_SPOTS_PER_ROW * y]);
				
			}
			
		}*/
		
		
		
		
		
		
		
		/*for (int x = 0; x < NUM_COLUMNS; x++) {
			//y starts at 1, leaving the top row blank except for 1st, last square
			for (int y = 1; y < NUM_ROWS; y++) {
				PathPosition entrance = PathPosition.NORTH;
				PathPosition exit = PathPosition.SOUTH;
				
				//path position logic
				if ((y + 1) == NUM_ROWS) {
					if ((x % 2) == 0)
						exit = PathPosition.EAST;
					else
						exit = PathPosition.WEST;
				} else if (y == 1 && x > 0 && x < (NUM_COLUMNS - 1)) {
					if ((x % 2) == 0)
						entrance = PathPosition.WEST;
					else
						entrance = PathPosition.EAST;
				}
				drawBoardPiece(g, x * cellWidth, y * cellHeight, cellWidth,
						cellHeight, model.getSpaceType(new Location(x
								* NUM_SPOTS_PER_COLUMN + y)), entrance, exit,
						selectedSpots[x * NUM_SPOTS_PER_COLUMN + y]);
			}

		}*/

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
			PathPosition pathExit, boolean selected) {
		// scale the picture
		int picWidth = (int) (width * pictureCellPercentage);
		int picHeight = (int) (height * pictureCellPercentage);
		int standardBorderWidth = (width - picWidth) / 2;
		int standardBorderHeight = (height - picHeight) / 2;

		Image scaledPic = scalePicture(boardPictures.get(type), picWidth,
				picHeight);
		
		picWidth = scaledPic.getWidth(null);
		picHeight = scaledPic.getHeight(null);
		int actualBorderWidth = (width - picWidth) / 2;
		int actualBorderHeight = (height - picHeight) / 2;
		
		if (selected) {
			g.setColor(Color.blue.brighter());
			g.fillRect(xTopLeft, yTopLeft, width, height);
			
			g.setColor(Color.BLACK);
			int innerX = (int) (xTopLeft + (width*(SELECTION_BORDER_PERCENTAGE)/2));
			int innerY = (int) (yTopLeft + (height * (SELECTION_BORDER_PERCENTAGE)/2));
			
			g.fillRect(innerX, innerY, (int) (width*(1-SELECTION_BORDER_PERCENTAGE)), (int) (height*(1-SELECTION_BORDER_PERCENTAGE)));
		}
		//TODO center images in square, figure out how to put them on a transparent background.
		g.drawImage(scaledPic, xTopLeft + actualBorderWidth, yTopLeft + actualBorderHeight,
				null);
		/*
		 * switch(type) { case BLUE: { g.setColor(Color.BLUE); break; } case
		 * GREEN: { g.setColor(Color.GREEN); break; } case ORANGE: {
		 * g.setColor(Color.ORANGE); break; } case PURPLE: {
		 * g.setColor(Color.MAGENTA); break; } case RED: {
		 * g.setColor(Color.RED); break; } case YELLOW: {
		 * g.setColor(Color.YELLOW); break; } }
		 */

		// TODO change path to circle with radius 1/2 borderwidth (or height),
		// centered in the middle of the two cells
		/*g.fillRect(xTopLeft + borderWidth, yTopLeft + borderHeight, picWidth,
				picHeight);*/

		//drawPath(g, xTopLeft, yTopLeft, width, height, pathEntrance);
		//drawPath(g, xTopLeft, yTopLeft, width, height, pathExit);

		

	}

	
	//TODO add in game pieces
	private void drawGamePiece(Graphics g, int xTopLeft, int yTopLeft,
			int width, int height, int numPlayer1Pieces, int numPlayer2Pieces,
			int spotNumber) {
		int picWidth = (int) (width * pictureCellPercentage);
		int picHeight = (int) (height * pictureCellPercentage);
		int borderWidth = (width - picWidth) / 2;
		int borderHeight = (height - picHeight) / 2;

		int pieceWidth = picWidth / MAX_PIECES_PER_ROW;
		int pieceHeight = picHeight / MAX_ROWS_PER_SPOT;

		for (Player player : model.getPieces(new Location(spotNumber))) {
			if (player == Player.PLAYER_1) {
				g.setColor(Color.WHITE);
				// draw shape
				// set color to black
				// draw same shape but 90% of the size, inside, this will give
				// me a border
			} else {

			}
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
