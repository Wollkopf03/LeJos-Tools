package Roboter;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensor {

	private EV3ColorSensor sensor;
	private SampleProvider red;
	private SampleProvider ambient;
	private SampleProvider rgb;
	private PID pid;

	private float[] redSample;
	private float[] ambientSample;
	private float[] rgbSample;

	public ColorSensor(int port) {
		sensor = new EV3ColorSensor(LocalEV3.get().getPort("S" + port));
		red = sensor.getMode("Red");
		ambient = sensor.getMode("Ambient");
		rgb = sensor.getMode("RGB");
		redSample = new float[red.sampleSize()];
		ambientSample = new float[ambient.sampleSize()];
		rgbSample = new float[rgb.sampleSize()];
	}

	public int getColor() {
		return sensor.getColorID();
	}

	public int getReflection() {
		red.fetchSample(redSample, 0);
		return (int) (redSample[0] * 100);
	}

	public int getAmbient() {
		ambient.fetchSample(ambientSample, 0);
		return (int) ambientSample[0];
	}

	public float[] getRGB() {
		rgb.fetchSample(rgbSample, 0);
		return rgbSample;
	}

	private PID getPID() {
		if (pid == null)
			pid = new PID();
		return pid;
	}

	public void PID(double speed, double kp, double ki, double kd, double soll, int port) {
		getPID().init(speed, kp, ki, kd, soll, red);
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
