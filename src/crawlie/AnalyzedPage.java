package crawlie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AnalyzedPage extends Page {
	public final Document SOURCE;
	public final String TITLE;
	public final String DOMAIN;
	public final String PREFIX;

	private PageQueue children;

	public AnalyzedPage(String url) {
		super(url);
		String title = "No title";
		Document source = null;
		try {
			source = Jsoup.connect(url).get();
			title = source.select("title").text();
		} catch (Exception e) {
			// e.printStackTrace();
			Logger.error("Unable to load page: " + url);
		}
		String prefix;
		String domain;
		try {
			prefix = URL.split("/")[0];
			domain = URL.split("/")[2];
		} catch (Exception e) {
			prefix = "NONE";
			domain = "NONE";
		}
		PREFIX = prefix;
		DOMAIN = domain;
		SOURCE = source;
		TITLE = title;
	}

	public PageQueue getLinks() {
		if (children == null) {
			Elements links = SOURCE.select("a[href]");
			children = new PageQueue();
			for (Element element : links) {
				String url = element.attr("abs:href");
				if (element.val().startsWith("/"))
					url = PREFIX + "//" + DOMAIN + url;
				Page newPage = new UnknownPage(url);
				children.add(newPage);
			}
		}
		return children;
	}

	@Override
	public String toString() {
		return String.format("Title: %s - URL: %s - Priority: %d", TITLE, URL, PRIORITY);
	}
}
