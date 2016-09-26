package gauffre.moteur;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static java.lang.System.exit;

public class SettingsDialog extends JDialog {
    final int MAX = 50;
    final int MIN = 2;

    public SettingsDialog(Settings settings){

        setSize(300, 400);

        /*
        Size Configuration
         */

        JLabel widthLabel = new JLabel("Width: ");
        JLabel heightLabel = new JLabel("Height: ");

        heightLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        SpinnerModel widthModel = new SpinnerNumberModel(10, MIN, MAX, 1);
        SpinnerModel heightModel = new SpinnerNumberModel(10, MIN, MAX, 1);

        JSpinner widthSpinner = new JSpinner(widthModel);
        JSpinner heightSpinner = new JSpinner(heightModel);

        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));

        sizePanel.add(widthLabel);
        sizePanel.add(widthSpinner);
        sizePanel.add(heightLabel);
        sizePanel.add(heightSpinner);

        add(sizePanel, BorderLayout.NORTH);

        /*
        Joueur1 panel configuration
         */

        JLabel J1Label = new JLabel("Joueur 1:");
        JRadioButton J1Humain = new JRadioButton("Humain");
        JRadioButton J1_IA_Facile = new JRadioButton("IA_Facile");
        JRadioButton J1_IA_Normal = new JRadioButton("IA_Normale");
        JRadioButton J1_IA_Difficile = new JRadioButton("IA_Difficile");

        ButtonGroup J1Group = new ButtonGroup();
        JPanel J1Panel = new JPanel();

        configurePanel(J1Panel, J1Group, J1Label, J1Humain, J1_IA_Facile, J1_IA_Normal, J1_IA_Difficile);
        J1Humain.setSelected(true);
        add(J1Panel, BorderLayout.WEST);

        /*
        Joueur2 panel configuration
         */

        JLabel J2Label = new JLabel("Joueur 2:");
        JRadioButton J2Humain = new JRadioButton("Humain");
        JRadioButton J2_IA_Facile = new JRadioButton("IA_Facile");
        JRadioButton J2_IA_Normal = new JRadioButton("IA_Normale");
        JRadioButton J2_IA_Difficile = new JRadioButton("IA_Difficile");

        ButtonGroup J2Group = new ButtonGroup();
        JPanel J2Panel = new JPanel();

        configurePanel(J2Panel, J2Group, J2Label, J2Humain, J2_IA_Facile, J2_IA_Normal, J2_IA_Difficile);
        J2Humain.setSelected(true);
        add(J2Panel, BorderLayout.EAST);

        /*
        Ok and Cancel buttons
         */

        JButton okButton = new JButton("Save And Restart");
        JButton cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(actionEvent -> setVisible(false));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(okButton, BorderLayout.WEST);
        buttonPanel.add(cancelButton, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();

    }

    /*
    Utility function to create the a panel and customize it
     */

    public void configurePanel(JPanel panel, ButtonGroup group, JLabel label,
                               JRadioButton b1, JRadioButton b2, JRadioButton b3, JRadioButton b4){
        group.add(b1);
        group.add(b2);
        group.add(b3);
        group.add(b4);

        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        panel.add(label);
        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    }

}
