// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.PneumaticsConstants.*;

import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CannonSubsystem extends SubsystemBase {
	private final PneumaticHub m_hub = new PneumaticHub(kHubID);
	private final Solenoid m_rightCannon = m_hub.makeSolenoid(0);
	private final Solenoid m_middleCannon = m_hub.makeSolenoid(1);
	private final Solenoid m_leftCannon = m_hub.makeSolenoid(2);

	/** Creates a new CannonSubsystem. */
	public CannonSubsystem() {
	}

	public Command fire() {
		return runEnd(() -> {
			m_rightCannon.set(true);
			m_middleCannon.set(true);
			m_leftCannon.set(true);
		}, () -> {
			m_rightCannon.set(false);
			m_middleCannon.set(false);
			m_leftCannon.set(false);
		});
	}
}
