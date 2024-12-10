// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.Constants.ControllerConstants;
import frc.robot.subsystems.ArduinoSubsystem;
import frc.robot.subsystems.ArduinoSubsystem.StatusCode;
import frc.robot.subsystems.CannonSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LightAndHornSubsystem;

public class RobotContainer {
	private final SerialPort m_arduino = new SerialPort(9600, Port.kUSB);
	private final ArduinoSubsystem m_arduinoSubsystem = new ArduinoSubsystem(m_arduino);
	private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
	private final LightAndHornSubsystem m_lightAndHornSubsystem = new LightAndHornSubsystem();
	private final CannonSubsystem m_cannonSubsystem = new CannonSubsystem(m_arduino);
	private final CommandPS4Controller m_controller = new CommandPS4Controller(
			ControllerConstants.kDriverControllerPort);

	public RobotContainer() {
		configureButtonBindings();
		SmartDashboard.putData(m_cannonSubsystem);
	}

	private void configureButtonBindings() {
		// -------------Driving -------------
		m_driveSubsystem.setDefaultCommand(
				m_driveSubsystem.drive(m_controller::getLeftY, m_controller::getL2Axis, m_controller::getR2Axis));

		// ------------Horning----------------
		m_controller.touchpad().whileTrue(m_lightAndHornSubsystem.horn());

		// ---------------Cannons---------------
		m_controller.povDown().onTrue(m_cannonSubsystem.chargeCannonsLow());
		m_controller.povRight().onTrue(m_cannonSubsystem.chargeCannonsMedium());
		m_controller.povUp().onTrue(m_cannonSubsystem.chargeCannonsHigh());
		m_controller.L1().and(m_controller.square()).whileTrue(m_cannonSubsystem.fireLeftCannon());
		m_controller.L1().and(m_controller.triangle()).whileTrue(m_cannonSubsystem.fireMiddleCannon());
		m_controller.L1().and(m_controller.circle()).whileTrue(m_cannonSubsystem.fireRightCannon());
		m_controller.L1().and(m_controller.cross()).whileTrue(m_cannonSubsystem.fireAllCannons());

		// ---------------Lights---------------
		RobotModeTriggers.disabled().negate().onTrue(m_lightAndHornSubsystem.spinLight());
		m_controller.share().onTrue(m_arduinoSubsystem.ledColor(StatusCode.SMOOTH_RAINBOW_PARTY_FUN_TIME));
		m_controller.options().onTrue(m_arduinoSubsystem.ledColor(StatusCode.SHEN_COLORS));
	}

	// TODO get auto command from auto chooser
	public Command getAutonomousCommand() {
		return m_cannonSubsystem.fillAllCannonLights();
	}
}
