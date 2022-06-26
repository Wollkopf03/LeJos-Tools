package fahrt;

import java.util.ArrayList;

public abstract class Fahrten {
	private static ArrayList<Fahrt> fahrten = new ArrayList<Fahrt>();
	static {
		fahrten.add(new Fahrt1());
		fahrten.add(new Fahrt2());
		fahrten.add(new Fahrt3());
		fahrten.add(new Fahrt4());
	}

	public static Fahrt getFahrt(int i) {
		if (i <= fahrten.size() && i > 0)
			return fahrten.get(i - 1);
		return null;
	}

	public static int getFahrten() {
		return fahrten.size();
	}
}
