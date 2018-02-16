import java.util.ArrayList;

public class Experiments {
	static ArrayList<Integer> log = new ArrayList<Integer>();
	static int period = -1;
	static int snap = -1;
	
	static void addToLog(int snapshot) {
		snap = snapshot;
		if (log.size() == 0)
			log.add(snapshot);
		else if (log.contains(snapshot)) { // last entry was the same entry
			//log.remove(log.size()-1);
			System.out.println("Period is: " + log.size());
			period = log.size();
			log.clear();
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
	
	public static void clear(){
		log.clear();
		period = -1;
	}

}