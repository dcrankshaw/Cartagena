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

	private CartagenaModel model;
	private boolean[] selectedSpots;
	private static final int BOARD_WIDTH = 800;
	private static final int BOARD_HEIGHT = 800;
	// private static final String basePath =
	// "/home/crank/git/Cartagena/images/drwhovillain";
	private static final String basePath = "/Users/Daniel/cartagena_secondtry/images/drwhovillain";
	private static final String fileEnding = ".jpg";
	private Map<SpaceType, BufferedImage> boardPictures;
	private static final int NUM_COLUMNS = 6;
	private static final int NUM_ROWS = 7;
	private static final int NUM_SPOTS_PER_COLUMN = 6;
	private static final int MAX_PIECES_PER_ROW = 4;
	private static final int MAX_ROWS_PER_SPOT = 3;

	private CartagenaMovePreparationModel movePrepModel;

	private static final double pictureCellPercentage = 0.7;

	private static final double PATH_WIDTH_PERCENTAGE = 0.5;

	public CartagenaBoardPanel(CartagenaModel cartagenaModel,
			CartagenaMovePreparationModel movePrep)
			throws CartagenaGuiException {
		this.model = cartagenaModel;
		this.movePrepModel = movePrep;

		Arrays.fill(selectedSpots, false);

		boardPictures = new HashMap<SpaceType, BufferedImage>();

		movePrepModel.addListener(new CartagenaMovePreparationModelListener() {

			@Override
			public void spotSelected(int i) {
				Arrays.fill(selectedSpots, false);
				selectedSpots[i] = true;
				repaint();
			}

			@Override
			public void makeMove() {
				//do nothing, the frame will use this method

			}
		});

		for (SpaceType current : SpaceType.values()) {
			try {
				BufferedImage img = ImageIO.read(new File(basePath
						+ current.ordinal() + fileEnding));
				boardPictures.put(current, img);
			} catch (Exception e) {
				throw new CartagenaGuiException(e.getMessage());
			}

		}

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				final int cx = (int) (p.getX() / (double) getWidth() * NUM_ROWS);
				final int cy = (int) (p.getY() / (double) getHeight() * NUM_COLUMNS);
				if (cx != 0 && cy == 0) {
					// if you click in the panel outside of a square, it
					// deselects the currently selected square
					movePrepModel.setStartLocation(null);
				} else {
					Location selected = new Location(cy + cx
							* NUM_SPOTS_PER_COLUMN);
					if (SwingUtilities.isRightMouseButton(e)) {
						movePrepModel.setEndLocation(selected);
					} else {
						movePrepModel.setStartLocation(selected);
					}
				}

			}

		});
	}

	public Dimension getPreferredSize() {
		return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		int cellWidth = getWidth() / NUM_COLUMNS;
		int cellHeight = getHeight() / NUM_ROWS;
		for (int x = 0; x < NUM_COLUMNS; x++) {

			for (int y = 1; y < NUM_ROWS; y++) {
				// TODO: set alternating colors here
				if ((x + y) % 2 == 0)
					g.setColor(Color.BLUE.brighter().brighter());
				else
					g.setColor(Color.BLACK);

				PathPosition entrance = PathPosition.NORTH;
				PathPosition exit = PathPosition.SOUTH;
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
								* NUM_SPOTS_PER_COLUMN + y)), entrance, exit, selectedSpots[x
								* NUM_SPOTS_PER_COLUMN + y]);
			}

		}

	}

	private Image scalePicture(BufferedImage original, int picWidth,
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
		int borderWidth = (width - picWidth) / 2;
		int borderHeight = (height - picHeight) / 2;

		Image scaledPic = scalePicture(boardPictures.get(type), picWidth,
				picHeight);

		g.drawImage(scaledPic, xTopLeft + borderWidth, yTopLeft + borderHeight,
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
		g.fillRect(xTopLeft + borderWidth, yTopLeft + borderHeight, picWidth,
				picHeight);

		drawPath(g, xTopLeft, yTopLeft, width, height, pathEntrance);
		drawPath(g, xTopLeft, yTopLeft, width, height, pathExit);

		if (selected) {

		}

	}

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
		default: //don't paint anything

		}
	}
	
	public void setCartagenaModel(CartagenaModel cartagenaModel)
	{
		this.model = cartagenaModel;
	}

}
