package Roboter;

import java.util.Timer;
import java.util.TimerTask;

import lejos.hardware.motor.Motor;
import lejos.robotics.SampleProvider;

public class PID {

	private Timer timer;
	private Task task;

	public PID() {
		timer = new Timer();
	}

	public void init(double speed, double kp, double ki, double kd, double soll, SampleProvider sensor) {
		task.cancel();
		task = new Task(speed, kp, ki, kd, soll, sensor);
		timer.scheduleAtFixedRate(task, 0, 10);
	}

	public void ende() {
		task.cancel();
		Motor.B.stop();
		Motor.C.stop();
	}
}

class Task extends TimerTask {

	private SampleProvider sensor;

	private double soll;
	private double speed = 360;
	private double integral = 0;
	private double lasterror = 0;
	private double kp;
	private double ki;
	private double kd;

	float[] ist;

	public Task(double speed, double kp, double ki, double kd, double soll, SampleProvider sensor) {
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		this.speed = speed;
		this.soll = soll;
		this.sensor = sensor;
		ist = new float[sensor.sampleSize()];
		Motor.B.forward();
		Motor.C.forward();
	}

	@Override
	public void run() {
		sensor.fetchSample(ist, 0);
		ist[0] = ist[0] * 100;
		Motor.B.setSpeed((int) (speed + kp * (soll - ist[0]) + ki * integral + kd * (soll - ist[0] - lasterror)));
		Motor.C.setSpeed((int) (speed - kp * (soll - ist[0]) + ki * integral + kd * (soll - ist[0] - lasterror)));
		lasterror = (int) (soll - ist[0]);
		integral += (int) (soll - ist[0]);
		if (soll == (int) (ist[0])) {
			integral = 0;
		}
		System.out.println((int) Motor.B.getSpeed() + "   " + Motor.C.getSpeed());
	}
}