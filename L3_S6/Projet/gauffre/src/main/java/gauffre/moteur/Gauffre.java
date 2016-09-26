package gauffre.moteur;
import java.util.*;

public interface Gauffre {

    int getWidth();
    int getHeight();

    int getLineSize(int l);
    int getColumnSize(int c);

    boolean getState(int l, int c);
    Configuration getConfig();
    void removeFrom( int l, int c);

    Gauffre cloneGauffre();
}
