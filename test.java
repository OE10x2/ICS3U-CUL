import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
public class test extends Application{
	Stage window = new Stage();
	Group root = new Group();
	Scene scene = new Scene(root, 800, 600);
	public static void main(String[] args) { launch(args); }
	@Override public void start(Stage primaryStage) throws Exception{
		window = primaryStage;
		window.setScene(scene);
		Image image = new Image("rifle\\idle\\survivor-idle_rifle_0.png");
		ImageView IV = new ImageView(image);
		IV.setTranslateX(100);
		IV.setTranslateY(200);
		double store1 = image.getWidth(), store2 = image.getHeight();
		System.out.println(store1 + " " + store2);
		root.getChildren().add(IV);
		Circle circle = new Circle(IV.getTranslateX() + image.getWidth() - 16, IV.getTranslateY() + 151.5, 4);
		circle.setFill(Color.RED);
		root.getChildren().add(circle);
		//Line testing = new Line();
		//problem: rotate a line about angle?
		window.show();
	}
}