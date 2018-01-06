
package marisa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import dictlib.ported.ArticleHeading;


public class Marisa implements java.io.Closeable {

  public static class IdKeyPair {
    public long Id;
    public String Key;
  }

  @SuppressWarnings("unused")
  private static MarisaLoader loader = new MarisaLoader();

  private TrieNative trie;
  private KeysetNative keyset;
  
  private java.nio.charset.Charset charset;
  private java.nio.charset.Charset environmentCharset;

  
  public Marisa() {
    try {
      trie = new TrieNative();
      keyset = new KeysetNative();        // don't create on read index
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }

//    this.charset = java.nio.charset.Charset.forName("UTF-16LE");
    this.charset = java.nio.charset.Charset.forName("UTF-8");
    
    this.environmentCharset = java.nio.charset.Charset.defaultCharset();
  }

  
  public void buildTrie(List<ArticleHeading> data, String indexFileName) {
    AddProcessNative addProcess = new AddProcessNative();
    addProcess.headingsList = data;
    addProcess.buildTrie(indexFileName);
  }
  
  public void linkData(Map<String, Integer> data, String refFileName) {
    AddProcessNative addProcess = new AddProcessNative();
    addProcess.workMap = data;
    addProcess.linkData(keyset, refFileName);
  }
  
  
  public void push_back(String key) {
    try {
      byte[] bytes = key.getBytes(charset);
      keyset.push_back(bytes);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  
  public void build() {
    try {
      this.trie.build(keyset);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    finally {
//      keyset.dispose();
    }
  }
  
  public void build(Collection<String> keys) {
    KeysetNative keyset = new KeysetNative();
    
    try {
      for (String key : keys) {
        byte[] bytes = key.getBytes(this.getCharset());
        keyset.push_back(bytes);
      }

      this.trie.build(keyset);
    } 
    finally {
      // keyset.dispose();
    }
  }
  
  public void load(String fileName) {
    byte[] bytes = fileName.getBytes(this.getEnvironmentCharset());
    trie.load(bytes);
  }

  public void save(String fileName) {
    byte[] bytes = fileName.getBytes(this.getEnvironmentCharset());
    this.trie.save(bytes);
  }

  public IdKeyPair lookup(String key) {
    return lookup(key, 0);
  }

  public IdKeyPair lookup(String key, long id) {
    byte[] bytes = key.getBytes(this.getCharset());

    AgentNative agent = new AgentNative();
    try {
      agent.set_query(bytes);
      agent.set_query(id);

      boolean matches = this.trie.lookup(agent);
      if (!matches) {
        return null;
      }

      KeyNative keyNative = agent.key();
      IdKeyPair ret = new IdKeyPair();

      ret.Id = keyNative.id();
      ret.Key = new String(keyNative.text(), this.getCharset());

      return ret;
    } finally {
      agent.dispose();
    }
  }
  
  
  public java.nio.charset.Charset getCharset() {
    return this.charset;
  }

  public void setCharset(java.nio.charset.Charset charset) {
    this.charset = charset;
  }

  public java.nio.charset.Charset getEnvironmentCharset() {
    return this.environmentCharset;
  }

  public void setEnvironmentCharset(java.nio.charset.Charset charset) {
    this.environmentCharset = charset;
  }

  public int size() {
    try {
      return (int) this.trie.num_keys();
    } catch (MarisaException ignore) {
      return 0;
    }
  }


  public IdKeyPair reverseLookup(String key) {
    return reverseLookup(key, 0);
  }

  public IdKeyPair reverseLookup(String key, long id) {
    byte[] bytes = key.getBytes(this.getCharset());

    AgentNative agent = new AgentNative();
    try {
      agent.set_query(bytes);
      agent.set_query(id);

      this.trie.reverse_lookup(agent);

      KeyNative keyNative = agent.key();
      IdKeyPair ret = new IdKeyPair();

      ret.Id = keyNative.id();
      ret.Key = new String(keyNative.text(), this.getCharset());

      return ret;
    } finally {
      agent.dispose();
    }
  }

  public List<IdKeyPair> commonPrefixSearch(String key) {
    return commonPrefixSearch(key, 0);
  }

  public List<IdKeyPair> commonPrefixSearch(String key, long id) {
    byte[] bytes = key.getBytes(this.getCharset());

    AgentNative agent = new AgentNative();
    try {
      agent.set_query(bytes);
      agent.set_query(id);

      List<IdKeyPair> ret = new ArrayList<IdKeyPair>();
      boolean matches = this.trie.common_prefix_search(agent);
      while (matches) {
        KeyNative keyNative = agent.key();
        IdKeyPair pair = new IdKeyPair();

        pair.Id = keyNative.id();
        pair.Key = new String(keyNative.text(), this.getCharset());
        ret.add(pair);

        matches = this.trie.common_prefix_search(agent);
      }

      return ret;
    } finally {
      agent.dispose();
    }
  }

  public List<IdKeyPair> predictiveSearch(String key) {
    return predictiveSearch(key, 0);
  }

  public List<IdKeyPair> predictiveSearch(String key, long id) {
    byte[] bytes = key.getBytes(this.getCharset());

    AgentNative agent = new AgentNative();
    try {
      agent.set_query(bytes);
      agent.set_query(id);

      List<IdKeyPair> ret = new ArrayList<IdKeyPair>();
      boolean matches = this.trie.predictive_search(agent);
      while (matches) {
        KeyNative keyNative = agent.key();
        IdKeyPair pair = new IdKeyPair();

        pair.Id = keyNative.id();
        pair.Key = new String(keyNative.text(), this.getCharset());
        ret.add(pair);

        matches = this.trie.predictive_search(agent);
      }

      return ret;
    } finally {
      agent.dispose();
    }
  }
  

  public void clear() {
    this.trie.clear();
  }

  public void close() {
    if (this.trie != null) {
      System.out.println("java: del-trie");
      keyset.dispose();
      trie.dispose();
      this.trie = null;
    }
    else{
      System.out.println("java: del-trie - null");
    }
  }
  
  public KeysetNative getKeyset(){
    return keyset;
  }

}
