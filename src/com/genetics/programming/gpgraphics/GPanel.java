package com.genetics.programming.gpgraphics;


import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import com.genetics.programming.utilities.GPUtils;

/**
 *
 * @author ekakoll
 */
public class GPanel extends javax.swing.JPanel {

    private static final int CONTRAST_PANEL_WIDTH = 476;
    private static final int CONTRAST_PANEL_HEIGHT = 331;
    private Thread animation;
    /**
     * The color used for plotting
     */
    protected Color color = Color.RED;
    /**
     * The position of the origin of the coordinate system
     */
    protected int xorigin = CONTRAST_PANEL_WIDTH / 2;
    protected int yorigin = CONTRAST_PANEL_HEIGHT / 2;
    /**
     * The number of pixels between 0 and 1 in x and y direction
     */
    protected int xratio = 25, yratio = 25;
    private boolean gpRunning = false;
    // Desired number of frame per second
    protected static final int FPS = 15;  // 50 fps
    // Maximum number of frame allowed to skip
    protected static final int MAX_FRAME_TO_SKIP = 5;
    // Desired amount of time taken for an update-render
    protected static final int FRAME_PERIOD = 1000 / FPS;
    //private GPGraphicsAdapter gp;
    private String actualSolution = "";
    private boolean foundCandidate;
    protected static int MAX_FUNCTIONS = 2;
    protected int numOfFunctions = 0;
    //protected Function functions[] = new Function[MAX_FUNCTIONS];
    protected Color colors[] = new Color[MAX_FUNCTIONS];
    private List<Function> functions = new ArrayList<Function>(MAX_FUNCTIONS);
    private GPGraphicsAdapter gpGraphicsAdapter;

    /**
     * Creates new form GrapherPanel
     */
    public GPanel() {
        initComponents();
        initGP();
        functions.add(new TargetFunction());
    }

    private void initGP() {
        gpGraphicsAdapter = new GPGraphicsAdapter();
    }

    private void startGP() {
        if (animation == null) {
            animation = new Thread() {

                @Override
                public void run() {
                    runGP();
                }
            };
        }
        animation.start();
    }

