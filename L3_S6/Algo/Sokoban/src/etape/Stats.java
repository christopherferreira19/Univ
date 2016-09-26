package etape;

public class Stats {

    private int pousseurConfiguration;
    private int sacConfiguration;
    private int pousseurDeplacements;
    private int sacDeplacements;

    public Stats() {
        this.sacConfiguration = 0;
        this.pousseurConfiguration = 0;
        this.pousseurDeplacements = 0;
        this.sacDeplacements = 0;
    }

    public void addPousseurConfiguration(int pousseurConfiguration) {
        this.pousseurConfiguration += pousseurConfiguration;
    }

    public void addSacConfiguration(int sacConfiguration) {
        this.sacConfiguration += sacConfiguration;
    }

    public void addPousseurDeplacements(int pousseurDeplacements) {
        this.pousseurDeplacements += pousseurDeplacements;
    }

    public void addSacDeplacements(int sacDeplacements) {
        this.sacDeplacements += sacDeplacements;
    }

    public void print() {
        System.out.println("[Configurations explorés]");
        System.out.println("  Pousseur : " + pousseurConfiguration);
        System.out.println("  Sac : " + sacConfiguration);

        System.out.println("[Deplacements]");
        System.out.println("  Pousseur : " + pousseurDeplacements);
        System.out.println("  Sac : " + sacDeplacements);
    }
}
