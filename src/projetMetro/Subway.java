package projetMetro;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
		SubwayStation b = getStationByName("Mairie d'Issy");
		long startTime = System.currentTimeMillis();
		IdentifyClusters();
		// getLine("7b").forEach(e->System.out.println(e.getStop1().getStop_name()));
		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) * 0.001 / 60 + " minutes");

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
	private Map<SubwayStation, Tuple<String, Double>> getNeighbours(SubwayStation s) {
		Map<SubwayStation, Tuple<String, Double>> n = new HashMap<>();
		graph.stream().filter(k -> k.getStop1().equals(s) || k.getStop2().equals(s)).forEach(k -> {
			if (k.getStop1().equals(s)) {
				n.put(k.getStop2(), new Tuple<String, Double>(k.getLine(), k.getDistance()));
			} else {
				n.put(k.getStop1(), new Tuple<String, Double>(k.getLine(), k.getDistance()));
			}
		});
		return n;

	}

	// METHODS FOR Unweighed Graph
	public List<Edge> BFS(SubwayStation src) {
		List<Edge> bfsTree = new ArrayList<>();
		Map<String, Boolean> visited = new HashMap<>();
		Map<SubwayStation, Tuple<String, Double>> voisins;
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
					bfsTree.add(new Edge(s, k.getKey(), k.getValue().getE1()));
				}
			});

		}

		return bfsTree;

	}

	public List<Edge> ShortestPathUW(SubwayStation a, SubwayStation b) {
		List<Edge> l = this.BFS(a);
		List<Edge> shortest = new ArrayList<>();
		Edge f = l.parallelStream().filter(e -> e.getStop2().equals(b)).findFirst().orElse(null);
		if (f == null) {
			return shortest;
		}
		SubwayStation x = f.getStop1();
		shortest.add(f);
		while (!x.equals(a)) {
			SubwayStation y = x;
			f = l.parallelStream().filter(e -> e.getStop2().equals(y)).findFirst().orElse(null);
			x = f.getStop1();
			shortest.add(f);

		}
		return shortest;
	}

	// METHODS FOR WEIGHED GRAPH

	public List<Edge> Djikistra(SubwayStation src) {
		List<Edge> djkTree = new ArrayList<Edge>();
		Map<String, Double> djkDistance = new HashMap<>();

		Map<SubwayStation, Tuple<String, Double>> voisins;

		// settled nodes
		Set<String> settled = new HashSet<>();
		// not settled nodes
		Set<String> unsettled = new HashSet<>();
		// sets all distance to infinity
		this.stops.forEach(s -> {
			djkDistance.put(s.getStop_name(), Double.MAX_VALUE);
		});

		// set distance of src to 0
		djkDistance.put(src.getStop_name(), 0.0);
		unsettled.add(src.getStop_name());
		while (unsettled.size() != 0) {

			SubwayStation current = this
					.getStationByName(djkDistance.entrySet().stream().filter(k -> unsettled.contains(k.getKey()))
							.sorted(Map.Entry.comparingByValue()).findFirst().orElse(null).getKey());
			unsettled.remove(current.getStop_name());
			voisins = this.getNeighbours(current);

			voisins.entrySet().forEach(kv -> {
				SubwayStation adj = kv.getKey();
				Double dist = kv.getValue().getE2() + djkDistance.get(current.getStop_name());
				String line = kv.getValue().getE1();
				if (!settled.contains(adj.getStop_name())) {
					if (dist < djkDistance.get(adj.getStop_name())) {
						Edge toModify = djkTree.stream().filter(e -> e.getStop2().equals(adj)).findFirst().orElse(null);
						if (toModify == null) {
							toModify = new Edge(current, adj, line);
							djkTree.add(toModify);
						} else {
							toModify.setStop1(current);
							toModify.setLine(line);
						}
						toModify.setDistance(dist);
						djkDistance.put(adj.getStop_name(), toModify.getDistance());
					}

					unsettled.add(adj.getStop_name());
				}
			});
			settled.add(current.getStop_name());
		}
		return djkTree;
	}

	// Shortest path for weigh graph
	public List<Edge> ShortestPathWDI(SubwayStation a, SubwayStation b) {
		List<Edge> l = this.Djikistra(a);
		List<Edge> shortest = new ArrayList<>();

		Edge f = l.parallelStream().filter(e -> e.getStop2().equals(b)).findFirst().orElse(null);
		if (f == null) {
			return shortest;
		}
		SubwayStation x = f.getStop1();
		shortest.add(f);
		while (!x.equals(a)) {
			SubwayStation y = x;
			f = l.parallelStream().filter(e -> e.getStop2().equals(y)).findFirst().orElse(null);
			x = f.getStop1();
			shortest.add(f);

		}
		return shortest;
	}

	// METHODS TO GET DIAMETER
	/*
	 * The methods
	 */
	public Tuple<Integer, Integer> getDiameterRadius() {
		Tuple<Integer, Integer> DiameterRadius = new Tuple<>(Integer.MIN_VALUE, Integer.MAX_VALUE);
		List<Tuple<SubwayStation, SubwayStation>> visited = new ArrayList<>();

		stops.forEach(s1 -> {
			stops.forEach(s2 -> {
				if (!s1.equals(s2)) {
					Tuple<SubwayStation, SubwayStation> v = visited.stream()
							.filter(e -> (e.getE1().equals(s1) && e.getE2().equals(s2))
									|| (e.getE1().equals(s2) && e.getE2().equals(s1)))
							.findFirst().orElse(null);
					if (v == null) {
						visited.add(new Tuple<SubwayStation, SubwayStation>(s1, s2));
						List<Edge> shortest = this.ShortestPathWDI(this.getStationByName(s1.getStop_name()),
								this.getStationByName(s2.getStop_name()));

						int n = shortest.size();
						if (n > DiameterRadius.getE1()) {
							DiameterRadius.setE1(n);
						} else if (n < DiameterRadius.getE2() && n > 1) {
							DiameterRadius.setE2(n);
						}
					}

				}
			});
		});

		return DiameterRadius;
	}

	// Clusters identifications
	/*
	 * This function returns a Map<Edge,List<Tuple>> that gives for each edge the
	 * list of shortest paths passing by this edge. The result is sorted according
	 * to the list size.
	 */
	public Map<Edge, List<Tuple<SubwayStation, SubwayStation>>> IdentifyClusters() {
		// initialize variables
		Map<Edge, List<Tuple<SubwayStation, SubwayStation>>> clusters = new HashMap<>();
		List<Tuple<SubwayStation, SubwayStation>> visited = new ArrayList<>();

		// Double loop on stops to identify all pairs
		stops.forEach(s1 -> {
			stops.forEach(s2 -> {
				if (!s1.equals(s2)) {
					// Looking for tuple already visited
					Tuple<SubwayStation, SubwayStation> v = visited.stream()
							.filter(e -> (e.getE1().equals(s1) && e.getE2().equals(s2))
									|| (e.getE1().equals(s2) && e.getE2().equals(s1)))
							.findFirst().orElse(null);
					// if not visited
					if (v == null) {
						// visit it
						Tuple<SubwayStation, SubwayStation> tup = new Tuple<SubwayStation, SubwayStation>(s1, s2);
						visited.add(tup);
						List<Edge> shortest = this.ShortestPathWDI(s1, s2);
						/*
						 * For each edge in the shortest path of s1 and s2 add this tuple to the edge.
						 */
						// checking for existing edge
						shortest.forEach(e -> {
							Edge k = clusters.entrySet().stream()
									.filter(es -> (es.getKey().getStop1().equals(e.getStop1())
											&& es.getKey().getStop2().equals(e.getStop2()))
											|| (es.getKey().getStop1().equals(e.getStop2())
													&& es.getKey().getStop2().equals(e.getStop1())))
									.map(l -> l.getKey()).findFirst().orElse(null);

							if (k == null) {
								clusters.put(e, new ArrayList<>());
								k = e;
							}
							clusters.get(k).add(tup);
						});

					}
				}
			});
		});

		// sort the map
		return clusters.entrySet().stream().sorted(Comparator.comparingInt(e -> e.getValue().size()))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
	}

}
