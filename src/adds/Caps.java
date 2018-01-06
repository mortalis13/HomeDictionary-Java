package adds;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Caps {

  List<int[]> posTable;
  Map<Integer, Boolean> capitals;
  
  
  public Caps(){
    capitals = new TreeMap<Integer, Boolean>();
    posTable = new ArrayList<int[]>();
  }
  
  
  public boolean isCapital(int cap, boolean normalize){
    if(normalize) cap = normalize(cap);
    Boolean processed = capitals.get(cap);
    
    if(processed == null){
      capitals.put(cap, true);
      return true;
    }
    
    return false;
  }
  
  public int normalize(int cap){
    int idx = Arrays.binarySearch( Vars.diacrits, cap );
    if( idx < 0 ) 
      return cap;
    
    return Vars.normalizedCaps[ Vars.dton[idx] ];
  }
  
  public void addPos(int[] pos){
    posTable.add(pos);
  }
  
  public void correctOffsets(int addOff){
    int size=posTable.size();
    System.out.println("posTable-size: "+size);
    int off=4+4*4*size+addOff;
    
    for(int[] pos:posTable){
      pos[1]+=off;
    }
  }
  
  
// ---------------------------------------------- Add ----------------------------------------------
  
  public List<int[]> getPosTable(){
    return posTable;
  }
  
  public void printPosTable(){
    System.out.println("-- posTable --");
    for(int[] pos:posTable){
      System.out.print((char) pos[0]+": ");
      for(int i=0; i<4; i++)
        System.out.print(pos[i]+" ");
      System.out.println();
    }
    System.out.println();
  }
  
}
