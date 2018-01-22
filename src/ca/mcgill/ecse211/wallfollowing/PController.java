package ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class PController implements UltrasonicController {

  /* Constants */
  private static final int MOTOR_SPEED = 200;
  private static final int FILTER_OUT = 20;

  private final int bandCenter;
  private final int bandWidth;
  private int distance;
  private int filterControl;

  public PController(int bandCenter, int bandwidth) {
    this.bandCenter = bandCenter;
    this.bandWidth = bandwidth;
    this.filterControl = 0;

    WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED); // Initalize motor rolling forward
    WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {

    // rudimentary filter - toss out invalid samples corresponding to null
    // signal.
    // (n.b. this was not included in the Bang-bang controller, but easily
    // could have).
    //
    if (distance >= 255 && filterControl < FILTER_OUT) {
      // bad value, do not set the distance var, however do increment the
      // filter value
      filterControl++;
    } else if (distance >= 255) {
      // We have repeated large values, so there must actually be nothing
      // there: leave the distance alone
      this.distance = distance;
    } else {
      // distance went below 255: reset filter and leave
      // distance alone.
      filterControl = 0;
      this.distance = distance;
    }

    // TODO: process a movement based on the us distance passed in (P style)
    int errorMultiplier = 3;
    int errorCorrection= errorMultiplier*Math.abs(this.distance-this.bandCenter);
    if (errorCorrection>200) errorCorrection=200;
    if (this.distance<=(this.bandCenter+this.bandWidth) && this.distance>=(this.bandCenter-this.bandWidth)) {
    	WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED);	
    	WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED);
    	WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
    }
    else if(this.distance < 20) {
    	WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED*2);
    	WallFollowingLab.rightMotor.setSpeed((int)(2.5*(MOTOR_SPEED)));
    	WallFollowingLab.leftMotor.backward();
    	WallFollowingLab.rightMotor.forward();
    	
    }
    else if (this.distance<this.bandCenter-this.bandWidth) {
    	WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED-errorCorrection/(errorMultiplier));
    	WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED+errorCorrection);
    	WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
    }
    else {
    	WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED-errorCorrection/(errorMultiplier));
    	WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED+errorCorrection);
    	WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
    }
  }


  @Override
  public int readUSDistance() {
    return this.distance;
  }

}
