package crawlie;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class FileManager {


  public static void storeFile(String url) {
    storeFile(url, Config.getInstance().getDownloadLocation() + "/");

  }

  public static void storeFile(String url, String folder) {
    try {
      String name = url.substring(url.lastIndexOf("/") + 1);
      InputStream in = (new URL(url)).openStream();

      if (!new File(folder).exists())
        new File(folder);
      OutputStream out = new BufferedOutputStream(new FileOutputStream(folder + name));

      for (int b; (b = in.read()) != -1;) {
        out.write(b);
      }
      out.close();
      in.close();
    } catch (IOException e) {
      Logger.error("Failed to download: " + url);
    }
  }

  public static boolean isFile(String url) {
    return url.substring(url.lastIndexOf(".") + 1).matches(
        Config.getInstance().getDownloadFiletype());
  }
}
