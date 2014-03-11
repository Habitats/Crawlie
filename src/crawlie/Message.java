package crawlie;

import java.text.SimpleDateFormat;

/**
 * Data class for messages. Rather self explanatory
 * 
 * @author Patrick
 * 
 */
public class Message {
  public enum Type {
    STATUS, LOG, ERROR;
  }

  public final String message;
  public final long time;
  public final Type type;

  public Message(String message, Type type) {
    this.time = System.currentTimeMillis();
    this.message = message;
    this.type = type;
  }

  @Override
  public String toString() {
    return new SimpleDateFormat("HH:mm:ss").format(time) + " > " + type.name() + " > " + message;
  }
}
