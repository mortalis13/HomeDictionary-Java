package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class TestWin {

  private JFrame frame;
  public JPanel pControls;
  public JPanel pMain;
  public JScrollPane spHeadingsList;
  public JList lHeadings;
  public JScrollPane spArticle;
  public JTextPane tpArticle;
  public JButton bScan;
  public JButton bRead;
  public JButton bSearch;
  public JComboBox cbSource;
  public JButton bSwap;
  public JComboBox cbTarget;
  public JTextField tfWord;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          TestWin window = new TestWin();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public TestWin() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 628, 471);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    pControls = new JPanel();
    pMain = new JPanel();
    
    spHeadingsList = new JScrollPane();
    spArticle = new JScrollPane();
    
    lHeadings = new JList();
    tpArticle = new JTextPane();
    
    bScan = new JButton("New button");
    bRead = new JButton("New button");
    bSearch = new JButton("New button");
    cbSource = new JComboBox();
    bSwap = new JButton("New button");
    cbTarget = new JComboBox();
    tfWord = new JTextField();
    
    tfWord.setColumns(10);
    
    spArticle.setViewportView(tpArticle);
    spHeadingsList.setViewportView(lHeadings);
    
    
    GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.TRAILING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
            .addComponent(pMain, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addComponent(pControls, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
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
          .addContainerGap()
          .addComponent(bScan, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(bRead, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(bSearch, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(cbSource, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(bSwap, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(cbTarget, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(tfWord, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
          .addContainerGap())
    );
    gl_pControls.setVerticalGroup(
      gl_pControls.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_pControls.createSequentialGroup()
          .addContainerGap()
          .addGroup(gl_pControls.createParallelGroup(Alignment.BASELINE)
            .addComponent(bScan, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
            .addComponent(bRead, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
            .addComponent(bSearch, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
            .addComponent(cbSource, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
            .addComponent(bSwap, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
            .addComponent(cbTarget, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
            .addComponent(tfWord, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
          .addContainerGap())
    );
    
    
    GroupLayout gl_pMain = new GroupLayout(pMain);
    gl_pMain.setHorizontalGroup(
      gl_pMain.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_pMain.createSequentialGroup()
          .addContainerGap()
          .addComponent(spHeadingsList, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(spArticle, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
          .addContainerGap())
    );
    gl_pMain.setVerticalGroup(
      gl_pMain.createParallelGroup(Alignment.LEADING)
        .addGroup(Alignment.TRAILING, gl_pMain.createSequentialGroup()
          .addContainerGap()
          .addGroup(gl_pMain.createParallelGroup(Alignment.TRAILING)
            .addComponent(spArticle, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
            .addComponent(spHeadingsList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))
          .addContainerGap())
    );
    
    pControls.setLayout(gl_pControls);
    pMain.setLayout(gl_pMain);
    frame.getContentPane().setLayout(groupLayout);
    
  }
}
