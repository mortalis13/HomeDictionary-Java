package ui;

import java.util.ArrayList;
import java.util.List;

import marisa.Marisa;
import marisa.Marisa.IdKeyPair;
import adds.Functions;


public class Test {
  
  String filesPath = "files/";
  String inFile = filesPath + "in.txt";
  String indexFile = filesPath + "out.idx";
  
  // String searchTest = "orange";
//  String searchTest = "orangeq";
//  String searchTest = "alcoba";
  
 String searchTest = "prowar";
  

  void write(){
    Marisa marisa = new Marisa();
    
    List<String> keyset = Functions.readFileToArray(inFile);
    
    long time = System.currentTimeMillis();
    marisa.build(keyset);
    marisa.save(indexFile);
    Functions.logTime("Index write", time);
    
    marisa.close();
    System.out.println("Write finished");
  }
  
  
  void read(){
    Marisa marisa = new Marisa();
    
    marisa.load(indexFile);
    
    long time = System.currentTimeMillis();
    IdKeyPair res = marisa.lookup(searchTest);
    Functions.logTime("Search word", time);
    
    if(res != null){
      long id = res.Id;
      System.out.println(searchTest + ": found, id: "+id);
    }
    else
      System.out.println(searchTest + ": not found");
    
    marisa.close();
  }

}
