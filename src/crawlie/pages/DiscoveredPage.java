package crawlie.pages;

import crawlie.Priority;

/**
 * Whenever a new page (url) is discovered, it is cacheds as an instance of this class
 * 
 * @author Patrick
 * 
 */
public class DiscoveredPage extends Page implements Comparable<DiscoveredPage> {


  public DiscoveredPage(String url, Page parent, AnalyzedPages analyzedPages,
      DiscoveredQueue discoveredQueue) {
    super(url, parent, analyzedPages, discoveredQueue);
    super.priority = Priority.getInstance().urlHeuristic(this);
  }

  @Override
  public int compareTo(DiscoveredPage other) {
    return other.priority - this.priority;
  }

  @Override
  public void analyze() {
    discoveredQueue.add(this);
  }
}
