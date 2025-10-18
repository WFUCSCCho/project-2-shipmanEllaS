/**********************************************************************************************
 * @file : Proj2.java
 * @description : Inserts a number of Villagers into two AVlTrees and BSTrees, each of which
 *                handle a randomized or a sorted dataset. The time in nanoseconds needed to
 *                complete insert and search operations for each of the four trees is displayed
 *                on the screen and printed in "output.txt" in CSV format. This file takes the
 *                filepath of the input pile, villagers.csv, and the numver of lines to be read
 *                from the file (up to 392).
 * @author : Ella Shipman
 * @date : October 18, 2025
 * @acknowledgement : Jessica Li's "Animal Crossing New Horizons Catalog", "villagers.csv" file.
 * https://www.kaggle.com/datasets/jessicali9530/animal-crossing-new-horizons-nookplaza-dataset.
 *********************************************************************************************/



import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Proj2 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java TestAVL <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        // For file input
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // Open the input file
        inputFileNameStream = new FileInputStream(inputFileName);
        inputFileNameScanner = new Scanner(inputFileNameStream);

        // ignore first line
        inputFileNameScanner.nextLine();

        //Read file and fill out arraylist of datatype Villager
        ArrayList<Villager> villagers = new ArrayList<>();
        String currLine = null;
        for (int i=2; i <= numLines; i++) {
            currLine = inputFileNameScanner.nextLine();     //get next line in file
            String[] villInfo = null;
            if (!currLine.isEmpty()) {
                villInfo = currLine.split(",");
            }
            String[] shortInfo = new String[4];
            shortInfo[0] = villInfo[0];     //name
            shortInfo[1] = villInfo[3];     //personality
            shortInfo[2] = villInfo[4];     //hobby
            shortInfo[3] = villInfo[7];     //favorite song

            Villager v = null;
            try {
                v = new Villager(shortInfo[0], shortInfo[1], shortInfo[2], shortInfo[3]);
            } catch (ArrayIndexOutOfBoundsException e) {}

            if (shortInfo[1] != null) {         //add object to villagers list
                villagers.add(v);
            } else {
                System.out.println("insert failed - line " + i);
            }
        }

        //Shuffling the list ----------------------------------------------------------------------------------
        Collections.shuffle(villagers);

        //Initialize AVL and BST trees
        AVLTree<Villager> AVL_Villagers_Rand = new AVLTree<>();     //Randomized dataset AVL
        BSTree<Villager> BST_Villagers_Rand = new BSTree<>();       //Randomized dataset BST
        AVLTree<Villager> AVL_Villagers_Sort = new AVLTree<>();     //Sorted dataset AVL
        BSTree<Villager> BST_Villagers_Sort = new BSTree<>();       //Sorted dataset BST

        //Insert - Randomized dataset AVL
        long AVLRandInsertStart = System.nanoTime();
        for (int i = 0; i < villagers.size(); i++) {
            AVL_Villagers_Rand.insert(villagers.get(i));
        }
        long AVLRandInsertEnd = System.nanoTime();
        long AVLRandInsert = AVLRandInsertEnd - AVLRandInsertStart;

        //Insert - Randomized dataset BST
        long BSTRandInsertStart = System.nanoTime();
        for (int i = 0; i < villagers.size(); i++) {
            BST_Villagers_Rand.insert(villagers.get(i));
        }
        long BSTRandInsertEnd = System.nanoTime();
        long BSTRandInsert = BSTRandInsertEnd - BSTRandInsertStart;

        //Search - Randomized dataset AVL
        long AVLRandSearchStart = System.nanoTime();
        for (int i = 0; i < villagers.size(); i++) {
            AVL_Villagers_Rand.contains(villagers.get(i));
        }
        long AVLRandSearchEnd = System.nanoTime();
        long AVLRandSearch = AVLRandSearchEnd - AVLRandSearchStart;

        //Search - Randomized dataset BST
        long BSTRandSearchStart = System.nanoTime();
        for (int i = 0; i < villagers.size(); i++) {
            BST_Villagers_Rand.contains(villagers.get(i));
        }
        long BSTRandSearchEnd = System.nanoTime();
        long BSTRandSearch = BSTRandSearchEnd - BSTRandSearchStart;
        //--------------------------------------------------------------------------------------------------------------

        //Sort dataset (ascending)
        Collections.sort(villagers);

        //Insert - Sorted dataset AVL
        long AVLSortInsertStart = System.nanoTime();
        for (int i = 0; i < villagers.size(); i++) {
            AVL_Villagers_Sort.insert(villagers.get(i));
        }
        long AVLSortInsertEnd = System.nanoTime();
        long AVLSortInsert = AVLSortInsertEnd - AVLSortInsertStart;

        //Insert - Sorted dataset BST
        long BSTSortInsertStart = System.nanoTime();
        for (int i = 0; i < villagers.size(); i++) {
            BST_Villagers_Sort.insert(villagers.get(i));
        }
        long BSTSortInsertEnd = System.nanoTime();
        long BSTSortInsert = BSTSortInsertEnd - BSTSortInsertStart;

        //Search - Sorted dataset AVL
        long AVLSortSearchStart = System.nanoTime();
        for (int i = 0; i < villagers.size(); i++) {
            AVL_Villagers_Sort.contains(villagers.get(i));
        }
        long AVLSortSearchEnd = System.nanoTime();
        long AVLSortSearch = AVLSortSearchEnd - AVLSortSearchStart;

        //Search - Sortedc dataset BST
        long BSTSortSearchStart = System.nanoTime();
        for (int i = 0; i < villagers.size(); i++) {
            BST_Villagers_Sort.contains(villagers.get(i));
        }
        long BSTSortSearchEnd = System.nanoTime();
        long BSTSortSearch = BSTSortSearchEnd - BSTSortSearchStart;

        //-----------------------------------------------------------------------------------------------------

        //Write results in file
        FileWriter writer = null;
        try {       //assume output.txt will be manually reset
            writer = new FileWriter("src/output.txt", true);
        }
        catch (IOException e) { System.out.println("FileNotFound!"); }

        //Output timer information on screen
        System.out.print("-------------------------------------------------------------------------------------------\n");
        System.out.print("Number of lines read from dataset: " + numLines + "/392\n");
        System.out.print("Randomized dataset (insertion): BST (" + BSTRandInsert +
                    " nsec) vs AVL (" + AVLRandInsert + " nsec)\n");
        System.out.print("Randomized dataset (search): BST (" + BSTRandSearch +
                    " nsec) vs AVL (" + AVLRandSearch +" nsec)\n");
        System.out.print("Sorted dataset (insertion): BST (" + BSTSortInsert +
                    " nsec) vs AVL (" + AVLSortInsert +" nsec)\n");
        System.out.print("Sorted dataset (search): BST (" + BSTSortSearch +
                    " nsec) vs AVL (" + AVLSortSearch +" nsec)\n");
        System.out.print("-------------------------------------------------------------------------------------------\n");

        //Append a line to output.txt with the information above, in CSV format
        writer.write(numLines + "," + BSTRandInsert+ "," + AVLRandInsert + "," +
                BSTRandSearch + "," + AVLRandSearch + "," + BSTSortInsert + "," +  AVLSortInsert + "," +
                BSTSortSearch + "," + AVLSortSearch + "\n");
        writer.flush();
        writer.close();
    }
}
