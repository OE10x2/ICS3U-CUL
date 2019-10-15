import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.Image;

public class IMPORT{
	HashMap<Image, Pair> picSizes = new HashMap<Image, Pair>();
	ArrayList<Image> RIdle = new ArrayList<Image>();
	ArrayList<Image> RMove = new ArrayList<Image>();
	ArrayList<Image> RReload = new ArrayList<Image>();
	ArrayList<Image> RShoot = new ArrayList<Image>();
	ArrayList<Image> HIdle = new ArrayList<Image>();
	ArrayList<Image> HMove = new ArrayList<Image>();
	ArrayList<Image> HReload = new ArrayList<Image>();
	ArrayList<Image> HShoot = new ArrayList<Image>();
	ArrayList<Image> ZMove = new ArrayList<Image>();
	ArrayList<Image> ZAttack = new ArrayList<Image>();
	public void run(){
		/**********************************************************************************************/
		//Import pictures
		for (int i = 0; i < new File("src\\rifle\\idle").list().length; i++){
			Image image = new Image("rifle\\idle\\survivor-idle_rifle_" + Integer.toString(i) + ".png");
			RIdle.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
		for (int i = 0; i < new File("src\\rifle\\move").list().length; i++){
			Image image = new Image("rifle\\move\\survivor-move_rifle_" + Integer.toString(i) + ".png");
			RMove.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
		for (int i = 0; i < new File("src\\rifle\\reload").list().length; i++){
			Image image = new Image("rifle\\reload\\survivor-reload_rifle_" + Integer.toString(i) + ".png");
			RReload.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
		for (int i = 0; i < new File("src\\rifle\\shoot").list().length; i++){
			Image image = new Image("rifle\\shoot\\survivor-shoot_rifle_" + Integer.toString(i) + ".png");
			RShoot.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
		for (int i = 0; i < new File("src\\handgun\\idle").list().length; i++){
			Image image = new Image("handgun\\idle\\survivor-idle_handgun_" + Integer.toString(i) + ".png");
			HIdle.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
		for (int i = 0; i < new File("src\\handgun\\move").list().length; i++){
			Image image = new Image("handgun\\move\\survivor-move_handgun_" + Integer.toString(i) + ".png");
			HMove.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
		for (int i = 0; i < new File("src\\handgun\\reload").list().length; i++){
			Image image = new Image("handgun\\reload\\survivor-reload_handgun_" + Integer.toString(i) + ".png");
			HReload.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
		for (int i = 0; i < new File("src\\handgun\\shoot").list().length; i++){
			Image image = new Image("handgun\\shoot\\survivor-shoot_handgun_" + Integer.toString(i) + ".png");
			HShoot.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
		for (int i = 0; i < new File("src\\zombie\\attack").list().length; i++){
			Image image = new Image("zombie\\attack\\attack" + Integer.toString(i) + ".png");
			ZAttack.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
		for (int i = 0; i < new File("src\\zombie\\move").list().length; i++){
			Image image = new Image("zombie\\move\\move" + Integer.toString(i) + ".png");
			ZMove.add(image);
			picSizes.put(image, new Pair(image.getWidth(), image.getHeight()));
		}
	}
}