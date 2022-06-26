package Roboter;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3GyroSensor;

public class GyroSensor {

	private EV3GyroSensor sensor;
	private PID pid;

	protected int offsetInDegrees = 0;
	protected int offsetInRate = 0;

	private float[] sample;

	public GyroSensor(int port) {
		sensor = new EV3GyroSensor(LocalEV3.get().getPort("S" + port));
	}

	public int getAngle() {
		sensor.getAngleMode().fetchSample(sample, 0);
		return (int) sample[0] + offsetInDegrees;
	}

	public int getRate() {
		sensor.getRateMode().fetchSample(sample, 0);
		return (int) sample[0] + offsetInRate;
	}

	public void rotate(int grad) {
		int now = getAngle();
		if (grad > now) {
			Motor.B.forward();
			Motor.C.backward();
		} else {
			Motor.B.forward();
			Motor.C.backward();
		}
		while (Math.abs(grad) != getAngle() + now)
			;
		Motor.B.stop();
		Motor.C.stop();
	}

	public void reset() {
		offsetInDegrees = getAngle() * -1;
		offsetInRate = getRate() * -1;
	}

	private PID getPID() {
		if (pid == null)
			pid = new PID();
		return pid;
	}

	public void PID(double speed, double kp, double ki, double kd, double soll, int port) {
		getPID().init(speed, kp, ki, kd, soll + offsetInDegrees, sensor);
	}

	public void stop() {
		int b;
		int c;
		b = Motor.B.getTachoCount();
		c = Motor.C.getTachoCount();
		pid.ende();
		if (b != Motor.B.getTachoCount() || c != Motor.B.getTachoCount()) {
			Motor.B.rotate(b - Motor.B.getTachoCount());
			Motor.C.rotate(c - Motor.C.getTachoCount());
		}
	}

	public int getAbsoluteAngle() {
		sensor.getAngleMode().fetchSample(sample, 0);
		return (int) sample[0];
	}

	public int getAbsoluteRate() {
		sensor.getRateMode().fetchSample(sample, 0);
		return (int) sample[0];
	}
}