package projetMetro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LinesData {
	
	List<SubwayStation> stops=new ArrayList<SubwayStation>();
	List<Edge> edges=new ArrayList<Edge>();
	
	//read stops for file stops.txt
	public void read_stops(String filepath) {
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
			stream.forEach(l -> {
				if (!l.contains("stop_id")) {
					String[] s = l.split(",");
					stops.add(new SubwayStation(Integer.parseInt(s[0]), s[2], s[3]));
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//read edges from stops_times.txt
	public void read_travels(String filepath){
		
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
			stream.forEach(l -> {
				if (!l.contains("stop_id")) {
					String[] s = l.split(",");
					SubwayStation s1=getStationById(Integer.parseInt(s[0]));
					SubwayStation s2=getStationById(Integer.parseInt(s[1]));
					edges.add(new Edge(s1,s2));
			
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private SubwayStation getStationById(int id) {
		return stops.stream().
				filter(ss->ss.getStop_id()==id).
				findFirst().orElse(null);
	}

}
