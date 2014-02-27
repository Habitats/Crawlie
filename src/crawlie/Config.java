package crawlie;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

  private static Config instance;

  private Properties prop;



  private Config() {
    prop = loadConfig("crawlie.cfg");

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


    maxPages = Integer.parseInt(prop.getProperty("max_pages"));
    maxWorkers = Integer.parseInt(prop.getProperty("max_workers"));
    cacheInterval = Integer.parseInt(prop.getProperty("cache_interval"));
    seed = prop.getProperty("seed");

  }

  public synchronized static Config getInstance() {
    if (instance == null)
      instance = new Config();
    return instance;
  }

  // ########### SINGLETON #######################################

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
  private int cacheInterval;
  private String seed;
  private boolean singleDomain;
  private String downloadFiletype;
  private String downloadLocation;
  private boolean includeImages;

  private Properties loadConfig(String path) {
    prop = new Properties();
    try {
      prop.load(new FileInputStream(new File(path)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return prop;
  }

  public Properties getProperties() {
    return prop;
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
}
