import terrain.Terrain;

class TerrainArgs {

    static Terrain read(String[] args) {
        Terrain terrain;
        if (args.length == 0) {
            terrain = Terrain.defaut();
        }
        else if (args.length == 1) {
            terrain = Terrain.read(SokobanManuel.class.getResourceAsStream(args[0]));
        }
        else if (args.length == 2) {
            terrain = Terrain.random(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    0.7);
        }
        else if (args.length == 3) {
            terrain = Terrain.random(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Double.parseDouble(args[2]));
        }
        else if (args.length == 4) {
            terrain = Terrain.random(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Double.parseDouble(args[2]),
                    Integer.parseInt(args[3]));
        }
        else {
            System.out.println("Usage : ");
            System.out.println(" * Sans arguments : Terrain par défaut");
            System.out.println(" * <nom fichier> : Terrain depuis le fichier");
            System.out.println(" * <largeur> <hauteur>: Terrain aléatoire avec 70% de case libre");
            System.out.println(" * <largeur> <hauteur> <ratio>: Terrain aléatoire avec ratio (0 <= ratio <= 1) de case libre");
            System.out.println(" * <largeur> <hauteur> <ratio> <seed>: Terrain aléatoire avec ratio (0 <= ratio <= 1) de case libre");
            throw new RuntimeException();
        }
        return terrain;
    }
}
