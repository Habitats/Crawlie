package crawlie;

import crawlie.crawler.CrawlerController;
import crawlie.gui.CrawlieGuiController;

/**
 * Main class for Crawlie
 * 
 * @author Patrick
 * 
 */
public class Crawlie {
  public static void main(String[] args) {
    if (Config.getInstance().guiEnabled()) {
      // start a resumable instance with GUI
      new CrawlieGuiController();
    } else {
      // start a single instance with no GUI
      // everything about the actual parser is contained here but its recommended to use the GUI
      // version to access the full set of features and an overall better experience
      new CrawlerController().initialize();
    }
    Config.getInstance().announceConfig();
  }
}
