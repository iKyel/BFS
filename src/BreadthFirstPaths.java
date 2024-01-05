/******************************************************************************
 *  Compilation:  BreadthFirstPaths.java
 *  Execution:    java BreadthFirstPaths G s
 *  Dependencies: Graph.java Queue.java Stack.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/41graph/tinyCG.txt
 *                https://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *                https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 *  Run breadth first search on an undirected graph.
 *  Runs in O(E + V) time.
 *
 *  %  java Graph tinyCG.txt
 *  6 8
 *  0: 2 1 5
 *  1: 0 2
 *  2: 0 1 3 4
 *  3: 5 4 2
 *  4: 3 2
 *  5: 3 0
 *
 *  %  java BreadthFirstPaths tinyCG.txt 0
 *  0 to 0 (0):  0
 *  0 to 1 (1):  0-1
 *  0 to 2 (1):  0-2
 *  0 to 3 (2):  0-2-3
 *  0 to 4 (2):  0-2-4
 *  0 to 5 (1):  0-5
 *
 *  %  java BreadthFirstPaths largeG.txt 0
 *  0 to 0 (0):  0
 *  0 to 1 (418):  0-932942-474885-82707-879889-971961-...
 *  0 to 2 (323):  0-460790-53370-594358-780059-287921-...
 *  0 to 3 (168):  0-713461-75230-953125-568284-350405-...
 *  0 to 4 (144):  0-460790-53370-310931-440226-380102-...
 *  0 to 5 (566):  0-932942-474885-82707-879889-971961-...
 *  0 to 6 (349):  0-932942-474885-82707-879889-971961-...
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.*;

public class BreadthFirstPaths {
    private static final int INFINITY = Integer.MAX_VALUE; // Vô cùng, đại diện cho khoảng cách
    
    // Một mảng boolean để kiểm tra xem một đỉnh đã được duyệt qua hay chưa.
    private boolean[] marked;  // marked[v] = is there an s-v path // 
    
    // Mảng để lưu đỉnh trước đỉnh hiện tại trên đường đi ngắn nhất.
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    
    // Mảng để lưu số cạnh trên đường đi ngắn nhất từ đỉnh nguồn đến mỗi đỉnh khác.
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path

    // Khởi tạo với một đỉnh nguồn cụ thể s.
    public BreadthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        validateVertex(s);
        bfs(G, s);

        assert check(G, s);
    }

    /**
     * Computes the shortest path between any one of the source vertices in {@code sources}
     * and every other vertex in graph {@code G}.
     * @param G the graph
     * @param sources the source vertices
     * @throws IllegalArgumentException if {@code sources} is {@code null}
     * @throws IllegalArgumentException if {@code sources} contains no vertices
     * @throws IllegalArgumentException unless {@code 0 <= s < V} for each vertex
     *         {@code s} in {@code sources}
     */
    
    // Khởi tạo với một tập hợp các đỉnh nguồn.
    public BreadthFirstPaths(Graph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        validateVertices(sources);
        bfs(G, sources);
    }

    // Thực hiện tìm kiếm theo chiều rộng từ một đỉnh nguồn cụ thể.
    // breadth-first search from a single source
    private void bfs(Graph G, int s) {
        Queue<Integer> q = new Queue<Integer>(); // Tạo một hàng đợi (Queue) để lưu trữ các đỉnh cần duyệt
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY; // Khởi tạo mảng distTo (số cạnh trên đường đi ngắn nhất) bằng giá trị INFINITY cho tất cả các đỉnh trong đồ thị. Điều này đảm bảo rằng tất cả các đỉnh ban đầu đều có khoảng cách vô hạn từ đỉnh nguồn s.
        distTo[s] = 0; // Đặt khoảng cách từ đỉnh nguồn s đến chính nó là 0
        marked[s] = true; //  Đánh dấu đỉnh nguồn s là đã được thăm.
        q.enqueue(s); //  Thêm đỉnh nguồn s vào hàng đợi để bắt đầu quá trình tìm kiếm.

        while (!q.isEmpty()) { // Thực hiện vòng lặp khi hàng đợi không trống. 
            int v = q.dequeue(); //  Lấy ra đỉnh đầu tiên từ hàng đợi để thăm.
            for (int w : G.adj(v)) { // Duyệt qua tất cả các đỉnh kề của đỉnh v.
                if (!marked[w]) { // Nếu đỉnh kề `w` chưa được thăm:
                    edgeTo[w] = v; // Ghi nhận rằng đỉnh `v` là đỉnh trước đỉnh `w` trên đường đi ngắn nhất.
                    distTo[w] = distTo[v] + 1; // Cập nhật số cạnh trên đường đi ngắn nhất từ đỉnh nguồn `s` đến đỉnh `w`.
                    marked[w] = true; // Đánh dấu đỉnh `w` là đã được thăm.
                    q.enqueue(w); // Thêm đỉnh `w` vào hàng đợi để duyệt tiếp theo.
                }
            }
        }
    }

    // Thực hiện tìm kiếm theo chiều rộng từ một tập hợp các đỉnh nguồn.
    // breadth-first search from multiple sources
    private void bfs(Graph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    /**
     * Is there a path between the source vertex {@code s} (or sources) and vertex {@code v}?
     * @param v the vertex
     * @return {@code true} if there is a path, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    
    // Kiểm tra xem có đường đi từ đỉnh nguồn đến đỉnh v hay không.
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    /**
     * Returns the number of edges in a shortest path between the source vertex {@code s}
     * (or sources) and vertex {@code v}?
     * @param v the vertex
     * @return the number of edges in such a shortest path
     *         (or {@code Integer.MAX_VALUE} if there is no such path)
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    
    // Trả về số cạnh trên đường đi ngắn nhất từ đỉnh nguồn đến đỉnh v.
    public int distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    /**
     * Returns a shortest path between the source vertex {@code s} (or sources)
     * and {@code v}, or {@code null} if no such path.
     * @param  v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    
    // Trả về đường đi ngắn nhất từ đỉnh nguồn đến đỉnh v.
    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }


    // check optimality conditions for single source
    private boolean check(Graph G, int s) {

        // check that the distance of s = 0
        if (distTo[s] != 0) {
            StdOut.println("distance of source " + s + " to itself = " + distTo[s]);
            return false;
        }

        // check that for each edge v-w dist[w] <= dist[v] + 1
        // provided v is reachable from s
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (hasPathTo(v) != hasPathTo(w)) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("hasPathTo(" + v + ") = " + hasPathTo(v));
                    StdOut.println("hasPathTo(" + w + ") = " + hasPathTo(w));
                    return false;
                }
                if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("distTo[" + v + "] = " + distTo[v]);
                    StdOut.println("distTo[" + w + "] = " + distTo[w]);
                    return false;
                }
            }
        }

        // check that v = edgeTo[w] satisfies distTo[w] = distTo[v] + 1
        // provided v is reachable from s
        for (int w = 0; w < G.V(); w++) {
            if (!hasPathTo(w) || w == s) continue;
            int v = edgeTo[w];
            if (distTo[w] != distTo[v] + 1) {
                StdOut.println("shortest path edge " + v + "-" + w);
                StdOut.println("distTo[" + v + "] = " + distTo[v]);
                StdOut.println("distTo[" + w + "] = " + distTo[w]);
                return false;
            }
        }

        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    // throw an IllegalArgumentException if vertices is null, has zero vertices,
    // or has a vertex not between 0 and V-1
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int vertexCount = 0;
        for (Integer v : vertices) {
            vertexCount++;
            if (v == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            validateVertex(v);
        }
        if (vertexCount == 0) {
            throw new IllegalArgumentException("zero vertices");
        }
    }

    /**
     * Unit tests the {@code BreadthFirstPaths} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]); // Tạo một đối tượng In để đọc dữ liệu từ tệp đồ thị được cung cấp thông qua đối số dòng lệnh args[0].
        Graph G = new Graph(in); // Tạo một đối tượng đồ thị G
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]); // Đọc đỉnh nguồn s
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s); // thực hiện tìm kiếm theo chiều rộng để tính toán đường đi ngắn nhất từ đỉnh nguồn đến tất cả các đỉnh khác trong đồ thị.

        for (int v = 0; v < G.V(); v++) { // Duyệt qua tất cả các đỉnh trong đồ thị.
            if (bfs.hasPathTo(v)) { // Kiểm tra xem có đường đi từ đỉnh nguồn đến đỉnh v hay không
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v)); // In ra dòng thông tin về đường đi ngắn nhất từ `s` đến `v`, bao gồm đỉnh nguồn, đỉnh đích và số cạnh trên đường đi.
                for (int x : bfs.pathTo(v)) { // Duyệt qua tất cả các đỉnh trên đường đi ngắn nhất từ `s` đến `v` và in chúng ra màn hình.
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d (-):  not connected\n", s, v); // Nếu không có đường đi từ s đến v
            }

        }
    }
   



}

/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
