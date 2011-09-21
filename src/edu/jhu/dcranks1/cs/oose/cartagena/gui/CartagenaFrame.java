package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Player;

public class CartagenaFrame extends JFrame
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CartagenaModel model;
	private CartagenaModelFactory modelFactory;
	
	private CartagenaMovePreparationModelListener movePrepModelListener;
	private CartagenaMovePreparationModel movePrepModel;
	private CartagenaCardsPanel player1CardPanel;
	private CartagenaCardsPanel player2CardPanel;
	
	private CartagenaBoardPanel boardPanel;
	
	public CartagenaFrame(CartagenaModelFactory factory) throws CartagenaGuiException
	{
		super("Cartagena - Dr. Who Villain Edition!");
		
		this.modelFactory = factory;
		this.model = modelFactory.createModel();
		
		
		
		this.movePrepModel = new CartagenaMovePreparationModelImpl();
		//JButton newGameButton = new JButton("New Game");
		//JButton endTurnButton = new JButton("End Turn");
		
		JLabel gameStatusLabel = new JLabel();
		gameStatusLabel.setOpaque(true);
		gameStatusLabel.setBackground(Color.BLACK);
		gameStatusLabel.setForeground(Color.WHITE);
		gameStatusLabel.setVerticalAlignment(JLabel.BOTTOM);
		gameStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		
		
		try
		{
			player1CardPanel = new CartagenaCardsPanel(this.model, Player.PLAYER_1, this.movePrepModel);
			player2CardPanel = new CartagenaCardsPanel(this.model, Player.PLAYER_2, this.movePrepModel);
			
			boardPanel = new CartagenaBoardPanel(this.model, this.movePrepModel);
			JPanel gamePlayPanel = new JPanel();
			
			
			JPanel contentPane = new JPanel();
			gamePlayPanel.setLayout(new BorderLayout());
			
			contentPane.setLayout(new BorderLayout());
			gamePlayPanel.add(boardPanel, BorderLayout.CENTER);
			gamePlayPanel.add(player1CardPanel, BorderLayout.WEST);
			gamePlayPanel.add(player2CardPanel, BorderLayout.EAST);
			
			contentPane.add(gamePlayPanel, BorderLayout.CENTER);
			contentPane.add(gameStatusLabel, BorderLayout.SOUTH);
			
			this.setContentPane(contentPane);
			
			movePrepModelListener = new CartagenaMovePreparationModelListener() {
				
				@Override
				public void makeMove() {
					if(movePrepModel.getEndLocation() != null)
					{
						CartagenaFrame.this.model.movePieceBackward(movePrepModel.getStartLocation(), movePrepModel.getEndLocation());
					}
					else if(movePrepModel.getCardSelected() != null)
					{
						CartagenaFrame.this.model.movePieceForward(movePrepModel.getStartLocation(), movePrepModel.getCardSelected());
					}
				}
				
				//don't need this
				@Override
				public void newGame()
				{
					CartagenaFrame.this.model = CartagenaFrame.this.modelFactory.createModel();
					CartagenaFrame.this.boardPanel.setCartagenaModel(CartagenaFrame.this.model);
					CartagenaFrame.this.player1CardPanel.setCartagenaModel(CartagenaFrame.this.model);
					CartagenaFrame.this.player2CardPanel.setCartagenaModel(CartagenaFrame.this.model);
				}
			};
			
			this.movePrepModel.addListener(movePrepModelListener);
			
			
			this.pack();
		}
		catch (CartagenaGuiException e)
		{
			throw e;
		}
	}

}
