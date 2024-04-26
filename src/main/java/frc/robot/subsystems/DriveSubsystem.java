package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.hal.ControlWord;
import edu.wpi.first.hal.DriverStationJNI;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {
	private static DriveSubsystem s_subsystem;

	private final CANSparkMax m_frontLeft = new CANSparkMax(DriveConstants.kFrontLeftID, MotorType.kBrushless);
	private final CANSparkMax m_frontRight = new CANSparkMax(DriveConstants.kFrontRightID, MotorType.kBrushless);
	private final CANSparkMax m_backLeft = new CANSparkMax(DriveConstants.kBackLeftID, MotorType.kBrushless);
	private final CANSparkMax m_backRight = new CANSparkMax(DriveConstants.kBackRightID, MotorType.kBrushless);

	private final RelativeEncoder m_leftEncoder = m_frontLeft.getEncoder();
	private final RelativeEncoder m_rightEncoder = m_frontRight.getEncoder();
	private final SparkMaxPIDController m_leftPIDController = m_frontLeft.getPIDController();
	private final SparkMaxPIDController m_rightPIDController = m_frontRight.getPIDController();

	private final AHRS m_gyro = new AHRS(DriveConstants.kGyroPort);

	private final DifferentialDriveOdometry m_odometry;

	ControlWord m_controlWord = new ControlWord();
	/** Prevents the motors from continuously being set to brake or coast mode */
	private boolean wasDisabled;

	public DriveSubsystem() {
		// Singleton
		if (s_subsystem != null) {
			try {
				throw new Exception("Drive subsystem already initialized!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		s_subsystem = this;
		m_frontLeft.restoreFactoryDefaults();
		m_frontLeft.setInverted(DriveConstants.kFrontLeftInvert);
		m_frontLeft.setIdleMode(IdleMode.kBrake);
		m_frontLeft.enableVoltageCompensation(12);
		m_frontLeft.setSmartCurrentLimit(DriveConstants.kSmartCurrentLimit);
		m_frontLeft.setSecondaryCurrentLimit(DriveConstants.kPeakCurrentLimit,
				DriveConstants.kPeakCurrentDurationMillis);
		m_frontLeft.setOpenLoopRampRate(DriveConstants.kRampRate);

		m_backLeft.restoreFactoryDefaults();
		m_backLeft.setIdleMode(IdleMode.kCoast);
		m_backLeft.enableVoltageCompensation(12);
		m_backLeft.setSmartCurrentLimit(DriveConstants.kSmartCurrentLimit);
		m_backLeft.setSecondaryCurrentLimit(DriveConstants.kPeakCurrentLimit,
				DriveConstants.kPeakCurrentDurationMillis);
		m_backLeft.setOpenLoopRampRate(DriveConstants.kRampRate);
		m_backLeft.follow(m_frontLeft, DriveConstants.kBackLeftOppose);

		m_frontRight.restoreFactoryDefaults();
		m_frontRight.setInverted(DriveConstants.kFrontRightInvert);
		m_frontRight.setIdleMode(IdleMode.kBrake);
		m_frontRight.enableVoltageCompensation(12);
		m_frontRight.setSmartCurrentLimit(DriveConstants.kSmartCurrentLimit);
		m_frontRight.setSecondaryCurrentLimit(DriveConstants.kPeakCurrentLimit,
				DriveConstants.kPeakCurrentDurationMillis);
		m_frontRight.setOpenLoopRampRate(DriveConstants.kRampRate);

		m_backRight.restoreFactoryDefaults();
		m_backRight.setIdleMode(IdleMode.kCoast);
		m_backRight.enableVoltageCompensation(12);
		m_backRight.setSmartCurrentLimit(DriveConstants.kSmartCurrentLimit);
		m_backRight.setSecondaryCurrentLimit(DriveConstants.kPeakCurrentLimit,
				DriveConstants.kPeakCurrentDurationMillis);
		m_backRight.setOpenLoopRampRate(DriveConstants.kRampRate);
		m_backRight.follow(m_frontRight, DriveConstants.kBackRightOppose);

		m_leftEncoder.setPositionConversionFactor(DriveConstants.kEncoderPositionConversionFactor);
		m_leftEncoder.setVelocityConversionFactor(DriveConstants.kEncoderVelocityConversionFactor);

		m_rightEncoder.setPositionConversionFactor(DriveConstants.kEncoderPositionConversionFactor);
		m_rightEncoder.setVelocityConversionFactor(DriveConstants.kEncoderVelocityConversionFactor);

		m_leftPIDController.setP(DriveConstants.kP);
		m_leftPIDController.setI(DriveConstants.kI);
		m_leftPIDController.setIZone(DriveConstants.kIz);
		m_leftPIDController.setD(DriveConstants.kD);
		m_leftPIDController.setFF(DriveConstants.kFF);
		m_leftPIDController.setOutputRange(DriveConstants.kMinOutput, DriveConstants.kMaxOutput);
		m_leftPIDController.setFeedbackDevice(m_leftEncoder);

		m_rightPIDController.setP(DriveConstants.kP);
		m_rightPIDController.setI(DriveConstants.kI);
		m_rightPIDController.setIZone(DriveConstants.kIz);
		m_rightPIDController.setD(DriveConstants.kD);
		m_rightPIDController.setFF(DriveConstants.kFF);
		m_rightPIDController.setOutputRange(DriveConstants.kMinOutput, DriveConstants.kMaxOutput);
		m_rightPIDController.setFeedbackDevice(m_rightEncoder);

		m_odometry = new DifferentialDriveOdometry(m_gyro.getRotation2d(), 0, 0);
		resetEncoders();
		setBackBrake();
	}

	public static DriveSubsystem get() {
		return s_subsystem;
	}

	public void periodic() {
		SmartDashboard.putNumber("Heading", getHeading());
		SmartDashboard.putNumber("L RPM: ", getLeftEncoderVelocity());
		SmartDashboard.putNumber("R RPM: ", getRightEncoderVelocity());

		SmartDashboard.putNumber("FL: ", m_frontLeft.getOutputCurrent());
		SmartDashboard.putNumber("FR: ", m_frontRight.getOutputCurrent());
		SmartDashboard.putNumber("BL: ", m_backLeft.getOutputCurrent());
		SmartDashboard.putNumber("BR: ", m_backRight.getOutputCurrent());

		SmartDashboard.putNumber("Curr X", DriveSubsystem.get().getPose().getX());
		SmartDashboard.putNumber("Curr Y", DriveSubsystem.get().getPose().getY());

		SmartDashboard.putNumber("Curr Encoder Position", DriveSubsystem.get().getAverageEncoderDistance());
		SmartDashboard.putString("Wheel Encoder Positions", String.format("(%.3f, %.3f)", 
				DriveSubsystem.get().getLeftEncoderPosition(), DriveSubsystem.get().getRightEncoderPosition()));
		m_odometry.update(m_gyro.getRotation2d(), getLeftEncoderPosition(),
				getRightEncoderPosition());
		// wasDisabled exists so the motors aren't constantly set to brake or coast mode
		// Without it, the code would continuously set the motors in brake or coast mode
		// If the robot is enabled, put the motors in brake mode
		if (isEnabledFast() && wasDisabled) {
			setFrontBrake();
			setBackCoast();
			wasDisabled = false;
			// If the robot is disabled, put the motors in coast mode
		} else if (!isEnabledFast() && !wasDisabled) {
			//setFrontCoast();
			setBackBrake();
			wasDisabled = true;
		}
	}

	/**
	 * @return The left encoder position (meters)
	 */
	public double getLeftEncoderPosition() {
		return m_leftEncoder.getPosition();
	}

	/**
	 * @return The right encoder position (meters)
	 */
	public double getRightEncoderPosition() {
		return m_rightEncoder.getPosition();
	}

	/**
	 * @return The average encoder distance of both encoders (meters)
	 */
	public double getAverageEncoderDistance() {
		return (getLeftEncoderPosition() + getRightEncoderPosition()) / 2.0;
	}

	/**
	 * @return The velocity of the left encoder (meters/s)
	 */
	public double getLeftEncoderVelocity() {
		return m_leftEncoder.getVelocity();
	}

	/**
	 * @return The velocity of the right encoder (meters/s)
	 */
	public double getRightEncoderVelocity() {
		return m_rightEncoder.getVelocity();
	}

	/**
	 * @return Pose of the robot
	 */
	public Pose2d getPose() {
		return m_odometry.getPoseMeters();
	}

	/**
	 * @return The heading of the gyro (degrees)
	 */
	public double getHeading() {
		return m_gyro.getYaw() * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
	}

	public double getPitch() {
		return m_gyro.getPitch() * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
	}

	/**
	 * @return The rate of the gyro turn (deg/s)
	 */
	public double getTurnRate() {
		return m_gyro.getRate() * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
	}

	/**
	 * Resets gyro position to 0
	 */
	public void zeroHeading() {
		m_gyro.zeroYaw();
	}

	/**
	 * Sets both encoders to 0
	 */
	public void resetEncoders() {
		m_leftEncoder.setPosition(0);
		m_rightEncoder.setPosition(0);
	}

	/**
	 * @param pose Pose to set the robot to
	 */
	public void resetOdometry(Pose2d pose) {
		resetEncoders();
		// TODO make the new position match the original we set in the constructor
		m_odometry.resetPosition(m_gyro.getRotation2d(), 0, 0, pose);
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
		m_frontLeft.set(leftSpeed);
		m_backLeft.set(leftSpeed);
		m_frontRight.set(rightSpeed);
		m_backRight.set(rightSpeed);
	}

	/**
	 * Hacky method that returns if the robot is enabled to bypass a WPILib bug with
	 * loop overruns
	 * 
	 * @author Jonathan Waters
	 * @return Whether or not the robot is enabled
	 */
	public boolean isEnabledFast() {
		DriverStationJNI.getControlWord(m_controlWord);

		return m_controlWord.getEnabled();
	}

	public void setBackBrake(){
		
		m_backLeft.setIdleMode(IdleMode.kBrake);
		m_backRight.setIdleMode(IdleMode.kBrake);
	}
	public void setBackCoast(){
		m_backLeft.setIdleMode(IdleMode.kCoast);
		m_backRight.setIdleMode(IdleMode.kCoast);
	}
	public void setFrontCoast(){
		m_frontLeft.setIdleMode(IdleMode.kCoast);
		m_frontRight.setIdleMode(IdleMode.kCoast);
	}
	public void setFrontBrake(){
		m_frontLeft.setIdleMode(IdleMode.kBrake);
		m_frontRight.setIdleMode(IdleMode.kBrake);
	}
}
