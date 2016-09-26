package gauffre.laf;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LafInitialization {

    public static void initLaf() {
		Logger logger = Logger.getLogger(LafInitialization.class.getCanonicalName());
        try {
        	if (SwingUtilities.isEventDispatchThread()) {
        		setLaf();
        	}
        	else {
        		SwingUtilities.invokeAndWait(new Runnable() {
                	@Override
                	public void run() {
                		setLaf();
                	}
            	});
        	}
        }
        catch (InterruptedException exc) {
            logger.log(Level.WARNING, "Erreur lors de l'initialisation de l'interface", exc);
        }
        catch (InvocationTargetException exc) {
            logger.log(Level.WARNING, "Erreur lors de l'initialisation de l'interface", exc);
        }
    }

    private static void setLaf() {
		UIManager.put("ClassLoader", LafInitialization.class.getClassLoader());
	    System.setProperty("awt.useSystemAAFontSettings", "on");
	    System.setProperty("swing.aatext", "true");
	
	    try {
	        UIManager.setLookAndFeel(new NovintecLaf());
	    }
	    catch (UnsupportedLookAndFeelException e) {
	        // Not likely
	    }
    }
}
