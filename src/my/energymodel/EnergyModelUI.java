/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EnergyModelUI.java
 *
 * Created on Apr 13, 2013, 2:43:51 PM
 */
package my.energymodel;

import java.awt.*;
import javax.swing.*;
//import java.util.Timer;
//import java.util.TimerTask;
import java.io.*;
import java.text.DecimalFormat;

/**
 *
 * @author PJ
 */
public class EnergyModelUI extends javax.swing.JFrame {
    
    int windSquarePos = 50;
    int windSquarePos1 = 50;
    int demandSquarePos = 50;
    int demandSquarePos1 = 50;
    int demandSquarePos2 = 0;
    int solarPos = 0;
    int nuclearPos = 0;
    int CHPPos = 0;
    int backupPos = 0;
    int unmetPos = 0;
    double excessGeneration = 0;
    double unmetDemand = 0;
    int i=0;
    int x=0;
    int z=0;
    Timer timer;
    double energyDemand[] = new double[8760];
    double windPower[] = new double[8760];
    double solarPower[] = new double[8760];
    double CHPPower[] = new double[8760];
    double hourlyHeatDemand[] = new double[8760];
    double hourlyElecForHeatDemand[] = new double[8760];
    double hourlySpaceHeatDemand[] = new double[8760];
    double hourlyWaterHeatDemand[] = new double[8760];
    double hourlyElecForIndustry[] = new double[8760];
    double hourlyH2Demand[] = new double[8760];
    double temperatureData[] = new double[8760];
    double totalGeneration[] = new double[8760];
    double backupUsed[] = new double[8760];
    double unmet[] = new double[8760];
    double storageContent[] = new double[8760];
    double storageChange[] = new double[8760];
    double H2StorageContent[] = new double[8760];
    double electrolysisDemand[] = new double[8760];

    double windCap;
    double solarCap;
    double nuclearCap;
    double backupCapInstalled;
    double backupCapUsed;
    double backupGeneration;
    double demandProfile;
    double appElecDemand;
    double appSolidDemand;
    double appGasDemand;
    double appLiquidDemand;
    double transportElecDemand;
    double transportSolidDemand;
    double transportGasDemand;
    double transportLiquidDemand;
    double transportH2Demand;
    double industryElecDemand;
    double industrySolidDemand;
    double industryGasDemand;
    double industryLiquidDemand;
    double industrySolidCHPDemand;
    double industryGasCHPDemand;
    double industryLiquidCHPDemand;
    double industryH2Demand;
    double heatLossCoefficient;
    double baseTemp;
    double totalHeatDemand;
    double totalDemandInProfile;
    double storageVolume;
    double storageCap;
    double H2StorageVolume;
    double H2ElectrolysisCap;
    double H2Produced;
    double H2Shortfall;
    double heatPumpCoP = 3.0;
    double hotWaterDemand;
    double transportDemand;
    double industryDemand;
    double biomassSupply;
    double biomassDemand;
    double solidDemand;
    double gasDemand;
    double liquidDemand;
    double solidBiofuel;
    double gasBiofuel;
    double liquidBiofuel;
    double residualEmissions;
    double heatPercentHeatpumps;
    double heatPercentDirectelec;
    double heatPercentSolid;
    double heatPercentGas;
    double heatPercentLiquid;
    double heatPercentSolidCHP;
    double heatPercentGasCHP;
    double heatPercentLiquidCHP;
    String useH2ForSynthFuels;
    double synthFuelH2Demand;
    double totalSupply;
    
    int balanceMarker = 0;
    int supplyMarker = 0;
    int demandMarker = 0;
    
    double [] spaceHeatingDailyProfile = {0.8,0.8,0.9,0.9,1.7,6.5,7.7,8.3,7.5,6.0,4.6,4.0,3.5,3.3,3.3,3.4,3.7,4.1,5.2,6.3,6.4,6.1,4.2,0.8};
    double [] hotWaterDailyProfile = {4,4,4,4,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2};//{0.1,0.1,0.1,0.5,1.5,3.5,7.6,10.2,11.2,6.9,4.6,3.0,1.9,1.9,2.1,3.0,7.0,7.8,7.1,5.8,4.5,3.7,3.2,2.7};
    double [] industrialDailyProfile = {4,4,4,4,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2,4.2};//{1,1,1,1,1,1,1,1,10.5,10.5,10.5,10.5,10.5,10.5,10.5,10.5,1,1,1,1,1,1,1,1};
    
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    DecimalFormat oneDForm = new DecimalFormat("#.#");

    /** Creates new form EnergyModelUI */
    public EnergyModelUI() {
        initComponents();
    }
    
    private void drawPanel() { 
        //for(i=1;i<20;i++){
        //squarePos = i*10; 
        GraphJPanel jPanel = new GraphJPanel(); 
        jPanel.setBackground(new java.awt.Color(255, 255, 255));
        //if(i==0){jPanel1.add(jPanel);}
        jPanel1.add(jPanel);
        
        repaint();
        
        unmetDemandLabel.setText(""+roundTwoDecimals(unmetDemand/1000));
        excessGenerationLabel.setText(""+roundOneDecimals(excessGeneration/1000));
        backupCapUsedLabel.setText(""+roundOneDecimals(backupCapUsed));
        backupGenLabel.setText(""+roundOneDecimals(backupGeneration/1000));
        biomassShortfallLabel.setText(""+roundOneDecimals(biomassSupply - (biomassDemand/1000)));
        H2ShortfallLabel.setText(""+roundOneDecimals(H2Shortfall));
        totalSupplyLabel.setText(""+roundOneDecimals(totalSupply));
        //i++;
    }
    
    //Code to display graphics using timer
    //public void Reminder(int seconds) {
    //    timer = new Timer();
    //    timer.schedule(new RemindTask(), seconds);
    //	}

