//Data class, for adding data to trajectories.

public class Data {
	
	private Trajectory InitialTraj;
	private Trajectory DestTraj;
	private long CreationTime;
	private long FetchingTime = 0;
	private long DeliveryTime = 0;
	private boolean isFetched;
	
	Data(Trajectory InitialTraj, Trajectory DestTraj, long CreationTime){
		this.InitialTraj = InitialTraj;
		this.DestTraj = DestTraj;
		this.CreationTime = CreationTime;
		isFetched = false;
	}
	
	public void setInitialTraj(Trajectory InitialTraj){
		this.InitialTraj = InitialTraj;
	}
	
	public Trajectory getInitialTraj(){
		return InitialTraj;
	}
	
	public void setDestTraj(Trajectory DestTraj){
		this.DestTraj = DestTraj;
	}

	public Trajectory getDestTraj(){
		return DestTraj;
	}
	
	public void setCreationTime(long CreationTime){
		this.CreationTime = CreationTime;
	}
	
	public long getCreationTime(){
		return CreationTime;
	}
	
	public void setFetchingTime(long FetchingTime){
		this.FetchingTime = FetchingTime;
	}
	
	public long getFetchingTime(){
		return FetchingTime;
	}
	
	public void setDeliveryTime(long DeliveryTime){
		this.DeliveryTime = DeliveryTime;
	}
	
	public long getDeliveryTime(){
		return DeliveryTime;
	}
	
	public void setFetchedStatus(boolean isFetched){
		this.isFetched = isFetched;
	}
	
	public boolean getFetchedStatus(){
		return isFetched;
	}
}
