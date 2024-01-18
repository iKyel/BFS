package ThuatToanASao;

import edu.princeton.cs.algs4.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AStar {
    private int start;
    private int goal;
    private double[] distTo; // Chi phí ngắn nhất từ đỉnh đầu đến đỉnh đó
    private double[] f;      // Ước lượng chi phí từ đỉnh đến đỉnh kết thúc thông qua đỉnh hiện tại
    private int[] edgeTo;    // Đỉnh trước đỉnh hiện tại trên đường đi ngắn nhất
    private IndexMinPQ<Double> pq; // Priority queue để chọn đỉnh có chi phí f thấp nhất

    public AStar(myDirectGraph G, int start, int goal, String outputFileName) {
    	try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))){ 	
        	this.start = start;
            this.goal = goal;
            distTo = new double[G.V()]; // g
            f = new double[G.V()]; // f
            edgeTo = new int[G.V()]; 
            pq = new IndexMinPQ<>(G.V()); // danh sách L

            for (int v = 0; v < G.V(); v++) {
                distTo[v] = Double.POSITIVE_INFINITY;
                f[v] = Double.POSITIVE_INFINITY;
            }
            distTo[start] = 0.0;
            f[start] = heuristic(G, start); 

            pq.insert(start, f[start]);

            writer.print("TT | TTK | k(u,v) | h(v) | g(v) | f(v) |\n");
            while (!pq.isEmpty()) {
                int v = pq.delMin();
                writer.print(G.getVertexNames()[v] + "  | ");
                if (v == goal) break; // Đã đến đỉnh kết thúc
                // gọi biến chỉ để in 
                int cnt = 0;
                for (int w : G.adj(v)) {
                	
                	// Nếu không phải là thằng đỉnh kề đầu tiên
                	if(cnt != 0) {
                		writer.print("   | ");
                	} 
                	
                	// In ra đỉnh kề
                	writer.print(G.getVertexNames()[w] + "   | "); 
                	// Kết thúc in đỉnh kề          	
                    relax(G, v, w, writer); 
                    writer.print("\n");
                    cnt++;
                }
                // In danh sách priority queue sau mỗi lần duyệt
                writer.print("Danh sách pq: [ ");
                for (int vertex : pq) {
                	writer.print(G.getVertexNames()[vertex] + f[vertex] + " ");
                }
                writer.print("]");
                writer.print("\n\n");               
            } 
    	}  catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    

    // thuật toán 
    private void relax(myDirectGraph G, int v, int w, PrintWriter writer) {
    	// Gọi biến
    	double k = G.getEdgeValue(v, w);
    	double h = G.getVertexValues()[w];
    	
    	// In k(u,v)
    	writer.print(k); // in ra giá trị cạnh tương ứng k(u,v)
    	for(int i=1; i<=7-(Double.toString(k).length()); i++) {
    		writer.print(" ");
    	}
    	writer.print("| ");
    	// kết thúc in k(u,v)
    	
    	// In h(v)
    	writer.print(h); // In ra h(v)
    	for(int i=1; i<=5-(Double.toString(h).length()); i++) {
    		writer.print(" ");
    	}
    	writer.print("| ");
    	// kết thúc in h(v)
    	
    	// in g9v)
        double g = distTo[v] + G.getEdgeValue(v, w); // g(v) = g(u) + k(u,v)
        writer.print(g); 
        for(int i=1; i<=5-(Double.toString(g).length()); i++) {
        	writer.print(" ");
    	}       
        writer.print("| ");
        // Kết thúc in g(v)
        
        // In f(v)
        double x = g + G.getVertexValues()[w];
        writer.print(x);
        for(int i=1; i<=5-(Double.toString(x).length()); i++) {
        	writer.print(" ");
    	}
        writer.print("| ");
        // Kết thúc in f(v)
        
        if (distTo[w] > g) {
            distTo[w] = g;
            edgeTo[w] = v;
            f[w] = distTo[w] + heuristic(G, w);       
            if (pq.contains(w)) pq.decreaseKey(w, f[w]);
            else pq.insert(w, f[w]);
        }
        writer.println();
    }

    private double heuristic(myDirectGraph G, int v) { // h
        // Đây là hàm heuristic, có thể điều chỉnh theo logic của bài toán
        // Trong trường hợp này, giả sử hàm heuristic là giá trị của đỉnh, có thể thay đổi tùy thuộc vào yêu cầu cụ thể
        return G.getVertexValues()[v];
    }

    public Iterable<Integer> pathTo() {
        Stack<Integer> path = new Stack<>();
        int current = goal;

        while (current != start) {
            path.push(current);
            current = edgeTo[current];
        }

        path.push(start);
        return path;
    }

    public static void main(String[] args) {
    	// Đọc file
        In in = new In(args[0]);
        int V = in.readInt();
        String[] vertexNames = new String[V];
        int[] vertexValues = new int[V];    

        for (int i = 0; i < V; i++) {
            String name = in.readString();
            int value = in.readInt();
            vertexNames[i] = name;
            vertexValues[i] = value;          
        }
        int E = in.readInt(); 
        
        myDirectGraph G = new myDirectGraph(V, vertexNames, vertexValues);
        for (int i = 0; i < E; i++) {
            String v = in.readString();
            String w = in.readString();
            int weight = in.readInt();
            G.addEdge(v, w, weight);           
        }   
        
        String startVertex = in.readString(); // Đỉnh bắt đầu   
        String goalVertex = in.readString();  // Đỉnh kết thúc

        
        int start = G.indexOfVertex(startVertex);
        int goal = G.indexOfVertex(goalVertex);
        
        // Kết thúc đọc file
        
        AStar astar = new AStar(G, start, goal, "outputASao.txt");

        // in đường đi
//        Iterable<Integer> path = astar.pathTo();
//        for (int vertex : path) {
//            StdOut.print(G.getVertexNames()[vertex] + " ");
//        }
    }
}

