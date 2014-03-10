package crawlie.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;

import crawlie.Config;

/**
 * Data structure that inherits properties from hashmap and priority queue. It ensures unique
 * entries.
 * 
 * It utilizes a MinMaxPriorityQueue from the google guava library due to the lack of a data
 * structure with similar capabilities in the standard lib
 * 
 * @author Patrick
 * 
 */
public class DiscoveredQueue implements Iterable<DiscoveredPage>, Serializable {
  private HashMap<String, Boolean> visited;
  private transient MinMaxPriorityQueue<DiscoveredPage> pages;
  private final int MAX_SIZE = 10000;
  private List<DiscoveredPage> tempPages;

  public DiscoveredQueue() {
    visited = new HashMap<String, Boolean>();
    pages = MinMaxPriorityQueue.maximumSize(MAX_SIZE).create();
  }

  public synchronized boolean visited(String url) {
    return visited.get(url) != null;
  }

  public synchronized Page pop() {
    Page page;

    while (pages.size() == 0)
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    page = pages.pollFirst();
    return page;
  }

  public synchronized void add(Page newPage) {
    if (Config.getInstance().singleDomain()
        && !Config.getInstance().getSeed().contains(newPage.domain)) {
      return;
    }

    pages.add((DiscoveredPage) newPage);
    // if (pages.size() >= MAX_SIZE)
    // pages.removeLast();
    notifyAll();
  }

  public synchronized void addVisited(Page page) {
    visited.put(page.url, true);
  }

  public synchronized int size() {
    return pages.size();
  }

  /**
   * Due to MinMaxPriorityQueue not being serializable this workaround is used
   */
  public void onSerialize() {
    tempPages = new ArrayList<DiscoveredPage>();
    tempPages.addAll(pages);
  }

  public void onDeserialize() {
    pages = MinMaxPriorityQueue.maximumSize(MAX_SIZE).create();
    pages.addAll(tempPages);
  }

  @Override
  public Iterator<DiscoveredPage> iterator() {
    return pages.iterator();
  }
}
