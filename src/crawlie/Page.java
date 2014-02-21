package crawlie;

abstract class Page implements Comparable<Page> {

	protected final String URL;
	protected final int PRIORITY;

	public Page(String url) {
		this.URL = url;
		this.PRIORITY = 0;
	}

	@Override
	public int compareTo(Page other) {
		return other.getPriority() - this.PRIORITY;
	}

	public String getUrl() {
		return URL;
	}

	public int getPriority() {
		return PRIORITY;
	}

}
