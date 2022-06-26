package util;

import java.util.ArrayList;

public class Tasks {

	private static ArrayList<Task> tasks = new ArrayList<Task>();

	protected static void addTask(Task task) {
		tasks.add(task);
	}

	public static void cancelAll() {
		tasks.clear();
	}
}
