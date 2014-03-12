package crawlie.crawler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import crawlie.Logger;


/**
 * Worker class for downloading files and storing themn in a desired directory. Having only one
 * thread doing this is painfully slow.
 * 
 * Workers work according to the producer/consumer principle
 * 
 * @author Patrick
 * 
 */
public class FileDownloadWorker implements Runnable {
  private String folder;
  private FileDownloadController fileManager;

  public FileDownloadWorker(FileDownloadController fileManager, String folder) {
    this.fileManager = fileManager;
    this.folder = folder;
  }

  @Override
  public void run() {

    while (fileManager.running()) {
      String url = fileManager.getUrlFromDownloadQueue();
      downloadFileFromUrl(url);
    }
  }

  /**
   * attempt to download a file from the internet
   */
  private void downloadFileFromUrl(String url) {
    // add a random prefix to the file to avoid overwriting another file with the same name. yes i
    // know it isn't 100% safe, but it's pretty cool nevetheless
    String fileName = Math.abs((int) (url.hashCode() / 1000.)) + "_" + url.substring(url.lastIndexOf("/") + 1);
    try {

      // create the folder if it doesn't already exsist
      File f = new File(folder);
      if (!f.exists()) {
        new File(folder);
        f.mkdirs();
        f.createNewFile();
      }
      // create a stream to the internet
      InputStream in = (new URL(url)).openStream();

      // create a stream to the disk
      OutputStream out = new BufferedOutputStream(new FileOutputStream(folder + fileName));

      // download the file and write it to disk
      for (int b; (b = in.read()) != -1;) {
        out.write(b);
      }

      // always close your streams people!
      out.close();
      in.close();
      Logger.getInstance().log("Downloaded > " + fileName);
    } catch (IOException e) {
      Logger.getInstance().error("Failed to download: " + url);
      // e.printStackTrace();
    }
  }
}
