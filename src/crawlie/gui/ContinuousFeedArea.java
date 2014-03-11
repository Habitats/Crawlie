package crawlie.gui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class ContinuousFeedArea extends JTextPane {
  private Queue<String> feed;
  private int maxLength = 1000;

  public ContinuousFeedArea() {
    feed = new ConcurrentLinkedDeque<String>();
    getDocument().addDocumentListener(new LimitLinesDocumentListener(200));
  }

  public synchronized void append(String str) {
    try {
      Document doc = getDocument();
      doc.insertString(doc.getLength(), str, null);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

}
