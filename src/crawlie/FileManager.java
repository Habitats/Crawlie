package crawlie;

import java.util.ArrayList;

public class FileManager {
  private static FileManager instance;

  public synchronized static FileManager getInstance() {
    if (instance == null)
      instance = new FileManager();
    return instance;
  }

  // ########### SINGLETON #######################################

  private ArrayList<String> filesToStore;

  private FileManager() {
    filesToStore = new ArrayList<String>();
    for (int i = 0; i < 20; i++) {
      Thread fw =
          new Thread(new FileWorker(this, Config.getInstance().getDownloadLocation() + "/"));
      fw.setName("FileWorker " + i);
      fw.start();
    }
  }

  private synchronized void addFileToStore(String file) {
    filesToStore.add(file);
    notify();
  }

  public synchronized String getFileToStore() {
    while (filesToStore.isEmpty())
      try {
        wait();
      } catch (InterruptedException e) {
      }
    return filesToStore.remove(0);
  }

  public void storeFile(String url) {
    storeFile(url, Config.getInstance().getDownloadLocation() + "/");
  }

  public void storeFile(final String url, final String folder) {
    addFileToStore(url);
  }

  public boolean isFile(String url) {
    return url.substring(url.lastIndexOf(".") + 1).matches(
        Config.getInstance().getDownloadFiletype());
  }

  public boolean running() {
    return true;
  }
}
