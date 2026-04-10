package browser;

import java.io.File;
import java.io.IOException;

import Communication_V1.BackendClient;
import Communication_V1.CommunicationHandler;
import Communication_V1.FirmwareClient;
import Communication_V1.FrontendClient;
import Communication_V1.SimulationClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws IOException {
		

		// CommunicationHandler communicationHandler = new CommunicationHandler();
		// BackendClient back = new BackendClient();
		// FrontendClient front = new FrontendClient();
		// FirmwareClient firm = new FirmwareClient();
		// SimulationClient simulation = new SimulationClient();

		// communicationHandler.runCommunication();
		// back.runBackendClient();
		// front.runFrontendClient();
		// firm.runFirmwareClient();
		// simulation.runSimulationClient();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene.fxml"));
		Parent root = loader.load();	
		//Controller controller = loader.getController();
		Scene scene = new Scene(root);	
		
		File iconFile = new File("src/browser/dronepic.png"); // Replace with the actual file path
        String iconPath = iconFile.toURI().toString();
        stage.getIcons().add(new Image(iconPath));
		System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
		stage.setTitle("Drone Mission");
		stage.setScene(scene);
		stage.show();


		
	}	

	public static void main(String[] args) {
		
		launch(args);
	}
}
