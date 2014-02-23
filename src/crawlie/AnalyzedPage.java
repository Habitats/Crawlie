package crawlie;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AnalyzedPage extends Page {
	public final String PARENT;
	public final Document SOURCE;
	public final String TITLE;
	public final String DOMAIN;
	public final String PREFIX;

	private ArrayList<DiscoveredPage> children;

	public AnalyzedPage(String url, String parent) {
		super(url);
		String title = "No title";
		Document source = null;
		PARENT = parent;
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

	public ArrayList<DiscoveredPage> getChildren() {
		if (children == null) {
			children = new ArrayList<DiscoveredPage>();
			Elements links = SOURCE.select("a[href]");
			for (Element element : links) {
				// remove unecessary stuff from URL
				String url = element.attr("abs:href").split("=")[0].split("#")[0];
				if (element.val().startsWith("/"))
					url = PREFIX + "//" + DOMAIN + url;
				DiscoveredPage newPage = new DiscoveredPage(url, URL);
				children.add(newPage);
			}
		}
		return children;
	}

	@Override
	public String toString() {
		return String.format("Title: %s - URL: %s", TITLE, URL);
	}
}
