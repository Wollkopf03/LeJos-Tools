package main;

import fahrt.Fahrten;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import util.Task;
import util.Tasks;

public class Main {

	private boolean motorwackeln = true, blind = false, running = false;
	private int fahrt = 1;

	public static void main(String[] args) throws InterruptedException {
		new Main();
	}

	public Main() throws InterruptedException {
		motorwackeln();
		print();
		while (true) {
			Thread.sleep(1000 / 20);
			if (!running) {
				if (Button.UP.isDown()) {
					while (Button.UP.isDown())
						;
					motorwackeln = !motorwackeln;
					print();
				}
				if (Button.RIGHT.isDown()) {
					while (Button.RIGHT.isDown())
						;
					fahrt++;
					print();
				}
				if (Button.DOWN.isDown()) {
					while (Button.DOWN.isDown())
						;
					blind = !blind;
					print();
				}
				if (Button.LEFT.isDown()) {
					while (Button.LEFT.isDown())
						;
					fahrt--;
					print();
				}
				if (Button.ENTER.isDown()) {
					while (Button.ENTER.isDown())
						;
					start();
				}
			}
			if (Button.ESCAPE.isDown())
				stop();
			if (fahrt <= 0) {
				fahrt = Fahrten.getFahrten();
				print();
			}
			if (fahrt > Fahrten.getFahrten()) {
				fahrt = 1;
				print();
				motorwackeln();
			}
		}
	}

	private void print() {
		LCD.clear();
		LCD.drawInt(fahrt, 9, 4);
		if (fahrt == 1)
			LCD.drawInt(Fahrten.getFahrten(), 4, 4);
		else
			LCD.drawInt(fahrt - 1, 4, 4);
		if (fahrt == Fahrten.getFahrten())
			LCD.drawInt(1, 14, 4);
		else
			LCD.drawInt(fahrt + 1, 14, 4);
		LCD.drawString("moterwackeln", 4, 2, motorwackeln);
		if (blind)
			LCD.drawString("blind", 6, 6);
		else
			LCD.drawString("sensor", 6, 6, true);
	}

	public void motorwackeln() {
		new Task() {

			private boolean direction = true;

			@Override
			public void action() {
				if (!getRunning())
					if (getMotorwackeln())
						if (direction) {
							Motor.A.forward();
							Motor.D.forward();
							direction = !direction;
						} else {
							Motor.A.backward();
							Motor.D.backward();
							direction = !direction;
						}
					else {
						Motor.A.flt(true);
						Motor.D.flt(true);
					}
			}

		};
	}

	public void start() {
		running = true;
		Tasks.cancelAll();
		new Task() {

			@Override
			public void action() {
				Fahrten.getFahrt(fahrt).fahrt(blind);
				running = false;
				fahrt++;
				print();
			}

		};
	}

	public void stop() {
		running = false;
		Tasks.cancelAll();
		motorwackeln();
	}

	public boolean getMotorwackeln() {
		return motorwackeln;
	}

	public boolean getRunning() {
		return running;
	}
}
