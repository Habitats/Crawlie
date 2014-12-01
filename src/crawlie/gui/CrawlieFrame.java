package crawlie.gui;

import javax.swing.*;

/**
 * The GUI frame, ie. main window.
 *
 * @author Patrick
 */
public class CrawlieFrame extends JFrame {

  private static final long serialVersionUID = 3215696573774506119L;

  public CrawlieFrame(CrawlieView panel) {
    setTitle("Crawlie");
    add(panel);

    buildFrame(this);
  }

  private void buildFrame(JFrame frame) {

    // fit the frame to its contents
    frame.pack();

    // position in center
    frame.setLocationRelativeTo(frame.getRootPane());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
