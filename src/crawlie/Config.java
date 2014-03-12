package crawlie;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Singleton to handle the configuration of the crawler
 * 
 * It reads the configuration from a .properties file and stores the values in appropriate fields
 * 
 * @author Patrick
 * 
 */
public class Config {

  private static Config instance;


  private Config() {
    prop = loadConfig("crawlie.properties");

    dbJdbcDriver = prop.getProperty("jdbcDriver");
    dbUrl = prop.getProperty("url");
    dbUser = prop.getProperty("user");
    dbTitle = prop.getProperty("title");
    dbTable = prop.getProperty("table");
    dbPassword = prop.getProperty("password");

    downloadLocation = prop.getProperty("download_location");
    downloadFiletype = prop.getProperty("download_filetype");
    singleDomain = Boolean.parseBoolean(prop.getProperty("single_domain"));
    includeImages = Boolean.parseBoolean(prop.getProperty("include_images"));
    storeContent = Boolean.parseBoolean(prop.getProperty("store_content"));
    cahceAncestors = Boolean.parseBoolean(prop.getProperty("cache_ancestors"));
    guiEnabled = Boolean.parseBoolean(prop.getProperty("enable_gui"));

    maxPages = Integer.parseInt(prop.getProperty("max_pages"));
    maxWorkers = Integer.parseInt(prop.getProperty("max_workers"));
    maxFileWorkers = Integer.parseInt(prop.getProperty("max_file_workers"));
    cacheInterval = Integer.parseInt(prop.getProperty("cache_interval"));
    seed = prop.getProperty("seed");

    predefinedAvoidance = prop.getProperty("avoid");
    predefinedPriority = prop.getProperty("prioritize");
  }

  public synchronized static Config getInstance() {
    if (instance == null)
      instance = new Config();
    return instance;
  }

  // ########### SINGLETON #######################################

  private Properties prop;

  // DATABASE
  private String dbJdbcDriver;
  private String dbUrl;
  private String dbUser;
  private String dbPassword;
  private String dbTitle;
  private String dbTable;

  // GENERAL
  private int maxPages;
  private int maxWorkers;
  private int maxFileWorkers;
  private int cacheInterval;
  private String seed;
  private boolean singleDomain;
  private String downloadFiletype;
  private String downloadLocation;
  private boolean includeImages;
  private boolean storeContent;
  private boolean cahceAncestors;
  private boolean guiEnabled;

  private boolean paused = true;
  private boolean guiLock = false;

  private String predefinedAvoidance;
  private String predefinedPriority;

  private String serializedFileName = "serialized.obj";



  /**
   * Load the config from the properties file
   */
  private Properties loadConfig(String path) {
    prop = new Properties();
    try {
      prop.load(new FileInputStream(new File(path)));
    } catch (IOException e) {
      // e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Missing crawlie.properties file!");
      System.exit(0);
    }
    return prop;
  }

  /** Post the current config to the log in a readable format */
  public void announceConfig() {
    Enumeration e = prop.propertyNames();
    Logger.getInstance().status("LOADING CONFIG...");
    while (e.hasMoreElements()) {
      String key = (String) e.nextElement();
      Logger.getInstance().status(key + ": " + prop.getProperty(key));
    }
    Logger.getInstance().status("LOADING CONFIG DONE - EDIT CRAWLIE.PROPERTIES TO CUSTOMIZE!");
  }

  public String getJdbcDriver() {
    return dbJdbcDriver;
  }

  public String getDatabaseUrl() {
    return dbUrl;
  }

  public String getDatabaseUser() {
    return dbUser;
  }

  public String getDatabasePassword() {
    return dbPassword;
  }

  public String getDatabaseTitle() {
    return dbTitle;
  }

  public String getDatabaseTable() {
    return dbTable;
  }

  public int getMaxPages() {
    return maxPages;
  }

  public int getMaxWorkers() {
    return maxWorkers;
  }

  public int getCacheInterval() {
    return cacheInterval;
  }

  public String getSeed() {
    return seed;
  }

  public String getDownloadFiletype() {
    return downloadFiletype;
  }

  public String getDownloadLocation() {
    return downloadLocation;
  }

  public boolean singleDomain() {
    return singleDomain;
  }

  public boolean includeImages() {
    return includeImages;
  }

  public boolean storeContent() {
    return storeContent;
  }

  public boolean cacheAncestors() {
    return cahceAncestors;
  }

  public boolean isPaused() {
    return paused;
  }

  public void setPaused(boolean paused) {
    this.paused = paused;
  }

  public String getSerializedFile() {
    return serializedFileName;
  }

  public int getMaxFileWorkers() {
    return maxFileWorkers;
  }

  public boolean guiEnabled() {
    return guiEnabled;
  }

  public void setGuiLock(boolean guiLock) {
    this.guiLock = guiLock;
  }

  public boolean isGuiLocked() {
    return guiLock;
  }

  public String getPredefinedAvoidance() {
    return predefinedAvoidance;
  }

  public String getPredefinedPriority() {
    return predefinedPriority;
  }

}
