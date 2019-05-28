package projetMetro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LinesData {

	static List<SubwayStation> stops = new ArrayList<SubwayStation>();
	static List<Edge> edges = new ArrayList<Edge>();

	public static void main(String[] args) {
		String[] lines= {"1","2","3","3b","4","5","6","7","7b","8","9","10","11","12","13","14"};
		for(String num:lines) {
			read_stops("RATP_GTFS_LINES/RATP_GTFS_METRO_"+num+"/stops.txt");
			read_travels("RATP_GTFS_LINES/RATP_GTFS_METRO_"+num+"/stop_times.txt");
			
		}
		//read_stops("RATP_GTFS_LINES/RATP_GTFS_METRO_12/stops.txt");
		//read_travels("RATP_GTFS_LINES/RATP_GTFS_METRO_12/stop_times.txt");
		edges.forEach(k -> System.out
				.println(k.getStop1().getStop_id() + " " + k.getStop2().getStop_id() + " " + k.getTime()));

	}

	// read stops for file stops.txt
	public static void read_stops(String filepath) {
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

	// read edges from stops_times.txt
	public static void read_travels(String filepath) {
		List<SubwayStation> line = new ArrayList<>();
		List<String> stop_time = new ArrayList<>();
		List<Integer> counters = new ArrayList<>();
		counters.add(0);
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
			stream.forEach(l -> {
				if (!l.contains("stop_id")) {
					String[] s = l.split(",");
					int c = Integer.parseInt(s[4]);
					if (c > counters.get(counters.size() - 1)) {
						SubwayStation s1 = getStationById(Integer.parseInt(s[3]));
						String time = s[2];
						stop_time.add(time);
						line.add(s1);
						counters.add(c);
					} else {
						// do nothing
					}

				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < line.size() - 1; i++) {
			edges.add(new Edge(line.get(i), line.get(i + 1), time_Diff(stop_time.get(i), stop_time.get(i + 1))));
		}

		
	}

	private static SubwayStation getStationById(int id) {
		return stops.stream().filter(ss -> ss.getStop_id() == id).findFirst().orElse(null);
	}

	private static int time_Diff(String time1, String time2) {
		String[] sub_t1 = time1.split(":");
		String[] sub_t2 = time2.split(":");

		if (Math.abs(Integer.parseInt(sub_t1[0]) - Integer.parseInt(sub_t2[0])) == 0) {
			return 60 * Math.abs(Integer.parseInt(sub_t1[1]) - Integer.parseInt(sub_t2[1]))
					+ Math.abs(Integer.parseInt(sub_t1[2]) - Integer.parseInt(sub_t2[2]));

		} else {
			return 60 * Math.abs(60 - Integer.parseInt(sub_t1[1]) - Integer.parseInt(sub_t2[1]))
					+ Math.abs(Integer.parseInt(sub_t1[2]) - Integer.parseInt(sub_t2[2]));
		}
	}

}
