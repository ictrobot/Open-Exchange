package oe.lib.misc;

import java.io.InputStream;
import java.util.Properties;
import oe.OpenExchange;
import oe.lib.Debug;
import oe.lib.Log;
import oe.qmc.QMC;

public class Localization {
  
  private static String loadedLanguage = "";
  private static final String path = "/assets/oe/lang/";
  private static final String defaultLangCode = "en_US";
  private static Properties lang = new Properties();
  private static Properties defaultLang = new Properties();
  
  public static String get(String key) {
    if (key.startsWith("tile.")) {
      Log.info(key);
    }
    if (getCurrentLanguage() == null) {
      return key;
    }
    if (getCurrentLanguage() != loadedLanguage) {
      load();
      if (getCurrentLanguage() != loadedLanguage) {
        return format(defaultLang.getProperty(key, key));
      }
    }
    return format(lang.getProperty(key, defaultLang.getProperty(key, key)));
  }
  
  private static String format(String str) {
    return String.format(str, QMC.name);
  }
  
  private static String getCurrentLanguage() {
    return OpenExchange.proxy.getCurrentLanguage();
  }
  
  private static boolean defaultLoaded = false;
  
  private static void load() {
    if (!defaultLoaded) {
      load(defaultLangCode, true);
      defaultLoaded = true;
    }
    load(getCurrentLanguage(), false);
  }
  
  private static void load(String langCode, boolean setDefault) {
    InputStream langStream = null;
    Properties fromFile = new Properties();
    try {
      langStream = OpenExchange.class.getResourceAsStream(path + langCode + ".properties");
      if (langStream == null) {
        // return;
      }
      fromFile.load(langStream);
      if (setDefault) {
        defaultLang = new Properties();
        defaultLang.putAll(fromFile);
      } else {
        lang = new Properties();
        lang.putAll(fromFile);
      }
      langStream.close();
    } catch (Exception e) {
      Debug.handleException(e);
      return;
    }
    if (!setDefault) {
      loadedLanguage = getCurrentLanguage();
    }
  }
}
