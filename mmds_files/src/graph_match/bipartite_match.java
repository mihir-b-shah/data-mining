package graph_match;


import java.util.*;
import java.io.*;


// adapted a usaco problem
public class bipartite_match {
    
    public static ArrayList<ArrayList<VP>> vect;
    public static int src;
    public static int sink;
    public static boolean[] vis;
    public static boolean eject = false;
    public static int INF = 1_000_000_000;
    public static int curr_flow = 0;
    
    public static class VP implements Comparable<VP> {
        public int id;
        public int flow;
        public int wt;
        public VP twin;
        
        public VP(int id, int wt, VP ref) {
            this.id = id; this.wt = wt; twin=ref;
        }
        
        @Override
        public int compareTo(VP v) {
            return v.wt-wt;
        }
        
        @Override
        public String toString() {
            return String.format("<%d, %d/%d>", id+1, flow, wt);
        }
    }
    
    public static void main(String[] args) throws Exception {
        Scanner f = new Scanner(new File("infile_bm.in"));
        PrintWriter out = new PrintWriter("outfile_bm.out");

        int N = f.nextInt();
        int M = f.nextInt();
        
        vect = new ArrayList<>();
        for(int i = 0; i<2+N+M; i++) 
            vect.add(new ArrayList<VP>());
        
        for(int i = 1; i<=N; i++) {
            VP v1 = new VP(i,1,null);
            VP v2 = new VP(0,0,v1);
            v1.twin = v2;
            vect.get(0).add(v1);
            vect.get(i).add(v2);
        }
        
        for(int i = N+1; i<=N+M; i++) {
            VP v1 = new VP(i,0,null);
            VP v2 = new VP(N+1+M,1,v1);
            v1.twin = v2;
            vect.get(N+1+M).add(v1);
            vect.get(i).add(v2);
        }
        
        for(int i = 1; i<=N; i++) {
            int n = f.nextInt();
            for(int j = 0; j<n; j++) {
                int e = f.nextInt()-1;
                VP v1 = new VP(N+e+1,1,null);
                VP v2 = new VP(i,0,v1);
                v1.twin = v2;
                vect.get(i).add(v1);
                vect.get(N+e+1).add(v2);
            }
        }

        /* edmonds karp algorithm
        1. find path, determine if augmented.
        2. backtrack and change flows-p
        */
        
        src = 0;
        sink = N+M+1;       
        vis = new boolean[vect.size()];
        int flow = 0;
        
        do {
            eject = false;
            curr_flow = 0;
            Arrays.fill(vis,false);
            recur(src, INF);
            flow += curr_flow;
        } while(eject);
        
        out.println(flow);
        out.flush();
        out.close();
        f.close();
    }
    
    public static void recur(int s, int f) {
        if(eject) return;
        vis[s] = true;
        if(s == sink) {
            eject = true;
            curr_flow = f;
            //System.out.println(s+1);
            return;
        }
        
        ArrayList<VP> next = vect.get(s);
        for(VP v: next) 
            if(!eject && !vis[v.id] && v.flow<v.wt) {
                recur(v.id, Math.min(f, v.wt-v.flow));
                v.flow += curr_flow;
                v.twin.flow = -v.flow;
            }
        
    }
}
