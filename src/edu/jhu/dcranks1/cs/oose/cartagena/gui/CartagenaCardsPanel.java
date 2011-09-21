package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaIllegalMoveEvent;
import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModelListener;
import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaMoveEvent;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Player;
import edu.jhu.cs.oose.fall2011.cartagena.iface.SpaceType;

public class CartagenaCardsPanel extends JPanel
{
	private CartagenaModel model;
	private static final int CARD_SPACE_WIDTH = 140;
	private static final int CARD_SPACE_HEIGHT = 70;
	private Player myPlayer;
	private CartagenaMovePreparationModel movePrepModel;
	private Map<SpaceType, BufferedImage> cardPictures;
	private static String fileEnding = "trans.png";
	private static String path = "images/drwhovillain";
	private static int INITIAL_HAND_SIZE = 6;
	private static float CARD_BORDER_PERCENTAGE = 0.1f;
	private Map<SpaceType, Integer> myCards;
	SpaceType[] typeOrder;
	private Color myColor;
	
	public CartagenaCardsPanel(CartagenaModel cartagenaModel, Player player, CartagenaMovePreparationModel movePrep) throws CartagenaGuiException
	{
		this.model = cartagenaModel;
		this.movePrepModel = movePrep;
		this.myPlayer = player;
		myCards = new HashMap<SpaceType, Integer>();
		cardPictures = new HashMap<SpaceType, BufferedImage>();
		typeOrder = SpaceType.values();
		updateCards();
		if(myPlayer.equals(Player.PLAYER_1))
		{
			myColor = Color.RED;
		}
		else
		{
			myColor = Color.GREEN;
		}
		
		/*This is a little hacky, but I couldn't find any specification saying that an enum
		* is always iterated over in the same order. I would assume that would be the case,
		* but this creates an ordering of the types that will be preserved. This means that
		* if for some reason the iteration order did change, we will always display our cards
		* in the same order (e.g. always BLUE before GREEN before RED...)
		*/
		
		
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		int pictureNumber = 0;
		for (SpaceType current : SpaceType.values()) {
			try {
				BufferedImage img = ImageIO.read(classLoader.getResourceAsStream(path + pictureNumber + fileEnding));
				cardPictures.put(current, img);
				pictureNumber++;
			} catch (Exception e) {
				
				throw new CartagenaGuiException("Error reading image files");
				
			}
		}
		
		model.addListener(new CartagenaModelListener() {
			
			@Override
			public void moveMade(CartagenaMoveEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void illegalMoveRejected(CartagenaIllegalMoveEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		this.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				int numCards = CartagenaCardsPanel.this.model.getCards(myPlayer).size();
				updateCards();
				int spot = ((int) ((double) e.getY()) / numCards * getHeight());
				int index = 0;
				int cardNumber = 0;
				SpaceType selectedCardType = typeOrder[0];
				while(cardNumber < spot)
				{
					//TODO getting array out of bounds
					selectedCardType = typeOrder[index];
					cardNumber += myCards.get(typeOrder[index++]);
				}
				CartagenaCardsPanel.this.movePrepModel.selectCard(selectedCardType);
			}
				
		});
	}
	
	private void updateCards()
	{
		for(SpaceType type: typeOrder)
		{
			myCards.put(type, Collections.frequency(model.getCards(myPlayer), type));
		}
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(CARD_SPACE_WIDTH, CARD_SPACE_HEIGHT*INITIAL_HAND_SIZE);
	}
	
	public void paint(Graphics g)
	{
		updateCards();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,getWidth(), getHeight());
		int numCards = CartagenaCardsPanel.this.model.getCards(myPlayer).size();
		int cardHeight = ((int) ((double) getHeight())/ numCards);
		int cardsAlreadyDealt = 0;
		for(SpaceType type: typeOrder)
		{
			
			int numCardsThisType = myCards.get(type);
			paintCard(numCardsThisType, type, cardsAlreadyDealt*cardHeight, cardHeight, g);
			
			cardsAlreadyDealt += numCardsThisType;
		}
		
	}
	
	private void paintCard(int numCards, SpaceType type, int yTopLeft, int cardHeight, Graphics g)
	{
		Image scaledPicture = CartagenaBoardPanel.scalePicture(cardPictures.get(type), getWidth(), (int) (cardHeight*(1-CARD_BORDER_PERCENTAGE)));
		if(model.getCurrentPlayer().equals(myPlayer))
		{
			g.setColor(myColor);
		}
		else
		{
			g.setColor(myColor.darker().darker());
		}
		
		
		
		for(int i = 0; i < numCards; i++)
		{
			
			int yStart = yTopLeft + cardHeight*i + ((int) (cardHeight*CARD_BORDER_PERCENTAGE))/2;
			g.fillRect(0, yStart, getWidth(), (int) (cardHeight*(1-CARD_BORDER_PERCENTAGE)));
			
			int picWidth = scaledPicture.getWidth(null);
			int picHeight = scaledPicture.getHeight(null);
			int actualBorderWidth = (getWidth() - picWidth) / 2;
			int actualBorderHeight = (cardHeight - picHeight) / 2;
			
			g.drawImage(scaledPicture, 0 + actualBorderWidth, yTopLeft + cardHeight*i + actualBorderHeight, scaledPicture.getWidth(null), scaledPicture.getHeight(null), null);
		}
		
	}
	
	
	public void setCartagenaModel(CartagenaModel cartagenaModel)
	{
		this.model = cartagenaModel;
	}
}
