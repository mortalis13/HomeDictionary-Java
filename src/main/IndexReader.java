package main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import marisa.Marisa;
import marisa.Marisa.IdKeyPair;
import adds.Functions;
import adds.Vars;
import dictlib.ported.ArticleHeading;
import dictlib.ported.Header;


public class IndexReader {

  byte[] _buf;
  List<ArticleHeading> headings;
  List<int[]> posTable;
  
  List<ArticleHeading> matchedHeadings;
  
  List<String> headingsText;
  
  String _artPrefix;
  String _dictName;
  
  Header _header;
  
  Marisa marisa;
  
  
  IndexReader(){
    headings = new ArrayList<ArticleHeading>();
    headingsText = new ArrayList<String>();
  }
  
  IndexReader(byte[] buf){
    _buf=buf;
    
    headings = new ArrayList<ArticleHeading>();
    headingsText = new ArrayList<String>();
  }
  
  
  void readIndex(String idxFile) {
    long time = System.currentTimeMillis();
    
    marisa = new Marisa();
    marisa.load(idxFile);
    
    Functions.logTime("Marisa load", time);
  }
  
  
  public void searchTest(){
    List<String> list = Functions.readFileToArray(Vars.headingsTextFile);
    int notFoundCount = 0;
    
    for(String word:list){
      IdKeyPair res = marisa.lookup(word);
      if(res == null){
        notFoundCount++;
      }
    }
    
    System.out.println("Not Found: " + notFoundCount);
  }
  
  
  int searchWord(String word, int source){
    String simlifiedWord = word, text;
    int id = -1;
    
    if(source == 0){
      matchedHeadings = new ArrayList<ArticleHeading>();
      simlifiedWord = simplify(word);
    }
    
    IdKeyPair res = marisa.lookup(word);
    if(res != null){
      text = word;
      id = (int) res.Id;
      
      matchedHeadings.add(new ArticleHeading(text, id, -1));
      System.out.println(_dictName + " :: " + word + ": found, id: "+id);
    }
    else
      System.out.println(_dictName + " :: " + word + ": not found");
    
    return id;
  }
  
  
//---------------------------------------------- add ----------------------------------------------
  
  public void clear(){
    if(marisa != null){
      System.out.println("java: close-marisa");
      marisa.close();
    }
    else{
      System.out.println("java: close-marisa - null");
    }
  }
  
  int getCapitalIndex(int cap){
    for(int i=0; i<posTable.size(); i++)
      if(posTable.get(i)[0]==cap)
        return i;
    return -1;
  }
  
  boolean equal(String text, String word){
    return text.equals(word);
  }
  
  boolean simplifiedEqual(String text, String simlifiedWord){
    String simlifiedText = simplify(text);
    return simlifiedText.equals(simlifiedWord);
  }
  
  String simplify(String text){
    StringBuffer sb=new StringBuffer(text);
    int len=text.length();
    
    for(int i=0; i<len; ++i){
      int idx = Arrays.binarySearch(Vars.diacrits, sb.charAt(i));
      if(idx >= 0){
        int ch1 = Vars.normalizedCaps[ Vars.dton[idx] ];
        sb.setCharAt(i, (char) ch1);
      }
    } 
    
    return sb.toString();
  }
  
  boolean isLangCodeCyrillic(int langCode){
    boolean isCyrillic = false;
    if( Arrays.binarySearch(Vars.cyrillicCodes, langCode) >= 0 ){
      isCyrillic=true;
    }
    return isCyrillic;
  }
  
  int searchWordReturn(int source, int ref){
    if(source == 0){
      if(matchedHeadings.size() == 0) return -1;
      return matchedHeadings.get(0).reference();
    }
    else if(source == 1)
      return ref;
    return -1;
  }
  
  
//---------------------------------------------- set/get ----------------------------------------------
  
  List<ArticleHeading> getHeadings(){
    return headings;
  }
  
  List<ArticleHeading> getMatchedHeadings(){
    return matchedHeadings;
  }
  
  List<String> getHeadingsText(){
    return headingsText;
  }
  
  String getArtPrefix(){
    return _artPrefix;
  }
  
  void setHeader(Header header){
    _header=header;
  }
  
  void setDictName(String dictName){
    _dictName=dictName;
  }
  
  
//---------------------------------------------- test ----------------------------------------------

  void printHeadings(List<ArticleHeading> headings){
    System.out.println("--Headings--");
    for(ArticleHeading h:headings){
      System.out.println(h.text());
    }
    System.out.println();
  }
  
  
  void printCapsCount(int totalCount){
    int idx=0, idx1=0, count=0;
    int[] pos, pos1;
    int len=posTable.size();
    char cap;
    
    System.out.println("-- caps count --");
    for(int i=0; i<len-1; i++){
      pos=posTable.get(i);
      pos1=posTable.get(i+1);
      
      cap=(char) pos[0];
          
      idx=pos[3];
      idx1=pos1[3];
      count=idx1-idx;
      
      System.out.println(cap+": "+count);
    }
    
    pos=posTable.get(len-1);
    cap=(char) pos[0];
    count=totalCount-pos[3];
    System.out.println(cap+": "+count);
    
    System.out.println();
  }
  
  
}
