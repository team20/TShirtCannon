package frc.robot.subsystems;

import static frc.robot.Constants.LightAndHornConstants.*;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightAndHornSubsystem extends SubsystemBase {
	private final Relay horn = new Relay(kHornPort);
	private final Relay light = new Relay(kLightPort);

	public LightAndHornSubsystem() {
	}

	public Command horn() {
		return runEnd(() -> horn.set(Value.kOn), () -> horn.set(Value.kOff));
	}

	public Command lightSpin() {
		return run(() -> light.set(Value.kForward));
	}

	public Command lightReverseSpin() {
		return run(() -> light.set(Value.kReverse));
	}
}
