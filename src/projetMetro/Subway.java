package projetMetro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Subway {
	private List<SubwayStation> stops = new ArrayList<>();
	private List<Edge> graph = new ArrayList<>();

	public Subway() {
		// gets data from RATPS_GTPS_LINES
		LinesData.GettingData(stops, graph);
		SubwayStation a = getStationByName("Pigalle");
		SubwayStation b=getStationByName("Châtelet");
		long startTime = System.currentTimeMillis();

		ShortestPath(a,b);

		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		
	}
	public void ShortestPath(SubwayStation a,SubwayStation b) {
		List<Edge> l=this.BFS(a);
		Edge f=l.stream().filter(e->e.getStop2().equals(b)).findFirst().orElse(null);
		SubwayStation x=f.getStop1();
		System.out.println("FIN: "+b.getStop_name());
		
		while(!x.equals(a)) {
			SubwayStation y=x;
			System.out.println(" Station: "+y.getStop_name()+" Ligne suivante:"+f.getLine());
			f=l.stream().filter(e->e.getStop2().equals(y)).findFirst().orElse(null);
			x=f.getStop1();
			
		}
		System.out.println("DEPART: "+x.getStop_name());
	}
	public SubwayStation getStationByName(String name) {
		return stops.stream().filter(s -> s.getStop_name().equals(name)).findFirst().orElse(null);
	}

	public List<Edge> getLine(String id) {
		// given the line. Gives the edges corresponding
		return graph.stream().filter(e -> e.getLine().equals(id)).collect(Collectors.toList());
	}

	public void printLine(String id) {
		// print the line path and the time between each station
		graph.stream().filter(k -> k.getLine().equals(id)).forEach(k -> System.out
				.println(k.getStop1().getStop_name() + "<->" + k.getStop2().getStop_name() + ":" + k.getDistance()));
	}

	public Set<String> transfers(SubwayStation s) {
		// returns a set with the transfers available at this station
		Set<String> transfers = new HashSet<String>();
		if (s.equals(null)) {
			return transfers;
		} else {
			graph.stream().filter(e -> e.getStop1().equals(s) || e.getStop2().equals(s))
					.forEach(e -> transfers.add(e.getLine()));
			return transfers;
		}
	}

	// get neighbours of a station s
	private Map<SubwayStation, String> getNeighbours(SubwayStation s) {
		Map<SubwayStation, String> n = new HashMap<>();
		graph.stream().filter(k -> k.getStop1().equals(s) || k.getStop2().equals(s)).forEach(k -> {
			if (k.getStop1().equals(s)) {
				n.put(k.getStop2(), k.getLine());
			} else {
				n.put(k.getStop1(), k.getLine());
			}
		});
		return n;

	}

	public List<Edge> BFS(SubwayStation src) {
		List<Edge> bfsTree = new ArrayList<>();
		Map<String, Boolean> visited = new HashMap<>();
		Map<SubwayStation, String> voisins;
		// initializing visited

		this.stops.forEach(k -> {
			visited.put(k.getStop_name(), false);
		});

		Queue<SubwayStation> queue = new LinkedList<>();
		queue.add(src);
		visited.put(src.getStop_name(), true);
		while (!queue.isEmpty()) {
			final SubwayStation s = queue.poll();
			
			// get unvisited voisins
			voisins = getNeighbours(s);
			voisins.entrySet().forEach(k -> {
				if (!visited.get(k.getKey().getStop_name())) {
					visited.put(k.getKey().getStop_name(), true);
					queue.add(k.getKey());
					bfsTree.add(new Edge(s, k.getKey(), k.getValue()));
				}
			});
			
		}

		return bfsTree;

	}


}
