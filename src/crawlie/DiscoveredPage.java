package crawlie;

public class DiscoveredPage implements Comparable<DiscoveredPage> {

	public final String PARENT;
	public final String URL;
	public final int PRIORITY;

	public DiscoveredPage(String url, String parent) {
		this.URL = url;
		this.PARENT = parent;
		this.PRIORITY = genHeuristic();
	}

	public int genHeuristic() {
		int priority = 0;

		if (URL.contains(".no/"))
			priority += 3;
		else if (URL.contains("facebook.com"))
			return 0;
		else if (URL.contains("penis"))
			priority += 5;
		else if (URL.contains("sport"))
			priority -= 10;
		return priority;
	}

	@Override
	public int compareTo(DiscoveredPage other) {
		return other.PRIORITY - this.PRIORITY;
	}
}