    private void runGP() {
        long startTime = 0L;
        long diffTime = 0L;
        int sleepTime = 0;


        int count = 0;
        long begin = System.currentTimeMillis();
        long duration = 0L;

        boolean noSolutionFound = true;

        StringBuilder tempBuilder = new StringBuilder();
        while ((duration <= 600000) && noSolutionFound) {
            if (gpRunning) {
                startTime = System.currentTimeMillis();
                gpGraphicsAdapter.nextGeneration();

                count++;

                generationTextArea.append("Generation # " + count + "\n");
                generationTextArea.selectAll();

                tempBuilder.setLength(0);
                GPUtils.printInorderToString(gpGraphicsAdapter.getPopulation().get(0).getRootNode(), tempBuilder);
                resultLabel.setText("<html>Best Of Generation: " + count +  "<br>" + tempBuilder.toString() + "</html>");
                resultPanelLayout.setHorizontalGroup(
                        resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(resultLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                resultPanelLayout.setVerticalGroup(
                        resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(resultLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));


                if (gpGraphicsAdapter.getPopulation().get(0).getNetFitness() <= 0.01) {
                    addFunction(new SolutionFunction(), Color.GREEN);
                    gpGraphicsAdapter.getSolutions().add(gpGraphicsAdapter.getPopulation().get(0));
                    gpGraphicsAdapter.printSolutions(0);
                    noSolutionFound = false;
                    //gpRunning = false;
                    foundCandidate = true;
                    //animation = null;
                    //break;
                }
                //}
                duration = System.currentTimeMillis() - begin;
                updateGP();
            }

            // Refresh the display
            repaint();
            diffTime = System.currentTimeMillis() - startTime;
            sleepTime = (int) (FRAME_PERIOD - diffTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
            }
        }
        //startButton.setEnabled(true);

        actualSolution = "<html>" + gpGraphicsAdapter.printSolution(count) + "<br>";
        actualSolution += "Elapsed Time: " + duration / 1000 / 60 + " minutes <br>";
        if (noSolutionFound == false) {
            actualSolution += "Generation of Solution: " + count;
        }
        actualSolution += "</html>";
        resultLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        resultLabel.setVerticalTextPosition(SwingConstants.CENTER);
        resultLabel.setText(actualSolution);
        resultPanelLayout.setHorizontalGroup(
                resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(resultLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        resultPanelLayout.setVerticalGroup(
                resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(resultLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        //duration = 0L;

    }

    public void shutDownGP() {
    }

    public void updateGP() {
    }

    public void drawGP(Graphics2D g2) {
        drawCoordinates(g2);
        plotFunction(g2);
    }

    protected void plotFunction(Graphics g) {

        for (int i = 0; i < functions.size(); i++) {
            for (int px = 0; px < CONTRAST_PANEL_WIDTH; px++) {
                try {
                    double x = (double) (px - xorigin) / (double) xratio;
                    double y = functions.get(i).apply(x);
                    g.setColor(colors[i]);
                    int py = yorigin - (int) (y * yratio);
                    g.fillOval(px - 1, py - 1, 3, 3);
                } catch (Exception e) {
                }
            }
        }
        if (foundCandidate) {
            g.setColor(Color.BLUE);
            for (int px = 0; px < CONTRAST_PANEL_WIDTH; px++) {

                try {
                    double x = (double) (px - xorigin) / (double) xratio;
                    double y = solutionFunction(x);
                    int py = yorigin - (int) (y * yratio);
                    g.fillOval(px - 1, py - 1, 3, 3);
                } catch (Exception e) {
                }
            }
        }
    }

    private void addFunction(Function f, Color c) {
        if (numOfFunctions < MAX_FUNCTIONS) {
            functions.add(f);
            colors[numOfFunctions++] = c;
        }
    }

    protected void drawCoordinates(Graphics g) {
        //g.setColor(Color.white);
        g.clearRect(0, 0, CONTRAST_PANEL_WIDTH, CONTRAST_PANEL_HEIGHT);

        g.setColor(Color.BLACK);
        g.drawLine(0, yorigin, CONTRAST_PANEL_WIDTH, yorigin);
        g.drawLine(xorigin, 0, xorigin, CONTRAST_PANEL_HEIGHT);

        g.setFont(new Font("TimeRoman", Font.PLAIN, 10));
        int px, py;
        int i = 1;
        py = yorigin + 12;
        g.drawString("0", xorigin + 2, py);
        for (px = xorigin + xratio; px < CONTRAST_PANEL_WIDTH; px += xratio) {
            g.drawString(Integer.toString(i++), px - 2, py);
            g.drawLine(px, yorigin - 2, px, yorigin + 2);
        }

        i = -1;
        for (px = xorigin - xratio; px >= 0; px -= xratio) {
            g.drawString(Integer.toString(i--), px - 2, py);
            g.drawLine(px, yorigin - 2, px, yorigin + 2);
        }

        i = 1;
        px = xorigin + 4;
        for (py = yorigin - yratio; py >= 0; py -= yratio) {
            g.drawString(Integer.toString(i++), px, py + 4);
            g.drawLine(xorigin - 2, py, xorigin + 2, py);
        }

        i = -1;
        for (py = yorigin + yratio; py < CONTRAST_PANEL_HEIGHT; py += yratio) {
            g.drawString(Integer.toString(i--), px, py + 4);
            g.drawLine(xorigin - 2, py, xorigin + 2, py);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;  // if using Java 2D
        super.paintComponent(g2d);       // paint background
        setBackground(Color.white);      // may use an image for background

        // Draw GP Components
        drawGP(g2d);
    }



    public double solutionFunction(double x) {
        return GPUtils.evaluate(gpGraphicsAdapter.extractSolution().getRootNode(), x);
    }

    private void initComponents() {

        controlPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();

        icon = getImageResource("dnaIcon.png");
        startButton.setIcon(icon);
        resultPanel = new javax.swing.JPanel();
        contrastPanel = new javax.swing.JPanel();
        generationPanel = new javax.swing.JPanel();
        generationScrollPane = new javax.swing.JScrollPane();
        generationTextArea = new javax.swing.JTextArea();
        resultLabel = new javax.swing.JLabel();

        resultLabel.setFont(new Font("Bodoni MT", 1, 14)); // NOI18N
        resultLabel.setForeground(new Color(0, 0, 204));
        resultLabel.setText("Press Esc or C to Close this window");

        //setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setBorder(new javax.swing.border.LineBorder(new Color(/*0, 0, 204*/0,0,0), 2, false));
        setForeground(new Color(0, 0, 204));
        setName("mainPanel");

        controlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 0, 11), new Color(0, 0, 204))); // NOI18N
        controlPanel.setName("controlPanel");
        controlPanel.setOpaque(false);

        startButton.setFont(new Font("Bodoni MT Black", 3, 12)); // NOI18N
        startButton.setForeground(new Color(0, 0, 255));
        startButton.setText("");
        startButton.setName("startButton");
        startButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        resultPanel.setBorder(new javax.swing.border.LineBorder(new Color(0, 0, 204), 1, true));
        resultPanel.setForeground(new Color(0, 0, 255));
        resultPanel.setName("descriptionPanel");

        resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
                resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(resultLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        resultPanelLayout.setVerticalGroup(
                resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(resultLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
                controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(controlPanelLayout.createSequentialGroup().addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        controlPanelLayout.setVerticalGroup(
                controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE));

        contrastPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Contrast", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 0, 11), new Color(0, 0, 204))); // NOI18N
        contrastPanel.setOpaque(false);

        javax.swing.GroupLayout contrastPanelLayout = new javax.swing.GroupLayout(contrastPanel);
        contrastPanel.setLayout(contrastPanelLayout);
        contrastPanelLayout.setHorizontalGroup(
                contrastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 464, Short.MAX_VALUE));
        contrastPanelLayout.setVerticalGroup(
                contrastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));

        generationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Generations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 0, 11), new Color(0, 0, 204))); // NOI18N
        generationPanel.setForeground(new Color(0, 0, 204));
        generationPanel.setOpaque(false);

        generationTextArea.setColumns(20);
        generationTextArea.setForeground(new Color(0, 0, 204));
        generationTextArea.setRows(5);
        generationScrollPane.setViewportView(generationTextArea);


        javax.swing.GroupLayout generationPanelLayout = new javax.swing.GroupLayout(generationPanel);
        generationPanel.setLayout(generationPanelLayout);
        generationPanelLayout.setHorizontalGroup(
                generationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(generationScrollPane));
        generationPanelLayout.setVerticalGroup(
                generationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generationPanelLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(generationScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()).addGroup(layout.createSequentialGroup().addComponent(contrastPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(generationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(contrastPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(generationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
    }

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == startButton) {
            generationTextArea.setText("");
            gpRunning = true;
            animation = null;
            resultPanel.remove(resultLabel);
            gpGraphicsAdapter.initGPGraphicsAdapter();
            startGP();
            //startButton.setEnabled(false);
        }
    }

    /**
     * This method allows the loading of images from relative paths
     *
     */
    private ImageIcon getImageResource(String fileName) {

        java.io.InputStream imgStream = this.getClass().getResourceAsStream(fileName );
        java.awt.image.BufferedImage myImg = null;

        try {
            myImg = javax.imageio.ImageIO.read(imgStream);
        } catch(IOException e) {
            e.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(myImg);
        return icon;
    }
    // Variables declaration
    private javax.swing.JPanel contrastPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel generationPanel;
    private javax.swing.JScrollPane generationScrollPane;
    private javax.swing.JTextArea generationTextArea;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel resultLabel;
    private javax.swing.GroupLayout resultPanelLayout;
    private Icon icon;
    // End of variables declaration
}

