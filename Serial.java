public class Serial{

    private int terrainXSize;
    private int terrainYSize;
    List<Float> sunlightHours;
    int numberOfTrees;
    List<Trees> trees;

    public static void main(String[] args){

        System.out.println();
        LoadMap();

    }

    public static void LoadMap(){
        Scanner scanner = new Scanner(System.in);

    }


    public static boolean checkMapLoaded(){
        return (sunlightHours || terrainXSize || terrainYSize ||
            numberOfTrees || trees )
    }

    public class Tree{
        
        int xCorner;
        int yCorner;
        int canopy;
        float sunlight;
        public Tree(int xCorner, int yCorner, int canopy){
            this.xCorner = xCorner;
            this.yCorner = yCorner;
            this.canopy = canopy;
        }
    }
}