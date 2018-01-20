package ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.motor.*;

public class BangBangController implements UltrasonicController {

  private final int bandCenter;
  private final int bandwidth;
  private final int motorLow;
  private final int motorHigh;
  private int distance;
  

  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
    this.bandwidth = bandwidth;
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab.rightMotor.setSpeed(motorHigh);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
    this.distance = distance;
    
    

    
    //The implementation instruction is  from the slides on mycources.
    
    int error = this.distance - this.bandCenter;	//error in cm
	if(Math.abs(error) <= this.bandwidth)	//abs is less than the threshold
	{										//robot is in the right direction //just keep moving
		WallFollowingLab.leftMotor.setSpeed(this.motorHigh);
		WallFollowingLab.rightMotor.setSpeed(this.motorHigh);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
	}
	else if(error < 0) 					//robot is too far from the wall
	{								// increase the rotation of the outside wheel, decrease the location of the inside wheel
		WallFollowingLab.leftMotor.setSpeed(this.motorHigh);	
		WallFollowingLab.rightMotor.setSpeed(this.motorLow);	
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
	}
	else									//the robot is too close to the wall
	{								// decrease the rotation of the outside wheel, increase the location of the inside wheel
		WallFollowingLab.leftMotor.setSpeed(this.motorLow);
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
