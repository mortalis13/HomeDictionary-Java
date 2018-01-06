package ui;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.ScanDict;
import adds.ArticleParser;
import adds.Functions;
import adds.WindowFunctions;
import java.awt.Font;


public class MainWindow {

  static MainWindow window;

  UIActions uiactions;
  ScanDict scanDict;
  ArticleParser aparser;
  
  private JFrame frmHomeDictionary;
  JList<String> list;
  
  JScrollPane pHeadings;
  JTextPane textPane;
  
  JTextField tfWord;
  JButton bSearch;
  JButton bRead;
  public JComboBox<String> cbSourceLang;
  public JComboBox<String> cbTargetLang;
  
  JPanel pControls;
  
  
  static{
    System.out.println("loading dictlib.dll");
    System.loadLibrary("dictlib");
  }

  
  Action exitAction = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      frmHomeDictionary.dispose();
    }
  };
  public JButton btnSearchTest;
  

  /** 
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          window = new MainWindow();
          window.frmHomeDictionary.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace(); 
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public MainWindow() {
    initialize();
  }
  
  
  void searchWord(){
    String word = tfWord.getText();
    
    long time = System.currentTimeMillis();
    scanDict.searchWord(word);
    Functions.logTime("SearchWord", time);
    
    uiactions.listFoundHeadings();
  }
  

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frmHomeDictionary = new JFrame();
    frmHomeDictionary.setTitle("Home Dictionary");

    frmHomeDictionary.setBounds(100, 100, 926, 700);
    frmHomeDictionary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frmHomeDictionary.setLocationRelativeTo(null);
    
    WindowFunctions.addShortcuts(frmHomeDictionary);
    
    textPane = new JTextPane();
    
    
    
  //------------------------------------------------- Actions -------------------------------------------------
    
    JButton bScan = new JButton("Scan");
    bScan.setMinimumSize(new Dimension(55, 40));
    bScan.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        scanDict.indexDict();
      }
    });
    
    bRead = new JButton("Read");
    bRead.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String sourceLang = (String) cbSourceLang.getSelectedItem();
        String targetLang = (String) cbTargetLang.getSelectedItem();
        
        // sourceLang = "De";
        // targetLang = "Ru";
        
        // scanDict.readIndex();
        scanDict.readIndex(sourceLang, targetLang);
        
        System.out.println("Index Read");
      }
    });
    
    
    bSearch = new JButton("Search");
    bSearch.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        searchWord();
        // String word = tfWord.getText();
        
        // long time = System.currentTimeMillis();
        // scanDict.searchWord(word);
        // Functions.logTime("SearchWord", time);
        
        // uiactions.listFoundHeadings();
      }
    });
    
    btnSearchTest = new JButton("Search Test");
    btnSearchTest.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // scanDict.searchTest();
//        scanDict.test();
        int x = 5%8;
        System.out.println("x:"+x);
      }
    });
    
    
    JButton bListHeadings = new JButton("List Headings");
    bListHeadings.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("No Implementation");
      }
    });
    
    JButton bSwapLangs = new JButton("Swap");
    bSwapLangs.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String source = (String) cbSourceLang.getSelectedItem();
        String target = (String) cbTargetLang.getSelectedItem();
        cbSourceLang.setSelectedItem(target);
        cbTargetLang.setSelectedItem(source);
      }
    });
    
    
    list = new JList<String>();
    list.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {}
    });
    list.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
          if(list.getModel().getSize() == 0) return;
          
          long time=System.currentTimeMillis();
          uiactions.showFoundArticle();
          Functions.logTime("ShowResult", time);
        }
      }
    });
    
    tfWord = new JTextField();
    tfWord.setFont(new Font("Tahoma", Font.PLAIN, 14));
    tfWord.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // bSearch.doClick();
        searchWord();
      }
    });
    
    
    cbSourceLang = new JComboBox<String>();
    cbSourceLang.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String item = (String) cbSourceLang.getSelectedItem();
        List<String> targets = scanDict.getTargets(item);
        cbTargetLang.setModel( new DefaultComboBoxModel<String>( targets.toArray(new String[0] )) );
      }
    });
    
    cbTargetLang = new JComboBox<String>();
    cbTargetLang.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String item = (String) cbTargetLang.getSelectedItem();
      }
    });
    
    frmHomeDictionary.addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {                             // assign static objects for use in other classes
        uiactions=new UIActions(window);
        scanDict=new ScanDict();
        scanDict.setUIActions(uiactions);
        aparser=new ArticleParser();
        
        uiactions.setTextPaneProps();
        
        scanDict.readDirections();
        uiactions.fillLanguages();
        
//        bRead.doClick();
      }
    });
    
    
    textPane.addHyperlinkListener(new HyperlinkListener() {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent e) {
        uiactions.processLink(e);
      }
    });
    
    
  //------------------------------------------------- Layout -------------------------------------------------
    
    pControls = new JPanel();
    JPanel pMain = new JPanel();
    
    pHeadings = new JScrollPane();
    JScrollPane pArticlePane = new JScrollPane();
    
    frmHomeDictionary.setMinimumSize(new Dimension(950, 500));
    
    pArticlePane.setViewportView(textPane);
    pHeadings.setViewportView(list);
    
    tfWord.setMargin(new Insets(2, 5, 2, 2));
    
    GroupLayout groupLayout = new GroupLayout(frmHomeDictionary.getContentPane());
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.TRAILING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
            .addComponent(pMain, Alignment.LEADING, 900, 900, Short.MAX_VALUE)
            .addComponent(pControls, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 900, Short.MAX_VALUE))
          .addContainerGap())
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(pControls, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(pMain, GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
          .addContainerGap())
    );


    GroupLayout gl_pControls = new GroupLayout(pControls);
    gl_pControls.setHorizontalGroup(
      gl_pControls.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_pControls.createSequentialGroup()
          .addComponent(tfWord, 0, 276, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(cbSourceLang, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(bSwapLangs, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(cbTargetLang, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(bScan, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(bRead, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(bListHeadings, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(bSearch, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
          )
    );
    gl_pControls.setVerticalGroup(
      gl_pControls.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_pControls.createSequentialGroup()
          .addGap(5)
          .addGroup(gl_pControls.createParallelGroup(Alignment.BASELINE)
            .addComponent(bScan, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
            .addComponent(bRead, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
            .addComponent(bListHeadings, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
            .addComponent(bSearch, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
            .addComponent(cbSourceLang, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
            .addComponent(bSwapLangs, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
            .addComponent(cbTargetLang, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
            .addComponent(tfWord, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
          .addGap(5))
    );
    
    
    GroupLayout gl_pMain = new GroupLayout(pMain);
    gl_pMain.setHorizontalGroup(
      gl_pMain.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_pMain.createSequentialGroup()
          .addComponent(pHeadings, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(pArticlePane, 300, 300, Short.MAX_VALUE)
          )
    );
    gl_pMain.setVerticalGroup(
      gl_pMain.createParallelGroup(Alignment.LEADING)
        .addGroup(Alignment.TRAILING, gl_pMain.createSequentialGroup()
          .addGroup(gl_pMain.createParallelGroup(Alignment.TRAILING)
            .addComponent(pArticlePane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
            .addComponent(pHeadings, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))
          )
    );
    
    
    pControls.setLayout(gl_pControls);
    pMain.setLayout(gl_pMain);
    frmHomeDictionary.getContentPane().setLayout(groupLayout);
  }
}
