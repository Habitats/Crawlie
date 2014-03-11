package crawlie.pages;

import crawlie.Priority;

/**
 * Whenever a new page (url) is discovered, it is cacheds as an instance of this class
 * 
 * @author Patrick
 * 
 */
public class DiscoveredPage extends AbstractPage implements Comparable<DiscoveredPage> {
  private static final long serialVersionUID = 2029011951639900955L;

  public DiscoveredPage(String url, AbstractPage parent, AnalyzedList analyzedPages,
      DiscoveredQueue discoveredQueue) {
    super(url, parent, analyzedPages, discoveredQueue);
    super.priority = Priority.getInstance().urlHeuristic(this);
  }

  /** required in order to sort instances according to priotity */
  @Override
  public int compareTo(DiscoveredPage other) {
    return other.priority - this.priority;
  }

  @Override
  public void analyze() {
    discoveredQueue.add(this);
  }
}
