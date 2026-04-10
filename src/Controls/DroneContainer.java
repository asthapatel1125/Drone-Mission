package Controls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Communication_V1.CommunicationHandler;
import browser.Main;

public class DroneContainer{

    private Drone drone = new Drone();
    
   
   // add lift and decent code.
   //All the keys
   int liftKey = 38, landKey = 40;// to lift and land
   int forwardKey = 87, backwardKey = 83;//W and S
   int leftKey = 65, rightKey = 68; // a and d
   int yawLeftKey = 37, yawRightKey = 39;// arrow keys left and right for yaw
   int yawFactor = 5;// how much to decrease or increase by
   int defaultPWM = 127; // motor is off
   int m1PWM = 127;
   int m2PWM = 127;
   int m3PWM = 127;
   int m4PWM = 127;
   int m1 = 127, m2 = 127, m3 = 127, m4 = 127;// for memory of last rpm when u release key from changing direction
   int maxPWM = 150; // default maximum PWM for all motors (150 clockwise and 104 for CC)
   int maxCCPWM = defaultPWM - (maxPWM - defaultPWM);
   int upKey = 61, downKey = 45; // up and down arrow for Max PWM controls
   int offset = 135, ccoffset = 119;// how much to decrease when changing direction
   int takeOff = 79, landing = 80, emergencyStop = 73;

//    //All the keys
//     int liftKey , landKey;// to lift and land
//     int forwardKey , backwardKey;//W and S
//     int leftKey , rightKey ; // a and d
//     int yawLeftKey , yawRightKey ;// arrow keys left and right for yaw
   
    public void setKeys () throws IOException {
	    Process process = Runtime.getRuntime().exec("java -jar cloud.jar M 1000000001");
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int counter = 0;
        String[] s = new String[8];
        while ((line = reader.readLine()) != null) {
            counter ++;
            if (counter == 10) {
                s = line.split("_");
            }
        }
        forwardKey = Integer.parseInt(s[0]);
        backwardKey = Integer.parseInt(s[1]);
        leftKey = Integer.parseInt(s[2]);
        rightKey = Integer.parseInt(s[3]);
        liftKey = Integer.parseInt(s[4]);
        landKey = Integer.parseInt(s[5]);
        yawLeftKey = Integer.parseInt(s[6]);
        yawRightKey = Integer.parseInt(s[7]);
        System.out.println(forwardKey);
  }



   int[] motor = new int []{127,127,127,127};// size of 4
   int ctrlKey = 17;

   //Key state for any key bindings
   boolean forward = false, backward = false, left = false, right = false, up = false, down = false, ctrl = false, lift = false, land = false, yawLeft = false, yawRight = false;

   // Motor layout
   //      M1       M2
   //         X   X
   //           X
   //         X   X
   //      M4       M3
       
