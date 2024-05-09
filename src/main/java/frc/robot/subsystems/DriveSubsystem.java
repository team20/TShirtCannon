package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {
	private static DriveSubsystem s_subsystem;

	private final TalonSRX m_frontLeft = new TalonSRX(DriveConstants.kFrontLeftID);
	private final TalonSRX m_frontRight = new TalonSRX(DriveConstants.kFrontRightID);
	private final TalonSRX m_backLeft = new TalonSRX(DriveConstants.kBackLeftID);
	private final TalonSRX m_backRight = new TalonSRX(DriveConstants.kBackRightID);

	public DriveSubsystem() {
		s_subsystem = this;
		m_frontLeft.setInverted(DriveConstants.kFrontLeftInvert);
		m_frontLeft.configPeakCurrentLimit(DriveConstants.kPeakCurrentLimit);
		m_frontRight.setInverted(DriveConstants.kFrontRightInvert);
		m_frontRight.configPeakCurrentLimit(DriveConstants.kPeakCurrentLimit);
		m_backLeft.follow(m_frontLeft);
		m_backLeft.configPeakCurrentLimit(DriveConstants.kPeakCurrentLimit);
		m_backRight.follow(m_frontRight);
		m_backRight.setInverted(DriveConstants.kBackRightInvert);
		m_backRight.configPeakCurrentLimit(DriveConstants.kPeakCurrentLimit);
	}

	public static DriveSubsystem get() {
		return s_subsystem;
	}

	public void periodic() {
		SmartDashboard.putNumber("Back Right", m_backRight.getSupplyCurrent());
		SmartDashboard.putNumber("Back Left", m_backLeft.getSupplyCurrent());
		SmartDashboard.putNumber("Front Right", m_frontRight.getSupplyCurrent());
		SmartDashboard.putNumber("Front Left", m_frontLeft.getSupplyCurrent());
	}

	

	public void arcadeDrive(double straight, double left, double right) {
		tankDrive(DriveConstants.kSpeedLimitFactor * (straight - left + right),
				DriveConstants.kSpeedLimitFactor * (straight + left - right));
	}

	public void arcadeDrive(double straight, double turn) {
		tankDrive(DriveConstants.kSpeedLimitFactor * (straight - turn),
				DriveConstants.kSpeedLimitFactor * (straight + turn));
	}

	/**
	 * @param leftSpeed  Left motors percent output
	 * @param rightSpeed Right motors percent output
	 */
	public void tankDrive(double leftSpeed, double rightSpeed) {
		// TODO only set front? back should follow
		m_frontLeft.set(ControlMode.PercentOutput, leftSpeed);
		m_frontRight.set(ControlMode.PercentOutput, rightSpeed);
	}

	

}
