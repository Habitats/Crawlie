package crawlie;

import java.util.ArrayList;
import java.util.Iterator;

public class AnalyzedPages  implements Iterable<AnalyzedPage> {
	private ArrayList<AnalyzedPage> pages;
	private int size;

	public AnalyzedPages() {
		pages = new ArrayList<AnalyzedPage>();
	}

	public synchronized void add(AnalyzedPage e) {
		pages.add(e);
		size += 1;
	}

	public synchronized int size() {
		return size;
	}

	public synchronized int cacheSize() {
		return pages.size();
	}

	@Override
	public synchronized Iterator<AnalyzedPage> iterator() {
		return pages.iterator();
	}

	public synchronized void dumpPages() {
		pages = new ArrayList<AnalyzedPage>();
	}
}
