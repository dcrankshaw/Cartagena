package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import edu.jhu.cs.oose.fall2011.cartagena.iface.CartagenaModel;
import edu.jhu.cs.oose.fall2011.cartagena.iface.Player;

public class CartagenaCardsPanel extends JPanel
{
	private CartagenaModel model;
	private static final int BOARD_WIDTH = 300;
	private static final int BOARD_HEIGHT = 70;
	private Player myPlayer;
	private CartagenaMovePreparationModel movePrepModel;
	
	public CartagenaCardsPanel(CartagenaModel cartagenaModel, Player p, CartagenaMovePreparationModel movePrep)
	{
		this.model = cartagenaModel;
		this.movePrepModel = movePrep;
		this.myPlayer = p;
		
		this.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				System.out.println("Cards Clicked: (" + p.x + "," + p.y + ")");
			}
				
		});
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0,0,getWidth(), getHeight());
		
	}
	
	public void setCartagenaModel(CartagenaModel cartagenaModel)
	{
		this.model = cartagenaModel;
	}
}
