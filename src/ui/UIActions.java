package ui;

import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;


public class UIActions {

  MainWindow _ui;
  
    boolean htmlMode=true;
//   boolean htmlMode=false;
  
  
  UIActions(MainWindow ui){
    _ui=ui;
  }
  
  
  void showFoundArticle(){
    String item=_ui.list.getSelectedValue();
    Map<String, String> map;
    map = _ui.scanDict.getHeadingsMap();
    
    String article = map.get(item);
    outputArticle(item, article);
  }
  
  void outputArticle(String heading, String article){
    article=_ui.aparser.parseArticle(heading, article);
//    _ui.textPane.setContentType("text/html; charset='UTF-8'");
    _ui.textPane.setContentType("text/html");
    
    _ui.textPane.setText(article);
    
    _ui.textPane.setCaretPosition(0);
    
  }
  
  public void processLink(HyperlinkEvent e){
    HyperlinkEvent.EventType eType=e.getEventType();
    
    if(eType == HyperlinkEvent.EventType.ACTIVATED){
      String title="";
      Element elm=e.getSourceElement();
      AttributeSet attrs=elm.getAttributes();
      
      SimpleAttributeSet value = (SimpleAttributeSet) attrs.getAttribute(HTML.Tag.A);
      if (value != null) {
        title = (String) value.getAttribute(HTML.Attribute.TITLE);
      }
      
      searchWordFromLink(title);
    }
  }
  
  public void searchWordFromLink(String word){
    String article = _ui.scanDict.searchWordFromLink(word);
    outputArticle(word, article);
  }
  
  
// ----------------------------------------- add -----------------------------------------
  
  public void listFoundHeadings(){
    Map<String, String> map;
    map = _ui.scanDict.getHeadingsMap();
    
    Set<String> keys = map.keySet();
    String[] listData = keys.toArray(new String[keys.size()]);
    _ui.list.setListData(listData);
    
    if(listData.length !=0 ){
      _ui.list.setSelectedIndex(0);
      showFoundArticle();
    }
  }
  
  public void setTextPaneProps(){
    _ui.textPane.setEditable(false);
    _ui.textPane.setDragEnabled(true);
    
//    _ui.textPane.setContentType("text/html; charset=utf-8");
//    textPane.setMargin(new Insets(5,5,5,5));
    
    // _ui.textPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));   //!
//    _ui.textPane.setFont(new Font("Consolas", Font.PLAIN, 16));   //!
//    _ui.textPane.setFont(new Font("Tahoma", Font.PLAIN, 14));     //! (default)
//    _ui.textPane.setFont(new Font("MS Song", Font.PLAIN, 14));     //! (default)
    
//    _ui.textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    
//    _ui.textPane.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
    _ui.textPane.setFont(new Font("Arial", Font.PLAIN, 14));
//   _ui.textPane.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
//    _ui.textPane.setFont(new Font("Courier New", Font.PLAIN, 16));
  }
  
  public void fillLanguages(){
    if(!_ui.scanDict.directionsRead) return;
    
    // List<String> sourceLangsList = _ui.scanDict.getSourceLangs();
    // if(sourceLangsList.size() == 0) return;
    
    // List<String> targetLangsList = _ui.scanDict.getTargets(sourceLangsList.get(0));
    
    // _ui.cbSourceLang.setModel( new DefaultComboBoxModel<String>(sourceLangsList.toArray(new String[0])) );
    // _ui.cbTargetLang.setModel( new DefaultComboBoxModel<String>(targetLangsList.toArray(new String[0])) );
    
    
    String[] sourceLangsList = {"En", "Es", "De", "Ru", "It"};
    String[] targetLangsList = {"Es", "Ru", "De"};
    
    _ui.cbSourceLang.setModel( new DefaultComboBoxModel<String>(sourceLangsList) );
    _ui.cbTargetLang.setModel( new DefaultComboBoxModel<String>(targetLangsList) );
  }
  
}
