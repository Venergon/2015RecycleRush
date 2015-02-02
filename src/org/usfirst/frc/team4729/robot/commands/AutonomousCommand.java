package org.usfirst.frc.team4729.robot.commands;

import org.usfirst.frc.team4729.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutonomousCommand extends Command {
	Timer timer = new Timer();
	
	public static boolean toteOrNo = false;
	public static boolean rampOrNo = false;
	
	
	double clampUp;
	double tiltUp;
	double emuUp;
	SendableChooser toteChooser = new SendableChooser();
	SendableChooser rampChooser = new SendableChooser();
	
	
    public AutonomousCommand(double clampUp, double tiltUp, double emuUp) {
    	requires(Robot.driveSubsystem);
    	requires(Robot.toteClamp);
    	requires(Robot.toteTilt);
    	requires(Robot.emuWinch);
    	requires(Robot.switches);
    	requires(Robot.hashDefine);
    	this.clampUp = clampUp;
    	this.tiltUp = tiltUp;
    	this.emuUp = emuUp;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	toteChooser.addDefault("Going for tote", new GoForTote());
    	toteChooser.addObject("Going for bin", new DontGoForTote());
    	SmartDashboard.putData("Tote Chooser", toteChooser);
    	rampChooser.addDefault("Going on Ramp", new GoOnRamp());
    	rampChooser.addObject("Not Going on Ramp", new DontGoOnRamp());
    	SmartDashboard.putData("Ramp Chooser", rampChooser);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    	Robot.driveSubsystem.resetEncoders();
    	Command toteDeterminer = (Command) toteChooser.getSelected();
    	toteDeterminer.start();
    	Command rampDeterminer = (Command) rampChooser.getSelected();
    	rampDeterminer.start();
    	
    	if (toteOrNo) { //Going for tote
    		Robot.toteClamp.moveDown();
    		while (Robot.toteClamp.readClampPot() < clampUp);
    		Robot.toteClamp.stop();
    		Robot.toteTilt.moveUp();
    		while (Robot.toteTilt.readTiltPot() < tiltUp); //wait until tote is fully up
    		Robot.toteTilt.stop();
    		timer.reset();
        	timer.start();
    		Robot.driveSubsystem.autoTank(Robot.hashDefine.autoSpeed(), Robot.hashDefine.autoSpeed());
    		if (rampOrNo) { //Going across ramp
    			while ((Robot.driveSubsystem.readLeftEncoder() + Robot.driveSubsystem.readRightEncoder()) < Robot.hashDefine.rampDistance());
    		}
    		else { //Not going across ramp
    			while ((Robot.driveSubsystem.readLeftEncoder() + Robot.driveSubsystem.readRightEncoder()) < Robot.hashDefine.noRampDistance());
    		}
    	}
    
    	else { //Going for bin
    		Robot.emuWinch.stop();
    		while (Robot.emuWinch.emuPotRead() < emuUp); // wait until bin is fully up
    		Robot.emuWinch.stop();
    		timer.reset();
        	timer.start();
        	Robot.driveSubsystem.autoTank(Robot.hashDefine.autoSpeed(), Robot.hashDefine.autoSpeed());
    		if (rampOrNo) { //Going across ramp
    			while ((Robot.driveSubsystem.readLeftEncoder() + Robot.driveSubsystem.readRightEncoder()) < Robot.hashDefine.rampDistance());
    		}
    		else {
    			while ((Robot.driveSubsystem.readLeftEncoder() + Robot.driveSubsystem.readRightEncoder()) < Robot.hashDefine.noRampDistance());
    		}
    	}
		Robot.driveSubsystem.autoTank(0, 0);
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
    }
}
 