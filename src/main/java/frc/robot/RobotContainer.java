// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.LEDs.LEDCommand;
import frc.robot.commands.drive.DefaultDriveCommand;
import frc.robot.commands.hornAndLight.HornCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LightAndHornSubsystem;
import frc.robot.subsystems.ArduinoSubsystem.StatusCode;

public class RobotContainer {
	private DriveSubsystem m_driveSubsystem = new DriveSubsystem();
	private LightAndHornSubsystem m_lightAndHornSubsystem = new LightAndHornSubsystem();

	private final Joystick m_Controller = new Joystick(ControllerConstants.kDriverControllerPort);

	private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

	public RobotContainer() {
		configureButtonBindings();
	}

	private void configureButtonBindings() {

		// -------------LED signaling-------------

		new POVButton(m_Controller, ControllerConstants.DPad.kLeft)
				.onTrue(new LEDCommand(StatusCode.BLINKING_PURPLE));

		new POVButton(m_Controller, ControllerConstants.DPad.kRight)
				.onTrue(new LEDCommand(StatusCode.BLINKING_YELLOW));

		new POVButton(m_Controller, ControllerConstants.DPad.kUp)
				.onTrue(new LEDCommand(StatusCode.RAINBOW_PARTY_FUN_TIME));

		new POVButton(m_Controller, ControllerConstants.DPad.kDown)
				.onTrue(new LEDCommand(StatusCode.DEFAULT));

		// -------------Driving -------------
		m_driveSubsystem.setDefaultCommand(new DefaultDriveCommand(
				() -> -m_Controller.getRawAxis(ControllerConstants.Axis.kLeftY),
				() -> m_Controller.getRawAxis(ControllerConstants.Axis.kLeftTrigger),
				() -> m_Controller.getRawAxis(ControllerConstants.Axis.kRightTrigger)));

		// ------------Horning----------------
		new JoystickButton(m_Controller, ControllerConstants.Button.kTrackpad).whileTrue(new HornCommand(m_lightAndHornSubsystem));
		
	}

	// TODO get auto command from auto chooser
	public Command getAutonomousCommand() {
		return m_autoChooser.getSelected();
	}
}
