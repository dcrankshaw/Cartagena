package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaIllegalMoveEvent;
import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModelListener;
import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaMoveEvent;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Player;

public class CartagenaFrame extends JFrame {

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
	private JLabel gameStatusLabel;
	private CartagenaBoardPanel boardPanel;
	
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem newGame;
	private JMenuItem endTurn;
	
	

	public CartagenaFrame(CartagenaModelFactory factory)
			throws CartagenaGuiException {
		super("Cartagena - Dr. Who Villain Edition!");

		this.modelFactory = factory;
		this.model = modelFactory.createModel();

		this.movePrepModel = new CartagenaMovePreparationModelImpl();
		// JButton newGameButton = new JButton("New Game");
		// JButton endTurnButton = new JButton("End Turn");
		
		
		this.gameStatusLabel = new JLabel();
		gameStatusLabel.setOpaque(true);
		gameStatusLabel.setBackground(Color.BLACK);
		gameStatusLabel.setForeground(Color.WHITE);
		gameStatusLabel.setVerticalAlignment(JLabel.BOTTOM);
		gameStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

		this.model.addListener(new CartagenaModelListener() {

			@Override
			public void moveMade(CartagenaMoveEvent event) {
				if(event.isGameEnded())
				{
					//TODO something for game over
				}
				
				//TODO something needs to happen when the turn ends
				else
				{
					CartagenaFrame.this.movePrepModel.resetModel();
					repaint();
				}
			}

			@Override
			public void illegalMoveRejected(CartagenaIllegalMoveEvent event) {
				CartagenaFrame.this.gameStatusLabel.setText(event.getDescription());
				CartagenaFrame.this.movePrepModel.resetModel();
				repaint();

			}
		});

		try {
			player1CardPanel = new CartagenaCardsPanel(this.model,
					Player.PLAYER_1, this.movePrepModel);
			player2CardPanel = new CartagenaCardsPanel(this.model,
					Player.PLAYER_2, this.movePrepModel);

			boardPanel = new CartagenaBoardPanel(this.model, this.movePrepModel);
			JPanel gamePlayPanel = new JPanel();

			JPanel contentPane = new JPanel();
			gamePlayPanel.setLayout(new BorderLayout());

			menuBar = new JMenuBar();
			menu = new JMenu();
			newGame = new JMenuItem();
			endTurn = new JMenuItem();
			
			this.setJMenuBar(menuBar);
			menuBar.setLayout(new BorderLayout());
			menuBar.setOpaque(true);
			menuBar.setBackground(Color.GRAY.brighter());
			menu.setText("Options");
			menu.setBackground(Color.GRAY.brighter());
			menu.setHorizontalAlignment(SwingUtilities.CENTER);
			menuBar.add(menu, BorderLayout.WEST);
			menu.add(newGame);
			menu.add(endTurn);
			menu.setOpaque(true);
			
			
			
			newGame.setText("New Game");
			endTurn.setText("End Turn");
			newGame.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					CartagenaFrame.this.model = CartagenaFrame.this.modelFactory.createModel();
					CartagenaFrame.this.boardPanel.setCartagenaModel(CartagenaFrame.this.model);
					CartagenaFrame.this.player1CardPanel.setCartagenaModel(CartagenaFrame.this.model);
					CartagenaFrame.this.player2CardPanel.setCartagenaModel(CartagenaFrame.this.model);
					CartagenaFrame.this.movePrepModel.resetModel();
					repaint();
				}
			});
			
			endTurn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					CartagenaFrame.this.model.endTurnEarly();
					repaint();
				}
			});
			
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
					if (movePrepModel.getEndLocation() != null) {
						CartagenaFrame.this.model.movePieceBackward(
								movePrepModel.getStartLocation(), movePrepModel
										.getEndLocation());
					} else if (movePrepModel.getCardSelected() != null) {
						CartagenaFrame.this.model.movePieceForward(
								movePrepModel.getStartLocation(), movePrepModel
										.getCardSelected());
					}
				}
			};

			this.movePrepModel.addListener(movePrepModelListener);

			this.pack();
		} catch (CartagenaGuiException e) {
			throw e;
		}
	}

}
