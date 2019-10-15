import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{
	public static void main(String[] args){
		System.out.println("LOADING...");
		launch(args);
	}
	int spacing = 25, defFont = 30;
	Stage window = new Stage();
	Group root = new Group(), finalRoot = new Group();
	VBox choices = new VBox(spacing), organized = new VBox(spacing);
	double mapSizeX = 5000, mapSizeY = 4000;
	double windowX = 800, windowY = 600;
	double userX = 400, userY = 300;
	double smallWindowX = 300, smallWindowY = 200;
	double miniX = 125, miniY = 100, miniWidth = 3, fixedSpace = 15;
	double endAimX = userX + windowX, endAimY = userY;
	Scene scene0 = new Scene(choices, smallWindowX, smallWindowY);
	Scene scene1 = new Scene(root, windowX, windowY);
	Scene scene2 = new Scene(finalRoot, smallWindowX, smallWindowY);
	int tempPic = 0, length = 0;
	int userPace = 5;
	int FPS = 60, nano = 1000000000;
	int userHealth = 100, zombiesKilled = 0, zombieHealth = 200;
	int rifleMax = 200, rifleOnce = 20;
	int pistolMax = 70, pistolOnce = 7;
	int defRifle = 20, defPistol = 7;
	double rotate = 0, circleWidth = 4;
	int cntTime = 0, finalTime = 0;
	boolean reloadCheck1 = false, shootCheck1 = false, stopReload1 = true;
	boolean reloadCheck2 = false, shootCheck2 = false, stopReload2 = true;
	boolean shoot = false, lose = false;
	boolean[] movement = new boolean[7];
	Image user = null; Circle topC = null;
	ImageView disUser = null, showZ = null;
	ComboBox modes = new ComboBox(), level = new ComboBox();
	Object getModes = null, getLevel = null; 
	ArrayList<Image> RIdle = null, RMove = null, RReload = null, RShoot = null;
	ArrayList<Image> HIdle = null, HMove = null, HReload = null, HShoot = null;
	ArrayList<Image> ZMove = null, ZAttack = null;
	ArrayList<Zomb> zombies = new ArrayList<Zomb>();
	ArrayList<Integer> ZCnt = new ArrayList<Integer>();
	ArrayList<Integer> ZLen = new ArrayList<Integer>();
	ArrayList<Integer> ZHealth = new ArrayList<Integer>();
	ArrayList<Boolean> stopMoving = new ArrayList<Boolean>();
	HashMap<Image, Pair> picSizes = new HashMap<Image, Pair>();
	RandomAccessFile addScore = null;
	@Override public void start(Stage primaryStage) throws Exception{
		window = primaryStage;
		window.setScene(scene0);
		/**********************************************************************************************/
		IMPORT files = new IMPORT();
		files.run();
		RIdle = files.RIdle; RMove = files.RMove;
		RReload = files.RReload; RShoot = files.RShoot;
		HIdle = files.HIdle; HMove = files.HMove;
		HReload = files.HReload; HShoot = files.HShoot;
		ZMove = files.ZMove; ZAttack = files.ZAttack;
		picSizes = files.picSizes;
		/**********************************************************************************************/
		//Small window settings
		Text text0 = new Text("Select the mode and the map to start");
		text0.setStyle("-fx-font: 15px\"Segoe UI\";");
		modes.getItems().add("Survival");
		modes.setValue("Survival");
		modes.setStyle("-fx-font: 15px\"Segoe UI\";");
		level.getItems().addAll("Easy", "Normal", "Hard");
		level.setValue("Normal");
		level.setStyle("-fx-font: 15px\"Segoe UI\";");
		Button cont = new Button("Launch");
		cont.setFont(new Font("Segoe UI", defFont));
		cont.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent event){
				if (level.getValue() != null && modes.getValue() != null){
					root.getChildren().clear();
					update(modes.getValue(), level.getValue());
					window.setScene(scene1);
					window.setMaximized(true);
				}
			}
		});
		HBox buttons0 = new HBox(spacing);
		buttons0.getChildren().addAll(modes, level);
		choices.getChildren().addAll(text0, buttons0, cont);
		/**********************************************************************************************/
		Text loseText = new Text("YOU LOSE!");
		loseText.setFont(new Font("Calibri", defFont));
		TextField typeScore = new TextField("Type your name here"); 
		typeScore.setPrefWidth(200);
		Button saveScore = new Button("Save Score");
		saveScore.setFont(new Font("Calibri", defFont));
		Text success = new Text("Successfully Saved!");
		success.setFont(new Font("Calibri", defFont));
		saveScore.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent event){
				organized.getChildren().add(success);
					try{
						if (level.getValue().equals("Easy")) addScore = new RandomAccessFile("HighScoreE.txt", "rw");
						if (level.getValue().equals("Normal")) addScore = new RandomAccessFile("HighScoreN.txt", "rw");
						if (level.getValue().equals("Hard")) addScore = new RandomAccessFile("HighScoreH.txt", "rw");
					}catch (FileNotFoundException e){};
				try{
					addScore.seek(addScore.length());
					String addIn = "";
					if (typeScore.getText().equals("Type your name here")) addIn += "User ";
					else addIn += typeScore.getText() + " ";
					addIn += finalTime + " " +  zombiesKilled + "\n";
					addScore.writeBytes(addIn);
				}catch (IOException e){}
			}
		});
		//create class to sort
		organized.getChildren().addAll(loseText, typeScore, saveScore);
		finalRoot.getChildren().add(organized);
		/**********************************************************************************************/
		window.show();
	}
	public void update(Object mode, Object map){
		movement[4] = true;
	  //Up, Down, Left, Right, Rifle, Pistol, Reload, Shoot
		scene1.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override public void handle(KeyEvent event){
				KeyCode keyPressed = event.getCode();
				if (keyPressed == KeyCode.W) movement[0] = true;
				else if (keyPressed == KeyCode.S) movement[1] = true;
				else if (keyPressed == KeyCode.A) movement[2] = true;
				else if (keyPressed == KeyCode.D) movement[3] = true;
				else if (keyPressed == KeyCode.DIGIT1){
					movement[4] = true;
					movement[5] = false;
					reloadCheck1 = true;
					stopReload1 = true;
				}
				else if (keyPressed == KeyCode.DIGIT2){
					movement[4] = false;
					movement[5] = true;
					reloadCheck2 = true;
					stopReload2 = true;
				}
				else if (keyPressed == KeyCode.R){
					movement[6] = true;
					if (movement[4]) {
						reloadCheck1 = true;
						stopReload1 = false;
						reloadCheck2 = false;
						stopReload2 = true;
					}else if (movement[5]) {
						reloadCheck2 = true;
						stopReload2 = false;
						reloadCheck1 = false;
						stopReload1 = true;
					}	
				}
			}
		});
		scene1.setOnKeyReleased(new EventHandler<KeyEvent>(){
			@Override public void handle(KeyEvent event){
				KeyCode keyReleased = event.getCode();
				if (keyReleased == KeyCode.W) movement[0] = false;
				else if (keyReleased == KeyCode.S) movement[1] = false;
				else if (keyReleased == KeyCode.A) movement[2] = false;
				else if (keyReleased == KeyCode.D) movement[3] = false;
			}
		});
		scene1.setOnMouseMoved(new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent event){
				rotate = Math.toDegrees(Math.atan2(event.getY() - windowY / 2, event.getX() - windowX / 2));
			}
		});
		scene1.setOnMousePressed(new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent event){
				rotate = Math.toDegrees(Math.atan2(event.getY() - windowY / 2, event.getX() - windowX / 2));
				shoot = true;
				shootCheck1 = true;
				stopReload1 = false;
				shootCheck2 = true;
				stopReload2 = false;
			}
		});
		scene1.setOnMouseReleased(new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent event){
				shoot = false;
			}
		});
		Duration fixedTime = null;
		if (map.equals("Easy")) fixedTime = Duration.seconds(5);
		if (map.equals("Normal")) fixedTime = Duration.seconds(4);
		if (map.equals("Hard")) fixedTime = Duration.seconds(2);
		Timeline t = new Timeline(new KeyFrame(fixedTime, new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent event){
				if (!lose){
					//Add new zombie
					int newX = (int)(Math.random() * 5000);
					int newY = (int)(Math.random() * 4000);
					zombies.add(new Zomb(newX, newY, 100));
					ZCnt.add(0); ZLen.add(0);
					ZHealth.add(zombieHealth);
					stopMoving.add(false);
				}
			}
		}));
		t.setCycleCount(Timeline.INDEFINITE);
		t.play();
		Timeline time = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event){
				if (!lose) finalTime++;
			}
		}));
		time.setCycleCount(Timeline.INDEFINITE);
		time.play();
		AnimationTimer timer = new AnimationTimer(){
			long cur = 0;
			@Override public void handle(long time){
				if (time - cur > nano / FPS){
					//60 FPS
					root.getChildren().clear();
					maps();
					updateStats();
					User();
					Zombies();
					aim();
					cur = time;
				}
			}
		};
		timer.start();
	}
	public void updateStats(){
		if (userHealth <= 0) {
			window.setScene(scene2);
			lose = true;
		}
		Text display1 = new Text(Integer.toString(finalTime));
		Text display2 = new Text(Integer.toString(userHealth));
		Text display3 = new Text(Integer.toString(zombiesKilled));
		display1.setFont(new Font("Calibri", defFont));
		display2.setFont(new Font("Calibri", defFont));
		display3.setFont(new Font("Calibri", defFont));
		display1.setTranslateX(200);
		display1.setTranslateY(150);
		display2.setTranslateX(400);
		display2.setTranslateY(150);
		display3.setTranslateX(600);
		display3.setTranslateY(150);
		//To get rid of magic numbers: HBox
		root.getChildren().addAll(display1, display2, display3);
	}
	public void aim(){
		  Line shootAim = new Line();
			shootAim.setStartX(windowX / 2);
			shootAim.setStartY(windowY / 2);
			double q = Math.atan2(endAimY - windowY / 2, endAimX - windowX / 2);
			double Trotate = Math.toRadians(rotate);
			endAimX = Math.cos(Trotate - q) * (endAimX - windowX / 2) - Math.sin(Trotate - q) * (endAimY - windowY / 2) + windowX / 2;
			endAimY = Math.sin(Trotate - q) * (endAimX - windowX / 2) + Math.cos(Trotate - q) * (endAimY - windowY / 2) + windowY / 2;
			shootAim.setEndX(endAimX);
			shootAim.setEndY(endAimY);
			root.getChildren().add(shootAim);
	}
	public void maps(){
		Rectangle borders = null;
		borders = new Rectangle(windowX / 2 - userX, windowY / 2 - userY, mapSizeX, mapSizeY);
		borders.setFill(Color.WHITE);
		borders.setStroke(Color.BLACK);
		borders.setStrokeWidth(5);
		root.getChildren().addAll(borders);
		Rectangle bounds = new Rectangle(15, 15, 125, 100);
		bounds.setFill(Color.WHITE);
		bounds.setStroke(Color.YELLOW);
		bounds.setStrokeWidth(3);
		double percentageX = miniX * userX / mapSizeX + 15;
		double percentageY = miniY * userY / mapSizeY + 15;
		Circle userCurrent = new Circle(percentageX, percentageY, 4);
		userCurrent.setFill(Color.GREEN);
		root.getChildren().addAll(bounds, userCurrent);
	}
	boolean stop1 = false, stop2 = false;
	public void User(){
		/**********************************************************************************************/
		//Displaying Character
		if (!movement[6] && movement[4] && shoot){
			if (!stop1 && rifleMax > 0 && rifleOnce > 0){
				length = RShoot.size();
				if (shootCheck1){
					tempPic = 0;
					shootCheck1 = false;
				}
				user = RShoot.get(tempPic);
			}else{
				length = RIdle.size();
				if (shootCheck1){
					tempPic = 0;
					shootCheck1 = false;
				}
				user = RIdle.get(tempPic);
			}
		}
		else if (!movement[6] && movement[5] && shoot){
			if (!stop1 && pistolMax > 0 && pistolOnce > 0){
				length = HShoot.size();
				if (shootCheck2){
					tempPic = 0;
					shootCheck2 = false;
				}
				user = HShoot.get(tempPic);
			}else{
				length = HIdle.size();
				if (shootCheck2){
					tempPic = 0;
					shootCheck2 = false;
				}
				user = HIdle.get(tempPic);
			}
		}
		else if (!movement[6] && movement[4] && (movement[0] || movement[1] || movement[2] || movement[3])){
			length = RMove.size();
			user = RMove.get(tempPic);
		}
		else if (!movement[6] && movement[4]){
			length = RIdle.size();
			user = RIdle.get(tempPic);
		}
		else if (!movement[6] && movement[5] && (movement[0] || movement[1] || movement[2] || movement[3])){
			length = HMove.size();
			user = HMove.get(tempPic);
		}
		else if (!movement[6] && movement[5]){
			length = HIdle.size();
			user = HIdle.get(tempPic);
		}
		else if (movement[6] && movement[4]){
			length = RReload.size();
			if (reloadCheck1){
				tempPic = 0;
				reloadCheck1 = false;
			}
			else if (stopReload1) tempPic = 0;
			user = RReload.get(tempPic);
		}
		else if (movement[6] && movement[5]){
			length = HReload.size();
			if (reloadCheck2){
				tempPic = 0;
				reloadCheck2 = false;
			}
			else if (stopReload2) tempPic = 0;
			user = HReload.get(tempPic);
		}
		disUser = new ImageView(user);
		if (tempPic < length-1)	tempPic++;
		else{
			tempPic = 0;
			if (movement[4] && shoot){
				System.out.println("YES " + rifleOnce + " " + rifleMax);
				if (rifleOnce <= 0) stop1 = true;
				else{
					stop1 = false;
					rifleOnce--;
				}
			}
			else if (movement[4] && movement[6]){
				if (rifleOnce + rifleMax <= defRifle){
					rifleOnce += rifleMax;
					rifleMax = 0;
				}else{
					rifleMax -= (defRifle - rifleOnce);
					rifleOnce = defRifle;
				}
				if (rifleMax <= 0) rifleMax = 0;
				if (rifleOnce <= 0) rifleOnce = 0;
			}
			else if (movement[5] && shoot){
				if (pistolOnce <= 0) stop2 = true;
				else{
					stop2 = false;
					pistolOnce--;
				}
			}
			else if (movement[5] && movement[6]){
				if (pistolOnce + pistolMax <= defPistol){
					pistolOnce += pistolMax;
					pistolMax = 0;
				}else{
					pistolMax -= (defPistol - pistolOnce);
					pistolOnce = defPistol;
				}
				if (pistolMax <= 0) pistolMax = 0;
				if (pistolOnce <= 0) pistolOnce = 0;
			}
			movement[6] &= false;
		}
		/********************************************************************************************/
		if (movement[0] && movement[2]){
			double fixedX = userX - picSizes.get(user).first / 2;
			double fixedY = userY - picSizes.get(user).second / 2;
			if (fixedX - Math.sqrt(userPace) >= 0) userX -= Math.sqrt(userPace);
			if (fixedY - Math.sqrt(userPace) >= 0) userY -= Math.sqrt(userPace);
		}
		else if (movement[0] && movement[3]){
			double fixedX = userX + picSizes.get(user).first / 2;
			double fixedY = userY - picSizes.get(user).second / 2;
			if (fixedX + Math.sqrt(userPace) <= mapSizeX) userX += Math.sqrt(userPace);
			if (fixedY - Math.sqrt(userPace) >= 0) userY -= Math.sqrt(userPace);
		}
		else if (movement[1] && movement[2]){
			double fixedX = userX - picSizes.get(user).first / 2;
			double fixedY = userY + picSizes.get(user).second / 2;
			if (fixedX - Math.sqrt(userPace) >= 0) userX -= Math.sqrt(userPace);
			if (fixedY + Math.sqrt(userPace) <= mapSizeY) userY += Math.sqrt(userPace);
		}
		else if (movement[1] && movement[3]){
			double fixedX = userX + picSizes.get(user).first / 2;
			double fixedY = userY + picSizes.get(user).second / 2;
			if (fixedX + Math.sqrt(userPace) <= mapSizeX) userX += Math.sqrt(userPace);
			if (fixedY + Math.sqrt(userPace) <= mapSizeY) userY += Math.sqrt(userPace);
		}
		else if (movement[0]){
			double fixedY = userY - picSizes.get(user).second / 2;
			if (fixedY - userPace >= 0) userY -= userPace;
		}
		else if (movement[1]){
			double fixedY = userY + picSizes.get(user).second / 2;
			if (fixedY + userPace <= mapSizeY) userY += userPace;
		}
		else if (movement[2]){
			double fixedX = userX - picSizes.get(user).first / 2;
			if (fixedX - userPace >= 0) userX -= userPace;
		}
		else if (movement[3]){
			double fixedX = userX + picSizes.get(user).first / 2;
			if (fixedX + userPace <= mapSizeX) userX += userPace;
		}
		disUser.setTranslateX(windowX / 2 - picSizes.get(user).first / 2);
		disUser.setTranslateY(windowY / 2 - picSizes.get(user).second / 2);
		disUser.setRotate(rotate);
		/********************************************************************************************/
		Text bullets = null;
		if (movement[4]) bullets = new Text(rifleOnce + "/" + rifleMax);
		else if (movement[5]) bullets = new Text(pistolOnce + "/" + pistolMax);
		bullets.setTranslateX(300);
		bullets.setTranslateY(100);
		bullets.setFont(new Font("Calibri", 50));
		Text reloadWarning = new Text("RELOAD");
		reloadWarning.setFont(new Font("Calibri", 50));
		reloadWarning.setTranslateX(500);
		reloadWarning.setTranslateY(100);
		root.getChildren().addAll(disUser, bullets);
		if (movement[4] && rifleOnce == 0) root.getChildren().add(reloadWarning);
		else if (movement[5] && pistolOnce == 0) root.getChildren().add(reloadWarning);
		/**********************************************************************************************/
	}
	public void Zombies(){
		//calculate slope & equation
		//NOTE: the y-values must be flipped to match the proper coordinate system
		double x = endAimX - windowX / 2;
		double y = windowY / 2 - endAimY;
		double slope = y/x;
		double b = -(windowY / 2) - windowX / 2 * slope;
		/********************************************************************************************/
		//If time equals 5 seconds: add new zombie at random location outside of user circle
		for (int i = 0; i < zombies.size(); i++){
			Zomb z = zombies.get(i);
			Image tempZ = ZMove.get(ZCnt.get(i));
			//See if zombie is attacked
			double zombieX = z.x + picSizes.get(tempZ).first / 2;
			double zombieY = z.y + picSizes.get(tempZ).second / 2;
			double tempS = Math.abs(slope * zombieX + zombieY + b) / Math.sqrt(slope*slope + 1);
			if (tempS <= 50 && shoot){
				//if 50 around the center, then the zombie is hit
				if (movement[4] && rifleOnce > 0) ZHealth.set(i, ZHealth.get(i) - 5);
				else if (movement[5] && pistolOnce > 0) ZHealth.set(i, ZHealth.get(i) - 4);
				//rifle does more damage, whereas pistol only does 4 damage
			}
			if (ZHealth.get(i) <= 0){
				zombies.remove(i);
				ZHealth.remove(i);
				ZLen.remove(i);
				ZCnt.remove(i);
				zombiesKilled++;
				continue;
			}
			/********************************************************************************************/
			//Now calculate distance to user; if closer than 100, attack
			double tempVar1 = (zombieX - windowX / 2) * (zombieX - windowX / 2);
			double tempVar2 = (zombieY - windowY / 2) * (zombieY - windowY / 2);
			if (Math.sqrt(tempVar1 + tempVar2) <= 100){
				//attack
				ZLen.set(i, ZAttack.size()-1); //max index
				if (ZCnt.get(i)+1 > ZLen.get(i)){
					ZCnt.set(i, 0);
					if (level.getValue().equals("Easy")) userHealth -= 2;
					else if (level.getValue().equals("Normal")) userHealth -= 4;
					else if (level.getValue().equals("Hard")) userHealth -= 5;
				}
				else ZCnt.set(i, ZCnt.get(i)+1);
				tempZ = ZAttack.get(ZCnt.get(i));
				stopMoving.set(i, true);
			}else{
				//move
				ZLen.set(i, ZMove.size()-1); //max index
				if (ZCnt.get(i)+1 > ZLen.get(i)) ZCnt.set(i, 0);
				else ZCnt.set(i, ZCnt.get(i)+1);
				stopMoving.set(i, false);
			}
			showZ = new ImageView(tempZ);
			Circle wtf = new Circle(windowX / 2, windowY / 2, circleWidth);
			wtf.setFill(Color.GREEN);
			Circle zombieC = new Circle(zombieX, zombieY, circleWidth);
			zombieC.setFill(Color.RED);
			/********************************************************************************************/
		  //rotate zombie; find user location first; traveled distance 1
			double delta = Math.atan2(Math.abs(zombieY - windowY / 2), Math.abs(zombieX - windowX / 2));
			double translateX = 3 * Math.cos(delta);
			double translateY = 3 * Math.sin(delta);
			if (!stopMoving.get(i)){
				if (zombieX > windowX / 2 && zombieY > windowY / 2) {
					z.x -= translateX; z.y -= translateY;
				}else if (zombieX > windowX / 2 && zombieY < windowY / 2) {
					z.x -= translateX; z.y += translateY;
				}else if (zombieX < windowX / 2 && zombieY > windowY / 2) {
					z.x += translateX; z.y -= translateY;
				}else if (zombieX < windowX / 2 && zombieY < windowY / 2) {
					z.x += translateX; z.y += translateX;
				}
				else if (zombieX == windowX / 2 && zombieY > windowY / 2) z.y -= translateY;
				else if (zombieX == windowX / 2 && zombieY < windowY / 2) z.y += translateY;
				else if (zombieY == windowY / 2 && zombieX > windowX / 2) z.x -= translateX;
				else if (zombieY == windowY / 2 && zombieX < windowX / 2) z.x += translateX;
				//According to where the zombie is in the user's perspective, calculate new coordinates
			}
			/********************************************************************************************/
			//fix zombie location according to user movement
			double fixedX = userX - picSizes.get(user).first / 2;
			double fixedY = userY - picSizes.get(user).second / 2;
			if (movement[0] && movement[2]){
				if (fixedX - Math.sqrt(userPace) >= 0) z.x += Math.sqrt(userPace);
				if (fixedY - Math.sqrt(userPace) >= 0) z.y += Math.sqrt(userPace);
			}
			else if (movement[0] && movement[3]){
				if (fixedX + Math.sqrt(userPace) <= mapSizeX) z.x -= Math.sqrt(userPace);
				if (fixedY - Math.sqrt(userPace) >= 0) z.y += Math.sqrt(userPace);
			}
			else if (movement[1] && movement[2]){
				if (fixedX - Math.sqrt(userPace) >= 0) z.x += Math.sqrt(userPace);
				if (fixedY + Math.sqrt(userPace) <= mapSizeY) z.y -= Math.sqrt(userPace);
			}
			else if (movement[1] && movement[3]){
				if (fixedX + Math.sqrt(userPace) <= mapSizeX) z.x -= Math.sqrt(userPace);
				if (fixedY + Math.sqrt(userPace) <= mapSizeY) z.y -= Math.sqrt(userPace);
			}
			else if (movement[0] && fixedY - userPace >= 0) z.y += userPace;
			else if (movement[1] && fixedY + userPace <= mapSizeY) z.y -= userPace;
			else if (movement[2] && fixedX - userPace >= 0) z.x += userPace;
			else if (movement[3] && fixedX + userPace <= mapSizeX) z.x -= userPace;
			//Cover all cases; calculate the distance traveled by the zombie
			//Should be always closer to the user
			
			double displayZX = z.x + picSizes.get(tempZ).first / 2;
			double displayZY = z.y + picSizes.get(tempZ).second / 2;
			//First, get the zombie's center
			double lastX = (displayZX - windowX / 2 + userX) / mapSizeX * miniX + fixedSpace;
			double lastY = (displayZY - windowY / 2 + userY) / mapSizeY * miniY + fixedSpace;
			//Now calculate the point in the minimap with respect to the user's location
			Circle displayZ = new Circle(lastX, lastY, circleWidth);
			displayZ.setFill(Color.RED);
			root.getChildren().add(displayZ);
			showZ.setRotate(180 + Math.toDegrees(Math.atan2(zombieY - windowY / 2, zombieX - windowX / 2)));
			//Rotate the zombie so that it's always facing the user
			showZ.setTranslateX(z.x);
			showZ.setTranslateY(z.y);
			root.getChildren().addAll(showZ, wtf, zombieC);
			/********************************************************************************************/
			Rectangle health = new Rectangle();
			health.setWidth(200);
			health.setHeight(fixedSpace);
			health.setTranslateX(z.x);
			health.setTranslateY(z.y - 10);
			//Display the health bar 10 units above the zombie
			health.setStroke(Color.BLACK);
			Rectangle partial = new Rectangle();
			partial.setHeight(fixedSpace);
			partial.setWidth(ZHealth.get(i));
			partial.setTranslateX(z.x);
			partial.setTranslateY(z.y - 10);
			//Break down the health bar to the 100% health and the health that's left
			//Thus 2 rectangles.
			partial.setFill(Color.RED);
			root.getChildren().addAll(health, partial);
		}
	}
}