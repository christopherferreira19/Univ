package gauffre.moteur;

import gauffre.Coords;

import java.util.Arrays;
import java.util.Objects;

public class Configuration{
    int [] Lignes;
    int hash;

    public Configuration( int [] L){
        Lignes = new int[L.length];
        System.arraycopy(L, 0, Lignes, 0, L.length);
        hash = Objects.hash(Lignes);
    }

    public int hashCode(){
        return hash;
    }

    public boolean equals( Object obj ){
        if( !(obj instanceof Configuration) )
            return false;
        Configuration c = (Configuration) obj;
        return Arrays.equals(Lignes, c.Lignes);
    }


}
