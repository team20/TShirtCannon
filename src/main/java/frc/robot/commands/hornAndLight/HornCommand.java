// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.hornAndLight;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LightAndHornSubsystem;

public class HornCommand extends Command {
  LightAndHornSubsystem m_lightAndHornSubsystem;
  /** Creates a new HornCommand. */
  public HornCommand(LightAndHornSubsystem lightAndHornSubsystem) {
    m_lightAndHornSubsystem = lightAndHornSubsystem;
    addRequirements(m_lightAndHornSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_lightAndHornSubsystem.hornOn();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_lightAndHornSubsystem.hornOff();
  }

}