   public void setkeyPressed(int key) {
       // invoked when a physical key is pressed down, uses keycode, int output.
       // TODO Auto-generated method stub
       //throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
       //System.out.println("u pressed key1:" + e.getKeyChar());
       //System.out.println("key code pressed:" + e.getKeyCode());
        //int key = e.getKeyCode();

      if (key == liftKey){//W
           lift = true; 
           if (m1PWM < maxPWM){
                   motor[0]= ++m1PWM;
                   motor[1]= --m2PWM;
                   motor[2]= ++m3PWM;
                   motor[3]= --m4PWM;
                   drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
                //   System.out.println("increse" + m1);
               //     for (int i = 0; i < motor.length; i++){
               //     System.out.println("Motor " + (i ) + " : " + motor[i]);
               // }
               }
           else {
                   maxCCPWM = defaultPWM - (maxPWM - defaultPWM);
                   motor[0]= maxPWM;
                   motor[1]= maxCCPWM;
                   motor[2]= maxPWM;
                   motor[3]= maxCCPWM;
                   drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
                   System.out.println("no increase");
               }

               m1 = motor[0];
               m2 = motor[1];
               m3 = motor[2];
               m4 = motor[3];
               //System.out.println(m1 + " "+ m2 +" " +m3 + " "+m4);

           // for (int i = 0; i < motor.length; i++){
           //         System.out.println("Motor " + (i + 1) + " : " + motor[i]);
           //     }
       }
       
       if (key == landKey){//W
           land = true; 
           if (m1PWM > defaultPWM){
               
                   motor[0]= --m1PWM;
                   motor[1]= ++m2PWM;
                   motor[2]= --m3PWM;
                   motor[3]= ++m4PWM;
                    drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
                   //System.out.println(fowardPWM);                   
               }
           else {
                   motor[0]= defaultPWM;
                   motor[1]= defaultPWM;
                   motor[2]= defaultPWM;
                   motor[3]= defaultPWM;
                   drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
               }

               m1 = motor[0];
               m2 = motor[1];
               m3 = motor[2];
               m4 = motor[3];

           // for (int i = 0; i < motor.length; i++){
           //         System.out.println("Motor " + (i + 1) + " : " + motor[i]);
           //     }
       }
       // changing direction is done by decreasing RPM of motors for that direction
       //pitch
       if (key == forwardKey ){ // add if statments to check if motor is running
           forward = true;

           motor[0]= offset;
           motor[1]= ccoffset;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
           System.out.println("front");
       }
       if (key == backwardKey ){
           backward = true;
           motor[2]= offset;
           motor[3]= ccoffset;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
           System.out.println("back");
       }
       //roll sideways
       if (key == rightKey ){
           right = true;
           motor[1]= ccoffset;
           motor[2]= offset;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
           System.out.println("right");
       }
       if (key == leftKey ){
           left = true;
           motor[0]= offset;
           motor[3]= ccoffset;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
           System.out.println("left");
       }

       //add YAW control (left and right arrow)
       // can do this by inc or dec RPm on opposing motors
       // go left means increase RPM on 1 and 3 and decreasing the other pair
       // go right means increase RPM on 2 and 4 and decreasing the other pair

       if (key == yawLeftKey ){
           yawLeft = true;
           motor[0]= m1 + yawFactor ;
           motor[1]= m2 - yawFactor ;
           motor[2]= m3 + yawFactor;
           motor[3]= m4 - yawFactor;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
           System.out.println("yaw left");
       }
       if (key == yawRightKey ){
          yawRight = true;
           motor[0]= m1 - yawFactor ;
           motor[1]= m2 + yawFactor;
           motor[2]= m3 - yawFactor;
           motor[3]= m4 + yawFactor;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
           System.out.println("yaw right");
       }

       // increase max PWM
       if (key == upKey){
           up = true;
           maxPWM++;
           System.out.println("max increased to" + maxPWM);
       }
       if (key == downKey){
           down = true;
           maxPWM--;
           System.out.println("max decreased to" + maxPWM);
       }

       if (key == takeOff){
        if (m1PWM < maxPWM){
                for (int i = defaultPWM; i < maxPWM; i++){
                        motor[0]= ++m1PWM;
                        motor[1]= --m2PWM;
                        motor[2]= ++m3PWM;
                        motor[3]= --m4PWM;
                        m1 = motor[0];
                        m2 = motor[1];
                        m3 = motor[2];
                        m4 = motor[3];
                        drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
                        drone.printMotorVals();
                }
            }
             else {
                   maxCCPWM = defaultPWM - (maxPWM - defaultPWM);
                   motor[0]= maxPWM;
                   motor[1]= maxCCPWM;
                   motor[2]= maxPWM;
                   motor[3]= maxCCPWM;
                   drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
                   System.out.println("no increase");
               }
               m1 = motor[0];
               m2 = motor[1];
               m3 = motor[2];
               m4 = motor[3];
       }
       if (key == landing){
            if (m1PWM > defaultPWM ){
                    for (int i = m1PWM; i > defaultPWM; i--){
                        motor[0]= --m1PWM;
                        motor[1]= ++m2PWM;
                        motor[2]= --m3PWM;
                        motor[3]= ++m4PWM;
                        m1 = motor[0];
                        m2 = motor[1];
                        m3 = motor[2];
                        m4 = motor[3];
                    drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
                    drone.printMotorVals();
                   //System.out.println(fowardPWM);           
                    }        
               }
           else {
                   motor[0]= defaultPWM;
                   motor[1]= defaultPWM;
                   motor[2]= defaultPWM;
                   motor[3]= defaultPWM;
                   drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
               }

               m1 = motor[0];
               m2 = motor[1];
               m3 = motor[2];
               m4 = motor[3];
       }
       if (key == emergencyStop){
                    motor[0]= defaultPWM;
                    motor[1]= defaultPWM;
                    motor[2]= defaultPWM;
                    motor[3]= defaultPWM;
                    m1PWM = motor[0];
                    m2PWM = motor[1];
                    m3PWM = motor[2];
                    m4PWM = motor[3];   
                    m1 = motor[0];
                    m2 = motor[1];
                    m3 = motor[2];
                    m4 = motor[3];
                   drone.setMotors(motor[0], motor[1], motor[2], motor[3]);

       }
       //take this out lol
       if (forward == true && backward == true){// get back to PWM of 127 
           System.out.println("Its gg boys");
               // for (; fowardPWM >= defaultPWM; fowardPWM-- ){
               // //FowardSpeed--;
               // System.out.println(fowardPWM);
               // }
           System.exit(0);
       }

       for (int i = 0; i < motor.length; i++){
                   System.out.println("Motor " + (i + 1) + " : " + motor[i]);
               }
            
               drone.printMotorVals();
              // this.setmotors(array);
       // to change max PWM while pressed in forward
       // if (forward == true && up == true){
       //     maxPWM++;
       // }
       // if (forward == true && down == true){
       //     maxPWM--;
       // }
       // if (e.getKeyCode() == ctrlKey ){// to change control keys (turn this off when we merge)
       //     ctrl = true;
       //     System.out.println("right");
       // }
       // if (forward == true && ctrl == true){
       //     forwardKey = e.getKeyCode();
       // }
       // if (backward == true && ctrl == true){
       //     backwardKey = e.getKeyCode();
       // }
       // if (left == true && ctrl == true){
       //     leftKey = e.getKeyCode();
       // }
       // if (right == true && ctrl == true){
       //     rightKey = e.getKeyCode();
       // }
       
   }


