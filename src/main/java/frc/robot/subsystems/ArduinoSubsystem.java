// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArduinoSubsystem extends SubsystemBase {
	private SerialPort m_arduino;

	/** The bytes that control the LED mode */
	public enum StatusCode {
		RESET((byte) 8),
		RAINBOW_PARTY_FUN_TIME((byte) 1),
		SMOOTH_RAINBOW_PARTY_FUN_TIME((byte) 2),
		SHEN_COLORS((byte) 5),
		DEFAULT((byte) 20);

		public byte code;

		private StatusCode(byte c) {
			code = c;
		}
	}

	/** Creates a new ArduinoSubsystem. */
	public ArduinoSubsystem(SerialPort arduino) {
		m_arduino = arduino;
		setCode(StatusCode.SMOOTH_RAINBOW_PARTY_FUN_TIME);
	}

	public void setCode(StatusCode code) {
		m_arduino.write(new byte[] { code.code }, 1);
	}

	public Command ledColor(StatusCode code) {
		return runOnce(() -> setCode(code));
	}
}
