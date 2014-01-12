package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Relay;
import org.dirtymechanics.frc.jaguars.JaguarPair;

public class EndGame {
    private final SmartJoystick stick;
    private final Relay hook;
    private final Relay deckLock;
    //private final Relay climbGear;
    private final JaguarPair leftDrive;
    private final JaguarPair rightDrive;
    private boolean pressed;
    private long pressTimer = Long.MAX_VALUE;
    private boolean ready;
    private long startTime = 0;
    private boolean init;
    private final Relay highLift;
    private final Relay lowLift;
    
    public EndGame(Relay hook, Relay deckLock, /*Relay climbGear,*/ Relay highLift, Relay lowLift,
            JaguarPair rightDrive, JaguarPair leftDrive, SmartJoystick stick) {
        this.hook = hook;
        this.deckLock = deckLock;
        //this.climbGear = climbGear;
        this.highLift = highLift;
        this.lowLift = lowLift;
        this.rightDrive = rightDrive;
        this.leftDrive = leftDrive;
        this.stick = stick;
    }
    
    public void cycle() {
        if (overrideEndgame()) {
            ready = false;
        }
        if (!init) {
            init = true;
            startTime = System.currentTimeMillis();
            lowLift.set(Relay.Value.kOff);
            highLift.set(Relay.Value.kOff);
            hook.set(Relay.Value.kForward);
        }
        if (time() > 3500) {
            deckLock.set(Relay.Value.kForward);
        }
        if (time() > 3500 && time() < 4000) {
            rightDrive.set(-0.5D);
            leftDrive.set(-0.5D);
        }
    }
    
    long time() {
        return System.currentTimeMillis() - startTime;
    }
    
    public boolean isReady() {
        if (stick.getZ() > .9D) {
            if (stick.getButton(8).state && stick.getButton(9).state) {
                if (!pressed) {
                    pressed = true;
                    pressTimer = System.currentTimeMillis();
                }
            } else {
                pressed = false;
                pressTimer = Long.MAX_VALUE;
            }
        } else {
            pressed = false;
            pressTimer = Long.MAX_VALUE;
        }
        if (pressed && System.currentTimeMillis() - pressTimer > 3000) {
            ready = true;
        }
        return ready;
    }
    
    public boolean overrideEndgame() {
        return stick.getZ() < 0 && stick.getButton(8).state && stick.getButton(9).state;
    }
}