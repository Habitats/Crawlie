package crawlie.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import crawlie.Config;
import crawlie.Crawlie;
import crawlie.Logger;


public class CrawlieController implements ActionListener, Observer {

  private CrawlieModel model;
  private CrawlieView view;
  private CrawlieFrame frame;

  public CrawlieController() {
    // set look and feel to the OS default over the standard java one (which frankly is quite ugly)
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

    model = new CrawlieModel();
    view = new CrawlieView();

    frame = new CrawlieFrame(view);

    model.addObserver(view);
    Logger.getInstance().addObserver(this);

    view.addController(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    System.out.println(e.getActionCommand());
    String sourceName = ((JComponent) e.getSource()).getName();
    Crawlie crawlie = new Crawlie();
    if (sourceName.equals(CrawlieView.START)) {
      crawlie.init();
      Config.getInstance().setPaused(false);
    } else if (sourceName.equals(CrawlieView.RESET)) {
      crawlie = new Crawlie();
      Config.getInstance().setPaused(false);
    } else if (sourceName.equals(CrawlieView.PAUSE)) {
      Config.getInstance().setPaused(true);
    } else if (sourceName.equals(CrawlieView.INITIALIZE_CACHE)) {
      crawlie.initializeCachedData();
    }
  }

  /** Main method with GUI */
  public static void main(String[] args) {
    CrawlieController controller = new CrawlieController();
  }

  @Override
  public void update(Observable o, Object arg) {
    model.setEntry((String) arg);
  }
}
