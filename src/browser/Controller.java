package browser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import Controls.Drone;
import Controls.DroneContainer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Controller implements Initializable{

    @FXML
    private WebView webView;

    @FXML
    private TextField textField;
    private WebEngine engine;
    private String homePage;

    private DroneContainer droneKey = new DroneContainer();
    private Drone dronePrint = new Drone();

    int liftKey = 38, landKey = 40;// to lift and land
    //int forwardKey = 87, backwardKey = 83;//W and S
    //int leftKey = 65, rightKey = 68; // a and d
    int yawLeftKey = 37, yawRightKey = 39;// arrow keys left and right for yaw
    int upKey = 61, downKey = 45; // up and down arrow for Max PWM controls

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        // File userDataDirectory = new File("Downloads");
        // engine.userDataDirectoryProperty().set(userDataDirectory);
        engine = webView.getEngine();
        //engine.userDataDirectoryProperty();

      
    

        // Set the constraints for the WebView within the AnchorPane
        AnchorPane.setTopAnchor(webView, 0.0);
        AnchorPane.setBottomAnchor(webView, 0.0);
        AnchorPane.setLeftAnchor(webView, 0.0);
        AnchorPane.setRightAnchor(webView, 0.0);

        //homePage = "www.google.com";
        homePage = "Frontend/mainscreen/mainscreen.html";//fix this
        //textField.setText(homePage);
        //engine.load("http://www.facebook.com");

        try {
            File file = new File(homePage);
            String url = file.toURI().toURL().toString();
            engine.load(url);
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        engine.locationProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Current URL: " + newValue);
            // You can perform additional actions based on the new URL //fix this
            if (newValue.contains("Frontend/DroneScreen/drone_screen.html")){// only turn on when on drone screen
                try {
                    droneKey.setKeys();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                webView.setOnKeyPressed(this::keyPressed);
                webView.setOnKeyReleased(this::keyReleased);
                
            }
            else if (newValue.contains("Frontend/SettingScreen/settings_screen.html")){// only turn on when on drone screen
                enableDownloadHandler();
                webView.setOnKeyPressed(null);
                webView.setOnKeyReleased(null);
                
            }
            else {// only turn on when on drone screen, disable the key listening
                webView.setOnKeyPressed(null);
                webView.setOnKeyReleased(null);
                disableDownloadHandler();
            }   
        });

    }  

    private void enableDownloadHandler() {
        // Load the download handler JavaScript code
        engine.executeScript(
                "var downloadHandler = function(url, filename) {" +
                        "var xhr = new XMLHttpRequest();" +
                        "xhr.responseType = 'blob';" +
                        "xhr.onload = function() {" +
                        "var a = document.createElement('a');" +
                        "a.href = window.URL.createObjectURL(xhr.response);" +
                        "a.download = filename;" +
                        "a.style.display = 'none';" +
                        "document.body.appendChild(a);" +
                        "a.click();" +
                        "document.body.removeChild(a);" +
                        "};" +
                        "xhr.open('GET', url);" +
                        "xhr.send();" +
                        "};" +
                        "window.java.downloadHandler = downloadHandler;");

        // Enable file downloads
        engine.executeScript("document.addEventListener('click', function(e) { " +
                "if (e.target.tagName === 'A' && e.target.href.startsWith('http')) {" +
                "e.preventDefault();" +
                "window.java.downloadHandler(e.target.href, e.target.download);" +
                "}" +
                "});");
    }

    private void disableDownloadHandler() {
        // Disable file downloads
        engine.executeScript("document.removeEventListener('click', function(e) {});");
    }


       
   public void keyPressed(KeyEvent e) {
       // invoked when a physical key is pressed down, uses keycode, int output.
       
       System.out.println("key code pressed:" + e.getCode());
       int key = (int) e.getCode().toString().charAt(0);
       System.out.println(key);
       String keyText = e.getCode().toString();

        switch (keyText) {
        case "UP":
            key = liftKey;
            break;
        case "DOWN":
            key = landKey;
            break;
        case "LEFT":
            key = yawLeftKey;
            break;
        case "RIGHT":
            key = yawRightKey;
            break;
        case "EQUALS":
            key = upKey;
            break;
        case "MINUS":
            key = downKey;
            break;
        default:
            // Handle other keys as needed
            break;
            
        }

            droneKey.setkeyPressed(key);
            //System.out.println("Key pressed:");
            //dronePrint.printMotorVals();   
   }

   public void keyReleased(KeyEvent e) {

    int key = (int) e.getCode().toString().charAt(0);
    String keyText = e.getCode().toString();

        switch (keyText) {
            case "UP":
                key = liftKey;
                break;
            case "DOWN":
                key = landKey;
                break;
            case "LEFT":
                key = yawLeftKey;
                break;
            case "RIGHT":
                key = yawRightKey;
                break;
            case "EQUALS":
                key = upKey;
                break;
            case "MINUS":
                key = downKey;
                break;
            default:
                // Handle other keys as needed
                break;
        }

        droneKey.setkeyReleased(key);
        //System.out.println("Key released:");
       //dronePrint.printMotorVals();
   }
    
}
