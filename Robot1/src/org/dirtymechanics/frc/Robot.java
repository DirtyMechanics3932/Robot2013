package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Compressor;
//import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
//import edu.wpi.first.wpilibj.camera.AxisCamera;
import org.dirtymechanics.frc.jaguars.JaguarPair;

public class Robot extends IterativeRobot {

    private JaguarPair leftDrive;
    private JaguarPair rightDrive;
    private Jaguar shooterMotor;
    private Relay shooterFeeder;
    private SmartJoystick leftStick;
    private SmartJoystick rightStick;
    private SmartJoystick controller;
    private Compressor compressor;
    private Transmission transmission;
    private Drive drive;
    private Shooter shooter;
    private Relay transmissionRelay;
    private Relay lowLift;
    private Relay highLift;
    private EndGame endGame;
    private Relay hook;
    private Relay deckLock;
    private Relay climbGear;
    private long start;
    private boolean firing;
    private long firingStart;
    private Victor victor;
    private Relay flippedFeeder;
    private Solenoid led;
    //private AxisCamera camera;
    
    public Robot() {
        leftStick = new SmartJoystick(2, new int[] { });
        rightStick = new SmartJoystick(1, new int[] { 1, 3, 4, 10, 11});
        controller = new SmartJoystick(3, new int[] { 1, 2, 3, 4 } );
        leftDrive = new JaguarPair(new Jaguar[] { new Jaguar(3), new Jaguar(4) });
        rightDrive = new JaguarPair(new Jaguar[] { new Jaguar(1), new Jaguar(2) });
        shooterMotor = new Jaguar(5);
        compressor = new Compressor(1, 1);
        transmissionRelay = new Relay(2);
        shooterFeeder = new Relay(3);
        flippedFeeder = new Relay(7);
        lowLift = new Relay(4);
        highLift = new Relay(5);
        hook = new Relay(6);
        deckLock = new Relay(8);
        //climbGear = new Relay(9);
        transmission = new Transmission(transmissionRelay, rightStick);
        drive = new Drive(leftDrive, rightDrive, transmission, leftStick, rightStick);
        shooter = new Shooter(shooterMotor, shooterFeeder, flippedFeeder, lowLift, highLift, hook, controller);
        endGame = new EndGame(hook, deckLock, /*climbGear,*/ highLift, lowLift, rightDrive, leftDrive, rightStick);
        led = new Solenoid(5);
        victor = new Victor(6);
        //camera = AxisCamera.getInstance();
        compressor.start();
    }
    
    public void robotInit() {
        
    }
    
    public void autonomousInit() {
        start = System.currentTimeMillis();
        firing = false;
        
    }
    
    public void autonomousPeriodic() {
        long time = System.currentTimeMillis();
        if (time - start < 15000) {
            shooterMotor.set(.85D);
        } else {
            shooterMotor.set(0D);
        }
        lowLift.set(Relay.Value.kForward);
        highLift.set(Relay.Value.kForward);
        /*if (time - start < 1400) {
            rightDrive.set(0.4D);
            leftDrive.set(0.4D);
        } else {
            rightDrive.set(0D);
            leftDrive.set(0D);
        }*/
        if (time - start > 7000 && time - start < 15000) {
            if (firing) {
                long rem = (System.currentTimeMillis() - firingStart) % 2000;
                if (rem > 0 && rem < 250) {
                    shooterFeeder.set(Relay.Value.kForward);
                } else {
                    shooterFeeder.set(Relay.Value.kOff);
                }
            } else {
                firing = true;
                firingStart = System.currentTimeMillis();
            }
        }
    }

    public void autonomousPeriodicTHUGLIFE() {
        if (System.currentTimeMillis() - start < 15000) {
            shooterMotor.set(1.0D);
        }
        lowLift.set(Relay.Value.kForward);
        highLift.set(Relay.Value.kForward);
        if (System.currentTimeMillis() - start > 2000) {
            if (firing) {
                //first
                if (System.currentTimeMillis() - firingStart > 2500 &&
                    System.currentTimeMillis() - firingStart < 2750) {
                    shooterFeeder.set(Relay.Value.kForward);
                } else if (System.currentTimeMillis() - firingStart > 2750 &&
                    System.currentTimeMillis() - firingStart < 5500) {
                    shooterFeeder.set(Relay.Value.kOff);
                }
                //end first
                
                //second
                else if (System.currentTimeMillis() - firingStart > 5500 &&
                    System.currentTimeMillis() - firingStart < 5750) {
                    shooterFeeder.set(Relay.Value.kForward);
                } else if (System.currentTimeMillis() - firingStart > 5750 &&
                    System.currentTimeMillis() - firingStart < 8500) {
                    shooterFeeder.set(Relay.Value.kOff);
                }
                //end second
                
                //third
                else if (System.currentTimeMillis() - firingStart > 8500 &&
                    System.currentTimeMillis() - firingStart < 8750) {
                    shooterFeeder.set(Relay.Value.kForward);
                } else if (System.currentTimeMillis() - firingStart > 8750 &&
                    System.currentTimeMillis() - firingStart < 11500) {
                    shooterFeeder.set(Relay.Value.kOff);
                }
                //end third
                
                //fourth
                else if (System.currentTimeMillis() - firingStart > 11500 &&
                    System.currentTimeMillis() - firingStart < 11750) {
                    shooterFeeder.set(Relay.Value.kForward);
                } else if (System.currentTimeMillis() - firingStart > 11750 &&
                    System.currentTimeMillis() - firingStart < 14500) {
                    shooterFeeder.set(Relay.Value.kOff);
                    shooterMotor.set(0D);
                }
                //end fourth
                
            } else {
                firing = true;
                firingStart = System.currentTimeMillis();
            }
        }
    }

    public void teleopPeriodic() {
        //DriverStationLCD.getInstance().updateLCD();
        led.set(true);
        leftStick.update();
        rightStick.update();
        controller.update();
        drive.update();
        shooter.update();
        if (!endGame.isReady()) {
            leftStick.update();
            rightStick.update();
            drive.update();
            shooter.update();
        } else {
            endGame.cycle();
        }
    }

    public void disabledPeriodic() {
        led.set(false);
    }
}