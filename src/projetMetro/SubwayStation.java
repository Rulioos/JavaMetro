package projetMetro;

public class SubwayStation {
	private int stop_id;
	private String stop_name;
	private String stop_loc;//stop's address
	private double lat;
	private double lon;
	
	public SubwayStation(int stop_id, String stop_name, String stop_loc,double lat,double lon) {
		super();
		this.stop_id = stop_id;
		this.stop_name = stop_name;
		this.lat=lat;
		this.lon=lon;
	}
	
	public boolean equals(SubwayStation s) {
		if(s==null) {
			return false;
		}
		return stop_name.equals(s.getStop_name());
	}

	public int getStop_id() {
		return stop_id;
	}

	public void setStop_id(int stop_id) {
		this.stop_id = stop_id;
	}

	public String getStop_name() {
		return stop_name;
	}

	public void setStop_name(String stop_name) {
		this.stop_name = stop_name;
	}

	public String getStop_loc() {
		return stop_loc;
	}

	public void setStop_loc(String stop_loc) {
		this.stop_loc = stop_loc;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	
	
	
}
