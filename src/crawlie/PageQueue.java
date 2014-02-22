package crawlie;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

public class PageQueue implements Iterable<Page> {
	private HashMap<String, Boolean> visited;
	private PriorityQueue<Page> pages;

	public PageQueue() {
		visited = new HashMap<String, Boolean>();
		pages = new PriorityQueue<Page>();

	}

	private synchronized boolean visited(String url) {
		return visited.get(url) != null;
	}

	public synchronized Page pop() {
		if (pages.size() == 0)
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		Page page = pages.poll();
		return page;
	}

	public synchronized void add(Page e) {
		if (!visited(e.URL)) {
			pages.add(e);
			visited.put(e.URL, true);
			notify();
		}
	}

	public synchronized boolean empty() {
		return size() == 0;
	}

	public synchronized int size() {
		return pages.size();
	}

	@Override
	public synchronized Iterator<Page> iterator() {
		return pages.iterator();
	}

}
