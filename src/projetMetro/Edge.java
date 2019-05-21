package projetMetro;

public class Edge {
	private SubwayStation stop1;
	private SubwayStation stop2;
	private int time;//travel time
	
	/* Constructors*/
	
	//For weighed graph
	public Edge(SubwayStation stop1, SubwayStation stop2, int time) {
		super();
		this.stop1 = stop1;
		this.stop2 = stop2;
		this.time = time;
	}
	//For unweighed graph
	public Edge(SubwayStation stop1, SubwayStation stop2) {
		super();
		this.stop1 = stop1;
		this.stop2 = stop2;
	}
	
	/*Getters and setters*/
	
	public SubwayStation getStop1() {
		return stop1;
	}


	public void setStop1(SubwayStation stop1) {
		this.stop1 = stop1;
	}

	public SubwayStation getStop2() {
		return stop2;
	}

	public void setStop2(SubwayStation stop2) {
		this.stop2 = stop2;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	
	

}
