
package nearest_neighbors;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class simil {
    
    public static void main(String[] args) {
        Scanner f = new Scanner(System.in);
        f.useDelimiter("\n|\\s\\s");
        ArrayList<String> sets = new ArrayList<>();
        HashMap<String, Integer> vals = new HashMap<>();
        String type = f.next();
        while(f.hasNext()) {
            String next = f.next();
            if(next.equals("999")) break;
            sets.add(next);
        }
        
        switch(type) {
            case "J": manage(0, vals, sets); break;
            case "M": manage(1, vals, sets); break;
            case "E": manage(2, vals, sets); break;
        }
        
        System.out.println(vals);    
    }
    
    public static void manage(int flag, HashMap<String,Integer> vals, 
            ArrayList<String> sets) {
        for(int i = 0; i<sets.size()-1; ++i)
            for(int j = i+1; j<sets.size(); ++j) {
                String val = null;
                switch(flag) {
                    case 0: val = jac_dist(sets.get(i), sets.get(j)); break;
                    case 1: val = manhat_dist(sets.get(i), sets.get(j)); break;
                    case 2: val = edit_dist(sets.get(i), sets.get(j)); break;
                }               
                vals.put(val, (vals.get(val) != null ? vals.get(val) : 0)+1);
            }
    }
    
    // lowercase character only
    public static String edit_dist(String s1, String s2) {
        int[][] dp = new int[s1.length()+1][s2.length()+1];
        for(int[] r: dp) Arrays.fill(r, -1);
        return Integer.toString(
                s1.length()+s2.length()-2*edit_helper(s1,s2,0,0,dp));
    }
    
    public static int edit_helper(String s1, String s2, 
            int p1, int p2, int[][] dp) {
        if(dp[p1][p2] != -1) return dp[p1][p2];
        if(p1<s1.length()&&p2<s2.length())
            if(s1.charAt(p1) == s2.charAt(p2)) 
                dp[p1][p2] = 1+edit_helper(s1,s2,p1+1,p2+1, dp);
            else
                dp[p1][p2] = Math.max(edit_helper(s1,s2,p1+1,p2, dp),
                        edit_helper(s1,s2,p1,p2+1, dp));
        else dp[p1][p2] = 0;
        return dp[p1][p2];
    }
    
    public static String manhat_dist(String s1, String s2) {
        int aggr= 0;
        for(int i = 0; i<s1.length(); ++i)
            aggr += Math.abs(s1.charAt(i)-s2.charAt(i));
        return Integer.toString(aggr);
    }
    
    public static String jac_dist(String s1, String s2) {
        int aggr = 0; int sel = 0;
        for(int i = 0; i<s1.length(); ++i) {
            if(s1.charAt(i) == '0' && s2.charAt(i) == '0') continue;
            if(s1.charAt(i) == '1' ^ s2.charAt(i) == '1') ++aggr;
            else if(s1.charAt(i) == '1' && s2.charAt(i) == '1') 
            {++sel; ++aggr;}
        }

        int gcd = gcd(aggr,sel);
        return String.format("%d/%d", (aggr-sel)/gcd, aggr/gcd);
    }
    
    public static int gcd(int a, int b) {
        if(a == 0) return b;
        else if(b == 0) return a;
        else if(a>b) return gcd(a%b,b);
        else if(a<b) return gcd(b%a,a);
        else return 0;
    }
}
