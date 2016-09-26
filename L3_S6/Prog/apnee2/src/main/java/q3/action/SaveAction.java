package q3.action;

import q3.DessinArea;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class SaveAction extends AbstractAction {

    private final JFrame frame;
    private final DessinArea dessinArea;

    public SaveAction(JFrame frame, DessinArea dessinArea) {
        super("Sauvegarder");
        this.frame = frame;
        this.dessinArea = dessinArea;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
        chooser.setFileFilter(filter);

        int result = chooser.showSaveDialog(dessinArea);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = chooser.getSelectedFile();
        String destName = selectedFile.getName();
        if (!destName.contains(".")) {
            destName += ".png";
        }

        File dest = new File(selectedFile.getParent(), destName);
        try {
            dessinArea.save(dest);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'écriture du fichier", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