   public void setkeyReleased(int key) {

    //int key = e.getKeyCode();

       if (key == liftKey){//W
           lift = false; 
       }
       if (key == landKey){//W
           land = false; 
       }
       if (key == forwardKey){//W
           forward = false; 
           motor[0]= m1;
           motor[1]= m2;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
       }
       if (key == backwardKey){//W
           backward = false; 
           motor[2]= m3;
           motor[3]= m4;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
       }
       if (key == leftKey ){
           left = false;  
           motor[0]= m1;
           motor[3]= m4;  
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
       }
       if (key == rightKey ){
           right = false;
           motor[1]= m2;
           motor[2]= m3;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
       }
       if (key == yawLeftKey ){
           yawLeft = false;
           motor[0]= m1 ;
           motor[1]= m2;
           motor[2]= m3 ;
           motor[3]= m4;
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
       }
       if (key == yawRightKey ){
          yawRight = false;
           motor[0]= m1;
           motor[1]= m2;
           motor[2]= m3;
           motor[3]= m4;   
           drone.setMotors(motor[0], motor[1], motor[2], motor[3]);
       }
       if (key == upKey){
           up = false;
       }
       if (key == downKey){
           down = false;
       }
       if (key== ctrlKey){
           ctrl = false;
       }
       if (key == takeOff){

       }
       if (key == landing){

       }
       if (key == emergencyStop){

       }

       System.out.println("release key PWM: ");
       for (int i = 0; i < motor.length; i++){
                   System.out.println("Motor " + (i + 1) + " : " + motor[i]);
               }
        drone.printMotorVals();
       
   }
    
}
