package gui;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

import game.Tile;

public class ImageLoader {
	
	public static String path = "C:\\Users\\Usuario\\Documents\\Projects\\Hexagon\\images\\";

	public static Image unitImage;
	
	public static void loadImages(){
		try {
			Tile.grass.image = ImageIO.read(new File(path + "grass.png"));
			Tile.water.image = ImageIO.read(new File(path + "water.png"));
			Tile.fertileGround.image = ImageIO.read(new File(path + "fertile_ground.png"));
			Tile.stone.image = ImageIO.read(new File(path + "stone.png"));
			Tile.stoneDepleted.image = ImageIO.read(new File(path + "stoneDepleted.png"));
			unitImage = ImageIO.read(new File(path + "unit.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
