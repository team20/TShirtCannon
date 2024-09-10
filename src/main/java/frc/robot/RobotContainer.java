// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.ControllerConstants.Axis;
import frc.robot.Constants.ControllerConstants.Button;
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
	private final CommandGenericHID m_controller = new CommandGenericHID(ControllerConstants.kDriverControllerPort);

	public RobotContainer() {
		configureButtonBindings();
	}

	private void configureButtonBindings() {
		// -------------Driving -------------
		m_driveSubsystem.setDefaultCommand(m_driveSubsystem.drive(
				() -> m_controller.getRawAxis(Axis.kLeftY),
				() -> m_controller.getRawAxis(Axis.kLeftTrigger),
				() -> m_controller.getRawAxis(Axis.kRightTrigger)));

		// ------------Horning----------------
		m_controller.button(Button.kTrackpad).whileTrue(m_lightAndHornSubsystem.horn());

		// ---------------Cannons---------------
		m_controller.povDown().onTrue(m_cannonSubsystem.chargeCannon(20));
		m_controller.povRight().onTrue(m_cannonSubsystem.chargeCannon(40));
		m_controller.povUp().onTrue(m_cannonSubsystem.chargeCannon(60));
		m_controller.button(Button.kLeftBumper).and(m_controller.button(Button.kSquare))
				.whileTrue(m_cannonSubsystem.fireLeftCannon());
		m_controller.button(Button.kLeftBumper).and(m_controller.button(Button.kTriangle))
				.whileTrue(m_cannonSubsystem.fireMiddleCannon());
		m_controller.button(Button.kLeftBumper).and(m_controller.button(Button.kCircle))
				.whileTrue(m_cannonSubsystem.fireRightCannon());
		m_controller.button(Button.kLeftBumper).and(m_controller.button(Button.kX))
				.whileTrue(m_cannonSubsystem.fireAllCannons());

		// ---------------Lights---------------
		RobotModeTriggers.disabled().negate().onTrue(m_lightAndHornSubsystem.spinLight());
		m_controller.button(Button.kShare)
				.onTrue(m_arduinoSubsystem.ledColor(StatusCode.SMOOTH_RAINBOW_PARTY_FUN_TIME));
		m_controller.button(Button.kOptions).onTrue(m_arduinoSubsystem.ledColor(StatusCode.SHEN_COLORS));
	}

	// TODO get auto command from auto chooser
	public Command getAutonomousCommand() {
		return m_cannonSubsystem.fillAllCannonLights();
	}
}
