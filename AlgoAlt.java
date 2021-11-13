/*

/**
 *
 * @author Franciszek Ruszkowski w1787351
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class AlgoAlt {

    int cap;
    int nodes;
    int[] num_nodes;
    static int matrix[][];
    int flow[][];
    int f;
    int totalflow;

    Stack<Integer> search = new Stack<Integer>();
    Stack<Integer> empty = new Stack<Integer>();

    AlgoAlt(int nodes) {

        this.nodes = nodes;

        this.num_nodes = new int[nodes];

        matrix = new int[nodes + 1][nodes + 1];
        flow = new int[nodes + 1][nodes + 1];

        matrix[0][0] = 0;
        flow[0][0] = 0;

        for (int i = 1; i < nodes + 1; i++) {
            matrix[0][i] = i - 1;
            matrix[i][0] = i - 1;
        }

        for (int j = 0; j < nodes; j++) {
            num_nodes[j] = j;
        }

        for (int m = 0; m <= nodes; m++) {
            for (int n = 0; n <= nodes; n++) {
                flow[m][n] = 0;

            }
        }
        for (int i = 1; i < nodes + 1; i++) {
            flow[0][i] = i - 1;
            flow[i][0] = i - 1;
        }

    }

    // Instert Method
    public void addEdge(int start, int end, int cap) {

        matrix[start + 1][end + 1] = cap;

    }
    
    // Search Method
    public int getCap(int start, int end){
        
        return matrix[start + 1][end + 1];
    }

    // Printing Method
    public void printGraph() {
        
        System.out.println("Graph: (Adjacency Matrix)");
        System.out.println(" ");
        for (int i = 0; i < nodes + 1; i++) {
            for (int j = 0; j < nodes + 1; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Finding successor element
    public int findNext(int v, Set<Integer> closed) {

        for (int i : num_nodes) {
            if (matrix[v + 1][i + 1] > 0 && !closed.contains(i) && flow[v + 1][i + 1] < matrix[v + 1][i + 1]) {
                return i;
            }
        }
        return -1;
    }
    
    // Depth First Search path search algorithm
    public Stack<Integer> DFS(int source, int target) {

        Stack<Integer> open = new Stack<Integer>();
        Set<Integer> closed = new HashSet<>();

        open.push(source);
        closed.add(source);

        while (!open.empty()) {

            int v = open.peek();
            int w = findNext(v, closed);

            if (w != -1) {
                open.push(w);
                closed.add(w);

                if (w == target) {
                    return open;
                }

            } else {
                open.pop();
            }
        }
        return null;
    }
    
    // Current capacity deducted by the flow from previous iteration
    public int AvailableCapacity(int v, int w) {

        return matrix[v + 1][w + 1] - flow[v + 1][w + 1];
    }

    // Ford-Fulkerson algorithm implementation with DFS
    public int Ford(int source, int target) {

        search = DFS(source, target);

        while (search != null) {
            f = AvailableCapacity(search.get(0), search.get(1));

            for (int i = 1; i < search.size() - 1; i++) {
                f = Math.min(f, AvailableCapacity(search.get(i), search.get(i + 1)));
            }
            for (int j = 0; j < search.size() - 1; j++) {
                flow[search.get(j) + 1][search.get(j + 1) + 1] = flow[search.get(j) + 1][search.get(j + 1) + 1] + f;
            }

            System.out.println("Current path : " + Arrays.toString(search.toArray()));
            System.out.println("Current flow on this path : " + f);

            search = DFS(source, target);
        }

        for (int lol = 0; lol < nodes; lol++) {
            totalflow = totalflow + flow[source + 1][lol];

        }
        System.out.println(" ");
        System.out.println("Graph: (Flow Matrix)");
        System.out.println(" ");
        System.out.println(" ");

        for (int i = 0; i < nodes + 1; i++) {
            for (int j = 0; j < nodes + 1; j++) {
                System.out.print(flow[i][j] + " ");
            }
            System.out.println();
        }

        return totalflow;

    }

    // Main Method, int x=0 for manual input, x=1 for parser file input
    public static void main(String[] args) {

        // int x = 0;
        int x = 1;

        if (x == 1) {

            try {

                Scanner in = new Scanner(new File("/Users/franciszekruszkowski/NetBeansProjects/AlgoAlt/src/main/java/wera.txt"));
                ArrayList<Integer> nums = new ArrayList<>();

                int nodes = in.nextInt();
                
                while (in.hasNextInt()) {
                    nums.add(in.nextInt());

                }
                
                
                int num_of_edges = nums.size() / 3;

                AlgoAlt g = new AlgoAlt(nodes);

                // Taking 3 elemenets at a time, storing them as node node (creating an edge) and capacity 
                for (int i = 0; i < num_of_edges * 3; i += 3) {
                    g.addEdge(nums.get(i), nums.get(i + 1), nums.get(i + 2));

                }

                int s = 0;
                int t = nodes - 1;
                int result = g.Ford(s, t);
                System.out.println(" ");
                System.out.println(" ");
                System.out.println("The maximum total flow is : " + result);
                System.out.println(" ");
                System.out.println(" ");
                g.printGraph();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {

            AlgoAlt g = new AlgoAlt(4);
            g.addEdge(0, 1, 6);
            g.addEdge(0, 2, 4);
            g.addEdge(1, 2, 2);
            g.addEdge(1, 3, 3);
            g.addEdge(2, 3, 5);

            int result = g.Ford(0, 3);
            g.printGraph();
            System.out.println("Total flow is :" + result);

        }

    }

}
