package Roboter;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.robotics.SampleProvider;

public class HiTColorSensor {

	private HiTechnicColorSensor sensor;
	private SampleProvider ambient;
	private SampleProvider rgb;

	private float[] ambientSample;
	private float[] rgbSample;

	public HiTColorSensor(int port) {
		sensor = new HiTechnicColorSensor(LocalEV3.get().getPort("S" + port));
		ambient = sensor.getMode("Ambient");
		rgb = sensor.getMode("RGB");
		ambientSample = new float[ambient.sampleSize()];
		rgbSample = new float[rgb.sampleSize()];
	}

	public int getColor() {
		return sensor.getColorID();
	}

	public int getAmbient(int port) {
		ambient.fetchSample(ambientSample, 0);
		return (int) ambientSample[0];
	}

	public float[] getRGB(int port) {
		rgb.fetchSample(rgbSample, 0);
		return rgbSample;
	}
}
