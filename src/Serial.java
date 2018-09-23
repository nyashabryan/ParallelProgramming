import java.util.Scanner;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Nyasha Bryan Katemauswa <address nyashabryan23@gmail.com>
 * @version 1.0
 * 
 * 
 */
public class Serial{

    private static int terrainXSize;
    private static int terrainYSize;
    private static int numberOfTrees;
    private static float[][] gridSunlightHours;
    private static String INPUT_FILE = "sample_input.txt";

    /**
     * Serial Application Main Method.
     * 
     * @param args
     */
    public static void main(String[] args){

        if (args.length > 0){
            numberOfTrees = Integer.parseInt(args[0]);
        }
        else{
            numberOfTrees = 0;
        }

        Tree[] trees = LoadMap();
        calculateTreeHours(trees);
        System.gc();

        long initTime = System.currentTimeMillis();
        int sum = calculateSunlightSum(trees);
        long duration = System.currentTimeMillis() - initTime;
        float average = sum /((float)numberOfTrees);
        printResults(average, duration);

    }

    /**
     * Take in the terrain details from a file or System.in
     * and use it to build the characteristics of the terrrain in the
     * static variables.
     * @return Tree[] The array of the trees in the map.
     */
    public static Tree[] LoadMap(){
        BufferedReader reader;
        try {
            reader =  new BufferedReader(new FileReader(INPUT_FILE));
            String line1 = reader.readLine();
            terrainXSize = (int) Integer.parseInt(line1.split(" ")[0]);
            terrainYSize = (int) Integer.parseInt(line1.split(" ")[1]);
            
            makeGrid(reader.readLine().split(" "));

            String line3 = reader.readLine();
            if (numberOfTrees == 0)
                numberOfTrees = (int) Integer.parseInt(line3.trim());
        
            List<String> treeLines = new ArrayList<String>();
            String xline;
            for (int i = 0; i < numberOfTrees; i++){
                xline = reader.readLine();
                if (xline == null || xline == "") break;
                treeLines.add(xline);
            }
            if (treeLines.size() != numberOfTrees)
                System.out.println("Number of trees not equal to treeLines");
            reader.close();
        
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
        for (int i = 0; i < terrainXSize; i++){
            for(int j = 0; j < terrainYSize; j++){
                gridSunlightHours[i][j] = Float.parseFloat(line[(i)* (terrainYSize) + j]);
            }
        }
    }

    /**
     * Makes and fills the Trees array.
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
     * Calculates the amount of sunlight that each of the trees had.
     */
    public static void calculateTreeHours(Tree[] trees){
        for (Tree tree: trees){
            if (tree == null) {
                System.out.println("Found a null");
                continue;
            }
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
     * Calculates the average tree sunlight hours in the terrain.
     * @return average trees sunlight hours 
     */
    public static int calculateSunlightSum(Tree[] trees){
        int total = 0;
        for (int i = 0; i < trees.length; i++){
            total += trees[i].sunlight;
        }
        return total;
    }

    public static void printResults(float average, long duration){
        System.out.print(numberOfTrees);
        System.out.print(",");
        System.out.print(average);
        System.out.print(",");
        System.out.println(Long.toString(duration));
    }

    public static void printTrees(Tree[] trees){
        for(Tree tree: trees){
            System.out.println(tree);
        }
    }
}