// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.ControllerConstants.Button;
import frc.robot.Constants.DriveConstants;
import frc.robot.commands.LEDs.LEDCommand;
import frc.robot.commands.arm.ArmScoreCommand;
import frc.robot.commands.arm.ArmScoreCommand.ArmPosition;
import frc.robot.commands.arm.ManualMotorCommand;
import frc.robot.commands.drive.AutoAlignmentCommand;
import frc.robot.commands.drive.BalancePIDCommand;
import frc.robot.commands.drive.DefaultDriveCommand;
import frc.robot.commands.drive.DriveBrakeModeCommand;
import frc.robot.commands.drive.DriveToApriltag;
import frc.robot.commands.drive.TurnTimeCommand;
import frc.robot.commands.gripper.WheelGripperCommand;
import frc.robot.commands.gripper.WheelGripperCommand.WheelGripperPosition;
import frc.robot.commands.util.DeferredCommand;
import frc.robot.commands.util.DeferredCommandAuto;
import frc.robot.subsystems.AprilTagSubsystem;
import frc.robot.subsystems.ArduinoSubsystem;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PoseEstimationSubsystem;
import frc.robot.subsystems.WheelGripperSubsystem;
import frc.robot.subsystems.ArduinoSubsystem.StatusCode;
import frc.robot.util.CommandComposer;
import hlib.drive.Pose;

public class RobotContainer {
	private DriveSubsystem m_driveSubsystem = new DriveSubsystem();
	private AprilTagSubsystem m_aprilTagSubsystem = new AprilTagSubsystem();

	private final Joystick m_Controller = new Joystick(ControllerConstants.kDriverControllerPort);

	private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

	public RobotContainer() {
		configureButtonBindings();
	}

	private void configureButtonBindings() {

		// -------------LED signaling-------------
		// // Signal for a cube
		new POVButton(Controller, ControllerConstants.DPad.kLeft)
				.onTrue(new LEDCommand(StatusCode.BLINKING_PURPLE));
		// Signal for a cone
		new POVButton(m_Controller, ControllerConstants.DPad.kRight)
				.onTrue(new LEDCommand(StatusCode.BLINKING_YELLOW));
		new POVButton(m_Controller, ControllerConstants.DPad.kUp)
				.onTrue(new LEDCommand(StatusCode.RAINBOW_PARTY_FUN_TIME));
		new JoystickButton(m_Controller, ControllerConstants.Button.kRightBumper)
				.onTrue(new LEDCommand(StatusCode.DEFAULT));

		// -------------Driving -------------
		m_driveSubsystem.setDefaultCommand(new DefaultDriveCommand(
				() -> -m_driverController.getRawAxis(ControllerConstants.Axis.kLeftY),
				() -> m_driverController.getRawAxis(ControllerConstants.Axis.kLeftTrigger),
				() -> m_driverController.getRawAxis(ControllerConstants.Axis.kRightTrigger)));

		// Fine Turning
		new JoystickButton(m_driverController, ControllerConstants.Button.kRightBumper)
				.whileTrue(new DefaultDriveCommand(() -> 0.0, () -> 0.0, () -> DriveConstants.kFineTurningSpeed));
		new JoystickButton(m_driverController, ControllerConstants.Button.kLeftBumper)
				.whileTrue(new DefaultDriveCommand(() -> 0.0, () -> DriveConstants.kFineTurningSpeed, () -> 0.0));

	}

	// TODO get auto command from auto chooser
	public Command getAutonomousCommand() {
		
		return m_autoChooser.getSelected();
	}
}
