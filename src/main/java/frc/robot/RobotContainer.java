// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.ControllerConstants.Axis;
import frc.robot.Constants.ControllerConstants.Button;
import frc.robot.subsystems.ArduinoSubsystem;
import frc.robot.subsystems.ArduinoSubsystem.StatusCode;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LightAndHornSubsystem;

public class RobotContainer {
	private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
	private final LightAndHornSubsystem m_lightAndHornSubsystem = new LightAndHornSubsystem();
	private final ArduinoSubsystem m_arduinoSubsystem = new ArduinoSubsystem();

	private final CommandPS4Controller m_controller = new CommandPS4Controller(
			ControllerConstants.kDriverControllerPort);

	private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

	public RobotContainer() {
		configureButtonBindings();
	}

	private void configureButtonBindings() {
		// -------------LED signaling-------------
		m_controller.povLeft().onTrue(m_arduinoSubsystem.writeStatus(StatusCode.BLINKING_PURPLE));
		m_controller.povRight().onTrue(m_arduinoSubsystem.writeStatus(StatusCode.BLINKING_YELLOW));
		m_controller.povUp().onTrue(m_arduinoSubsystem.writeStatus(StatusCode.RAINBOW_PARTY_FUN_TIME));
		m_controller.povDown().onTrue(m_arduinoSubsystem.writeStatus(StatusCode.DEFAULT));

		// -------------Driving -------------
		m_driveSubsystem.setDefaultCommand(m_driveSubsystem.drive(
				() -> -m_controller.getRawAxis(Axis.kLeftY),
				() -> m_controller.getRawAxis(Axis.kLeftTrigger),
				() -> m_controller.getRawAxis(Axis.kRightTrigger)));

		// ------------Horning----------------
		m_controller.button(Button.kTrackpad).whileTrue(m_lightAndHornSubsystem.horn());

	}

	// TODO get auto command from auto chooser
	public Command getAutonomousCommand() {
		return m_autoChooser.getSelected();
	}
}
