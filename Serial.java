public class Serial{

    private int terrainXSize;
    private int terrainYSize;
    List<Float> sunlightHours;
    int numberOfTrees;
    List<Trees> trees;
    float[][] gridSunlightHours;

    public static void main(String[] args){

        System.out.println();
        LoadMap();

        calculateTreeHours();
        int average = calculateSunlightAverage();
        printResults();

    }

    public static void LoadMap(){
        Scanner scanner = new Scanner(System.in);

    }

    public static void calculateTreeHours(){
        for (Tree tree: this.trees){
            for (int i = tree.xCorner; i < tree.xCorner + tree.canopy;
                i++){
                if (i > terrainXSize - 1) continue;
                for (int j = tree.yCorner; i < tree.yCorner + tree.canopy;
                i++){
                    if (j > terrainYSize -1) continue;
                    tree.sunlight += gridSunlightHours[i][j];
                }
                
                }
        }
    }
    /**
    
     */
    public static float calculateSunlightAverage(){
        float total = 0;
        for (Tree tree: this.trees){
            total += trees.sunlight;
        }

        return total /((float)numberOfTrees)
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