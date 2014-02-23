package crawlie;

public class DiscoveredPage extends Page implements Comparable<DiscoveredPage> {

	public final String PARENT;
	public final int PRIORITY;

	public DiscoveredPage(String url, String parent) {
		super(url);
		this.PARENT = parent;
		this.PRIORITY = Priority.getInstance().urlHeuristic(this);
	}

	@Override
	public int compareTo(DiscoveredPage other) {
		return other.PRIORITY - this.PRIORITY;
	}
}
