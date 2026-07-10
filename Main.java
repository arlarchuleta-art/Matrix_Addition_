// Adam Archuleta
// July 2026
// Concurrency: Matrix Addition
// Citation: AI tools were used to help troubleshoot the initial threading 
// logic and file parsing. I have manually reviewed, tested, and updated the
// code myself to fit the assignment requirements.
//
// Assignment Question Answer: 
// Q: How do we handle concurrent operations safely in this program?
// A: By calculating the exact midpoint of the rows and columns, we split the target 
// matrix into four distinct, non-overlapping quadrants. Because each thread writes 
// data to a completely isolated section of the matrix array in memory, they can run 
// simultaneously without any race conditions or data interference.

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a file name as a command line argument.");
            return;
        }

        // Open the file given on the command prompt
        Path path = Paths.get(args[0]);
        Scanner inFile = null;
        try {
            inFile = new Scanner(path);
        } catch(IOException e) {
            System.out.println("IOException occurred. Check your file name and/or path.");
            System.exit(1);
        }

        // Read matrix dimensions
        if (!inFile.hasNextInt()) {
            System.out.println("File is empty or incorrectly formatted.");
            return;
        }
        int rows = inFile.nextInt();
        int columns = inFile.nextInt();

        // Read in the matrices A and B
        int[][] A = readInMatrix(rows, columns, inFile);
        int[][] B = readInMatrix(rows, columns, inFile);
        inFile.close();

        // Create the result matrix C
        int[][] C = new int[rows][columns];

        // Instantiate four ThreadOperation objects mapped to string indicators
        ThreadOperation t1 = new ThreadOperation(A, B, C, "UPPER_LEFT");
        ThreadOperation t2 = new ThreadOperation(A, B, C, "UPPER_RIGHT");
        ThreadOperation t3 = new ThreadOperation(A, B, C, "LOWER_LEFT");
        ThreadOperation t4 = new ThreadOperation(A, B, C, "LOWER_RIGHT");

        // Start up all the threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // Use join to make sure they finish processing before printing
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            System.out.println("A thread was interrupted during execution.");
        }

        // Print original matrices to match the video demo requirements
        print2dArray(A);
        System.out.println();
        print2dArray(B);
        System.out.println();

        // Print out the summed matrix with the required label
        System.out.println("Sum of above arrays:");
        print2dArray(C);
    }   

    public static int[][] readInMatrix(int rows, int columns, Scanner inFile) {
        int[][] toReturn = new int[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (inFile.hasNextInt()) {
                    toReturn[r][c] = inFile.nextInt();
                }
            }
        }
        return toReturn;
    }

    public static void print2dArray(int[][] matrix) {
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                // Formatting width to keep columns lined up perfectly
                System.out.printf("%-3d ", matrix[r][c]);
            }
            System.out.println();
        }
    }
}