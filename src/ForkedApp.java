import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.ArrayList;
import java.lang.RuntimeException;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * @author Nyasha Bryan Katemauswa <address nyashabryan23@gmail.com>
 * @version 1.0
 */
public class ForkedApp{

    private static int terrainXSize;
    private static int terrainYSize;
    static int numberOfTrees;
    static float[][] gridSunlightHours;
    private static int SEQUENCIAL_CUTOFF;
    static final ForkJoinPool pool = new ForkJoinPool();

    /**
     * The application main method.
     * @param args
     */
    public static void main(String[] args){

        System.out.println("Starting");
        Tree[] trees = LoadMap();

        System.out.println("Calculating tree hours and average.....");
        System.gc();
        SEQUENCIAL_CUTOFF = trees.length/2;
        System.out.println(SEQUENCIAL_CUTOFF);

        calculateTreeHours(trees);

        // Computations start
        long initTime = System.currentTimeMillis();
        float average = calculateSunlightAverage(trees);
        
        long duration = System.currentTimeMillis() - initTime;
        
        System.out.println("Printing results.....");
        printResults(average, trees, duration);
        
    }

    public static float calculateSunlightAverage(Tree[] trees){
        return pool.invoke(new SumHoursCalculator(trees, 0, trees.length))/numberOfTrees;
    }

    public static class SumHoursCalculator extends RecursiveTask<Float>{
        Tree[] trees;
        Float sum;
        int low;
        int high;

        SumHoursCalculator(Tree[] trees, int low, int high){
            this.trees = trees;
            this.low = low;
            this.high = high;
        }

        protected Float compute(){
            if (high - low > SEQUENCIAL_CUTOFF){
                SumHoursCalculator left = new SumHoursCalculator(
                    trees, low, (high + low)/2
                );
                SumHoursCalculator right = new SumHoursCalculator(
                    trees, (high + low)/2, high
                );

                right.fork();
                Float leftAns = left.compute();
                Float rightAns = new Float(0);
                try{
                    rightAns = right.join();
                } catch(RuntimeException e){
                    System.out.println(e);
                }
                return leftAns + rightAns;

            }else{
                float sum = 0;
                for (int i = low; i < high; i++)
                sum += trees[i].sunlight;
                return sum;
            }     
        }

    }

    /**
     * Calculates the amount of sunlight that each of the trees had.
     */
    public static void calculateTreeHours(Tree[] trees){
        for (Tree tree: trees){
            for (int i = tree.xCorner; i < tree.xCorner + tree.canopy;
                i++){
                if (i > terrainXSize - 1) continue;
                for (int j = tree.yCorner; j < tree.yCorner + tree.canopy;
                j++){
                    if (j > terrainYSize -1) continue;
                    tree.sunlight += gridSunlightHours[i][j];
                }
                
            }
        }
    }

    /**
     * Take in the terrain details from a file or System.in
     * and use it to build the characteristics of the terrrain in the
     * static variables. 
     */
    public static Tree[] LoadMap(){
        System.out.println("Loading Map.....");
        BufferedReader reader;
        try {
            reader =  new BufferedReader(new FileReader("sample_input.txt"));
            String line1;
            line1 = reader.readLine();
            terrainXSize = (int) Integer.parseInt(line1.split(" ")[0]);
            terrainYSize = (int) Integer.parseInt(line1.split(" ")[1]);
            
            System.out.println("Making Grid.....");
            makeGrid(reader.readLine().split(" "));                
            String line3 = reader.readLine();
            numberOfTrees = (int) Integer.parseInt(line3.trim());
            

        
            System.out.println("Scanning lines");
            List<String> treeLines = new ArrayList<String>();
            String xline;
            while (true){
                xline = reader.readLine();
                if (xline == null || xline.equals("")) break;
                treeLines.add(xline);
            }
            reader.close();

            System.out.println("Making trees.....");
            return makeTrees(treeLines);
        
        }catch (FileNotFoundException e){
            System.out.println(e.toString());
            return new Tree[numberOfTrees];
        }catch (IOException e){
            System.out.println(e);
            return new Tree[numberOfTrees];
        }
    }


    /**
     * Makes and fills in the gridSunlightHours static variable from a line
     * of input.
     * @param line String array of the sunlight hours as floats
     */
    public static void makeGrid(String[] line){
        
        gridSunlightHours = new float[terrainXSize][terrainYSize];
        System.out.println(line.length);
        System.out.println(terrainXSize);
        System.out.println(terrainYSize);
        for (int i = 0; i < terrainXSize; i++){
            for(int j = 0; j < terrainYSize; j++){
                gridSunlightHours[i][j] = Float.parseFloat(line[(i)* (terrainYSize) + j]);
            }
        }
    }

    /**
     * Makes and fills the trees array.
     * @param treeLines
     */
    public static Tree[] makeTrees(List<String> treeLines){
        Tree[] trees = new Tree[numberOfTrees];
        if (treeLines ==  null) return trees;
        Tree tree = null;
        String[] line;
        for (int i = 0; i < numberOfTrees; i++){
            line = treeLines.get(i).split(" ");
            if (line.length > 2) tree = Tree.newTree(line);
            if (tree != null) trees[i] = tree;
        }
        return trees;
    }

    /**
     * Method to print the results of the experiment.
     * @param average The average exposure hours of the trees in the terrain.
     * @param trees The list of the trees in the terrain.
     * @param duration The time it took for all the computation in the experiment.
     */
    public static void printResults(float average, Tree[] trees, long duration){
        System.out.println("Computation took " + Long.toString(duration));
        System.out.println(average);
        System.out.println(trees.length);
        for (Tree tree: trees){
        //    System.out.println(tree.sunlight);
        }
    }

}