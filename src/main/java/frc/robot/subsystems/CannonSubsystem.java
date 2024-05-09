// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CannonSubsystem extends SubsystemBase {
  private final PneumaticHub m_hub = new PneumaticHub();
  private final Solenoid m_solenoid0 = m_hub.makeSolenoid(0);
  private final Solenoid m_solenoid1 = m_hub.makeSolenoid(1);
  private final Solenoid m_leftCannon = m_hub.makeSolenoid(2);
  private final Solenoid m_solenoid3 = m_hub.makeSolenoid(3);
  private final Solenoid m_solenoid5 = m_hub.makeSolenoid(5);

  /** Creates a new CannonSubsystem. */
  public CannonSubsystem() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Command fire() {
    return runEnd(() -> {
      // m_solenoid0.set(true);
      // m_solenoid1.set(true);
      m_leftCannon.set(true);
      // m_solenoid3.set(true);
      // m_solenoid5.set(true);
    }, () -> {
      // m_solenoid0.set(false);
      // m_solenoid1.set(false);
      m_leftCannon.set(false);
      // m_solenoid3.set(false);
      // m_solenoid5.set(false);
    });
  }
}
