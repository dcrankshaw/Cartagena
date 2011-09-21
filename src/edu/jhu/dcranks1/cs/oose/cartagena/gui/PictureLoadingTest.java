package edu.jhu.dcranks1.cs.oose.cartagena.gui;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class PictureLoadingTest {

	private static final int NUM_SPOTS_PER_ROW = 6;
	
	public static void main(String[] args)
	{
		for(int spaceNumber = 0; spaceNumber  < 36; spaceNumber++)
		{
			int y = (spaceNumber) / NUM_SPOTS_PER_ROW;
			int x = 0;
			if(y % 2 == 0)
			{
				x = (NUM_SPOTS_PER_ROW - (spaceNumber % NUM_SPOTS_PER_ROW)) - 1;
			}
			else
			{
				x = (spaceNumber) % NUM_SPOTS_PER_ROW;
			}
			
			System.out.println("S: " + (spaceNumber + 1) + "\tx: " + x + "\ty: " + y + "\t\tCalcS: " + calcSpace(x, y));
			
			
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
