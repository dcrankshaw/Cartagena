package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Player;

public class CartagenaFrame extends JFrame
{
	
	private CartagenaModel model;
	
	
	public CartagenaFrame(CartagenaModel _model) throws CartagenaGuiException
	{
		super("Cartagena - Dr. Who Villain Edition!");
		
		this.model = _model;
		JButton newGameButton = new JButton("New Game");
		JButton endTurnButton = new JButton("End Turn");
		
		JLabel gameStatusLabel = new JLabel();
		
		CartagenaCardsPanel player1CardPanel = new CartagenaCardsPanel(this.model, Player.PLAYER_1);
		CartagenaCardsPanel player2CardPanel = new CartagenaCardsPanel(this.model, Player.PLAYER_2);
		
		try
		{
			CartagenaBoardPanel boardPanel = new CartagenaBoardPanel(this.model);
			JPanel gamePlayPanel = new JPanel();
			
			
			JPanel contentPane = new JPanel();
			
			contentPane.setLayout(new BorderLayout());
			contentPane.add(gamePlayPanel, BorderLayout.EAST);
			contentPane.add(player1CardPanel, BorderLayout.NORTH);
			contentPane.add(player2CardPanel, BorderLayout.SOUTH);
			contentPane.add(boardPanel, BorderLayout.CENTER);
			
			gamePlayPanel.setLayout(new BorderLayout());
			gamePlayPanel.add(newGameButton, BorderLayout.WEST);
			gamePlayPanel.add(endTurnButton, BorderLayout.EAST);
			gamePlayPanel.add(gameStatusLabel, BorderLayout.CENTER);
			
			this.setContentPane(contentPane);
			
			
			this.pack();
		}
		catch (CartagenaGuiException e)
		{
			throw e;
		}
		
		
		
		
	}

}
