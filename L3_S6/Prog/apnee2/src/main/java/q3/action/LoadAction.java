package q3.action;

import q3.DessinArea;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class LoadAction extends AbstractAction {

    private final JFrame frame;
    private final DessinArea dessinArea;

    public LoadAction(JFrame frame, DessinArea dessinArea) {
        super("Charger");
        this.frame = frame;
        this.dessinArea = dessinArea;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
        chooser.setFileFilter(filter);

        int result = chooser.showOpenDialog(dessinArea);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File src = chooser.getSelectedFile();
        try {
            dessinArea.load(src);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'ouverture du fichier", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
