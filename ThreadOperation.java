// Adam Archuleta
// July 2026
// Thread handler that maps grid coordinates and processes submatrix additions.
// Citation: The dynamic quadrant index math and threading implementation were drafted with AI assistance. I manually reviewed, tested, and rewrote the final logic to ensure it behaves correctly.

public class ThreadOperation extends Thread {
    private int[][] A;
    private int[][] B;
    private int[][] C;
    private String quadrant;

    public ThreadOperation(int[][] A, int[][] B, int[][] C, String quadrant) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.quadrant = quadrant;
    }

    public static int[] getQuadrantIndexes(int rows, int columns, String quadrant) {
        // Integer division automatically handles odd/even matrix splitting correctly
        int midRow = rows / 2;
        int midCol = columns / 2;
        
        int rowStart = 0;
        int rowEnd = 0;
        int colStart = 0;
        int colEnd = 0;

        if (quadrant.equals("UPPER_LEFT")) {
            rowStart = 0;
            rowEnd = midRow - 1;
            colStart = 0;
            colEnd = midCol - 1;
        } else if (quadrant.equals("UPPER_RIGHT")) {
            rowStart = 0;
            rowEnd = midRow - 1;
            colStart = midCol;
            colEnd = columns - 1;
        } else if (quadrant.equals("LOWER_LEFT")) {
            rowStart = midRow;
            rowEnd = rows - 1;
            colStart = 0;
            colEnd = midCol - 1;
        } else if (quadrant.equals("LOWER_RIGHT")) {
            rowStart = midRow;
            rowEnd = rows - 1;
            colStart = midCol;
            colEnd = columns - 1;
        }

        // Return array maps to: {row_start, row_end, column_start, column_end}
        return new int[]{rowStart, rowEnd, colStart, colEnd};
    }

    @Override
    public void run() {
        int rows = A.length;
        int columns = A[0].length;
        
        // Retrieve dynamic boundary indexes for this thread's designated quadrant
        int[] indexes = getQuadrantIndexes(rows, columns, quadrant);
        int rStart = indexes[0];
        int rEnd = indexes[1];
        int cStart = indexes[2];
        int cEnd = indexes[3];

        // Perform submatrix addition safely within boundaries
        for (int r = rStart; r <= rEnd; r++) {
            for (int c = cStart; c <= cEnd; c++) {
                C[r][c] = A[r][c] + B[r][c];
            }
        }
    }
}