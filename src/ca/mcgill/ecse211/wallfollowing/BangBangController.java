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
    
//    if (distance >= 255 && filterControl < FILTER_OUT) {
//        // bad value, do not set the distance var, however do increment the
//        // filter value
//        filterControl++;
//      } else if (distance >= 255) {
//        // We have repeated large values, so there must actually be nothing
//        // there: leave the distance alone
//        this.distance = distance;
//      } else {
//        // distance went below 255: reset filter and leave
//        // distance alone.
//        filterControl = 0;
//        this.distance = distance;
//      }
    
    int error = this.distance - (this.bandCenter + bandwidth);	//error in cm
	if(Math.abs(error) <= this.bandwidth)	    //abs is less than the threshold
	{										    //robot is in the right direction //just keep moving
		WallFollowingLab.leftMotor.setSpeed(this.motorHigh);
		WallFollowingLab.rightMotor.setSpeed(this.motorHigh);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
	}
	else if(error < 0) 					//robot is too far from the wall
	{								// increase the rotation of the outside wheel, decrease the location of the inside wheel
		WallFollowingLab.leftMotor.setSpeed(this.motorHigh);	
		WallFollowingLab.rightMotor.setSpeed(75);	
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
	}
	else									//the robot is too close to the wall
	{								// decrease the rotation of the outside wheel, increase the location of the inside wheel
		WallFollowingLab.leftMotor.setSpeed(75);
		WallFollowingLab.rightMotor.setSpeed(this.motorHigh);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
	}
    
    
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
