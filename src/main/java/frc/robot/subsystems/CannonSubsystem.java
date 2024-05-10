// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.CannonConstants.*;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CannonSubsystem extends SubsystemBase {
	private final PneumaticHub m_hub = new PneumaticHub(kHubID);
	private final DoubleSolenoid m_leftCannon = m_hub.makeDoubleSolenoid(0, 2);
	private final DoubleSolenoid m_middleCannon = m_hub.makeDoubleSolenoid(5, 4);
	private final DoubleSolenoid m_rightCannon = m_hub.makeDoubleSolenoid(1, 3);
	private final AnalogInput m_leftCannonPressure = new AnalogInput(1);
	private final AnalogInput m_middleCannonPressure = new AnalogInput(2);
	private final AnalogInput m_rightCannonPressure = new AnalogInput(3);

	/** Creates a new CannonSubsystem. */
	public CannonSubsystem() {
	}

	private double getLeftCannonPressure() {
		// Subtract 0.47 volts to normalize the voltage to 0 to represent 0 PSI and
		// multiply by 50 PSI/volt to get cannon PSI.
		return (m_leftCannonPressure.getVoltage() - 0.47) * kPsiPerVolt;
	}

	private double getMiddleCannonPressure() {
		// Subtract 0.47 volts to normalize the voltage to 0 to represent 0 PSI and
		// multiply by 50 PSI/volt to get cannon PSI.
		return (m_middleCannonPressure.getVoltage() - 0.47) * kPsiPerVolt;
	}

	private double getRightCannonPressure() {
		// Subtract 0.47 volts to normalize the voltage to 0 to represent 0 PSI and
		// multiply by 50 PSI/volt to get cannon PSI.
		return (m_rightCannonPressure.getVoltage() - 0.47) * kPsiPerVolt;
	}

	@Override
	public void periodic() {
		SmartDashboard.putNumber("Left Cannon Voltage", m_leftCannonPressure.getVoltage());
		SmartDashboard.putNumber("Middle Cannon Voltage", m_middleCannonPressure.getVoltage());
		SmartDashboard.putNumber("Right Cannon Voltage", m_rightCannonPressure.getVoltage());
		SmartDashboard.putNumber("Left Cannon Pressure", getLeftCannonPressure());
		SmartDashboard.putNumber("Middle Cannon Pressure", getMiddleCannonPressure());
		SmartDashboard.putNumber("Right Cannon Pressure", getRightCannonPressure());
	}

	/**
	 * Creates a command to charge the all the cannons to the target PSI.
	 * 
	 * @param psi The target PSI.
	 * @return The command.
	 */
	public Command chargeCannon(int psi) {
		return runEnd(() -> {
			if (getLeftCannonPressure() < psi) {
				m_leftCannon.set(Value.kReverse);
			} else {
				m_leftCannon.set(Value.kOff);
			}
			if (getMiddleCannonPressure() < psi) {
				m_middleCannon.set(Value.kReverse);
			} else {
				m_middleCannon.set(Value.kOff);
			}
			if (getRightCannonPressure() < psi) {
				m_rightCannon.set(Value.kReverse);
			} else {
				m_rightCannon.set(Value.kOff);
			}
		}, () -> {
			m_leftCannon.set(Value.kOff);
			m_middleCannon.set(Value.kOff);
			m_rightCannon.set(Value.kOff);
		}).until(() -> getLeftCannonPressure() >= psi && getMiddleCannonPressure() >= psi
				&& getRightCannonPressure() >= psi);
	}

	public Command fireLeftCannon() {
		return runEnd(() -> m_leftCannon.set(Value.kForward), () -> m_leftCannon.set(Value.kOff));
	}

	public Command fireMiddleCannon() {
		return runEnd(() -> m_middleCannon.set(Value.kForward), () -> m_middleCannon.set(Value.kOff));
	}

	public Command fireRightCannon() {
		return runEnd(() -> m_rightCannon.set(Value.kForward), () -> m_rightCannon.set(Value.kOff));
	}

	public Command fireAllCannons() {
		return runEnd(() -> {
			m_leftCannon.set(Value.kForward);
			m_middleCannon.set(Value.kForward);
			m_rightCannon.set(Value.kForward);
		}, () -> {
			m_leftCannon.set(Value.kOff);
			m_middleCannon.set(Value.kOff);
			m_rightCannon.set(Value.kOff);
		});
	}
}
