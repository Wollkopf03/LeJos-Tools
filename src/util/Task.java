package util;

public abstract class Task extends Thread {

	{
		Tasks.addTask(this);
	}

	@Override
	public final void run() {
		while (!isInterrupted())
			try {
				action();
			} catch (Exception e) {
			}
	}

	protected abstract void action();
}
