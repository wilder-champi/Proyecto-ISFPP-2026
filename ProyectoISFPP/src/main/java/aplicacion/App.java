package aplicacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/vistas/principal.fxml"));
		stage.setScene(new Scene(loader.load()));
		stage.setTitle("ISFPP");
		stage.show();
	}
}