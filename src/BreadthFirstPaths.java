import edu.princeton.cs.algs4.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstPaths {
//    public static boolean[] marked;
//    public static int[] edgeTo;
//    private final int s; // source vertex
    
    private boolean[] marked;
    private int[] edgeTo;
    private final int s; // source vertex

    public BreadthFirstPaths(myGraph G, int s, int des, String[] vertexNames, PrintWriter writer) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        myBFS(G, s, des, vertexNames, writer);
        bfs(G, s);
    }

    public void myBFS(myGraph G, int source, int des, String[] vertexNames, PrintWriter writer) {
    	
    		writer.printf("Phat trien trang thai | Trang thai ke | Danh sach L");
    		writer.printf("\n");
            
            Queue<Integer> queue = new LinkedList<>();
            marked[source] = true;
            queue.add(source);
            
            while (!queue.isEmpty()) {
            	int s = 0;
            	int d = 0;
                int v = queue.poll();
                writer.printf("           " + vertexNames[v] + "          | ");
                for (int w : G.adj(v)) {
                	if (!marked[w]) {
                		writer.printf(vertexNames[w] + " ");
                		s++;
                    }           	
                }
                s = s*2;
                d = 15 - s;
                for(int i=1; i<d; i++) {
                	writer.printf(" ");
                }
                writer.printf("| ");
                for (int w : G.adj(v)) {       	
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        marked[w] = true;
                        queue.add(w);
                    }
                }
                for (int item : queue) {
                    writer.print(vertexNames[item] + " ");
                }    
                writer.println();
                if(v == des) {
                	break;
                }
            } 
    	
    	
    }
    private void bfs(myGraph G, int s) {
        Queue<Integer> queue = new LinkedList<>();
        marked[s] = true;
        queue.add(s);

        while (!queue.isEmpty()) {
            int v = queue.poll();         
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    marked[w] = true;
                    queue.add(w);
                }
            }
            
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;

        LinkedList<Integer> path = new LinkedList<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.addFirst(x);
        }
        path.addFirst(s);

        return path;
    }
    
    
    // In đường
    public void printPath(myGraph G, int source, int destination, String[] vertexNames, PrintWriter writer) {
        if (hasPathTo(destination)) {
            writer.printf("Đường đi %s đến %s:  ", vertexNames[source], vertexNames[destination].charAt(0));
            for (int x : pathTo(destination)) {
                writer.print(vertexNames[x] + " ");
            }
            writer.println();
        } else {
            writer.printf("%s to %c: not connected\n", vertexNames[source], vertexNames[destination].charAt(0));
        }
    }
    

        
    public static void main(String[] args) {
    	
//    	boolean[] marked;
//        int[] edgeTo;
//        int s; // source vertex
    	
            if (args.length < 1) {
                StdOut.println("Sử dụng: java BreadthFirstPaths <tệp_nhập>");
                return;
            }

            In in = new In(args[0]);
            myGraph G = new myGraph(in);

            // Đọc số lượng đỉnh và tên đỉnh
            int numVertices = G.V();
            String[] vertexNames = G.getVertexNames();

            // Đọc đỉnh nguồn và đỉnh đích
            String sourceVertex = in.readString();
            String destinationVertex = in.readString();

            int sourceIndex = -1, destinationIndex = -1;

            // Lấy chỉ số của đỉnh nguồn và đỉnh đích
            for (int i = 0; i < numVertices; i++) {
                if (vertexNames[i].equals(sourceVertex)) {
                    sourceIndex = i;
                } else if (vertexNames[i].equals(destinationVertex)) {
                    destinationIndex = i;
                }

                if (sourceIndex != -1 && destinationIndex != -1) {
                    break;
                }
            }

            if (sourceIndex == -1 || destinationIndex == -1) {
                StdOut.println("Đỉnh nguồn hoặc đỉnh đích không hợp lệ.");
                return;
            }
            
            
            try (PrintWriter writer = new PrintWriter(new FileWriter("output.txt"))){
            	// in bảng
            	BreadthFirstPaths myBFS = new BreadthFirstPaths(G, sourceIndex, destinationIndex,vertexNames, writer);
            	
            	// in đường
            	myBFS.printPath(G, sourceIndex, destinationIndex, vertexNames, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
                
    	
      }   
}