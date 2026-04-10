package Controls;

import Communication_V1.BackendClient;
import Communication_V1.CommunicationHandler;
import Communication_V1.FirmwareClient;
import Communication_V1.FrontendClient;
import Communication_V1.SimulationClient;

public class Drone {
  private int xPos = 0; // left and right
  private int yPos = 0; // forward and backward
  private int zPos = 0; // up and down

  CommunicationHandler communicationHandler = new CommunicationHandler();
  BackendClient back = new BackendClient();
		FrontendClient front = new FrontendClient();
		FirmwareClient firm = new FirmwareClient();
		SimulationClient simulation = new SimulationClient();
    

  private Motor frontLeft = new Motor();
  private Motor frontRight = new Motor();
  private Motor backLeft = new Motor();
  private Motor backRight = new Motor();

  // Forward, Backward, Up, Down, Right, Left
  //private String currState = "";

  private String[] ledColors = {"red", "yellow", "green"};
  private String ledColor = ledColors[0];
  
  public void printMotorVals() {
    System.out.println(frontLeft.PWM + "," + frontRight.PWM + "," + backLeft.PWM + "," + backRight.PWM);
   //System.out.println(frontRight.PWM);
    //System.out.println(backLeft.PWM);
    //System.out.println(backRight.PWM);
  }

  // public void setCurrState(String currState) {
  //   this.currState = currState;
  // }

  public void updateXPos(int increaseAmount) {
    this.xPos += increaseAmount;
    System.out.println("xPos updated: " + xPos);
  }

  public void updateYPos(int increaseAmount) {
    this.yPos += increaseAmount;
    System.out.println("yPos updated: " + yPos);
  }

  public void updateZPos(int increaseAmount) {
    this.zPos += increaseAmount;
    System.out.println("zPos updated: " + zPos);
  }

  public void setLedColor(int led){
    this.ledColor = ledColors[led];
    System.out.println("led changed to: " + ledColor);
  }

  public void setMotors(int frontLeftVal, int frontRightVal, int backLeftVal, int backRightVal) {
    this.frontLeft.setSpeed(frontLeftVal);
    this.frontRight.setSpeed(frontRightVal);
    this.backLeft.setSpeed(backLeftVal);
    this.backRight.setSpeed(backRightVal);
    String m1 =String.valueOf(frontLeftVal);
    String m2 =String.valueOf(frontRightVal);
    String m3 =String.valueOf(backRightVal);
    String m4 =String.valueOf(backLeftVal);
    String Drone = "simulation_Drone_" + m1 + "_"+ m2 + "_"+ m3 + "_"+ m4 + "_"+ "125_125_125";
    System.out.println(Drone);
    communicationHandler.sendToClients(Drone);
  }

  public void goUp() {
    this.setMotors(0, 0, 0, 0);
    this.updateZPos(1);
  }
  public void goDown() {
    this.setMotors(0, 0, 0, 0);
    this.updateZPos(-1);
  }
  public void goForward() {
    this.setMotors(0, 0, 0, 0);
    this.updateYPos(1);
  }
  public void goBackwards() {
    this.setMotors(0, 0, 0, 0);
    this.updateYPos(-1);
  }
  public void goRight() {
    this.setMotors(0, 0, 0, 0);
    this.updateXPos(1);
  }
  public void goLeft() {
    this.setMotors(0, 0, 0, 0);
    this.updateXPos(-1);
  }
}
