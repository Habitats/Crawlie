package crawlie.gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class CrawlieFrame extends JFrame {
  private static final long serialVersionUID = 3215696573774506119L;

  public CrawlieFrame(CrawlieView panel) {
    setTitle("Crawlie");
    add(panel);

    buildFrame(this);
  }

  private void buildFrame(JFrame frame) {

    frame.getContentPane().setBackground(Color.black);

    frame.pack();

    frame.setLocationRelativeTo(frame.getRootPane());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    frame.setVisible(true);
  }
}
