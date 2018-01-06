package main;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import adds.Functions;
import adds.Vars;
import dictlib.DictNative;
import dictlib.ported.ArticleHeading;
import dictlib.ported.CachePage;
import dictlib.ported.DictionaryReader;
import dictlib.ported.ReadBitStream;


public class Dictionary {

  private String dictName;
  private String fileName;
  private String dictFileName;
  private String indexFileName;
  private String refFileName;
  
  private String sourceLangName;
  private String sourceLangShortName;
  private String targetLangName;
  private String targetLangShortName;
  
  private int sourceLang;
  private int targetLang;
  
  private byte[] ibuf;
  private byte[] dbuf;
  
  private List<ArticleHeading> headings;
  private List<ArticleHeading> matchedHeadings;
  
  private ReadBitStream bstr;
  private DictionaryReader reader;
  private RandomAccessFile refReader;
  
  private DictNative dictNative;
  
  private IndexReader ireader;
  private IndexWriter iwriter;
  
  private boolean scanned;
  
  
  Dictionary(){
    scanned = false;
    dictName = "dict";
    refReader = null;
  }
  
  Dictionary(String fileName){
    this();
    
    dictFileName = fileName.replace(".idx", ".lsd");
    indexFileName = fileName.replace(".lsd", ".idx");
    refFileName = indexFileName + ".ref";
    this.fileName = dictFileName.replace(".lsd", "");
  }
  
  
  public void scanNative(){
    String lsdFile = Vars.dictPath + dictFileName;
    String idxFile = Vars.indexPath + indexFileName;
    
    dictNative = new DictNative();
    dictNative.indexDict(lsdFile, idxFile);
  }

  
// ------------------------------------------------- Scanning -------------------------------------------------
  
  public void scanDictForIndex(){
    long time=System.currentTimeMillis();
    
    String lsdFile = Vars.dictPath + dictFileName;

    dbuf = Functions.readBinFile(lsdFile);
    bstr = new ReadBitStream(dbuf, dbuf.length);
    reader = new DictionaryReader(bstr);

    time=System.currentTimeMillis();
    
    reader.ReadForIndex();
    dictName = reader.name();
    
    Functions.logTime("scanDictForIndex", time);
  }
  
  void collectHeadings() {
    long time=System.currentTimeMillis();
    
    int pCount = reader.pagesCount();
    headings = new ArrayList<ArticleHeading>();

    for (int pageNumber=0; pageNumber < pCount; ++pageNumber) {
      bstr.Seek(reader.header().pagesOffset + 512 * pageNumber);

      CachePage page = new CachePage();
      page.loadHeader(bstr);
      int hCount = page.headingsCount();

      if (page.isLeaf()) {
        String prefix = "";
        
        for (int idx = 0; idx < hCount; ++idx) {
          ArticleHeading heading = new ArticleHeading();
          heading.Load(reader.decoder(), bstr, prefix);
          prefix = heading.text();
          headings.add(heading);
        }
      }
    }
    
    Functions.logTime("collectHeadings: "+dictName, time);
  }
  
  public void writeIndex(){
    long time = System.currentTimeMillis();
    
    iwriter = new IndexWriter();
    iwriter.writeHeadingsTrie(Vars.indexPath, indexFileName, headings, reader.header());
    
    Functions.logTime("writeIndex: "+dictName, time);
  }
  
  
//----------------------------------------------- Read Index -----------------------------------------------
  
  public void scanIndex(){
    long time=System.currentTimeMillis();
    
    String idxFile = Vars.indexPath + indexFileName;
    String refFile = Vars.indexPath + refFileName;
    
    ireader = new IndexReader();
    ireader.readIndex(idxFile);
    
    try {
      refReader = new RandomAccessFile(refFile, "r");
    } catch (FileNotFoundException e) {
      // e.printStackTrace();
      System.out.println("No .ref file");
      refReader = null;
    }
    
    Functions.logTime("scanIndex", time);
  }
  
