import java.util.ArrayList;

public class Experiments {
	static ArrayList<Long> log = new ArrayList<Long>();
	
	static void addToLog(long snapshot) {
		for(int i=log.size()-1;i>=0;i--)
			if (log.get(i) == snapshot) { //to find the period 
				System.out.println("Period is: " + (int)(log.size()-i));
				break;
			}
		log.add(snapshot);
	}
	
	static void printLog() {
		System.out.println("Printing log: ");
		for (int i=0; i<log.size(); i++) {
			System.out.println("\t" + log.get(i));
		}
	}
}