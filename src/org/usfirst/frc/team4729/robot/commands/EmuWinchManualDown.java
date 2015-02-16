package org.usfirst.frc.team4729.robot.commands;

import org.usfirst.frc.team4729.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class EmuWinchManualDown extends Command {
Timer timer = new Timer();
    public EmuWinchManualDown() {
    	requires(Robot.emuWinch);
    	//requires(Robot.manualOrAuto);
    	//requires(Robot.hashDefine);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		SmartDashboard.putString("A Frame Winch Manual State", "Going Down");
    	if (Robot.manual) {
    		Robot.emuWinch.moveDown();
    	} else {
    		while (Robot.emuWinch.emuPotRead() < Robot.EMU_DOWN_ANGLE){
    			Robot.emuWinch.moveDown();
    		}
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.emuWinch.stop();
    	SmartDashboard.putString("A Frame Winch Manual State", "Inactive");
    }
}