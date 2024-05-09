package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LightAndHornConstants;

public class LightAndHornSubsystem extends SubsystemBase {
    
    Relay hornRelay = new Relay(LightAndHornConstants.kRelayPort);
    Relay lightRelay = new Relay(LightAndHornConstants.kRelayPort);

    public LightAndHornSubsystem(){
        lightOn();
    }
    
    public void periodic(){   
    }

    public void hornOff(){
        hornRelay.set(Value.kOff);
    }
    
    public void lightOff(){
        lightRelay.set(Value.kOff);
    }

     public void hornOn(){
        hornRelay.set(Value.kOn);
    }
    
    public void lightOn(){
        lightRelay.set(Value.kOn);
    }

}
