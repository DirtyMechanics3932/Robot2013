package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author admin
 */
public class Shooter {
    private final Jaguar wheel;
    private final Relay feeder;
    private final Relay lowLift;
    private final Relay highLift;
    private final SmartJoystick joystick;
    private boolean shooting = false;
    private long shootTimer = 0;
    private long shootTimeout = 100;
    private long cooldownTimer = 0;
    private long cooldownTimeout = 1000;
    private final Relay flippedFeeder;
    private final Relay hook;
    
    
    public Shooter(Jaguar wheel, Relay feeder, Relay flippedFeeder, Relay lowLift, Relay highLift, Relay hook, SmartJoystick joystick) {
        this.wheel = wheel;
        this.feeder = feeder;
        this.flippedFeeder = flippedFeeder;
        this.joystick = joystick;
        this.lowLift = lowLift;
        this.highLift = highLift;
        this.hook = hook;
    }
    
    public void update() {
        //Feeder
        if (joystick.getButton(6).state) {
            if (!shooting && System.currentTimeMillis() - cooldownTimer > cooldownTimeout) {
                shooting = true;
                shootTimer = System.currentTimeMillis();
                feeder.set(Relay.Value.kForward);
            } else {
                if (System.currentTimeMillis() - shootTimer > shootTimeout) {
                    feeder.set(Relay.Value.kOff);
                }
            }
        } else {
            if (shooting) {
                if (System.currentTimeMillis() - shootTimer > shootTimeout) {
                    shooting = false;
                    cooldownTimer = System.currentTimeMillis();
                }
            } else {
                feeder.set(Relay.Value.kOff);
            }
        }
        
        if (joystick.getButton(5).state) {
            if (!shooting && System.currentTimeMillis() - cooldownTimer > cooldownTimeout) {
                shooting = true;
                shootTimer = System.currentTimeMillis();
                flippedFeeder.set(Relay.Value.kForward);
            } else {
                if (System.currentTimeMillis() - shootTimer > shootTimeout) {
                    flippedFeeder.set(Relay.Value.kOff);
                }
            }
        } else {
            if (shooting) {
                if (System.currentTimeMillis() - shootTimer > shootTimeout) {
                    shooting = false;
                    cooldownTimer = System.currentTimeMillis();
                }
            } else {
                flippedFeeder.set(Relay.Value.kOff);
            }
        }
        //Wheel
        if (joystick.getButton(3).state) {
            wheel.set(1.0D);
        } else {
            wheel.set(0);
        }
        //Lift A
        if (joystick.getButton(2).state) {
            lowLift.set(Relay.Value.kOff);
        } else {
            lowLift.set(Relay.Value.kForward);
        }
        //Lift B
        if (joystick.getButton(4).state) {
            highLift.set(Relay.Value.kOff);
        } else {
            highLift.set(Relay.Value.kForward);
        }
        //Hook
        if (joystick.getButton(1).state) {
            hook.set(Relay.Value.kForward);
        } else {
            hook.set(Relay.Value.kOff);
        }
    }
}