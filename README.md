# Team 20's T-Shirt cannon

## Controls

The T-Shirt cannon is a standard tank drive, left trigger to turn left, right trigger to turn right, and left joystick to go forwards and backwards.

Charging the cannons uses the DPad. All cannons charge at the same time to the same pressure. There are three pressure levels, low (5 PSI), medium (7.5 PSI), and high (10 PSI). These can be adjusted via dashboard if desired. An Elastic layout file has been added to make it easy to access these controls. Pressing down charges to low pressure, right charges to medium pressure, and up charges to high pressure.

Firing the cannons requires the left bumper to be held down to prevent accidental firings. Once the left bumper is held down, press the square button to fire the left cannon, press the triangle button to fire the middle cannon, and press the circle button to fire the right cannon.

Press and hold the touchpad to turn on the horn, release to turn it off.

| Controls                                                                 | Action                   |
| ------------------------------------------------------------------------ | ------------------------ |
| [**Drive Controls**](./src/main/java/frc/robot/RobotContainer.java#L35)  |                          |
| Left Trigger                                                             | Spin left                |
| Right Trigger                                                            | Spin right               |
| Left Joystick                                                            | Drive forward/backward   |
| [**Cannon Controls**](./src/main/java/frc/robot/RobotContainer.java#L42) |                          |
| DPad Down                                                                | Charge cannons to low    |
| DPad Right                                                               | Charge cannons to medium |
| DPad Up                                                                  | Charge cannons to high   |
| Left Bumper + Square                                                     | Fire left cannon         |
| Left Bumper + Triangle                                                   | Fire middle cannon       |
| Left Bumper + Circle                                                     | Fire right cannon        |
| Left Bumper + X                                                          | Fire all cannons         |
| [**Horn Controls**](./src/main/java/frc/robot/RobotContainer.java#L39)   |                          |
| Touchpad (hold)                                                          | Turn on the horn         |
| [**LED Controls**](./src/main/java/frc/robot/RobotContainer.java#L51)    |                          |
| Share                                                                    | RainbowPartyFunTime      |
| Options                                                                  | Shen Lights              |

## The cannon

There are three cannons, each with a double solenoid and an analog pressure sensor.

### Pressure calculations

The analog pressure sensor starts at ~0.47 volts when the PSI is 0. At 45 PSI, the voltage is ~1.37 volts. This works out to 50 PSI per volt. To [calculate the pressure from the analog sensor](./src/main/java/frc/robot/subsystems/CannonSubsystem.java#L42), we read the voltage, subtract 0.47 (to normalize the voltage to 0), and multiply by 50 to get the PSI of the cannon.

### Controlling the cannon solenoids

When the double solenoid is set to `kForward`, the cannon builds pressure. When the double solenoid is set to `kReverse`, the cannon fires.

The [command](./src/main/java/frc/robot/subsystems/CannonSubsystem.java#L82) that charges all the cannons simultaneously checks if a cannon is below the target PSI. If it is, it will set the cannon's solenoid to `kReverse` and charges the cannon. If the cannon's pressure is above the target PSI, it sets the cannon to `kOff`, stopping the charge. The command runs these checks for all three cannons. The command ends when all the cannons are above the target pressure. As a backup measure, the [command will set all solenoids to `kOff`](./src/main/java/frc/robot/subsystems/CannonSubsystem.java#L107) when the command is ended. This also means if you fire a cannon early, all cannons will stop charging.

The [commands](./src/main/java/frc/robot/subsystems/CannonSubsystem.java#L153) that fire a cannon sets the solenoid to `kForward`, then sets the solenoid to `kOff`.

## The horn and light

The horn and light are each controlled by a [VEX Relay](https://web.archive.org/web/20030419143006/http://www.innovationfirst.com/FirstRobotics/pdfs/SpikeBLUEUsersManual.pdf) (connected to the Relay ports).

The horn is honked by setting the relay to `kOn`. The [command](./src/main/java/frc/robot/subsystems/LightAndHornSubsystem.java#L22) that does this then also sets the relay to `kOff` to turn off the horn when the command ends. The [button binding](./src/main/java/frc/robot/RobotContainer.java#L40) is hold to run, so the command ends and the horn turns off when the button is released.

The light can be turned on by setting the relay to either `kForward` or `kReverse`, which represent different directions that the light can spin. `kForward` spins the light clockwise, `kReverse` spins the light counterclockwise.
