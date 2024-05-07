package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightAndHornSubsystem extends SubsystemBase {
    private final Relay relay = new Relay(0);

    public LightAndHornSubsystem() {
    }

    public Command horn() {
        return runEnd(() -> relay.set(Value.kForward), () -> relay.set(Value.kOff));
    }
}
