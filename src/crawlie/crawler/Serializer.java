package crawlie.crawler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import crawlie.Config;

/**
 * Serializer class giving the crawler the option to cache its current data to disk, in order to
 * resume its business when restarted
 * 
 * @author Patrick
 * 
 */
public class Serializer {

  private static Serializer instance;

  private Serializer() {};

  public synchronized static Serializer getInstance() {
    if (instance == null)
      instance = new Serializer();
    return instance;
  }

  public void serializeCurrentData(Object obj) {
    try {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Config.getInstance().getSerializedFile()));
      out.writeObject(obj);
      out.close();
    } catch (IOException e) {
//      e.printStackTrace();
    }
  }

  public Object deserializeCurrentData() throws IOException {
    Object obj = null;
    ObjectInputStream in;
    try {
      in = new ObjectInputStream(new FileInputStream(Config.getInstance().getSerializedFile()));
      obj = in.readObject();
      in.close();
    } catch (IOException | ClassNotFoundException e) {
      throw new IOException(e);
    }
    return obj;
  }
}
