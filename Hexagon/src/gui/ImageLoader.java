package gui;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

import game.Tile;

public class ImageLoader {
	
	public static String path = "C:\\Users\\Usuario\\Documents\\Projects\\Hexagon\\images\\";

	public static Image load(String name) {
		try {
			return ImageIO.read(new File(path + name +".png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
