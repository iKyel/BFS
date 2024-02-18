package BestFirstSearch;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import edu.princeton.cs.algs4.*;

public class BestFirstSearch {
    private boolean[] marked; // Đánh dấu các đỉnh đã thăm
    private int[] edgeTo; // Đỉnh cha của mỗi đỉnh trong đường đi ngắn nhất
    private double[] distTo; // Khoảng cách ngắn nhất từ nguồn đến mỗi đỉnh
    private IndexMinPQ<Double> pq; // Hàng đợi ưu tiên lưu trữ các đỉnh theo thứ tự tốt nhất
    private static int tar; // target vertex
    
    public BestFirstSearch(myDirectGraph G, int source, PrintWriter writer) {
    	
    		marked = new boolean[G.V()];
            edgeTo = new int[G.V()];
            distTo = new double[G.V()];
            pq = new IndexMinPQ<>(G.V());

            for (int v = 0; v < G.V(); v++)
                distTo[v] = Double.POSITIVE_INFINITY;

            distTo[source] = 0.0;
            pq.insert(source, distTo[source]);
            

            writer.println("Phat trien TT | Trang thai ke | Danh sach L ");
            while (!pq.isEmpty()) {
                int v = pq.delMin(); // Lấy đỉnh gần nhất

                
                writer.print(G.getVertexNames()[v]);
                writer.print(G.getVertexValues()[v]);
                for(int i=1; i<=15-(Double.toString(G.getVertexValues()[v]).length()); i++) {
                	writer.print(" ");
            	}
                writer.print("|");
                
                marked[v] = true;
                
                int sodinh=0;
                int k = 0;
                for (int w : G.adj(v)) {
                	sodinh++;
                	writer.print(G.getVertexNames()[w]);
                	writer.print(G.getVertexValues()[w] + " ");
                    k += Integer.toString(G.getVertexValues()[w]).length();
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = G.getVertexValues()[w]; // Đánh giá tùy thuộc vào bài toán cụ thể
                        pq.insert(w, distTo[w]);
                    }
                    
                }
                
                if(v == tar) {
                	writer.print("TTKT-DUNG");
                	break;
                }
                
                for(int i=1; i<=14-(k+(sodinh*2-1)); i++) {
                	writer.print(" ");
            	}
                writer.print("|");
                for (int vertex : pq) {
                	writer.print(G.getVertexNames()[vertex] + G.getVertexValues()[vertex] + " ");
                }
                writer.println();
                System.out.println();
                // Kiểm tra xem có đường đi từ nguồn đến đích hay không
                
            }
    	
    	
    }

    // Kiểm tra xem có tồn tại đường đi từ nguồn đến đích hay không
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // Trả về một đường đi ngắn nhất từ nguồn đến đích (nếu tồn tại)
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != 0; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(0); // Đỉnh nguồn
        return path;
    }

    // Khoảng cách ngắn nhất từ nguồn đến đích
    public double distTo(int v) {
        return distTo[v];
    }
    public static void main(String[] args) {
    	// Đọc dữ liệu từ đầu vào
        In in = new In(args[0]);
        int V = in.readInt(); // Số lượng đỉnh
        String[] vertexNames = new String[V];
        int[] vertexValues = new int[V];

        // Đọc thông tin về các đỉnh và giá trị của chúng
        for (int i = 0; i < V; i++) {
            String name = in.readString();
            int value = in.readInt();
            vertexNames[i] = name;
            vertexValues[i] = value;
        }

        int E = in.readInt(); // Số lượng cạnh
        myDirectGraph G = new myDirectGraph(V, vertexNames, vertexValues);

        // Đọc thông tin về các cạnh và thêm chúng vào đồ thị
        for (int i = 0; i < E; i++) {
            String v = in.readString();
            String w = in.readString();
            G.addEdge(v, w);
        }

     // Chọn đỉnh nguồn và đỉnh kết thúc
        String sourceVertexName = in.readString();
        String targetVertexName = in.readString();

        // Chạy thuật toán Best-First Search từ đỉnh nguồn đến đỉnh kết thúc
        tar = G.indexOfVertex(targetVertexName);
        int sourceVertexIndex = G.indexOfVertex(sourceVertexName);
        int targetVertexIndex = G.indexOfVertex(targetVertexName);
        try (PrintWriter writer = new PrintWriter(new FileWriter("outputBestFirstSearch.txt"))) {
        	
        	BestFirstSearch bfs = new BestFirstSearch(G, sourceVertexIndex, writer);
        	writer.println();
            // Kiểm tra xem có đường đi từ nguồn đến đích hay không
            if (bfs.hasPathTo(targetVertexIndex)) {
                // In ra đường đi ngắn nhất từ nguồn đến đích (nếu có)
            	writer.print("Shortest path from " + sourceVertexName + " to " + targetVertexName + ": ");
                for (int x : bfs.pathTo(targetVertexIndex)) {
                	writer.print(G.getVertexNames()[x] + " ");
                }         
            } else {
               // Nếu không có đường đi từ nguồn đến đích
            	writer.println("No path from " + sourceVertexName + " to " + targetVertexName);
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
           
        
    }

}

