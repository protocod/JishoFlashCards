/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Wolverine
 */
public class MainFrame extends javax.swing.JFrame {

    private final Map<String, String> words = new HashMap<>();
    private final Map<String, String> wrongWords = new HashMap<>();
    
    private List<String> examWords = new ArrayList<>();
    Random randomGenerator = new Random();;
    int randomIndex = -1;
    Integer correctWordsNum = 0;
    Integer wrongWordsNum = 0;
    Color greenColor = new Color(29,184,32);
    Charset charset = Charset.forName("UTF-8");
    DefaultListModel fileListModel = new DefaultListModel();
    boolean translationShown = false;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() 
    {
        initComponents();
        this.setLocationRelativeTo(null);
        endLabel.setVisible(false);
        loadFileList();
    }
    
    private void getWord()
    {
        if(examWords.size() > 0)
        {
            randomIndex = randomGenerator.nextInt(examWords.size());
            wordTextField.setText(examWords.get(randomIndex));
        }
        else
        {
            endLabel.setVisible(true);
        }
        
        transTextField.setText("");
        transTextField.grabFocus();
    }
    
    private void checkWord()
    {
        boolean result;
        String correctTranslation = words.get(examWords.get(randomIndex));
        String userTranslation = transTextField.getText().trim();
        
        if(correctTranslation.contains("("))
        {
            if(correctTranslation.indexOf("(") == 0)
            {
                correctTranslation = correctTranslation.substring(correctTranslation.indexOf(")") + 1);
            }
            else
            {
                correctTranslation = correctTranslation.substring(0, correctTranslation.indexOf("(") - 1);
            }
        }
      
        if(correctTranslation.contains(";"))
        {
            result = false;
            correctTranslation = correctTranslation.replace(",", ";");
            String[] multiWords = correctTranslation.split(";");
            
            for(String multiWord : multiWords)
            {
                if(multiWord.trim().equalsIgnoreCase(userTranslation))
                {
                    result = true;
                    break;
                }
            }
        }
        else
        {
            result = correctTranslation.trim().equalsIgnoreCase(userTranslation);
        }
        
        String currentWord = examWords.get(randomIndex);
        String currentTranslation = words.get(examWords.get(randomIndex));
                
        if(result)
        {
            if(!translationShown)
            {
                correctWordsNum++;
                correctLabel.setText(correctWordsNum.toString());
            }
            else
            {
                wrongWordsNum++;
                wrongLabel.setText(wrongWordsNum.toString());
                wrongWords.put(currentWord, currentTranslation);
                wrongWordsArea.append(currentWord + " - " + currentTranslation + "\n");
                translationShown = false;
            }
            
            showTextField.setForeground(greenColor);
            showTextField.setText(currentWord + " - " + currentTranslation + "\n");
        }
        else
        {
            wrongWordsNum++;
            wrongLabel.setText(wrongWordsNum.toString());
            showTextField.setForeground(Color.red);
            showTextField.setText(currentWord + " - " + currentTranslation + "\n");
            
            wrongWords.put(currentWord, currentTranslation);
            wrongWordsArea.append(currentWord + " - " + currentTranslation + "\n");
        }
        
        transTextField.setText("");
        transTextField.grabFocus();
        
        examWords.remove(randomIndex);
        getWord();
    }

