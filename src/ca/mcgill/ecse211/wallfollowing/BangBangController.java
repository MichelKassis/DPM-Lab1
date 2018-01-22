package ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.motor.*;

public class BangBangController implements UltrasonicController {

  private final int bandCenter;
  private final int bandwidth;
  private final int motorLow;
  private final int motorHigh;
  private int distance;
  private int filterControl;
  private static final int FILTER_OUT = 20;
  private final int DELTA = 175;
  

  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
    this.bandwidth = bandwidth;
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    this.filterControl = 0;
    WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab.rightMotor.setSpeed(motorHigh);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
    this.distance = distance;
    
    
    //The implementation instruction is  from the slides on mycources.

    if (distance >= 255 && filterControl < FILTER_OUT) {
      // bad value, do not set the distance var, however do increment the
      // filter value
      filterControl++;
    } else if (distance >= 255) {
      // We have repeated large values, so there must actually be nothing
      // there: leave the distance alone
      this.distance = 255;
    } else {
      // distance went below 255: reset filter and leave
      // distance alone.otor
      filterControl = 0;
      this.distance = distance;
    }

	int error = this.distance - this.bandCenter;
	if(Math.abs(error) < this.bandwidth) {
		WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
	    WallFollowingLab.rightMotor.setSpeed(motorHigh);
	    WallFollowingLab.leftMotor.forward();
	    WallFollowingLab.rightMotor.forward();
	}
	else if (error < 0 ) {  // the robot is too far from the wall
		WallFollowingLab.leftMotor.setSpeed(motorHigh*3); 
	    WallFollowingLab.rightMotor.setSpeed(motorLow);
	    WallFollowingLab.leftMotor.forward();
	    WallFollowingLab.rightMotor.backward();
		
	}else if(error > 0) {   // the robot is close to the world
		WallFollowingLab.leftMotor.setSpeed(motorLow); 
	    WallFollowingLab.rightMotor.setSpeed(motorHigh);
	    WallFollowingLab.leftMotor.forward();
	    WallFollowingLab.rightMotor.forward();
		
	}
    
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
