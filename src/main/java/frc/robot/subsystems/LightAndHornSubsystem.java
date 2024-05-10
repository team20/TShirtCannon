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

	/**
	 * Creates a command to turn the horn on, then off.
	 * 
	 * @return The command.
	 */
	public Command horn() {
		return runEnd(() -> horn.set(Value.kOn), () -> horn.set(Value.kOff));
	}

	/**
	 * Creates a command to turn on the light and spin it.
	 * 
	 * @return The command.
	 */
	public Command spinLight() {
		return run(() -> light.set(Value.kForward));
	}

	/**
	 * Creates a command to turn on the light and spin it the opposite direction.
	 * 
	 * @return The command.
	 */
	public Command spinLightReverse() {
		return run(() -> light.set(Value.kReverse));
	}

	/**
	 * Creates a command to turn off the light.
	 * 
	 * @return The command.
	 */
	public Command disableLight() {
		return runOnce(() -> light.set(Value.kOff));
	}
}
