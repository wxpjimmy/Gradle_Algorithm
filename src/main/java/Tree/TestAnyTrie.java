package Tree;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by jimmy on 2/27/14.
 */
public class TestAnyTrie {
    public static void main(String[] args) throws FileNotFoundException, IOException {
       File f = new File("/Users/jimmy/Documents/PathDataNoCloud.txt");


       FileInputStream fr = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(fr));
        Map<String, Integer> paths = new HashMap<String, Integer>();
        String input = null;
        int index = 0;
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
        int count = 0;
        int dup = 0;
        for(Map.Entry<String, Integer> path:paths.entrySet()){
            String key = path.getKey().replace(" ", "").replace(".jpg", "");
            String[] partialKey = key.split("/");
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
            }
        }
        System.out.println("split count:" + count + ", duplicate key: " + dup);
        long cost = System.nanoTime() - start;
        System.out.println("Pre process key cost: " + cost + ", total key number: " + keyToValues.keySet().size());

        start = System.nanoTime();

        AnyTrie at = new AnyTrie();
        for(String key: keyToValues.keySet()){
            at.insert(key);
        }

        cost = System.nanoTime() - start;
        System.out.println("Build tree cost: " + cost + ", tree size: " + at.getSize());

        Set<Integer> fianlRes = new HashSet<Integer>();
        Set<String> result = at.findKeywords("2014");
        if(result != null){
            for(String key: result){
                fianlRes.addAll(keyToValues.get(key));
            }
        }
        //print(result);
        printInt(fianlRes);

        fianlRes.clear();
        Set<String> upload = at.findKeywords("upload");
        if(upload != null){
            for(String key: upload){
                fianlRes.addAll(keyToValues.get(key));
            }
        }
        //print(result);
        printInt(fianlRes);

        fianlRes.clear();
        Set<String> s1 = at.findKeywords("年会");
        if(s1 != null){
            for(String key: s1){
                fianlRes.addAll(keyToValues.get(key));
            }
        }
        //print(result);
        printInt(fianlRes);

        fianlRes.clear();
        Set<String> s2 = at.findKeywords("iv");
        if(s2 != null){
            for(String key: s2){
                fianlRes.addAll(keyToValues.get(key));
            }
        }
       // print(result);
        printInt(fianlRes);
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
