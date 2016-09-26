package gauffre.moteur;

import javax.swing.*;

public class Settings {

    int width;
    int height;
    JoueurChoix joueur1;
    JoueurChoix joueur2;


    public enum JoueurChoix{
        HUMAIN, IA;
    }

    public Settings(){
        width = 10;
        height = 10;
        joueur1 = JoueurChoix.HUMAIN;
        joueur2 = JoueurChoix.HUMAIN;
    }






}
