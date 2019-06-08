package projetMetro;

public class Edge {
	private SubwayStation stop1;
	private SubwayStation stop2;
	private int time;//travel time
	private String line;
	private double distance;
	/* Constructors*/
	
	//For weighed graph
	public Edge(SubwayStation stop1, SubwayStation stop2, int time,String line) {
		super();
		this.stop1 = stop1;
		this.stop2 = stop2;
		this.time = time;
		this.line=line;
		setDistance();
	}
	//For unweighed graph
	public Edge(SubwayStation stop1, SubwayStation stop2,String line) {
		super();
		this.stop1 = stop1;
		this.stop2 = stop2;
		this.line=line;
	}
	
	//
	
	/*Getters and setters*/
	
	
	
	public SubwayStation getStop1() {
		return stop1;
	}


	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
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
	public double getDistance() {
		return distance;
	}
	public void setDistance() {
		this.distance = Math.sqrt(
				Math.pow((stop2.getLat()-stop1.getLat()),2)
				+ Math.pow((stop2.getLon()-stop1.getLon()),2)
				);
	}
	
	
	

}
