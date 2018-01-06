package adds;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;

import dictlib.ported.ArticleHeading;

public class Functions {


//---------------------------------------------- Read/Write ----------------------------------------------

	public static byte[] readBinFile(String name) {
		try {
			FileInputStream f = new FileInputStream(name);

			int fileSize = f.available();
			byte[] buf = new byte[fileSize];
			f.read(buf);
			
			f.close();

			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void sortHeadings(List<ArticleHeading> data) {
    Collections.sort(data);
  }
	
	
  public static void writeReferenceMap(Map<Integer, Integer> refMap, String file){
    try {
      int bufsize = refMap.size()*4;
      
      FileOutputStream fout=new FileOutputStream(file);
      BufferedOutputStream bos = new BufferedOutputStream(fout, bufsize);
      
      for(int ref: refMap.values()){
        byte[] b = splitIntToBytes(ref);
        bos.write(b);
      }
      bos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void writeDirections(Map<String, List<String>> directionsMap){
    try {
      FileOutputStream fout=new FileOutputStream(Vars.directionsFile);
      byte[] bytes;
      
      int dirCount = directionsMap.size();
      fout.write(dirCount);
      
      for(String key : directionsMap.keySet()){
        String dir = key;
        List<String> dictsList = directionsMap.get(key);
        
        int dirLen = dir.length();
        int dictsCount = dictsList.size();
        
        fout.write(dirLen);
        bytes = dir.getBytes();
        fout.write(bytes);
        
        fout.write(dictsCount);
        
        for(String dictName : dictsList){
          int len = dictName.length();
          bytes = dictName.getBytes();
          fout.write(len);
          fout.write(bytes);
        }
      }
      
      fout.close();
      
      System.out.println("\nWrite directions finished");
    } catch (Exception e) {
      System.out.println("error-writing-file: " + e.getMessage());
    }
  }
  
  public static Map<String, List<String>> readDirections(){
    Map<String, List<String>> directionsMap = new TreeMap<String, List<String>>();
    
    try {
      FileInputStream fin = new FileInputStream(Vars.directionsFile);
      byte[] bytes;
      
      int dirCount = fin.read();
      
      for(int i=0; i<dirCount; ++i){
        int dirLen = fin.read();
        String dir="";
        
        for(int j=0; j<dirLen; ++j){
          int sym = fin.read();
          dir += (char) sym;
        }
        
        int dictsCount = fin.read();
        List<String> dictsList = new ArrayList<String>();
        
        for(int j=0; j<dictsCount; ++j){
          int len = fin.read();
          String dictName="";
          
          for(int k=0; k<len; ++k){
            int sym = fin.read();
            dictName += (char) sym;
          }
          
          dictsList.add(dictName);
        }
        
        directionsMap.put(dir, dictsList);
      }
      
      fin.close();
      
      System.out.println("\nRead directions finished");
      
      return directionsMap;
    } catch (Exception e) {
      System.out.println("No directions.config file");
      return null;
    }
  }
  
  
//---------------------------------------------- Read from bytes ----------------------------------------------
  
  public static int readInt(ByteArrayInputStream bastr) throws Exception{
    int res=0;
    
    byte[] bytes=new byte[4];
    bastr.read(bytes);
    
    ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE);
    buffer.put(bytes);
    buffer.flip(); 
    res=buffer.getInt();
    res=Integer.reverseBytes(res);
    
    return res;
  }
  
  public static short readShort(ByteArrayInputStream bastr) throws Exception{
    short res=0;
    
    byte[] bytes=new byte[2];
    bastr.read(bytes);
    
    ByteBuffer buffer = ByteBuffer.allocate(Short.SIZE/Byte.SIZE);
    buffer.put(bytes);
    buffer.flip(); 
    res=buffer.getShort();
    res=Short.reverseBytes(res);
    
    return res;
  }
  
  public static byte[] splitIntToBytes(int x){
    byte[] bytes=new byte[4];
    ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE);
    buffer.putInt(x);
    bytes=buffer.array();
    return bytes;
  }
  
  public static int composeIntFromBytes(byte[] bytes){
    int res;
    ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE);
    buffer.put(bytes);
    buffer.flip(); 
    res=buffer.getInt();
    return res;
  }
  
  public static byte[] splitShortToBytes(int x){
    byte[] bytes=new byte[2];
    ByteBuffer buffer = ByteBuffer.allocate(Short.SIZE/Byte.SIZE);
    buffer.putShort((short) x);
    bytes=buffer.array();
    return bytes;
  }
  
  public static short composeShortFromBytes(byte[] bytes){
    short res;
    ByteBuffer buffer = ByteBuffer.allocate(Short.SIZE/Byte.SIZE);
    buffer.put(bytes);
    buffer.flip(); 
    res=buffer.getShort();
    return res;
  }

  
//---------------------------------------------- Log ----------------------------------------------

	public static String formatTime(int time, String format) {
		Formatter timeFormat = new Formatter();
		timeFormat.format(format, (float) time / 1000);
		String res=timeFormat.toString();
		timeFormat.close();
		return res;
	}
	
	public static void logTime(String text, long time) {
    String timeString = formatTime((int) (System.currentTimeMillis()-time), text + ": %.2f s");
    log(timeString);
  }
	
	public static void log(String text) {
	  System.out.println(text);
	}
	
	
	public static String formatTimeNS(int time, String format) {
	  Formatter timeFormat = new Formatter();
	  timeFormat.format(format, time);
	  String res=timeFormat.toString();
	  timeFormat.close();
	  return res;
	}
	
	public static void logTimeNS(String text, long time) {
	  String timeString = formatTime((int) (System.nanoTime()-time), text + ": %.2f s");
	  logNS(timeString);
	}

	public static void logNS(String text) {
	  System.out.println(text);
	}

	
//---------------------------------------------- Add ----------------------------------------------
	
  public static void printBytes(byte[] bytes){
    System.out.println("----- Bytes:");
    for(int i=0; i< bytes.length; i++){
       System.out.print((int) (bytes[i] & 0xff) + " ");
    }
    System.out.println("\n----- End Bytes:\n");
  }
  
	public static void printBytes(String str){
    byte[] bytes = str.getBytes();
    System.out.println("----- Bytes:");
    for(int i=0; i< bytes.length; i++){
       System.out.print((int) (bytes[i] & 0xff) + " ");
    }
    System.out.println("\n----- End Bytes:\n");
	}
	
	public static void printBytes(String str, String charset){
    try {
      byte[] bytes = str.getBytes(charset);
  	  System.out.println("----- Bytes:");
  	  for(int i=0; i< bytes.length; i++){
  	    System.out.print((int) (bytes[i] & 0xff) + " ");
  	  }
  	  System.out.println("\n----- End Bytes:\n");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
	}
	
  public static int fromBinary(String binStr, int len){
    int res=0;
    char[] bits=binStr.toCharArray();
    for(int i=0; i<len; ++i){
      int bit=bits[i]=='0' ? 0:1;
      res = (res<<1) | bit;
    }
    return res;
  }  
  
  public static void printPosTable(ArrayList<int[]> posTable){
    System.out.println("\n--Pos Table--");
    for(int[] pos:posTable){
      for(int val:pos)
        System.out.print(val+" ");
      System.out.println();
    }
    System.out.println();
  }	
  
  public static void initParser(){
    String style="<style>"
      + ".label{color: green; font-weight: bold;} "
      + ".example{color: #666666; font-style: normal; } "
      + ".par-1, .par-2, .par-3, .par-4{margin:'0 0 2px 0';}"
      + ".par-1{padding-left: 10px; margin-bottom: 5px;} "
      + ".par-2{padding-left: 20px;} "
      + ".par-3{padding-left: 30px;} "
      + ".par-4{padding-left: 40px;} "
      + ".heading{font-weight:700; margin:'5px 0 5px 10px'; font-size:115%;}"
      + ".dict-name{text-align:right; color: #8e6bc5; font-weight:700; maring:0; padding: '5px 5px 0 0'; border-top: 1px solid #bbbbbb; }"
      + ".article{margin:'5px 0 10px 0'}"
      + "hr{border-color: green; }"
      + "body{font-family:'Arial Trebuchet MS'}"
      + "</style>\n";
    
    String body = "<body><b>The first text</b> The second text <i>The third text</i></body>";
    
    String init=style+body;
    init=Jsoup.parse(init).html();
  }
  
  public static void printDirectionsMap(Map<String, List<String>> directionsMap){
    System.out.println("--Directions--");
    for(String key : directionsMap.keySet()){
      System.out.println(key);
      List<String> dictsList = directionsMap.get(key);
      for(String dictName : dictsList){
        System.out.println("    "+dictName);
      }
    }
    System.out.println();
  }
  
  public static void writeHeadingsText(List<ArticleHeading> data, String file){
    try {
      String enc = "UTF-8";
      
      FileOutputStream fos = new FileOutputStream(file);
      OutputStreamWriter osw = new OutputStreamWriter(fos, enc);
      
      for (ArticleHeading art : data) {
        String text = art.text();
        osw.write(text + "\n");
      }
      
      osw.close();
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static List<String> readFileToArray(String file) {
    List<String> res = new ArrayList<String>();
    String line = null;
    BufferedReader br = null;

    try {
      String enc = "UTF-8";
      
      FileInputStream fis = new FileInputStream(file);
      InputStreamReader isr = new InputStreamReader(fis, enc);
      
      br = new BufferedReader(isr);
      
      while ((line = br.readLine()) != null) {
        if( line.trim().length() != 0 )
          res.add(line);
      }
      
      System.out.println("\nWords count: "+res.size());
    } 
    catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)
          br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return res;
  }
  
}
