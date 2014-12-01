package crawlie.gui;

import java.awt.*;

import javax.swing.*;

import crawlie.Message;

/**
 * The actual configuration of the GUI is in this class. It implements a listener interface in order to get information
 *
 * @author Patrick
 */
public class CrawlieView extends JPanel implements CrawlieListener {

  private static final long serialVersionUID = -8336882687395127678L;

  public static final String START = "START";
  public static final String STOP = "STOP";
  public static final String RESET = "RESET";
  public static final String INITIALIZE_CACHE = "INITIALIZE CACHE";

  private JButton pause;
  private JButton start;
  private JButton reset;
  private JButton initCache;

  private ContinuousFeedArea errorArea;
  private ContinuousFeedArea statusArea;
  private ContinuousFeedArea logArea;

  public CrawlieView() {
    start = new JButton(START);
    start.setName(START);
    pause = new JButton(STOP);
    pause.setName(STOP);
    reset = new JButton(RESET);
    reset.setName(RESET);
    initCache = new JButton(INITIALIZE_CACHE);
    initCache.setName(INITIALIZE_CACHE);

    statusArea = new ContinuousFeedArea();
    errorArea = new ContinuousFeedArea();
    logArea = new ContinuousFeedArea();

    setLayout(new GridBagLayout());
    Dimension dim = new Dimension(800, 500);
    setPreferredSize(dim);
    setMinimumSize(dim);
    setMaximumSize(dim);

    add(statusArea, new GBC(0, 3).setSpan(4, 2));
    add(errorArea, new GBC(2, 1).setSpan(2, 2).setWeight(.9, 0.8));
    add(logArea, new GBC(0, 1).setSpan(2, 2).setWeight(0.8, 0.8));

    add(start, new GBC(0, 0).setWeight(0.3, 0));
    add(pause, new GBC(1, 0).setWeight(0.3, 0));
    add(reset, new GBC(2, 0).setWeight(0.3, 0));
    add(initCache, new GBC(3, 0).setWeight(0.3, 0));

  }

  public void addController(CrawlieGuiController controller) {
    start.addActionListener(controller);
    pause.addActionListener(controller);
    reset.addActionListener(controller);
    initCache.addActionListener(controller);
  }

  @Override
  public void addStatusMessage(Message message) {
    statusArea.append(message + "\n");
  }

  @Override
  public void addLogMessage(Message message) {
    logArea.append(message + "\n");
  }

  @Override
  public void addErrorMessage(Message message) {
    errorArea.append(message + "\n");
  }
}
