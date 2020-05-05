/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 *
 * @author stevewang
 */
public class QuestionGeneratorGUI extends javax.swing.JFrame {

    private File QuestionTemplateFile;
    private File OntologyFile; 
    private String QuestionTemplatePath;
    private String OntologyPath;
    
    /**
     * Creates new form QuestionGeneratorGUI
     */
    public QuestionGeneratorGUI() {
        initComponents();
        QuestionTemplateFile = null;
        OntologyFile = null;
        QuestionTemplatePath = "";
        OntologyPath = "";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FileChooser = new javax.swing.JFileChooser();
        MainPanel = new javax.swing.JPanel();
        templateTextField = new javax.swing.JTextField();
        QuestionTemplateButton = new javax.swing.JButton();
        ontologyTextField = new javax.swing.JTextField();
        ChooseOntologyButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        GenerateQuestionsButton = new javax.swing.JButton();
        outoutScrollPanel = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextArea();
        QuitButton = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        templateTextField.setEditable(false);

        QuestionTemplateButton.setText("Choose Template File (.txt)");
        QuestionTemplateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuestionTemplateButtonActionPerformed(evt);
            }
        });

        ontologyTextField.setEditable(false);

        ChooseOntologyButton.setText("Choose Ontology File (.owl)");
        ChooseOntologyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChooseOntologyButtonActionPerformed(evt);
            }
        });

        GenerateQuestionsButton.setText("Generate Questions");
        GenerateQuestionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenerateQuestionsButtonActionPerformed(evt);
            }
        });

        outputTextArea.setColumns(20);
        outputTextArea.setRows(5);
        outoutScrollPanel.setViewportView(outputTextArea);

        QuitButton.setText("Quit");
        QuitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitButtonActionPerformed(evt);
            }
        });

        jCheckBox1.setText("many");

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(GenerateQuestionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(ontologyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                                    .addComponent(templateTextField))
                                .addGap(18, 18, 18)
                                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(QuestionTemplateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ChooseOntologyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(outoutScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(QuitButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(templateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(QuestionTemplateButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ontologyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChooseOntologyButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(GenerateQuestionsButton)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outoutScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(QuitButton)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void QuestionTemplateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuestionTemplateButtonActionPerformed
        JFileChooser fc = new JFileChooser();
        int returnVal  = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            QuestionTemplateFile = fc.getSelectedFile();
            QuestionTemplatePath = QuestionTemplateFile.getPath();
            templateTextField.setText("File: "+ QuestionTemplateFile.getName() + ".");
            System.out.println("Question Template File: "+ QuestionTemplateFile.getName() + ".");
            System.out.println("From path: " + QuestionTemplatePath);
        }else{
            System.out.println("User cancelled question template file choosing process.");
        }
    }//GEN-LAST:event_QuestionTemplateButtonActionPerformed

    private void ChooseOntologyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChooseOntologyButtonActionPerformed
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            OntologyFile = fc.getSelectedFile();
            OntologyPath = OntologyFile.getPath();
            ontologyTextField.setText("File: "+ OntologyFile.getName()+ ".");
            System.out.println("Ontology File: "+ OntologyFile.getName() + ".");
            System.out.println("From path: "+ OntologyPath);
        } else {
            System.out.println("User cancalled ontology selection process.");
        }
    }//GEN-LAST:event_ChooseOntologyButtonActionPerformed

    private void QuitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_QuitButtonActionPerformed

    private void GenerateQuestionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerateQuestionsButtonActionPerformed
        outputTextArea.setText("");
        outputTextArea.setText("Qenerating Questions....");
        ArrayList <String> generateQuestions = new ArrayList <String> ();
        if (QuestionTemplatePath == "") {
            //QuestionTemplatePath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/Templates.txt";
            QuestionTemplatePath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/templates_complex_2.txt";
            QuestionTemplateFile = new File(QuestionTemplatePath);
           // outputTextArea.setText("default Questions : templates from Steve");
        }
        if (OntologyPath == "") {
            
            OntologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/AfricanWildlifeOntology1.owl";
             //OntologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/movieontology.owl";
             //OntologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/stuff3.owl";
            //OntologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/btl2.owl";
            OntologyFile = new File(OntologyPath);
            outputTextArea.setText("Ontology : AfricanWildlifeOntology1.owl ");
        }
        
        // atao foana
        {
            try {
                
                
                System.out.println("questionsPath = "+ QuestionTemplatePath);
                System.out.println("OntologyPath = "+ OntologyPath);
                
                ArrayList<String> lines = new ArrayList<String>();
                
                
                TestOntology questGen = new TestOntology(QuestionTemplateFile.getAbsolutePath(), OntologyFile.getAbsolutePath());
                System.out.println("Raikitra_tena_izy with interface");
                // many questions
                ArrayList<String> questions;
                
                if (jCheckBox1.isSelected())
                    questions = questGen.KetrikaTestAllIteration(300);
                else questions = questGen.KetrikaTest2();
                // generateQuestions =OWLHandler.generateQuestions(QuestionTemplateFile, OntologyFile);
                
                for (String question: questions) {
                    if (!question.contains("No valid")) {
                        outputTextArea.append("\n"+question);
                        lines.add(question);
                    }
                }
                
                /*String pathGenQuest = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/genQuests_AWO.txt";
                FileWriter writer = new FileWriter(pathGenQuest); 
                for(String str: lines) {
                writer.write(str + System.lineSeparator());
                }
                writer.close();*/
                
            } 
            catch (OWLOntologyCreationException ex) {
                Logger.getLogger(QuestionGeneratorGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(QuestionGeneratorGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_GenerateQuestionsButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        
        
       /**/
     /*   try {
            String filenameTemplapte = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/templates_complex_2.txt";
             //String ontologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/btl2.owl";
            String ontologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/AfricanWildlifeOntology1.owl";
            //String ontologyPath  = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/stuff3.owl";
            TestOntology test = new TestOntology(filenameTemplapte, ontologyPath);
            //test.Test();
            // several questions
            //test.TestKetrika();
            
            System.out.println("Raikitra_tena_izy");
            // many questions
           test.KetrikaTestAllIteration(300);
               
        } catch (Exception ex) {
            Logger.getLogger(QuestionGeneratorGUI.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
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
                java.util.logging.Logger.getLogger(QuestionGeneratorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(QuestionGeneratorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(QuestionGeneratorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(QuestionGeneratorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
            //</editor-fold>
            //</editor-fold>
            //</editor-fold>
            try {
            /* Create and display the form */
            
            
            //TestOntology test = new TestOntology();
            
            
             java.awt.EventQueue.invokeLater(new Runnable() {
             public void run() {
                new QuestionGeneratorGUI().setVisible(true);
             }
             });
            
            /*TestOntology test = new TestOntology();
            test.Test();
            MyProject.Init();*/
            
            } catch (Exception ex) {
            Logger.getLogger(QuestionGeneratorGUI.class.getName()).log(Level.SEVERE, null, ex);
            }          
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ChooseOntologyButton;
    private javax.swing.JFileChooser FileChooser;
    private javax.swing.JButton GenerateQuestionsButton;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JButton QuestionTemplateButton;
    private javax.swing.JButton QuitButton;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField ontologyTextField;
    private javax.swing.JScrollPane outoutScrollPanel;
    private javax.swing.JTextArea outputTextArea;
    private javax.swing.JTextField templateTextField;
    // End of variables declaration//GEN-END:variables
}
