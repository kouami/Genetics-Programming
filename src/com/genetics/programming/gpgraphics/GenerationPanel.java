package com.genetics.programming.gpgraphics;


import javax.swing.JPanel;

/**
 * @author ekakoll
 */
public class GenerationPanel extends JPanel {

    public GenerationPanel() {
        init();
    }

    public void init() {
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Generations  Display", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 204))); // NOI18N
        this.setForeground(new java.awt.Color(0, 0, 204));
        this.setName("targetPanel");

        javax.swing.GroupLayout targetPanelLayout = new javax.swing.GroupLayout(this);
        this.setLayout(targetPanelLayout);
        targetPanelLayout.setHorizontalGroup(
                targetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 221, Short.MAX_VALUE)
        );
        targetPanelLayout.setVerticalGroup(
                targetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 229, Short.MAX_VALUE)
        );

    }
}
