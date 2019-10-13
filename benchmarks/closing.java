
import java.io.*;
import java.util.*;

public class closing {

    public static ArrayList<ArrayList<Edge>> graph;
    public static int[][] map;
    
    public static class Edge {
        public int node;
        public boolean done;
        
        public Edge(int n) {
            node = n;
        }
        
        @Override
        public String toString() {
            return done ? "" : String.format("%s ", node);
        }
    }
    
    public static void main(String[] args) throws Exception {
        Scanner f = new Scanner(new File("closing.in"));
        PrintWriter out = new PrintWriter("closing.out");

        int N = f.nextInt();
        int M = f.nextInt();
        
        graph = new ArrayList<>();
        map = new int[N][N];
        for(int i = 0; i<N; i++)
            Arrays.fill(map[i], -1);
        
        for(int i = 0; i<N; i++)
            graph.add(new ArrayList<Edge>());
        
        int[] index = new int[N];
        
        for(int i = 0; i<M; i++) {
            int A = f.nextInt()-1;
            int B = f.nextInt()-1;
            
            graph.get(A).add(new Edge(B));
            graph.get(B).add(new Edge(A));
            
            map[A][B] = index[A]++;
            map[B][A] = index[B]++;
        }
        
        boolean[] cleared = new boolean[N];

        for(int i = 0; i<N; i++) {
            out.println(bfs(cleared, graph));
            int n = f.nextInt();
            clear(n-1, graph);
            cleared[n-1] = true;
        }
        
        // Algorithm is O(M + 3N
        
        out.flush();
        out.close();
        f.close();
    } 

    public static void clear(int n, ArrayList<ArrayList<Edge>> graph) {
        for(int i = 0; i<graph.get(n).size(); i++) {
            Edge e = graph.get(n).get(i);
            e.done = true;
            graph.get(e.node).get(map[e.node][n]).done = true;
        } 
    }
    
    public static String bfs(boolean[] cleared, ArrayList<ArrayList<Edge>> graph) {
        Queue<Edge> queue = new LinkedList<>(); 
        int source = 0;
        
        while(cleared[source])
            source++;
        
        boolean[] visited = new boolean[graph.size()];
        queue.offer(new Edge(source));
        visited[source] = true;
        
        while(!queue.isEmpty()) {  
            Edge src = queue.poll();
            
            for(int i = 0; i<graph.get(src.node).size(); i++) {
                Edge n = graph.get(src.node).get(i);
                if(!visited[n.node] && !n.done) {
                    queue.add(n);
                    visited[n.node] = true;
                }
            }
        }
        
        for(int i = 0; i<visited.length; i++) {
            if(!visited[i] && !cleared[i])
                return "NO";
        }
        
        return "YES";
    }
}
