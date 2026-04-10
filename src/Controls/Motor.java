package Controls;

public class Motor {
  public int PWM = 127; // initially stationary
  private static final int PWMMax = 254;
  private static final int PWMMin = 1;

  public void setSpeed(int PWMValue) {
    // doesn't let motors go too fast or too slow
    if (PWMValue > PWMMax) 
      this.PWM = PWMMax;
    else if (PWMValue < 0)
      this.PWM = PWMMin;
    else 
      this.PWM = PWMValue; 
  }

  // write function to increase max PWM
  
}