    //class RemindTask extends TimerTask {
    //    public void run() {
    //        //System.out.format("Time's up!%n");
            //squarePos = Integer.parseInt(windPower[i]);
    //         drawPanel();
    //         if(i < 8760){
    //             Reminder(10);
    //         }
    //        else{System.out.println("Stopped");}
            //timer.cancel(); //Terminate the timer thread
    //    }
    //}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        HLCField = new javax.swing.JTextField();
        totalHeatDemandLabel = new javax.swing.JLabel();
        storageVolumeField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        storageCapField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        windCapField = new javax.swing.JTextField();
        solarCapField = new javax.swing.JTextField();
        appElecDemandField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        nuclearCapField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        backupCapUsedLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        balanceButton = new javax.swing.JButton();
        balanceButton.setVisible(false);
        backupCapField = new javax.swing.JTextField();
        excessGenerationLabel = new javax.swing.JLabel();
        backupGenLabel = new javax.swing.JLabel();
        unmetDemandLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        supplyButton = new javax.swing.JButton();
        supplyButton.setVisible(false);
        jLabel23 = new javax.swing.JLabel();
        demandButton = new javax.swing.JButton();
        demandButton.setVisible(false);
        jLabel15 = new javax.swing.JLabel();
        runButton = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        hotWaterDemandField = new javax.swing.JTextField();
        transportElecDemandField = new javax.swing.JTextField();
        industryElecDemandField = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        heatDemandPercentHeatpumps = new javax.swing.JTextField();
        heatDemandPercentDirectelec = new javax.swing.JTextField();
        heatDemandPercentSolid = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        appSolidDemandField = new javax.swing.JTextField();
        transportSolidDemandField = new javax.swing.JTextField();
        industrySolidDemandField = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        appGasDemandField = new javax.swing.JTextField();
        transportGasDemandField = new javax.swing.JTextField();
        industrialGasDemandField = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        appLiquidDemandField = new javax.swing.JTextField();
        transportLiquidDemandField = new javax.swing.JTextField();
        industrialLiquidDemandField = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        biomassSupplyField = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        biomassShortfallLabel = new javax.swing.JLabel();
        H2ShortfallLabel = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        heatDemandPercentGas = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        heatDemandPercentLiquid = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        H2StorageVolumeField = new javax.swing.JTextField();
        H2StorageVolumeField.setVisible(false);
        H2ElectrolysisCapField = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        industryH2DemandField = new javax.swing.JTextField();
        industrialGasCHPDemandField = new javax.swing.JTextField();
        industrySolidCHPDemandField = new javax.swing.JTextField();
        heatDemandPercentSolidCHP = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        heatDemandPercentGasCHP = new javax.swing.JTextField();
        heatDemandPercentLiquidCHP = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        industrialLiquidCHPDemandField = new javax.swing.JTextField();
        transportH2DemandField = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        sythFuelsComboBox = new javax.swing.JComboBox();
        jLabel53 = new javax.swing.JLabel();
        totalSupplyLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(9000, 60));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(jPanel1);

        jTabbedPane1.addTab("Visualisation", jScrollPane1);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 2, 991, 310));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        HLCField.setText("4.398");
        jPanel2.add(HLCField, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 90, 79, -1));

        totalHeatDemandLabel.setForeground(new java.awt.Color(153, 153, 153));
        jPanel2.add(totalHeatDemandLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, 40, 20));

        storageVolumeField.setText("50");
        jPanel2.add(storageVolumeField, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 50, -1));

        jLabel17.setText("Industry");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 140, 20));

        storageCapField.setText("10");
        jPanel2.add(storageCapField, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 50, -1));

        jLabel1.setText("Excess generation ");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 114, 30));

        jLabel12.setText("Back-up capacity used");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 310, 160, 30));

        jLabel13.setText("Back-up generation");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 360, 114, 30));

        windCapField.setText("140");
        jPanel2.add(windCapField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 79, -1));

        solarCapField.setText("70");
        jPanel2.add(solarCapField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 79, -1));

        appElecDemandField.setText("105");
        jPanel2.add(appElecDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, 50, -1));

        jLabel2.setText("Wind");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 84, 20));

        jLabel24.setText("Space heating");
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, 200, 20));

        nuclearCapField.setText("0");
        jPanel2.add(nuclearCapField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 79, -1));

        jLabel8.setText("GW");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, 38, -1));

        jLabel4.setText("Unmet demand");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 114, 30));

        jLabel9.setText("TWh/yr");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 50, 50, 20));

        jLabel16.setText("       ");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        jLabel19.setText("GWh");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 38, -1));

        backupCapUsedLabel.setBackground(new java.awt.Color(255, 255, 255));
        backupCapUsedLabel.setOpaque(true);
        jPanel2.add(backupCapUsedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 310, 79, 27));

        jLabel3.setText("TWh/yr");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 310, 50, 27));

        jLabel20.setText("Lights, appliances  cooking");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 200, 200, 20));

        jLabel18.setText("GW");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 38, -1));

        jLabel5.setText("Solar");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 84, 20));

        jLabel11.setText("GW");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 310, -1, 27));

        balanceButton.setText("Balance");
        balanceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                balanceButtonActionPerformed(evt);
            }
        });
        jPanel2.add(balanceButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        backupCapField.setText("45");
        jPanel2.add(backupCapField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 79, -1));

        excessGenerationLabel.setBackground(new java.awt.Color(255, 255, 255));
        excessGenerationLabel.setOpaque(true);
        jPanel2.add(excessGenerationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 310, 79, 27));

        backupGenLabel.setBackground(new java.awt.Color(255, 255, 255));
        backupGenLabel.setOpaque(true);
        jPanel2.add(backupGenLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 360, 79, 26));

        unmetDemandLabel.setBackground(new java.awt.Color(255, 255, 255));
        unmetDemandLabel.setOpaque(true);
        jPanel2.add(unmetDemandLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 79, 26));

        jLabel6.setText("TWh/yr");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 360, 50, 30));

        jLabel22.setText("Hot water demand (heat)");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 160, 20));

        jLabel10.setText("Elec. Storage");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 84, 20));

        jLabel7.setText("Nuclear");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 84, 20));

        supplyButton.setText("Supply");
        supplyButton.setToolTipText("");
        supplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplyButtonActionPerformed(evt);
            }
        });
        jPanel2.add(supplyButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, -1, -1));

        jLabel23.setText("Transport");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 230, 140, 20));

        demandButton.setText("Demand");
        demandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demandButtonActionPerformed(evt);
            }
        });
        jPanel2.add(demandButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, -1, -1));

        jLabel15.setText("Back-up capacity installed");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 170, 20));

        runButton.setBackground(new java.awt.Color(0, 204, 51));
        runButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        runButton.setText("Run");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });
        jPanel2.add(runButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 60, 30));

        jLabel21.setText("TWh/yr");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, 50, 30));

        hotWaterDemandField.setText("96");
        jPanel2.add(hotWaterDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, 79, -1));

        transportElecDemandField.setText("42");
        jPanel2.add(transportElecDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 50, -1));

        industryElecDemandField.setText("171");
        jPanel2.add(industryElecDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 260, 50, -1));

        jLabel25.setText("Heat loss coeff (GW/degC)");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 90, 200, 20));

        jLabel26.setText("% demand met by...");
        jPanel2.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 150, 130, 20));

        jLabel27.setText("Heat pumps");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 130, -1, -1));

        jLabel28.setText("Direct elec.");
        jPanel2.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, -1, -1));

        jLabel29.setText("Solid");
        jPanel2.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 130, -1, -1));

        heatDemandPercentHeatpumps.setText("80");
        jPanel2.add(heatDemandPercentHeatpumps, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 150, 50, -1));

        heatDemandPercentDirectelec.setText("15");
        jPanel2.add(heatDemandPercentDirectelec, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, 50, -1));

        heatDemandPercentSolid.setText("5");
        jPanel2.add(heatDemandPercentSolid, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 150, 50, -1));

        jLabel30.setText("Electricity");
        jPanel2.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 180, -1, -1));

        jLabel31.setText("Solid");
        jPanel2.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 180, 30, -1));

        appSolidDemandField.setText("0");
        jPanel2.add(appSolidDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 200, 50, -1));

        transportSolidDemandField.setText("0");
        jPanel2.add(transportSolidDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 230, 50, -1));

        industrySolidDemandField.setText("0");
        jPanel2.add(industrySolidDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 260, 50, -1));

        jLabel32.setText("Gas");
        jPanel2.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 180, 30, -1));

        appGasDemandField.setText("0");
        jPanel2.add(appGasDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 200, 50, -1));

        transportGasDemandField.setText("0");
        jPanel2.add(transportGasDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 230, 50, -1));

        industrialGasDemandField.setText("61");
        jPanel2.add(industrialGasDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 260, 50, -1));

        jLabel33.setText("Liquid");
        jPanel2.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 180, 50, -1));

        appLiquidDemandField.setText("0");
        jPanel2.add(appLiquidDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 200, 50, -1));

        transportLiquidDemandField.setText("90");
        jPanel2.add(transportLiquidDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 230, 50, -1));

        industrialLiquidDemandField.setText("12");
        jPanel2.add(industrialLiquidDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 260, 50, -1));

        jLabel34.setText("Biomass");
        jPanel2.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 50, 20));

        biomassSupplyField.setText("274");
        biomassSupplyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                biomassSupplyFieldActionPerformed(evt);
            }
        });
        jPanel2.add(biomassSupplyField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 50, 30));

        jLabel35.setText("TWh/yr");
        jPanel2.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 180, 50, -1));

        jLabel36.setText("H2 surplus/shortfall");
        jPanel2.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 360, 114, 30));

        jLabel37.setText("Biomass surplus/shortfall");
        jPanel2.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 310, 160, 30));

        biomassShortfallLabel.setBackground(new java.awt.Color(255, 255, 255));
        biomassShortfallLabel.setOpaque(true);
        jPanel2.add(biomassShortfallLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 310, 79, 27));

        H2ShortfallLabel.setBackground(new java.awt.Color(255, 255, 255));
        H2ShortfallLabel.setOpaque(true);
        jPanel2.add(H2ShortfallLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 360, 79, 26));

        jLabel38.setText("TWh/yr");
        jPanel2.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 310, -1, 27));

        jLabel39.setText("TWh/yr");
        jPanel2.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 360, 50, 30));

        jLabel40.setForeground(new java.awt.Color(153, 153, 153));
        jLabel40.setText("TWh/yr");
        jPanel2.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 90, 50, 20));

        jLabel41.setText("Gas");
        jPanel2.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 130, 30, -1));

        heatDemandPercentGas.setText("0");
        jPanel2.add(heatDemandPercentGas, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 150, 50, -1));

        jLabel42.setText("Liquid");
        jPanel2.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 130, 40, -1));

        heatDemandPercentLiquid.setText("0");
        jPanel2.add(heatDemandPercentLiquid, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 150, 50, -1));

        jLabel43.setText("Use H2 for synthetic fuels?");
        jPanel2.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 180, 20));

        H2StorageVolumeField.setText("20000");
        jPanel2.add(H2StorageVolumeField, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 50, -1));

        H2ElectrolysisCapField.setText("35");
        jPanel2.add(H2ElectrolysisCapField, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 50, -1));

        jLabel44.setText("CHP(s)");
        jPanel2.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 180, 50, -1));

        jLabel45.setText("CHP(g)");
        jPanel2.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 180, 50, -1));

        jLabel46.setText("H2");
        jPanel2.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 180, 40, -1));

        industryH2DemandField.setText("0");
        jPanel2.add(industryH2DemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 260, 50, -1));

        industrialGasCHPDemandField.setText("0");
        jPanel2.add(industrialGasCHPDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 260, 50, -1));

        industrySolidCHPDemandField.setText("26");
        jPanel2.add(industrySolidCHPDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 260, 50, -1));

        heatDemandPercentSolidCHP.setText("0");
        jPanel2.add(heatDemandPercentSolidCHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 150, 50, -1));

        jLabel47.setText("CHP(s)");
        jPanel2.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 130, 50, -1));

        jLabel48.setText("CHP(g)");
        jPanel2.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 130, 50, -1));

        heatDemandPercentGasCHP.setText("0");
        jPanel2.add(heatDemandPercentGasCHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 150, 50, -1));

        heatDemandPercentLiquidCHP.setText("0");
        jPanel2.add(heatDemandPercentLiquidCHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 150, 50, -1));

        jLabel49.setText("CHP(l)");
        jPanel2.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 130, 50, -1));

        jLabel50.setText("TWh/yr");
        jPanel2.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 50, 20));

        jLabel51.setText("CHP(l)");
        jPanel2.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 180, 50, -1));

        industrialLiquidCHPDemandField.setText("0");
        jPanel2.add(industrialLiquidCHPDemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 260, 50, -1));

        transportH2DemandField.setText("13");
        jPanel2.add(transportH2DemandField, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 230, 50, -1));

        jLabel52.setText("Hydrogen");
        jPanel2.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 84, 20));

        sythFuelsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
        jPanel2.add(sythFuelsComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 70, -1));

        jLabel53.setForeground(new java.awt.Color(153, 153, 153));
        jLabel53.setText("Total heat demand");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 70, 120, 20));

        totalSupplyLabel.setForeground(new java.awt.Color(153, 153, 153));
        jPanel2.add(totalSupplyLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 40, 20));

        jScrollPane2.setViewportView(jPanel2);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 380));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 980, 390));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        
        balanceButton.setVisible(true);supplyButton.setVisible(true);demandButton.setVisible(true);
        balanceMarker = 1; supplyMarker = 0; demandMarker = 0;
        
        appElecDemand = Double.parseDouble(appElecDemandField.getText());
        appSolidDemand = Double.parseDouble(appSolidDemandField.getText());
        appGasDemand = Double.parseDouble(appGasDemandField.getText());
        appLiquidDemand = Double.parseDouble(appLiquidDemandField.getText());
        transportElecDemand = Double.parseDouble(transportElecDemandField.getText());
        transportSolidDemand = Double.parseDouble(transportSolidDemandField.getText());
        transportGasDemand = Double.parseDouble(transportGasDemandField.getText());
        transportLiquidDemand = Double.parseDouble(transportLiquidDemandField.getText());
        transportH2Demand = Double.parseDouble(transportH2DemandField.getText());
        industryElecDemand = Double.parseDouble(industryElecDemandField.getText());
        industrySolidDemand = Double.parseDouble(industrySolidDemandField.getText());
        industryGasDemand = Double.parseDouble(industrialGasDemandField.getText());
        industryLiquidDemand = Double.parseDouble(industrialLiquidDemandField.getText());
        industrySolidCHPDemand = Double.parseDouble(industrySolidCHPDemandField.getText());
        industryGasCHPDemand = Double.parseDouble(industrialGasCHPDemandField.getText());
        industryLiquidCHPDemand = Double.parseDouble(industrialLiquidCHPDemandField.getText());
        industryH2Demand = Double.parseDouble(industryH2DemandField.getText());
            
        heatLossCoefficient = Double.parseDouble(HLCField.getText());
        baseTemp = 16-(385/((heatLossCoefficient/4.398)*119));//Gains need linking to appliances energy reduction 
        totalDemandInProfile = 0;
        windCap = Double.parseDouble(windCapField.getText());
        solarCap = Double.parseDouble(solarCapField.getText());
        nuclearCap = Double.parseDouble(nuclearCapField.getText());
        backupCapInstalled = Double.parseDouble(backupCapField.getText());
        storageVolume = Double.parseDouble(storageVolumeField.getText());
        storageCap = Double.parseDouble(storageCapField.getText());
        H2StorageVolume = Double.parseDouble(H2StorageVolumeField.getText());
        H2ElectrolysisCap = Double.parseDouble(H2ElectrolysisCapField.getText());
        storageContent[0] = 0; //storageVolume/2;
        H2StorageContent[0] = H2StorageVolume/2;
        H2Produced = 0;
        H2Shortfall = 0;
        excessGeneration = 0;
        unmetDemand = 0;
        backupCapUsed = 0;
        backupGeneration = 0;
        biomassDemand = 0; solidDemand = 0; gasDemand = 0; liquidDemand = 0;
        x = 0;
        hotWaterDemand = Double.parseDouble(hotWaterDemandField.getText())*1000/8760;
        transportDemand = Double.parseDouble(transportElecDemandField.getText())*1000/8760;
        industryDemand = Double.parseDouble(industryElecDemandField.getText())*1000/8760;
        biomassSupply = Double.parseDouble(biomassSupplyField.getText());
        heatPercentHeatpumps = Double.parseDouble(heatDemandPercentHeatpumps.getText());
        heatPercentDirectelec = Double.parseDouble(heatDemandPercentDirectelec.getText());
        heatPercentSolid = Double.parseDouble(heatDemandPercentSolid.getText());
        heatPercentGas = Double.parseDouble(heatDemandPercentGas.getText());
        heatPercentLiquid = Double.parseDouble(heatDemandPercentLiquid.getText());
        heatPercentSolidCHP = Double.parseDouble(heatDemandPercentSolidCHP.getText());
        heatPercentGasCHP = Double.parseDouble(heatDemandPercentGasCHP.getText());
        heatPercentLiquidCHP = Double.parseDouble(heatDemandPercentLiquidCHP.getText());
        useH2ForSynthFuels = sythFuelsComboBox.getSelectedItem().toString();
        
        try{
          // Open the file that is the first 
          // command line parameter
          //FileInputStream fstream = new FileInputStream("textfile.txt");
          // Get the object of DataInputStream
          //DataInputStream in = new DataInputStream(fstream);
          BufferedReader br = new BufferedReader(new FileReader("textfile.txt"));
          String strLine;
          strLine = br.readLine();
          //Read File Line By Line
          while (strLine != null)   {
          // Print the content on the console
          //System.out.println (strLine);
          String splitarray[] = strLine.split(",");
          energyDemand[x] = Double.parseDouble(splitarray[0]);
          windPower[x] = Double.parseDouble(splitarray[1])*windCap;
          solarPower[x] = Double.parseDouble(splitarray[2])*solarCap;
          temperatureData[x] = Double.parseDouble(splitarray[3]);
          if(baseTemp - temperatureData[x] > 0){
              hourlySpaceHeatDemand[x] = ((baseTemp - temperatureData[x]) * heatLossCoefficient);
          }
          else{
              hourlySpaceHeatDemand[x] = 0;
          }
          //System.out.println(""+windPower[x]);
          
          x++;
          strLine = br.readLine();
          }
          //Close the input stream
          //in.close();
          }
          catch (Exception e){//Catch exception if any
          System.err.println("Error: " + e.getMessage());
          }
          x=0;
          for(i=0;i<8760;i++){
              totalDemandInProfile += energyDemand[i];
              
              hourlyWaterHeatDemand[i] = hotWaterDemand * 24 * (hotWaterDailyProfile[x]/100);
              hourlySpaceHeatDemand[i] = hourlySpaceHeatDemand[i] * 24 * (spaceHeatingDailyProfile[x]/100);
              hourlyElecForIndustry[i] = (industryElecDemand/8760)*1000 * 24 * (industrialDailyProfile[x]/100);
                      
              hourlyHeatDemand[i] = hourlySpaceHeatDemand[i] + hourlyWaterHeatDemand[i];
              totalHeatDemand += hourlyHeatDemand[i];
              hourlyElecForHeatDemand[i] = (hourlyHeatDemand[i]*(heatPercentHeatpumps/100))/heatPumpCoP +
                      (hourlyHeatDemand[i]*(heatPercentDirectelec/100));
              solidDemand += (hourlyHeatDemand[i]*(heatPercentSolid/100))/0.9;
              gasDemand += (hourlyHeatDemand[i]*(heatPercentGas/100))/0.9;
              liquidDemand += (hourlyHeatDemand[i]*(heatPercentLiquid/100))/0.9;
              
              solidDemand += (hourlyHeatDemand[i]*(heatPercentSolidCHP/100))/0.6;
              gasDemand += (hourlyHeatDemand[i]*(heatPercentGasCHP/100))/0.6;
              liquidDemand += (hourlyHeatDemand[i]*(heatPercentLiquidCHP/100))/0.6;
              
              CHPPower[i] = (hourlyHeatDemand[i]*(heatPercentSolidCHP/100))*(0.3/0.6) + 
                      (hourlyHeatDemand[i]*(heatPercentGasCHP/100))*(0.3/0.6) + 
                      (hourlyHeatDemand[i]*(heatPercentLiquidCHP/100))*(0.3/0.6) + 
                      ((industrySolidCHPDemand/8760)*1000 * 24 * (industrialDailyProfile[x]/100))*0.3 + 
                      ((industryGasCHPDemand/8760)*1000 * 24 * (industrialDailyProfile[x]/100))*0.3 +
                      ((industryLiquidCHPDemand/8760)*1000 * 24 * (industrialDailyProfile[x]/100))*0.3 ;
              
              //hourlyH2Demand[i] = (industryH2Demand*1000)/8760 * 24 * (industrialDailyProfile[x]/100) + 
              //        (transportH2Demand*1000)/8760;
              
              x++;
              if(x==23){x=0;}
          }
          totalHeatDemandLabel.setText(""+(int)Math.round(totalHeatDemand/1000));
          totalHeatDemand = 0;
          totalSupply = 0;
          
          for(i=0;i<8760;i++){
              energyDemand[i] = energyDemand[i] / (totalDemandInProfile/1000) * appElecDemand;
              energyDemand[i] = energyDemand[i] + hourlyElecForHeatDemand[i] + transportDemand + hourlyElecForIndustry[i];
              totalGeneration[i] = windPower[i] + solarPower[i] + (nuclearCap*0.9) + CHPPower[i];
              totalSupply += totalGeneration[i]/1000;
              // If demand greater than supply take from store
              if(energyDemand[i]>totalGeneration[i]){
                  if(storageContent[i]<storageCap){
                      if(storageContent[i]<(energyDemand[i]-totalGeneration[i])){
                          storageChange[i] = -storageContent[i];
                      }
                      else{storageChange[i] = -(energyDemand[i]-totalGeneration[i]);}
                  }
                  else if(storageCap<(energyDemand[i]-totalGeneration[i])){
                          storageChange[i] = -storageCap;
                  }
                  else{storageChange[i] = -(energyDemand[i]-totalGeneration[i]);}
              }
              // Is supply greater than demand give to store
              if(energyDemand[i]<=totalGeneration[i]){
                  if((storageVolume-storageContent[i])<storageCap){
                      if((storageVolume-storageContent[i])<(totalGeneration[i]-energyDemand[i])){
                          storageChange[i] = (storageVolume-storageContent[i]);
                      }
                      else{storageChange[i] = (totalGeneration[i]-energyDemand[i]);}
                  }
                  else if(storageCap<(totalGeneration[i]-energyDemand[i])){
                          storageChange[i] = storageCap;
                  }
                  else{storageChange[i] = (totalGeneration[i]-energyDemand[i]);}
              }
              //Adjust storage content
              if(i<8759){storageContent[i+1] = storageContent[i] + storageChange[i];}
              energyDemand[i] = energyDemand[i] + storageChange[i];
          }
          
          //Calculate backup generation and unmet demand
          for(i=0;i<8760;i++){
              if(energyDemand[i]>totalGeneration[i]){
                  if((energyDemand[i]-totalGeneration[i])<backupCapInstalled){
                      backupGeneration += (energyDemand[i]-totalGeneration[i]);
                      backupUsed[i] = (energyDemand[i]-totalGeneration[i]);
                      unmet[i] = 0;
                      if((energyDemand[i]-totalGeneration[i])>backupCapUsed){
                          backupCapUsed = (energyDemand[i]-totalGeneration[i]);
                      }
                  }
                  else{
                      backupGeneration += backupCapInstalled;
                      backupUsed[i] = backupCapInstalled;
                      unmetDemand += ((energyDemand[i]-totalGeneration[i]) - backupCapInstalled);
                      unmet[i] = ((energyDemand[i]-totalGeneration[i]) - backupCapInstalled);
                      backupCapUsed = backupCapInstalled;
                  }
              }
              else{
                  
                  backupUsed[i] = 0;
                  unmet[i] = 0;
              }
                    
              //Calculate electricity to hydrogen electrolysis
              
              //Code if hydrogen storage modelled
              /*if((H2StorageContent[i] - hourlyH2Demand[i])< 0 ){
                  H2Shortfall += (hourlyH2Demand[i] - H2StorageContent[i]);
                  H2StorageContent[i] = 0;
              }
              else{H2StorageContent[i] = H2StorageContent[i] - hourlyH2Demand[i];}
              
              if(energyDemand[i]<totalGeneration[i]){
                  if((totalGeneration[i] - energyDemand[i]) > (H2StorageVolume - H2StorageContent[i])/0.7){
                      if((H2StorageVolume - H2StorageContent[i])/0.7 > H2ElectrolysisCap){
                          electrolysisDemand[i] = H2ElectrolysisCap;
                      }
                      else{electrolysisDemand[i] = (H2StorageVolume - H2StorageContent[i])/0.7;}
                  }
                  else{
                     if((totalGeneration[i] - energyDemand[i]) > H2ElectrolysisCap){
                         electrolysisDemand[i] = H2ElectrolysisCap;
                     }
                     else{electrolysisDemand[i] = (totalGeneration[i] - energyDemand[i]);}
                  }
              }
              else{electrolysisDemand[i] = 0;}*/
              
              if(energyDemand[i]<totalGeneration[i]){
                  if((totalGeneration[i] - energyDemand[i]) > H2ElectrolysisCap){
                         electrolysisDemand[i] = H2ElectrolysisCap;
                     }
                  else{electrolysisDemand[i] = (totalGeneration[i] - energyDemand[i]);}
              }
              else{electrolysisDemand[i] = 0;}

              //Adjust storage content
              //if(i<8759){H2StorageContent[i+1] = H2StorageContent[i] + electrolysisDemand[i]*0.7;}
              energyDemand[i] = energyDemand[i] + electrolysisDemand[i];
              H2Produced += electrolysisDemand[i]*0.7;

              if(totalGeneration[i]>energyDemand[i]){
                  excessGeneration += (totalGeneration[i] - energyDemand[i]);
              }         
          }
          
          solidDemand += (appSolidDemand + transportSolidDemand + industrySolidDemand + industrySolidCHPDemand)*1000;
          gasDemand += (appGasDemand + transportGasDemand + industryGasDemand + industryGasCHPDemand)*1000 + (backupGeneration/0.5);
          liquidDemand += (appLiquidDemand + transportLiquidDemand + industryLiquidDemand + industryLiquidCHPDemand)*1000;
          
          //Synth fuels assumption: SABATIER: 1 Biomass + 0.5 H2 --> 1 Biomethane. FISCHER-TROPSCH: 1 Biomass + 0.5 H2 --> 0.8 Synthetic liquid fuel.
          if(useH2ForSynthFuels == "Yes"){
              
              synthFuelH2Demand = (gasDemand*0.5) + (liquidDemand*0.5);
              
              if(synthFuelH2Demand < (H2Produced - ((transportH2Demand + industryH2Demand)*1000))){//Enough H2  
                  if(biomassSupply*1000 > (solidDemand + (gasDemand/1) + (liquidDemand/0.8))){ // Enough H2, enough biomass
                      biomassDemand = (solidDemand + (gasDemand/1) + (liquidDemand/0.8));
                      synthFuelH2Demand = (gasDemand*0.5) + (liquidDemand*0.5);
                      
                      solidBiofuel = solidDemand;
                      gasBiofuel = gasDemand;
                      liquidBiofuel = liquidDemand;
                  }
                  else{
                      //Enough H2, not enough biomass
                      if(biomassSupply*1000 < solidDemand){
                          synthFuelH2Demand = 0;
                          biomassDemand = solidDemand + (gasDemand/1) + (liquidDemand/0.8);
                          
                          solidBiofuel = biomassSupply*1000;
                          gasBiofuel = 0;
                          liquidBiofuel = 0;
                      }
                      else if(biomassSupply*1000 < solidDemand+(gasDemand/1)){
                          synthFuelH2Demand = ((biomassSupply*1000 - solidDemand)/(gasDemand/1))*gasDemand*0.5;
                          biomassDemand = solidDemand + (gasDemand/1) + (liquidDemand/0.8);
                          
                          solidBiofuel = solidDemand;
                          gasBiofuel = ((biomassSupply*1000 - solidDemand)/(gasDemand/1)) * gasDemand;
                          liquidBiofuel = 0;
                      }
                      else{
                          synthFuelH2Demand = (gasDemand*0.5) + (((biomassSupply*1000 - solidDemand - (gasDemand/1)) / 
                                  (liquidDemand/0.8)) * liquidDemand*0.5);
                          biomassDemand = solidDemand + (gasDemand/1) + (liquidDemand/0.8);
                          
                          solidBiofuel = solidDemand;
                          gasBiofuel = gasDemand;
                          liquidBiofuel = ((biomassSupply*1000 - solidDemand - (gasDemand/1)) / (liquidDemand/0.8)) * liquidDemand;
                      }
                      
                  }
              }
              else{ //Not enough H2
                  if(biomassSupply*1000 > (solidDemand + (gasDemand/1) + (liquidDemand/0.8))){//enough biomass not enough H2
                      if((H2Produced - ((transportH2Demand + industryH2Demand)*1000)) < 0){
                            synthFuelH2Demand = 0;
                            biomassDemand = solidDemand 
                                    + (gasDemand/0.6)
                                    + (liquidDemand/0.5);
                            
                            solidBiofuel = solidDemand;
                            if((biomassSupply*1000 - solidDemand) > (gasDemand/0.6)){gasBiofuel = gasDemand;}
                            else{gasBiofuel = ((biomassSupply*1000 - solidDemand)/(gasDemand/0.6))*gasDemand;}
                            if((biomassSupply*1000 - solidDemand - (gasDemand/0.6)) > (liquidDemand/0.5)){liquidBiofuel = liquidDemand;}
                            else{liquidBiofuel = ((biomassSupply*1000 - solidDemand - (gasDemand/0.6))/(liquidDemand/0.5))*liquidDemand;}
                      }
                      else if((H2Produced - ((transportH2Demand + industryH2Demand)*1000)) < (gasDemand*0.5)){
                          synthFuelH2Demand = (H2Produced - ((transportH2Demand + industryH2Demand)*1000));
                          biomassDemand = solidDemand 
                                  + ((H2Produced - ((transportH2Demand + industryH2Demand)*1000))/(gasDemand*0.5)) * (gasDemand/1)
                                  + (1 - ((H2Produced - ((transportH2Demand + industryH2Demand)*1000))/(gasDemand*0.5))) * (gasDemand/0.6)
                                  + (liquidDemand/0.5);
                      }
                      else{
                          synthFuelH2Demand = (H2Produced - ((transportH2Demand + industryH2Demand)*1000));
                          biomassDemand = solidDemand
                                  + (gasDemand/1)
                                  + ((H2Produced - ((transportH2Demand + industryH2Demand)*1000) - (gasDemand*0.5))/(liquidDemand*0.5)) * (liquidDemand/0.8)
                                  + (1 - ((H2Produced - ((transportH2Demand + industryH2Demand)*1000) - (gasDemand*0.5))/(liquidDemand*0.5))) * (liquidDemand/0.5);
                      }
                  }
                  else{// not enough H2 not enough biomass
                        if((H2Produced - ((transportH2Demand + industryH2Demand)*1000)) < 0){
                            synthFuelH2Demand = 0;
                            biomassDemand = solidDemand 
                                    + (gasDemand/0.6)
                                    + (liquidDemand/0.5);
                        }
                        else if((H2Produced - ((transportH2Demand + industryH2Demand)*1000)) < (gasDemand*0.5)){
                            synthFuelH2Demand = (H2Produced - ((transportH2Demand + industryH2Demand)*1000));
                            biomassDemand = solidDemand 
                                    + ((H2Produced - ((transportH2Demand + industryH2Demand)*1000))/(gasDemand*0.5)) * (gasDemand/1)
                                    + (1 - ((H2Produced - ((transportH2Demand + industryH2Demand)*1000))/(gasDemand*0.5))) * (gasDemand/0.6)
                                    + (liquidDemand/0.5);
                        }
                        else{
                            synthFuelH2Demand = (H2Produced - ((transportH2Demand + industryH2Demand)*1000));
                            biomassDemand = solidDemand
                                    + (gasDemand/1)
                                    + ((H2Produced - ((transportH2Demand + industryH2Demand)*1000) - (gasDemand*0.5))/(liquidDemand*0.5)) * (liquidDemand/0.8)
                                    + (1 - ((H2Produced - ((transportH2Demand + industryH2Demand)*1000) - (gasDemand*0.5))/(liquidDemand*0.5))) * (liquidDemand/0.5);
                        }
                  }
              }
          }
          else{  //No synth fuels
              synthFuelH2Demand = 0;
              biomassDemand = solidDemand + (gasDemand/0.6) + (liquidDemand/0.5); 
          }
          
          H2Shortfall = (H2Produced/1000) - (transportH2Demand + industryH2Demand + synthFuelH2Demand/1000);         
         
          //Reminder(10);  
          drawPanel();
    }//GEN-LAST:event_runButtonActionPerformed

    private void demandButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demandButtonActionPerformed
        balanceMarker = 0; supplyMarker = 0; demandMarker = 1;
        drawPanel();
    }//GEN-LAST:event_demandButtonActionPerformed

    private void balanceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_balanceButtonActionPerformed
        balanceMarker = 1; supplyMarker = 0; demandMarker = 0;
        drawPanel(); 
    }//GEN-LAST:event_balanceButtonActionPerformed

    private void supplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplyButtonActionPerformed
        balanceMarker = 0; supplyMarker = 1; demandMarker = 0;
        drawPanel();
    }//GEN-LAST:event_supplyButtonActionPerformed

    private void biomassSupplyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_biomassSupplyFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_biomassSupplyFieldActionPerformed

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
            java.util.logging.Logger.getLogger(EnergyModelUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EnergyModelUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EnergyModelUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EnergyModelUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new EnergyModelUI().setVisible(true);
            }
        });
    }
    
    double roundTwoDecimals(double d) {
	return Double.valueOf(twoDForm.format(d));
    }

    // Rounds to one decimal places
    double roundOneDecimals(double d) {        	
	return Double.valueOf(oneDForm.format(d));
    }
    
    public class GraphJPanel extends JPanel{
        // draw shapes with Java 2D API
        
        @Override
        public void paintComponent( Graphics g )
        {
            super.paintComponent( g ); // call superclass's paintComponent

            Graphics2D g2d = ( Graphics2D ) g; // cast g to Graphics2D
            
            g.setColor(Color.black);
            g2d.drawLine(25, 25, 25, 225);
            g2d.drawLine(25, 225, 8785, 225);
            g2d.drawLine(25, 25, 23, 25);
            g2d.drawLine(25, 125, 23, 125);
            g2d.drawString("100", 2, 129);
            g2d.drawString("200", 2, 29);
            
            // Draw balance graph
            if(balanceMarker == 1){
                for(int p=1;p<8760;p++){
                    g.setColor(Color.blue);
                    windSquarePos = (int)Math.round(totalGeneration[p-1]);
                    windSquarePos1 = (int)Math.round(totalGeneration[p]);
                    //g2d.fillRect(p+26, 25+(200-squarePos), 1, 2);
                    g2d.drawLine(p+26, 25+(200-windSquarePos1), p+25, 25+(200-windSquarePos));
                    g.setColor(Color.black);
                    demandSquarePos = (int)Math.round(energyDemand[p-1]);
                    demandSquarePos1 = (int)Math.round(energyDemand[p]);
                    //g2d.fillRect(p+26, 25+(200-squarePos), 1, 2);
                    g2d.drawLine(p+26, 25+(200-demandSquarePos1), p+25, 25+(200-demandSquarePos));
                    if(windSquarePos>demandSquarePos){
                        g.setColor(Color.green);
                        g2d.drawLine(p+25, 25+(200-windSquarePos+1), p+25, 25+(200-demandSquarePos-1));
                    }
                    else if(windSquarePos<demandSquarePos){
                        g.setColor(Color.red);
                        g2d.drawLine(p+25, 25+(200-windSquarePos-1), p+25, 25+(200-demandSquarePos+1));
                    }
                }
                g.setColor(Color.green);
                g.drawString("Surplus", 150, 20);
                g.setColor(Color.red);
                g.drawString("Shortfall", 250, 20);
            }
            //Draw demand graph
            else if(demandMarker == 1){
                for(int p=1;p<8760;p++){
                    
                    g.setColor(Color.blue);
                    demandSquarePos = (int)Math.round(energyDemand[p-1]);                  
                    g2d.drawLine(p+25, 25+(200-demandSquarePos), p+25, 225);
                    
                    if(electrolysisDemand[p-1]>0){
                        g.setColor(Color.green);
                        demandSquarePos = (int)Math.round(electrolysisDemand[p-1] + hourlyElecForIndustry[p-1] + transportDemand + hourlyElecForHeatDemand[p-1]);                        
                        g2d.drawLine(p+25, 25+(200-demandSquarePos), p+25, 225);
                    }
                    if(hourlyElecForIndustry[p-1]>0){
                        g.setColor(Color.magenta);
                        demandSquarePos = (int)Math.round(hourlyElecForIndustry[p-1] + transportDemand + hourlyElecForHeatDemand[p-1]);                        
                        g2d.drawLine(p+25, 25+(200-demandSquarePos), p+25, 225);
                    }
                    if(transportDemand>0){
                        g.setColor(Color.orange);
                        demandSquarePos = (int)Math.round(transportDemand + hourlyElecForHeatDemand[p-1]);                        
                        g2d.drawLine(p+25, 25+(200-demandSquarePos), p+25, 225);
                    }
                    if(hourlyElecForHeatDemand[p-1]>0){
                        g.setColor(Color.red);
                        demandSquarePos = (int)Math.round(hourlyElecForHeatDemand[p-1]);                        
                        g2d.drawLine(p+25, 25+(200-demandSquarePos), p+25, 225);
                    }

                    g.setColor(Color.black);
                    g2d.drawLine(25, 225, 8785, 225); 
                    
                    demandSquarePos = (int)Math.round(energyDemand[p-1]);
                    demandSquarePos1 = (int)Math.round(storageChange[p-1]);
                    if(storageChange[p-1]<0){
                        g.setColor(Color.pink);
                        g2d.drawLine(p+25, 25+(200-demandSquarePos+demandSquarePos1), p+25, 25+(200-demandSquarePos));   
                    }
                    else if(storageChange[p-1]>0){
                        g.setColor(Color.yellow);
                        g2d.drawLine(p+25, 25+(200-demandSquarePos+demandSquarePos1), p+25, 25+(200-demandSquarePos));
                    }
                }
                g.setColor(Color.red);
                g.drawString("Space heat & hotwater", 50, 20);
                g.setColor(Color.orange);
                g.drawString("Electric vehicles", 200, 20);
                g.setColor(Color.magenta);
                g.drawString("Industry", 350, 20);
                g.setColor(Color.green);
                g.drawString("Electrolysis", 450, 20);
                g.setColor(Color.blue);
                g.drawString("Appliances, lights & cooking", 550, 20);
                g.setColor(Color.yellow);
                g.drawString("To electricity store", 750, 20);
                g.setColor(Color.pink);
                g.drawString("From electricity store", 900, 20);
                
            }
            //Draw supply graph 
            else if(supplyMarker == 1){
                for(int p=1;p<8760;p++){
                    g.setColor(Color.green);
                    windSquarePos = (int)Math.round(windPower[p-1]);
                    solarPos = (int)Math.round(solarPower[p-1]);
                    nuclearPos = (int)Math.round((nuclearCap * 0.9));
                    CHPPos = (int)Math.round((CHPPower[p-1]));
                    backupPos = (int)Math.round(backupUsed[p-1]);
                    unmetPos = (int)Math.round(unmet[p-1]);
                    g.setColor(Color.orange);
                    g2d.drawLine(p+25, 25+(200-nuclearPos), p+25, 225);
                    g.setColor(Color.pink);
                    g2d.drawLine(p+25, 25+(200-nuclearPos)-CHPPos, p+25, 25+(200-nuclearPos));
                    g.setColor(Color.yellow);
                    g2d.drawLine(p+25, 25+(200-nuclearPos)-CHPPos-solarPos, p+25, 25+(200-nuclearPos)-CHPPos);                    
                    g.setColor(Color.gray);
                    g2d.drawLine(p+25, 25+(200-nuclearPos)-CHPPos-solarPos-windSquarePos-backupPos-unmetPos, p+25, 25+(200-nuclearPos)-CHPPos-solarPos-windSquarePos-backupPos);
                    g.setColor(Color.red);
                    g2d.drawLine(p+25, 25+(200-nuclearPos)-CHPPos-solarPos-windSquarePos-backupPos, p+25, 25+(200-nuclearPos)-CHPPos-solarPos-windSquarePos);
                    g.setColor(Color.blue);
                    g2d.drawLine(p+25, 25+(200-nuclearPos)-CHPPos-solarPos-windSquarePos, p+25, 25+(200-nuclearPos)-CHPPos-solarPos);
                    g.setColor(Color.black);
                    g2d.drawLine(25, 226, 8785, 226); 
                }
                g.setColor(Color.blue);
                g.drawString("Wind", 150, 20);
                g.setColor(Color.YELLOW);
                g.drawString("Solar PV", 250, 20);
                g.setColor(Color.orange);
                g.drawString("Nuclear", 350, 20);
                g.setColor(Color.red);
                g.drawString("Back-up power", 450, 20);
                g.setColor(Color.GRAY);
                g.drawString("Unmet demand", 600, 20);
            }
        }      
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField H2ElectrolysisCapField;
    private javax.swing.JLabel H2ShortfallLabel;
    private javax.swing.JTextField H2StorageVolumeField;
    private javax.swing.JTextField HLCField;
    private javax.swing.JTextField appElecDemandField;
    private javax.swing.JTextField appGasDemandField;
    private javax.swing.JTextField appLiquidDemandField;
    private javax.swing.JTextField appSolidDemandField;
    private javax.swing.JTextField backupCapField;
    private javax.swing.JLabel backupCapUsedLabel;
    private javax.swing.JLabel backupGenLabel;
    private javax.swing.JButton balanceButton;
    private javax.swing.JLabel biomassShortfallLabel;
    private javax.swing.JTextField biomassSupplyField;
    private javax.swing.JButton demandButton;
    private javax.swing.JLabel excessGenerationLabel;
    private javax.swing.JTextField heatDemandPercentDirectelec;
    private javax.swing.JTextField heatDemandPercentGas;
    private javax.swing.JTextField heatDemandPercentGasCHP;
    private javax.swing.JTextField heatDemandPercentHeatpumps;
    private javax.swing.JTextField heatDemandPercentLiquid;
    private javax.swing.JTextField heatDemandPercentLiquidCHP;
    private javax.swing.JTextField heatDemandPercentSolid;
    private javax.swing.JTextField heatDemandPercentSolidCHP;
    private javax.swing.JTextField hotWaterDemandField;
    private javax.swing.JTextField industrialGasCHPDemandField;
    private javax.swing.JTextField industrialGasDemandField;
    private javax.swing.JTextField industrialLiquidCHPDemandField;
    private javax.swing.JTextField industrialLiquidDemandField;
    private javax.swing.JTextField industryElecDemandField;
    private javax.swing.JTextField industryH2DemandField;
    private javax.swing.JTextField industrySolidCHPDemandField;
    private javax.swing.JTextField industrySolidDemandField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField nuclearCapField;
    private javax.swing.JButton runButton;
    private javax.swing.JTextField solarCapField;
    private javax.swing.JTextField storageCapField;
    private javax.swing.JTextField storageVolumeField;
    private javax.swing.JButton supplyButton;
    private javax.swing.JComboBox sythFuelsComboBox;
    private javax.swing.JLabel totalHeatDemandLabel;
    private javax.swing.JLabel totalSupplyLabel;
    private javax.swing.JTextField transportElecDemandField;
    private javax.swing.JTextField transportGasDemandField;
    private javax.swing.JTextField transportH2DemandField;
    private javax.swing.JTextField transportLiquidDemandField;
    private javax.swing.JTextField transportSolidDemandField;
    private javax.swing.JLabel unmetDemandLabel;
    private javax.swing.JTextField windCapField;
    // End of variables declaration//GEN-END:variables
}
