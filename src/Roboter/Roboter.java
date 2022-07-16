package Roboter;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public abstract class Roboter {

	private static Wheel leftWheel;
	private static Wheel rightWheel;
	private static WheeledChassis chassis;
	private static MovePilot pilot;

	private static ColorSensor[] colorSensor = new ColorSensor[4];
	private static GyroSensor[] gyroSensor = new GyroSensor[4];
	private static UltraSchallSensor[] ultraSchallSensor = new UltraSchallSensor[4];
	private static HiTColorSensor[] hiTechnicColorSensor = new HiTColorSensor[4];

	public static void init(double leftDiameter, double rightDiameter, double trackWidth, double linAcc,
			double angAcc) {
		init(leftDiameter, rightDiameter, trackWidth);
		setPilotAcc(linAcc, angAcc);
	}

	public static void init(double leftDiameter, double rightDiameter, double trackWidth) {
		leftWheel = WheeledChassis.modelWheel(Motor.B, leftDiameter).offset(-trackWidth / 2);
		rightWheel = WheeledChassis.modelWheel(Motor.C, rightDiameter).offset(trackWidth / 2);
		chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		Sound.beepSequenceUp();
		new Thread() {
			@Override
			public void run() {
				while (true) {
					int pressed = Button.waitForAnyPress();
					if (pressed == Button.ID_ESCAPE)
						System.exit(0);
				}
			}
		}.start();
	}

	public static void reset() {
		Motor.A.resetTachoCount();
		Motor.B.resetTachoCount();
		Motor.C.resetTachoCount();
		Motor.D.resetTachoCount();
		for (int i = 0; i < 4; i++)
			if (gyroSensor[i] != null)
				gyroSensor[i].reset();
	}

	public static void quickstart(String message, Key key) {
		LCD.drawString(message, 2, 2);
		boolean pressed = false;
		int pressedKey;
		while (!pressed) {
			pressedKey = Button.waitForAnyPress();
			if (key == null || pressedKey == key.getId())
				pressed = true;
		}
		reset();
		wait(0.25);
		LCD.clearDisplay();
	}

	public static void quickstart(String message, int key) {
		LCD.drawString(message, 2, 2);
		boolean pressed = false;
		int pressedKey;
		while (!pressed) {
			pressedKey = Button.waitForAnyPress();
			if (pressedKey == key || key == Button.ID_ALL)
				pressed = true;
		}
		reset();
		wait(0.25);
		LCD.clearDisplay();
	}

	public static void wait(double sekunden) {
		Delay.msDelay((int) (sekunden * 1000));
	}

	public static void on() {
		pilot.setLinearSpeed(25);
		pilot.forward();
	}

	public static void on(double speed) {
		Motor.A.flt();
		Motor.B.flt();
		Motor.C.flt();
		Motor.D.flt();
		if (speed > 0) {
			pilot.setLinearSpeed(speed);
			pilot.forward();
		} else if (speed < 0) {
			pilot.setLinearAcceleration(-speed);
			pilot.backward();
		}
	}

	public static void off() {
		pilot.stop();
	}

	public static void off(boolean hard) {
		pilot.stop();
		if (hard) {
			Motor.A.stop();
			Motor.B.stop();
			Motor.C.stop();
			Motor.D.stop();
		}
	}

	public static void standby() {
		Motor.A.flt();
		Motor.B.flt();
		Motor.C.flt();
		Motor.D.flt();
	}

	public static void travel(double distanz) {
		pilot.travel(distanz);
	}

	public static void travel(double distanz, boolean warten) {
		pilot.travel(distanz, !warten);
	}

	public static void travel(double distanz, double speed) {
		if (speed > 0) {
			pilot.setLinearSpeed(speed);
			pilot.travel(distanz);
		} else if (speed < 0) {
			pilot.setLinearSpeed(-speed);
			pilot.travel(-distanz);
		}
	}

	public static void travel(double distanz, double speed, boolean warten) {
		if (speed > 0) {
			pilot.setLinearSpeed(speed);
			pilot.travel(distanz, !warten);
		} else if (speed < 0) {
			pilot.setLinearSpeed(-speed);
			pilot.travel(-distanz, !warten);
		}
	}

	public static void setPilotAcc(double fahrBeschleunigung, double drehBeschleunigung) {
		if (fahrBeschleunigung > 0 && drehBeschleunigung > 0) {
			pilot.setAngularAcceleration(drehBeschleunigung);
			pilot.setLinearAcceleration(fahrBeschleunigung);
		}
		Motor.A.setAcceleration(6000);
		Motor.B.setAcceleration(6000);
		Motor.C.setAcceleration(6000);
		Motor.D.setAcceleration(6000);
	}

	public static void setMotorAcc(char motor, int acceleration) {
		if (acceleration > 0) {
			switch (motor) {
				case 'a':
				case 'A':
					Motor.A.setAcceleration(acceleration);
					break;
				case 'b':
				case 'B':
					Motor.A.setAcceleration(acceleration);
					break;
				case 'c':
				case 'C':
					Motor.A.setAcceleration(acceleration);
					break;
				case 'd':
				case 'D':
					Motor.A.setAcceleration(acceleration);
					break;
			}
		}
	}

	public static void travelArch(double radius, double distanz) {
		pilot.travelArc(radius, distanz);
	}

	public static void travelArch(double radius, double distanz, boolean warten) {
		pilot.travelArc(radius, distanz, !warten);
	}

	public static void travelArch(double radius, double distanz, double speed) {
		if (speed > 0) {
			pilot.setLinearSpeed(speed);
			pilot.setAngularSpeed(speed);
			pilot.travelArc(radius, distanz);
		} else if (speed > 0) {
			pilot.setLinearSpeed(-speed);
			pilot.setAngularSpeed(-speed);
			pilot.travelArc(radius, -distanz);
		}
	}

	public static void travelArch(double radius, double distanz, double speed, boolean warten) {
		if (speed > 0) {
			pilot.setLinearSpeed(speed);
			pilot.setAngularSpeed(speed);
			pilot.travelArc(radius, distanz, !warten);
		} else if (speed > 0) {
			pilot.setLinearSpeed(-speed);
			pilot.setAngularSpeed(-speed);
			pilot.travelArc(radius, -distanz, warten);
		}
	}

	public static void turn(int grad) {
		pilot.rotate(grad);
	}

	public static void turn(int grad, boolean warten) {
		pilot.rotate(grad, warten);
	}

	public static void turn(int grad, double speed, boolean warten) {
		if (speed > 0) {
			pilot.setAngularSpeed(speed);
			pilot.rotate(grad, !warten);
		} else if (speed < 0) {
			pilot.setAngularSpeed(-speed);
			pilot.rotate(-grad, !warten);
		}
	}

	public static void turn(int grad, double speed) {
		if (speed > 0) {
			pilot.setAngularSpeed(speed);
			pilot.rotate(grad);
		} else if (speed < 0) {
			pilot.setAngularSpeed(-speed);
			pilot.rotate(-grad);
		}
	}

	public static void turnMotor(char motor) {
		if (motor == 'A' || motor == 'a') {
			Motor.A.setSpeed(25);
			Motor.A.forward();
		} else if (motor == 'B' || motor == 'b') {
			Motor.B.setSpeed(25);
			Motor.B.forward();
		} else if (motor == 'C' || motor == 'c') {
			Motor.C.setSpeed(25);
			Motor.C.forward();
		} else if (motor == 'D' || motor == 'd') {
			Motor.D.setSpeed(25);
			Motor.D.forward();
		}
	}

	public static void turnMotor(char motor, int grad, double speed, boolean warten) {
		if (speed > 0) {
			if (motor == 'A' || motor == 'a') {
				Motor.A.setSpeed((int) speed);
				Motor.A.rotate(grad, !warten);
			} else if (motor == 'B' || motor == 'b') {
				Motor.B.setSpeed((int) speed);
				Motor.B.rotate(grad, !warten);
			} else if (motor == 'C' || motor == 'c') {
				Motor.C.setSpeed((int) speed);
				Motor.C.rotate(grad, !warten);
			} else if (motor == 'D' || motor == 'd') {
				Motor.D.setSpeed((int) speed);
				Motor.D.rotate(grad, !warten);
			}
		} else if (speed < 0) {
			if (motor == 'A' || motor == 'a') {
				Motor.A.setSpeed((int) -speed);
				Motor.A.rotate(-grad, !warten);
			} else if (motor == 'B' || motor == 'b') {
				Motor.B.setSpeed((int) -speed);
				Motor.B.rotate(-grad, !warten);
			} else if (motor == 'C' || motor == 'c') {
				Motor.C.setSpeed((int) -speed);
				Motor.C.rotate(-grad, !warten);
			} else if (motor == 'D' || motor == 'd') {
				Motor.D.setSpeed((int) -speed);
				Motor.D.rotate(-grad, !warten);
			}
		}
	}

	public static void turnMotor(char motor, int grad, double speed) {
		if (speed > 0) {
			if (motor == 'A' || motor == 'a') {
				Motor.A.setSpeed((int) speed);
				Motor.A.rotate(grad);
			} else if (motor == 'B' || motor == 'b') {
				Motor.B.setSpeed((int) speed);
				Motor.B.rotate(grad);
			} else if (motor == 'C' || motor == 'c') {
				Motor.C.setSpeed((int) speed);
				Motor.C.rotate(grad);
			} else if (motor == 'D' || motor == 'd') {
				Motor.D.setSpeed((int) speed);
				Motor.D.rotate(grad);
			}
		} else if (speed < 0) {
			if (motor == 'A' || motor == 'a') {
				Motor.A.setSpeed((int) -speed);
				Motor.A.rotate(-grad);
			} else if (motor == 'B' || motor == 'b') {
				Motor.B.setSpeed((int) -speed);
				Motor.B.rotate(-grad);
			} else if (motor == 'C' || motor == 'c') {
				Motor.C.setSpeed((int) -speed);
				Motor.C.rotate(-grad);
			} else if (motor == 'D' || motor == 'd') {
				Motor.D.setSpeed((int) -speed);
				Motor.D.rotate(-grad);
			}
		}
	}

	public static void turnMotor(char motor, int grad, boolean warten) {
		if (motor == 'A' || motor == 'a')
			Motor.A.rotate(grad);
		else if (motor == 'B' || motor == 'b')
			Motor.B.rotate(grad);
		else if (motor == 'C' || motor == 'c')
			Motor.C.rotate(grad);
		else if (motor == 'D' || motor == 'd')
			Motor.D.rotate(grad);
	}

	public static void turnMotor(char motor, double speed) {
		if (speed > 0) {
			if (motor == 'A' || motor == 'a') {
				Motor.A.setSpeed((int) speed);
				Motor.A.forward();
			} else if (motor == 'B' || motor == 'b') {
				Motor.B.setSpeed((int) speed);
				Motor.B.forward();
			} else if (motor == 'C' || motor == 'c') {
				Motor.C.setSpeed((int) speed);
				Motor.C.forward();
			} else if (motor == 'D' || motor == 'd') {
				Motor.D.setSpeed((int) speed);
				Motor.D.forward();
			}
		} else if (speed < 0) {
			if (motor == 'A' || motor == 'a') {
				Motor.A.setSpeed((int) -speed);
				Motor.A.backward();
			} else if (motor == 'B' || motor == 'b') {
				Motor.B.setSpeed((int) -speed);
				Motor.B.backward();
			} else if (motor == 'C' || motor == 'c') {
				Motor.C.setSpeed((int) -speed);
				Motor.C.backward();
			} else if (motor == 'D' || motor == 'd') {
				Motor.D.setSpeed((int) -speed);
				Motor.D.backward();
			}
		}
	}

	public static void turnTo(int degree, int port) {
		do {
			turn(getGyroSensor(port).getAbsoluteAngle() * -1);
		} while (getGyroSensor(port).getAbsoluteAngle() != 0);
	}

	public static MovePilot getPilot() {
		return pilot;
	}

	public static void addSensor(SensorType sensorType, int port) {
		switch (sensorType) {
			case Color:
				colorSensor[port - 1] = new ColorSensor(port);
				break;
			case HiTColor:
				hiTechnicColorSensor[port - 1] = new HiTColorSensor(port);
				break;
			case Gyro:
				gyroSensor[port - 1] = new GyroSensor(port);
				break;
			case UltraSchall:
				ultraSchallSensor[port - 1] = new UltraSchallSensor(port);
				break;
		}
	}

	public static ColorSensor getColorSensor(int port) {
		return colorSensor[port - 1];
	}

	public static GyroSensor getGyroSensor(int port) {
		return gyroSensor[port - 1];
	}

	public static UltraSchallSensor getUltraSchallSensor(int port) {
		return ultraSchallSensor[port - 1];
	}

	public static HiTColorSensor getHiTechnicColorSensor(int port) {
		return hiTechnicColorSensor[port - 1];
	}

	public static void resetTachoCount() {
		Motor.B.resetTachoCount();
		Motor.C.resetTachoCount();
	}

	public enum SensorType {
		Color, HiTColor, Gyro, UltraSchall
	}
}