    private void loadFileList()
    {
        Path dir = Paths.get("data");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.csv")) 
        {
            fileListModel.clear();
            for (Path file: stream) 
            {
                fileListModel.addElement(file);
            }
            
            fileList.setModel(fileListModel);
        }
        catch (IOException | DirectoryIteratorException x) 
        {
            JOptionPane.showMessageDialog(this
                    , "Error while loading file list: " + x.getMessage()
                    , "File list read error"
                    , JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadFile(Path file)
    {
        words.clear();
        examWords.clear();
        wrongWords.clear();
        
        showTextField.setText("");
        wrongWordsArea.setText("");
        endLabel.setVisible(false);
        
        BufferedReader reader = null;
        try 
        {
            String line;
            Integer lineNum = 0;
            reader = Files.newBufferedReader(file, charset);
            while ((line = reader.readLine()) != null)
            {
                lineNum++;
                String[] tempWords = line.trim().split(":");
                if((tempWords.length != 2) || (tempWords[0].isEmpty()) || (tempWords[1].isEmpty()))
                {
                    JOptionPane.showMessageDialog(this
                    , "Invalid word found @ " + lineNum.toString() + ": " + line
                    , "Invalid word"
                    , JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                this.words.put(tempWords[0].trim(), tempWords[1].trim());
            }   
            examWords = new ArrayList<>(words.size());
            words.keySet().stream().forEach((word) -> {
                examWords.add(word);
            }); 
            totalWordsLabel.setText(Integer.toString(words.size()));
            
            correctWordsNum = 0;
            correctLabel.setText(correctWordsNum.toString());
            
            wrongWordsNum = 0;
            wrongLabel.setText(wrongWordsNum.toString());         
            getWord();
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this
                    , "Error while loading file " + file.toString() + ". Check file."
                    , "File read error"
                    , JOptionPane.ERROR_MESSAGE);
        } 
        finally 
        {
            try 
            {
                if(reader != null)
                {
                    reader.close();
                }
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        transTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        checkWordBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        wrongLabel = new javax.swing.JLabel();
        correctLabel = new javax.swing.JLabel();
        totLabel = new javax.swing.JLabel();
        totalWordsLabel = new javax.swing.JLabel();
        showTranslationBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        wordTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        showTextField = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        wrongWordsArea = new javax.swing.JTextArea();
        repeatButton = new javax.swing.JButton();
        repeatWrongButton = new javax.swing.JButton();
        endLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        loadFilesBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Jisho Flash Cards");
        setForeground(java.awt.Color.white);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        transTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transTextFieldActionPerformed(evt);
            }
        });
        transTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                transTextFieldKeyPressed(evt);
            }
        });

        jLabel1.setText("Превод:");

        checkWordBtn.setText("Провери");
        checkWordBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkWordBtnActionPerformed(evt);
            }
        });

        jLabel4.setText("Верни:");

        jLabel5.setText("Грешни:");

        wrongLabel.setText("0");

        correctLabel.setText("0");

        totLabel.setText("Общо думи:");

        totalWordsLabel.setText("0");

        showTranslationBtn.setText("Покажи значение");
        showTranslationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTranslationBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Дума:");

        wordTextField.setEditable(false);
        wordTextField.setBackground(new java.awt.Color(255, 255, 255));
        wordTextField.setFont(new java.awt.Font("SimHei", 0, 18)); // NOI18N
        wordTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setText("Превод:");

        showTextField.setEditable(false);
        showTextField.setColumns(20);
        showTextField.setLineWrap(true);
        showTextField.setRows(5);
        showTextField.setWrapStyleWord(true);
        jScrollPane2.setViewportView(showTextField);

        jLabel7.setText("Сгрешени/непознати думи:");

        wrongWordsArea.setEditable(false);
        wrongWordsArea.setColumns(20);
        wrongWordsArea.setLineWrap(true);
        wrongWordsArea.setRows(5);
        wrongWordsArea.setWrapStyleWord(true);
        jScrollPane4.setViewportView(wrongWordsArea);

        repeatButton.setText("Отначало");
        repeatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatButtonActionPerformed(evt);
            }
        });

        repeatWrongButton.setText("Отначало, само сгрешени");
        repeatWrongButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatWrongButtonActionPerformed(evt);
            }
        });

        endLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        endLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        endLabel.setText("Тестът приключи!");
        endLabel.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4))
                    .addComponent(wordTextField)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(203, 203, 203)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(totLabel)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(correctLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
                                            .addComponent(totalWordsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(wrongLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(endLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(transTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(showTranslationBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(checkWordBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(repeatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(repeatWrongButton, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(checkWordBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showTranslationBtn)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totLabel)
                        .addComponent(totalWordsLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(correctLabel)
                                .addComponent(endLabel))
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(wrongLabel)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(repeatButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(repeatWrongButton)))
                .addGap(9, 9, 9)
                .addComponent(wordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        loadFilesBtn.setText("Зареждане на думи");
        loadFilesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFilesBtnActionPerformed(evt);
            }
        });

        fileList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        fileList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                fileListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(fileList);

        jLabel6.setText("Файлове:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 12, Short.MAX_VALUE)
                        .addComponent(loadFilesBtn))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(loadFilesBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadFilesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFilesBtnActionPerformed
        if (fileList.getSelectedIndex() >= 0) 
        {
            loadFile((Path)fileList.getSelectedValue());
        }
    }//GEN-LAST:event_loadFilesBtnActionPerformed

    private void showTranslationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTranslationBtnActionPerformed
        if(randomIndex > -1)
        {
            showTextField.setForeground(Color.black);
            showTextField.setText(words.get(examWords.get(randomIndex)));
            translationShown = true;
        }
    }//GEN-LAST:event_showTranslationBtnActionPerformed

    private void checkWordBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkWordBtnActionPerformed
        if(!transTextField.getText().isEmpty())
        {
            checkWord();
        }
    }//GEN-LAST:event_checkWordBtnActionPerformed

    private void transTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_transTextFieldKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_transTextFieldKeyPressed

    private void transTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transTextFieldActionPerformed
        if(!transTextField.getText().isEmpty())
        {
            checkWord();
        }
    }//GEN-LAST:event_transTextFieldActionPerformed

    private void fileListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_fileListValueChanged

        if (evt.getValueIsAdjusting() == false) 
        {
            
        }
    }//GEN-LAST:event_fileListValueChanged

    private void repeatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatButtonActionPerformed
        if (fileList.getSelectedIndex() >= 0) 
        {
            loadFile((Path)fileList.getSelectedValue());
        }
    }//GEN-LAST:event_repeatButtonActionPerformed

    private void repeatWrongButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatWrongButtonActionPerformed
        
        words.clear();
        examWords.clear();
        endLabel.setVisible(false);
        
        words.putAll(wrongWords);
        for(String wrongWord : words.keySet())
        {
            examWords.add(wrongWord);
        }
        wrongWords.clear();
        wrongWordsArea.setText("");
        showTextField.setText("");
        totalWordsLabel.setText(Integer.toString(words.size()));
        
        correctWordsNum = 0;
        correctLabel.setText(correctWordsNum.toString());

        wrongWordsNum = 0;
        wrongLabel.setText(wrongWordsNum.toString()); 
        getWord();
    }//GEN-LAST:event_repeatWrongButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkWordBtn;
    private javax.swing.JLabel correctLabel;
    private javax.swing.JLabel endLabel;
    private javax.swing.JList fileList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton loadFilesBtn;
    private javax.swing.JButton repeatButton;
    private javax.swing.JButton repeatWrongButton;
    private javax.swing.JTextArea showTextField;
    private javax.swing.JButton showTranslationBtn;
    private javax.swing.JLabel totLabel;
    private javax.swing.JLabel totalWordsLabel;
    private javax.swing.JTextField transTextField;
    private javax.swing.JTextField wordTextField;
    private javax.swing.JLabel wrongLabel;
    private javax.swing.JTextArea wrongWordsArea;
    // End of variables declaration//GEN-END:variables
}
