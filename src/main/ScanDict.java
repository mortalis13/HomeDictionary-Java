package main;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ui.UIActions;
import adds.Functions;
import adds.Vars;


public class ScanDict {
  
  UIActions _uiactions;
  public boolean directionsRead=false;
  
   boolean testSearch=false;
//  boolean testSearch=true;
  
  long time;
  String searchWord;
  String foundHeading;
  
  List<Dictionary> dicts;
  Map<String, String> headingsMap;
  
  Map<String, List<String>> directionsMap;
  Map<String, List<String>> directionsRelMap;
  
  Set<String> sourceLangs;
  Set<String> targetLangs;


  public ScanDict(){
    dicts = new ArrayList<Dictionary>();
    
    searchWord="mesa";
//     searchWord="albondiga";
//     searchWord="planeta";
//     searchWord="pena";
    
//     searchWord="language";
//     searchWord="prowar";
    
// 	  searchWord="kalt";
//	  searchWord="arbeitend";
//	  searchWord="klein";
//	  searchWord="zweit";
  }
  
  
// ---------------------------------------------- Test ----------------------------------------------
  
  public void test(){
  }
  
// ---------------------------------------------- Scanning ----------------------------------------------
  
  public void indexDict(){
    time=System.currentTimeMillis();
    
    if(directionsMap!=null) directionsMap.clear();
    directionsMap = new TreeMap<String, List<String>>();
    
    getAllDictionariesFromFolder(0);
    
    int dictsCount = dicts.size();
    for(int i=0; i<dictsCount; ++i){
      long time1=System.currentTimeMillis();
      
      Dictionary dict = dicts.get(i);
      
      System.out.println("\n---------\nScanning dictionary [" + (i+1) + "/" + dictsCount + "]: " + dict.getFileName());
      
      // dict.scanDictForIndex();
      // dict.collectHeadings();
      // dict.writeIndex();
      
      dict.scanNative();
      accumulateDirections(dict);
      dict.clear(true);
      
      Functions.logTime("\nDict Scanned", time1);
      System.out.println("---------\n");
    }
    
    dicts.clear();
    Functions.writeDirections(directionsMap);
    directionsMap.clear();
    
    Functions.logTime("\nindexDict Total", time);
  }
  
  void accumulateDirections(Dictionary dict){
    String direction = dict.getDirection();
    String fileName = dict.getFileName();
    
    List<String> dictsList = directionsMap.get(direction);
    if(dictsList == null){
      dictsList = new ArrayList<String>();
      dictsList.add(fileName);
      directionsMap.put(direction, dictsList);
    }
    else{
      dictsList.add(fileName);
      directionsMap.put(direction, dictsList);
    }
  }
  

//----------------------------------------------- Read Index -----------------------------------------------
  
  public void readIndex() {
    time=System.currentTimeMillis();
    
    if(dicts.size() != 0){
      System.out.println("clearing-dicts");
      for(Dictionary dict: dicts){
        dict.clear();
      }
    }
    
    getAllDictionariesFromFolder(0);
    
    for(Dictionary dict: dicts){
      dict.scanIndex();
      dict.scanDictForRead();
    }
    
    Functions.logTime("\nreadIndex Total", time);
  }
  
  public void readIndex(String sourceLang, String targetLang) {
    time=System.currentTimeMillis();
    
    if(dicts.size() != 0){
      System.out.println("clearing-dicts");
      for(Dictionary dict: dicts){
        dict.clear();
      }
    }
    
    getDictionariesFromFolder(sourceLang, targetLang);
    System.out.println("\nTotal dictionaries [" + sourceLang+targetLang + "]: " + dicts.size() + "\n");
    
    for(Dictionary dict: dicts){
      dict.scanIndex();
      dict.scanDictForRead();
      
      System.out.println("-- Dictionary Loaded: " + dict.getDictName() + "\n");
    }
//    Functions.initParser();
    
    Functions.logTime("\nreadIndex Total", time);
  }
  
  public void readDirections(){
    directionsMap = Functions.readDirections();
    if(directionsMap == null) return;
    directionsRead=true;
    
    directionsRelMap = new TreeMap<String, List<String>>();
    sourceLangs = new HashSet<String>();
    targetLangs = new HashSet<String>();
    
    for(String dir : directionsMap.keySet()){
      String source = dir.substring(0,2);
      String target = dir.substring(2,4);
      
      List<String> targetsList = directionsRelMap.get(source);
      if(targetsList == null){
        targetsList = new ArrayList<String>();
        targetsList.add(target);
        directionsRelMap.put(source, targetsList);
      }
      else{
        targetsList.add(target);
        directionsRelMap.put(source, targetsList);
      }
      
      sourceLangs.add(source);
      targetLangs.add(target);
    }
  }
  
  
//----------------------------------------------- Search Words -----------------------------------------------
  
  public void searchWord(String word){
    if(testSearch) word=searchWord;
    if(word.trim().length() == 0) word=searchWord;
    
    headingsMap = new HashMap<String, String>();
    for(Dictionary dict:dicts){
      dict.searchWord(word, headingsMap);
    }
  }
  
  public String searchWordFromLink(String word){
    String article="";
    
    for(Dictionary dict:dicts){
      article += dict.searchWordFromLink(word);
    }
    
    return article;
  }
  
  public void searchTest(){
    for(Dictionary dict:dicts){
      dict.searchTest();
    }
  }
  
  
//----------------------------------------------- Set/Get -----------------------------------------------
  
  public void setUIActions(UIActions uiactions){
    _uiactions=uiactions;
  }
  
  public String getFoundHeading(){
    return foundHeading;
  }
  
  public Map<String, List<String>> getDirections(){
    return directionsMap;
  }
  
  public Map<String, String> getHeadingsMap(){
    return headingsMap;
  }
  
  public List<String> getSourceLangs(){
    return new ArrayList<String>(sourceLangs);
  }
  
  public List<String> getTargetLangs(){
    return new ArrayList<String>(targetLangs);
  }
  
  
//----------------------------------------------- Add -----------------------------------------------
  
  public void getAllDictionariesFromFolder(int type) {
    dicts.clear();
    String path="", filter="";
    
    if(type == 0){
      path=Vars.dictPath;
      filter="lsd";
    }
    else if(type == 1){
      path=Vars.indexPath;
      filter="idx";
    }
    
    Path dir = Paths.get(path);
    
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{"+filter+"}")) {
      for (Path entry: stream) {
        String file = entry.getFileName().toString();
        Dictionary dict=new Dictionary(file);
        
        dicts.add(dict);
      }
    } catch (IOException x) {
      System.err.println(x);
    }
  }
  
  public void getDictionariesFromFolder(String sourceLang, String targetLang) {
    dicts.clear();
    
    String direction = sourceLang + targetLang;
    List<String> dictsList = directionsMap.get(direction);
    
    for(String dictName: dictsList){
      Dictionary dict = new Dictionary(dictName + ".idx");
      dicts.add(dict);
    }
  }
  
  public List<String> getTargets(String sourceLang){
    List<String> targetsList = directionsRelMap.get(sourceLang);
    return targetsList;
  }
  
}
