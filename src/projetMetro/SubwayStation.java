package projetMetro;

public class SubwayStation {
	private int stop_id;
	private String stop_name;
	private String stop_loc;//stop's address
	
	public SubwayStation(int stop_id, String stop_name, String stop_loc) {
		super();
		this.stop_id = stop_id;
		this.stop_name = stop_name;
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
	
	
	
	
}
