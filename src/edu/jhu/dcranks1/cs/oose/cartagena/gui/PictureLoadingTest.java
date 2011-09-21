package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class PictureLoadingTest {

	private static final int NUM_SPOTS_PER_ROW = 6;
	
	public static void main(String[] args)
	{
		
		int x = 0;
		int y = -1;
		int pieceNumber = 1;
		int MAX_PIECES_PER_ROW = 4;
		
		for(int i = 0; i < 6; i++)
		{
			if(((pieceNumber-1) % (MAX_PIECES_PER_ROW)) == 0)
			{
				y++;
			}
			
			int xP = x + ((pieceNumber - 1) % MAX_PIECES_PER_ROW);
			System.out.println("(" + xP + ", " + y + ")");
			pieceNumber++;
		}
		
		System.out.println("\nBREAK\n");
		
		for(int i = 0; i < 6; i++)
		{
			if(((pieceNumber - 1) % (MAX_PIECES_PER_ROW)) == 0)
			{
				y++;
			}
			
			int xP = x + ((pieceNumber - 1) % MAX_PIECES_PER_ROW);
			System.out.println("(" + xP + ", " + y + ")");
			pieceNumber ++;
		}
		
		
		
	}
	
	public static int calcSpace(int x, int y)
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

}
