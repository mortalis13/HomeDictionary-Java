package main;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import marisa.Marisa;
import adds.Caps;
import adds.Functions;
import adds.Vars;
import dictlib.ported.ArticleHeading;
import dictlib.ported.Header;


public class IndexWriter {
  
  String _artPrefix;
  String _dictName;
  
  
  void sortHeadings(List<ArticleHeading> data) {
    Collections.sort(data);
  }

  void writeHeadingsTrie(String indexPath, String file, List<ArticleHeading> data, Header header){
    long time;
    time = System.currentTimeMillis();
    
    sortHeadings(data);
    
    String indexFileName = indexPath + file;
    Marisa marisa = new Marisa();
    marisa.buildTrie(data, indexFileName);
   
    marisa.close();
  }
  
  void writeHeadingsTrie1(String indexPath, String file, List<ArticleHeading> data, Header header){
    //   Map<String, Integer> textRefMap = new HashMap<String, Integer>();
    //    Map<Integer, Integer> idRefMap = new TreeMap<Integer, Integer>();
    //    Marisa marisa = new Marisa();
    //   
    //   for (ArticleHeading art: data) {
    //     String text = art.text();
    //     textRefMap.put(text, art.reference());
    //     marisa.push_back(text);
    //   }
    //   
    //   marisa.build();
    //   marisa.save(indexPath + file);
    //   
    //   Functions.logTime("marisa-write", time);
    //   
    //   // time = System.currentTimeMillis();
    //   
    //   // String refFileName = indexPath + file + ".ref";
    //   // marisa.linkData(textRefMap, refFileName);
    //   
    //   // Functions.logTime("reference-write", time);
    //    
    ////    marisa.close();
    //  
    //    
    //    // write references list
    //    
    //    try{
    //      time = System.currentTimeMillis();
    //      
    //      KeysetNative keyset = marisa.getKeyset();
    //      int len = (int) keyset.size();
    //      
    //      for(int i=0; i<len; ++i){
    //        KeyNative key = keyset.get(i);
    //        String text = new String(key.text(), "UTF-8");
    //        idRefMap.put((int) key.id(), textRefMap.get(text));
    //      }
    //      // Functions.logTime("reference-collect", time);
    //      
    //      // time = System.currentTimeMillis();
    //      Functions.writeReferenceMap(idRefMap, indexPath + file + ".ref");
    //      // Functions.logTime("reference-write", time);
    //      
    //      Functions.logTime("reference-collect", time);
    //      
    //      // Functions.writeMapKeys(idRefMap);
    //      // Functions.writeReferences(refs, indexPath + file + ".ref");
    //    } catch (Exception e) {
    //      e.printStackTrace(); 
    //    }
    //
    //    marisa.close();
  }
  

//---------------------------------------------- set/get ----------------------------------------------
  
  void setArtPrefix(String artPrefix){
    _artPrefix=artPrefix;
  } 
  
  void setDictName(String name){
    _dictName=name;
  } 
  
  
//---------------------------------------------- test ----------------------------------------------
  
  void sortHeadingsTest(List<ArticleHeading> data) {
    Functions.writeHeadingsText(data, "files/headings-before-sort.txt");
//    sortHeadings(data);
    Functions.writeHeadingsText(data, "files/headings-after-sort.txt");
  }
  
}
