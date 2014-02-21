package crawlie;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

public class PageQueue implements Iterable<Page> {
	private HashMap<String, Page> visited;
	private PriorityQueue<Page> discovered;

	public PageQueue() {
		visited = new HashMap<String, Page>();
		discovered = new PriorityQueue<Page>();

	}

	private synchronized boolean visited(String url) {
		return visited.get(url) != null;
	}

	public synchronized Page pop() {
		if (discovered.size() == 0)
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return discovered.poll();
	}

	public synchronized void add(Page e) {
		if (!visited(e.URL)) {
			discovered.add(e);
			visited.put(e.URL, e);
			notify();
		}
	}

	public synchronized int size() {
		return discovered.size();
	}

	@Override
	public synchronized Iterator<Page> iterator() {
		return discovered.iterator();
	}

}
