package frc.robot;

public final class Constants {
	public static final class ArduinoConstants {
		/**
		 * The I2C address of the Arduino as defined by the address passed into
		 * Wire.begin() in the Arduino code
		 */
		public static final int kAddress = 0x18;
	}

	public static final class ControllerConstants {
		public static final int kDriverControllerPort = 0;
		public static final int kOperatorControllerPort = 1;
		public static final double kDeadzone = 0.1;
		public static final double kTriggerDeadzone = .05;
	}

	public static final class DriveConstants {
		public static final int kFrontLeftID = 3;
		public static final boolean kFrontLeftInvert = false;
		public static final int kBackLeftID = 4;
		public static final boolean kBackLeftOppose = false;

		public static final int kFrontRightID = 1;
		public static final boolean kFrontRightInvert = true;
		public static final int kBackRightID = 2;
		public static final boolean kBackRightInvert = true;

		// TODO re-evaluate current limits
		public static final int kSmartCurrentLimit = 55;
		public static final int kPeakCurrentLimit = 65;
		public static final int kPeakCurrentDurationMillis = 0;

		public static final double kTurningMultiplier = 0.5;
		public static final double kSpeedLimitFactor = 0.5;

	}

	public static final class CannonConstants {
		public static final int kHubID = 1;
		/**
		 * At 0 PSI, the analog pressure sensor reads ~0.47 volts. At 45 PSI, the analog
		 * pressure sensor reads 1.37 volts.
		 */
		public static final double kPsiPerVolt = 45 / (1.37 - 0.47);
		public static final int kLEDCount = 10;
		public static final int kSetCannonLEDCode = 50;
		public static final int kResetCannonLEDCode = 51;
		public static final int kLeftStripCode = 1;
		public static final int kMiddleStripCode = 2;
		public static final int kRightStripCode = 3;
	}

	public static final class LightAndHornConstants {
		public static final int kHornPort = 0;
		public static final int kLightPort = 1;
	}
}