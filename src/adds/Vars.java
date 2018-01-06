package adds;

import java.util.HashMap;
import java.util.Map;

public class Vars {
  
  public static String dictPath = "dictionaries/";
  public static String indexPath = dictPath + "index/";
  
  public static String directionsFile = "config/directions.config";
  
  public static String headingsTextFile = "files/headings.txt";
  

  public static int[] diacrits={
    'ß',
    'à', 'á', 'â', 'ã', 'ä', 'å', 'æ',
    'ç',
    'è', 'é', 'ê', 'ë',
    'ì', 'í', 'î', 'ï',
    'ñ',
    'ò', 'ó', 'ô', 'õ', 'ö',
    'ù', 'ú', 'û', 'ü',
    'ý', 'ÿ'
  };
  
  public static int[] normalizedCaps={'s', 'a', 'c', 'e', 'i', 'n', 'o', 'u', 'y'};
  
  public static int[] dton={
    0,
    1,1,1,1,1,1,1,
    2,
    3,3,3,3,
    4,4,4,4,
    5,
    6,6,6,6,6,
    7,7,7,7,
    8,8
  };
  
  public static int[] cyrillicCodes = {
    1049
  };
  
  public static Map<Integer, String> langNames = new HashMap<Integer, String>()
  {{ 
    put(1031, "German"); 
    put(1033, "English"); 
    put(1034, "Spanish"); 
    put(1036, "French"); 
    put(1040, "Italian"); 
    put(1049, "Russian"); 
  }};
  
  public static Map<Integer, String> langShortNames = new HashMap<Integer, String>()
  {{ 
    put(1031, "De"); 
    put(1033, "En"); 
    put(1034, "Es"); 
    put(1036, "Fr"); 
    put(1040, "It"); 
    put(1049, "Ru"); 
  }};
  
}
