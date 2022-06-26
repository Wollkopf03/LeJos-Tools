package Roboter;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class UltraSchallSensor {

	private EV3UltrasonicSensor sensor;
	private PID pid;

	private float[] sample;

	public UltraSchallSensor(int port) {
		sensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S" + port));
	}

	public float getDistance() {
		sensor.getDistanceMode().fetchSample(sample, 0);
		return sample[0];
	}

	private PID getPID() {
		if (pid == null)
			pid = new PID();
		return pid;
	}

	public void PID(double speed, double kp, double ki, double kd, double soll, int port) {
		getPID().init(speed, kp, ki, kd, soll, sensor);
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
}