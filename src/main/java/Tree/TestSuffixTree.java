package Tree;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jimmy on 3/2/14.
 */
public class TestSuffixTree {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File f = new File("/Users/jimmy/Documents/PathDataNoCloud.txt");

        FileInputStream fr = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(fr));
        Map<String, Integer> paths = new HashMap<String, Integer>();
        String input = null;
        int index = 0;
        int countNian = 0;
        while((input = br.readLine())!=null){
            String[] parts = input.split(":");
            String dest = parts[parts.length-1].trim();
            dest = dest.substring(0, dest.length()-1);
            paths.put(dest, index++);
            //System.out.println(dest);
        }

        System.out.println("paths size:" + paths.size());
        // Pattern pattern = Pattern.

        long start = System.nanoTime();
        Map<String, Set<Integer>> keyToValues = new HashMap<String, Set<Integer>>();
        Set<String> keys = new HashSet<String>();
        int count = 0;
        int dup = 0;
        MySuffixTree at = new MySuffixTree();
        for(Map.Entry<String, Integer> path:paths.entrySet()){
            String key = path.getKey().replace(" ", "").replace(".jpg", "");
           /* String[] partialKey = key.split("/");
            for(String k:partialKey){
                if(k.isEmpty())
                    continue;
                count++;
                String rk = k.replace("-", "").replace("(", "").replace(")", "").replace("_", "");
                if(keyToValues.containsKey(rk)){
                    dup++;
                    System.out.println("duplicate key:" + rk);
                    keyToValues.get(rk).add(path.getValue());
                }else{
                    HashSet<Integer> values = new HashSet<Integer>();
                    values.add(path.getValue());
                    keyToValues.put(rk, values);
                }
            }*/
            key = key.replace("-", "").replace("(", "").replace(")", "").replace("_", "").replace("/", "").replace(".", "");
            //if(key.contains("2014")){
                countNian++;
                at.buildSuffixTree(key, path.getValue());
               // System.out.println(key);
           // }
           // keys.add(key);

        }

        //at.print();

        long cost = System.nanoTime() - start;
        System.out.println("Build tree cost: " + cost + ", tree size: ");

        System.out.println("年会： " + countNian);
        /*start = System.nanoTime();
        Set<String> result = at.getMatched("2014");
        cost = System.nanoTime() - start;
        System.out.println("Search cost: " + cost);
        print(result);

        start = System.nanoTime();
        Set<String> upload = at.getMatched("upload");
        cost = System.nanoTime() - start;
        System.out.println("Search cost: " + cost);
        print(upload);

        start = System.nanoTime();
        Set<String> s1 = at.getMatched("年会");
        cost = System.nanoTime() - start;
        System.out.println("Search cost: " + cost);
        print(s1);

        start = System.nanoTime();
        Set<String> s2 = at.getMatched("iv");
        cost = System.nanoTime() - start;
        System.out.println("Search cost: " + cost);*/
        //Set<Integer> fianlRes = new HashSet<Integer>();
        start = System.nanoTime();
        Set<Integer> result = at.getMatchResult("2014");
        cost = System.nanoTime() - start;
        System.out.println("Search cost: " + cost);
        //print(result);
       // printInt(fianlRes);
        printInt(result);

        start = System.nanoTime();
        Set<Integer> upload = at.getMatchResult("upload");
        cost = System.nanoTime() - start;
        System.out.println("Search cost: " + cost);
        printInt(upload);

        start = System.nanoTime();
        Set<Integer> s1 = at.getMatchResult("年会");
        cost = System.nanoTime() - start;
        System.out.println("Search cost: " + cost);
        printInt(s1);

        start = System.nanoTime();
        Set<Integer> s2 = at.getMatchResult("iv");
        cost = System.nanoTime() - start;
        System.out.println("Search cost: " + cost);
        printInt(s2);
    }

    public static void print(Set<String> set){
        if(set == null){
            System.out.println("No result!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(String s:set){
            sb.append(s).append(",");
        }
        System.out.println(sb.toString());
    }

    public static void printSB(Set<StringBuilder> set){
        if(set == null){
            System.out.println("No result!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(StringBuilder s:set){
            sb.append(s).append(",");
        }
        System.out.println(sb.toString());
    }
    public static void printInt(Set<Integer> set){
        if(set == null){
            System.out.println("No result!");
            return;
        }
        System.out.println(set.size());
        StringBuilder sb = new StringBuilder();
        for(Integer s:set){
            sb.append(s).append(",");
        }
        System.out.println(sb.toString());
    }
}
