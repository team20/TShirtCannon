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

		public static final class Axis {
			public static final int kLeftX = 0;
			public static final int kLeftY = 1;
			public static final int kRightX = 2;
			public static final int kLeftTrigger = 3;
			public static final int kRightTrigger = 4;
			public static final int kRightY = 5;
		}

		public static final class Button {
			/** Left middle button */
			public static final int kSquare = 1;
			/** Bottom button */
			public static final int kX = 2;
			/** Right middle button */
			public static final int kCircle = 3;
			/** Top button */
			public static final int kTriangle = 4;
			public static final int kLeftBumper = 5;
			public static final int kRightBumper = 6;
			public static final int kLeftTrigger = 7;
			public static final int kRightTrigger = 8;
			public static final int kShare = 9;
			public static final int kOptions = 10;
			public static final int kLeftStick = 11;
			public static final int kRightStick = 12;
			public static final int kPS = 13;
			public static final int kTrackpad = 14;
		}

		public static final class DPad {
			public static final int kUp = 0;
			public static final int kRight = 90;
			public static final int kDown = 180;
			public static final int kLeft = 270;
		}
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

	public static final class PneumaticsConstants{
		public static final int hubID = 1000;

	}

	public static final class LightAndHornConstants{
		public static final int hornID = 0;
		public static final int lightID = 3;
		
	}

}
