package crawlie.gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class CrawlieFrame extends JFrame {

  public CrawlieFrame(CrawlieView panel) {

    // setTitle("Server Bro v0.1");

    add(panel);

    buildFrame(this);
  }

  private void buildFrame(JFrame frame) {

    frame.getContentPane().setBackground(Color.black);

    frame.pack();

    frame.setLocationRelativeTo(frame.getRootPane());
    // frame.setLocation(0, 0);
    // frame.setSize(new Dimension(800, 500));
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // frame.setResizable(false);
    frame.setVisible(true);

  }

}
