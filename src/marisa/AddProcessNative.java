package marisa;

import java.util.List;
import java.util.Map;

import dictlib.ported.ArticleHeading;

public class AddProcessNative {
  
  public Map<String, Integer> workMap;
  public List<ArticleHeading> headingsList;
  
  public native void buildTrie(String indexFileName);
  public native void linkData(KeysetNative keyset, String refFileName);
  
}
