/**
 * Tree class to represent all the tree objects in the terrain.
 */
public class Tree{
    
    int xCorner;
    int yCorner;
    int canopy;
    float sunlight;

    /**
     * Tree constructor using the integer values of the tree 
     * characteristics.
     * @param xCorner
     * @param yCorner
     * @param canopy
     */
    public Tree(int xCorner, int yCorner, int canopy){
        this.xCorner = xCorner;
        this.yCorner = yCorner;
        this.canopy = canopy;
    }

    /**
     * Another constructor for the Tree object. 
     * @param details integer array containing the xCorner, yCorner 
     * and the canopy values of the Tree. 
     */
    public Tree(int[] details){
        this(0, 0, 0);
        if (details.length > 3){
            this.xCorner =  details[0];
            this.yCorner = details[1];
            this.canopy = details[2];
        }
    }

    /**
     * Another constructor for the Tree object.
     * @param details string array containing the xCorner, yCorner and
     * the canopy values of the tree.
     */
    public static Tree newTree(String[] details){
        try{
            
            int xCorner = (int)Integer.parseInt(details[0]);
            int yCorner = (int)Integer.parseInt(details[1]);
            int canopy = (int)Integer.parseInt(details[2]);
            return new Tree(xCorner, yCorner, canopy);
        }catch (NumberFormatException e){
            System.out.println(e);
            return null;
        }
    }

}