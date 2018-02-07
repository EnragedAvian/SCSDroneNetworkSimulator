import java.util.ArrayList;

public class Experiments {
	static ArrayList<Integer> log = new ArrayList<Integer>();
	
	static void addToLog(int snapshot) {
		if (log.size() == 0)
			log.add(snapshot);
		else if (log.get(log.size()-1) == snapshot) { // last entry was the same entry
			log.remove(log.size()-1);
			if (log.contains(snapshot))
				System.out.println("Period is: " + log.size());
			log.add(snapshot);
		}
		else
			log.add(snapshot);
	}
	
	static void printLog() {
		System.out.println("Printing log: ");
		for (int i=0; i<log.size(); i++) {
			System.out.println("\t" + log.get(i));
		}
	}
}
