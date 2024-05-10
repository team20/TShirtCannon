// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ArduinoConstants;

public class ArduinoSubsystem extends SubsystemBase {
	/**
	 * The I2C device we're connecting to. Port.kMXP means we use the I2C connection
	 * on the MXP port, which runs through the navX
	 */
	private I2C i2c = new I2C(Port.kMXP, ArduinoConstants.kAddress);
	/** The byte that indicates what LED mode we want to use */
	private byte[] m_statusCode = new byte[1];

	/** The bytes that control the LED mode */
	public enum StatusCode {
		RESET((byte) 8),
		RAINBOW_PARTY_FUN_TIME((byte) 1),
		SMOOTH_RAINBOW_PARTY_FUN_TIME((byte) 2),
		BLINKING_YELLOW((byte) 3),
		BLINKING_PURPLE((byte) 4),
		SHEN_COLORS((byte) 5),
		DEFAULT((byte) 20);

		public byte code;

		private StatusCode(byte c) {
			code = c;
		}
	}

	/** Creates a new ArduinoSubsystem. */
	public ArduinoSubsystem() {
		setCode(StatusCode.DEFAULT);
	}

	public void setCode(StatusCode code) {
		i2c.writeBulk(m_statusCode);
	}

	public Command ledColor(StatusCode code) {
		return runOnce(() -> setCode(code));
	}
}
