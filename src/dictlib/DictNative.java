package dictlib;

public class DictNative {
  
  long handle;
  
  public DictNative() {
    handle = -1;
  }
  

  public native void indexDict(String lsdFile, String indexFile);
  public native void readIndex(String lsdFile);

  public native byte[] decodeArticle(int reference);
  
  public native byte[] getDictName();
  public native String getDirection();
  
  public native void clear();
  public native void clearGlobal();
  
  public native String test();
  
}
