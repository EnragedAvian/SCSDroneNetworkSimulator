import java.util.ArrayList;

public class Experiments {
	static ArrayList<ArrayList<Integer>> log = new ArrayList<ArrayList<Integer>>();
	static long period = -1;
	public static ArrayList<Integer> snap;
	
	static void addToLog(ArrayList<Integer> snapshot) {
		for(int i=log.size()-1;i>=0;i--) {
			boolean periodCheck = true;
			for(int j=0;j<snapshot.size();j++)
				if(snapshot.get(j) != log.get(i).get(j))
					periodCheck = false;
			
			if (periodCheck) { //to find the period
				period = log.size()-i;
				System.out.println("Period is: " + period);
				break;
			}
		}
		log.add(snapshot);
		snap = snapshot;
	}
	
	static void printLog() {
		System.out.println("Printing log: ");
		for (int i=0; i<log.size(); i++) {
			System.out.println("\t" + log.get(i));
		}
	}
	
	public static void clear() {
		log.clear();
		period = -1;
	}
}