  public void scanDictForRead(){
    long time=System.currentTimeMillis();
    
    dictNative = new DictNative();

    String lsdFile = Vars.dictPath + dictFileName;
    dictNative.readIndex(lsdFile);
    
    try {
      dictName = new String(dictNative.getDictName(), "UTF-16LE");
      ireader.setDictName(dictName);
//      byte[] dnb = dictNative.getDictName();
//      Functions.printBytes(dnb);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    Functions.logTime("scanDictForRead", time);
  }
  
  public void scanDictForRead1(){             // using ported dictlib .java classes
    long time=System.currentTimeMillis();
    
    if(scanned){
      System.out.println("! scanned");
      return;
    } 
    
    String lsdFile = Vars.dictPath + dictFileName;
    
    dbuf = Functions.readBinFile(lsdFile);
    bstr = new ReadBitStream(dbuf, dbuf.length);
    reader = new DictionaryReader(bstr);
    reader.Read();
    dictName = reader.name();
    
    ireader.setHeader(reader.header());
    
    scanned = true;
    
    Functions.logTime("scanDictForRead: "+dictName, time);
  }

  
//------------------------------------------------- Search Words -------------------------------------------------

  public void searchWord(String word, Map<String, String> headingsMap){
    ireader.searchWord(word, 0);
    matchedHeadings = ireader.getMatchedHeadings();
    
    String dictNameLocal = "\n<div class='dict-name'>" + dictName + "</div>\n";
    
    for(ArticleHeading h: matchedHeadings){
      String key = h.text();
      
      int id = h.id();
      int reference = getRefById(id, true);
      if(reference == -1){
        System.out.println("No article reference");
        // return;
      } 
      
      System.out.println("ref: " + reference);
      
      String article = "stub...";
      if(reference != -1){
        article = decodeArticle(reference, dictNameLocal);
      }
      
      if(headingsMap.containsKey(key)){                    // ??? put articles only for the first heading
        String prev = headingsMap.get(key);
        article = prev + article ;
        headingsMap.put(key, article);
      }
      else{
        headingsMap.put(key, article);
      }
    }
  }
  
  public String searchWordFromLink(String word){
    String dictNameLocal = "\n<div class='dict-name'>" + dictName + "</div>\n";
  
    int id = ireader.searchWord(word, 1);
    if(id == -1) return "";
    
    int reference = getRefById(id, true);
    if(reference == -1) return "";
    
    System.out.println("ref-link: " + reference);
    
    String article = decodeArticle(reference, dictNameLocal);
    return article;
  }
  
  String tryEncodings(byte[] bytes){
    String res = "";
    
    try {
      SortedMap<String, Charset> map = Charset.availableCharsets();
      System.out.println("  Encodings:\n");
      for(String enc: map.keySet()){
          String tmp = new String(bytes, enc);
          res += "[m1]" + enc + " :: " + tmp + "[/m]\n";
      }
      System.out.println("  Encodings:\n");
    
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    
    return res;
  }
  
  
  String decodeArticle(int reference, String dictNameLocal){
    String article="";
    // article = reader.decodeArticle(bstr, reference);           // using ported dictlib classe
    
    try {
      // article = new String(dictNative.decodeArticle(reference), "UTF-8");
      // article = new String(dictNative.decodeArticle(reference), "UTF-16LE");
      
      byte[] bytes = dictNative.decodeArticle(reference);
//      article = new String(bytes, "UTF-16LE");
      
      article = new String(bytes, "UTF-16LE");
      // article += "[m1]qweqwe[/m][m1]asdasd[/m]";
      
//      Functions.printBytes(bytes);
//      article += tryEncodings(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    article = dictNameLocal + article;
    article = "<div class='article'>" + article + "</div>";
    
    return article;
  }
  
  public int getRefById(int id){
    return getRefById(id, false);
  }
  
  public int getRefById(int id, boolean reverseOrder){
    if(refReader == null) return -1;
    int res = -1;
    
    try {
      int pos = id * 4;
      refReader.seek(pos);
      
      if(reverseOrder){
        int ch4 = refReader.read();       // inverse bytes order
        int ch3 = refReader.read();
        int ch2 = refReader.read();
        int ch1 = refReader.read();
        
        res = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
      }
      else{
        res = refReader.readInt();      // direct bytes order
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return res;
  }
  
  
//------------------------------------------------- Add -------------------------------------------------
  
  public void searchTest(){
    ireader.searchTest();
  }
  
  public void clear(){
    clear(false);
  }
  
  public void clear(boolean clearGlobal){
    dbuf = null;
    bstr = null;
    reader = null;
    iwriter = null;
    
    if(headings != null){
      headings.clear();
      headings = null;
    }
    
    if(dictNative != null){
      System.out.println("clearing-dictNative");
      if(clearGlobal)
        dictNative.clearGlobal();
      else
        dictNative.clear();
      dictNative = null;
    }
    
    if(ireader != null){
      System.out.println("clearing-ireader");
      ireader.clear();
      ireader = null;
    }
  }
  
  public void clearRead(){
    
  }
  
  String getDirection(){
    return dictNative.getDirection();
  }
  
  String getDirection1(){
    String res="";
    
    sourceLang = reader.getSourceLangCode();
    targetLang = reader.getTargetLangCode();
    
    sourceLangShortName = Vars.langShortNames.get(sourceLang);
    targetLangShortName = Vars.langShortNames.get(targetLang);
    
    res = sourceLangShortName + targetLangShortName;
    return res;
  }
  
  String getFileName(){
    return fileName;
  }
  
  String getDictName(){
    return dictName;
  }
  
}
