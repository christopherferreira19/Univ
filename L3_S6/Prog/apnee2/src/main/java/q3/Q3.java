package q3;

import q3.action.*;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static javax.swing.KeyStroke.getKeyStroke;

public class Q3 implements Runnable {

    static {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception exc) {
            //
        }
    }

    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;

    private UndoAction undoAction;
    private RedoAction redoAction;
    private PasteAction pasteAction;

    public Q3() {
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Dessin");
        DessinArea area = new DessinArea();
        this.undoAction = new UndoAction(area);
        this.redoAction = new RedoAction(area);
        this.pasteAction = new PasteAction(area);
        area.initActions(undoAction, redoAction, pasteAction);

        DessinListener dessinListener = new DessinListener(area);
        area.addMouseListener(dessinListener);
        area.addMouseMotionListener(dessinListener);

        frame.setJMenuBar(createMenu(frame, area));
        frame.add(area);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JMenuBar createMenu(JFrame frame, DessinArea area) {
        JMenuItem newItem = new JMenuItem(new NewAction(area));
        JMenuItem quitItem = new JMenuItem(new QuitAction());
        JMenuItem saveItem = new JMenuItem(new SaveAction(frame, area));
        JMenuItem loadItem = new JMenuItem(new LoadAction(frame, area));
        JMenu fichierMenu = new JMenu("Fichier");

        CopyAction copyAction = new CopyAction(area);
        JMenuItem undoItem = new JMenuItem(undoAction);
        JMenuItem redoItem = new JMenuItem(redoAction);
        JMenuItem copyItem = new JMenuItem(copyAction);
        JMenuItem pasteItem = new JMenuItem(pasteAction);
        JMenu editMenu = new JMenu("Edition");

        JMenuBar menuBar = new JMenuBar();

        newItem.setMnemonic('N');
        quitItem.setMnemonic('Q');
        fichierMenu.setMnemonic('F');
        undoItem.setMnemonic('U');
        redoItem.setMnemonic('R');
        editMenu.setMnemonic('E');

        fichierMenu.add(newItem);
        fichierMenu.add(loadItem);
        fichierMenu.add(saveItem);
        fichierMenu.add(quitItem);

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        menuBar.add(fichierMenu);
        menuBar.add(editMenu);

        registerShortcuts(frame, copyAction);
        return menuBar;
    }

    private void registerShortcuts(JFrame frame, CopyAction copyAction) {
        InputMap inputMap = frame.getRootPane().getInputMap();
        inputMap.put(getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "undo");
        inputMap.put(getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "redo");
        inputMap.put(getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "copy");
        inputMap.put(getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "paste");

        ActionMap actionMap = frame.getRootPane().getActionMap();
        actionMap.put("undo", undoAction);
        actionMap.put("redo", redoAction);
        actionMap.put("copy", copyAction);
        actionMap.put("paste", pasteAction);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Q3());
    }
}
