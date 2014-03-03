package Tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jimmy on 2/27/14.
 */
public class AnyTrie {

    private AnyTrieNode root = new AnyTrieNode(null);
    private int size = 0;

    public class AnyTrieNode{
            public Character value;
            public AnyTrieNode parent;
            public Map<Character, AnyTrieNode> children;
            //public boolean isWord = false;
            public Set<String> keyword;
            public int childSize = 0;

            public AnyTrieNode(AnyTrieNode parent){
                this.parent = parent;
                children = new HashMap<Character, AnyTrieNode>();
            }

            public Set<String> getChildrenKeywords(){
                Set<String> result = new HashSet<String>();
                if(keyword != null)
                    result.addAll(keyword);
               /* for(int i=0;i<26;i++){
                    if(children[i]!=null){
                        Set<String> cR = children[i].getChildrenKeywords();
                        if(cR != null)
                            result.addAll(cR);
                    }
                }*/
                for(AnyTrieNode atn:children.values()){
                    Set<String> cR = atn.getChildrenKeywords();
                    if(cR != null)
                        result.addAll(cR);
                }
                return result;
            }

           public void getChildrenKeywords(Set<String> result){
               if(keyword != null)
                   result.addAll(keyword);
               /* for(int i=0;i<26;i++){
                    if(children[i]!=null){
                        Set<String> cR = children[i].getChildrenKeywords();
                        if(cR != null)
                            result.addAll(cR);
                    }
                }*/
               for(AnyTrieNode atn:children.values()){
                   atn.getChildrenKeywords(result);
               }
           }
    }

    public void insert(String value){
        insert(value,value);
        for(int i=1;i<value.length();i++){
            String partial = value.substring(i);
            insert(partial, value);
        }
    }

    public void insert(String value, String keyword){
        if(value == null || value.isEmpty())
            return;
        AnyTrieNode ro = root;
        for(int i=0;i<value.length();i++){
            Character wd = value.charAt(i);
            if(!ro.children.containsKey(wd)){
                AnyTrieNode atn = new AnyTrieNode(ro);
                atn.value = wd;
                ro.children.put(wd, atn);
                ro.childSize++;
            }
            ro = ro.children.get(wd);
        }
        //ro.isWord = true;
        if(ro.keyword == null){
            ro.keyword = new HashSet<String>();
        }
        ro.keyword.add(keyword);
        size++;
    }

    public boolean remove(String value){
        if(value == null || value.isEmpty())
            return true;
        AnyTrieNode ro = root;

        for(int i=0;i<value.length();i++){
            Character wd = value.charAt(i);
            if(ro.children.containsKey(wd)){
                ro = ro.children.get(wd);
            }else{
                return false;
            }
        }

        if( ro.childSize > 0){
            ro.keyword = null;
        }else{
            AnyTrieNode parent = ro.parent;
            Character key = ro.value;
            parent.children.remove(key);
            parent.childSize--;
            while(parent != null && parent.childSize==0 && parent.keyword == null){
                if(parent.parent!=null){
                    key = parent.value;
                    parent = parent.parent;
                    parent.children.remove(key);
                    parent.childSize--;
                }else
                    break;
            }
        }
        size--;
        return true;
    }

    public boolean find(String value){
        if(value == null || value.isEmpty())
            return true;
        AnyTrieNode start = root;
        for(int i=0;i<value.length();i++){
            Character index = value.charAt(i);
            if(start.children.containsKey(index)){
                start = start.children.get(index);
            }else{
                return false;
            }
        }
        return start.keyword != null;
    }

    public int getSize(){
        return size;
    }

    public Set<String> findKeywords(String value){
        long st = System.nanoTime();
        if(value == null || value.isEmpty())
            return null;
        AnyTrieNode start = root;
        for(int i=0;i<value.length();i++){
            Character index = value.charAt(i);
            if(start.children.containsKey(index)){
                start = start.children.get(index);
            }else{
                return null;
            }
        }
        long cost = System.nanoTime() - st;
        Set<String> result =  start.getChildrenKeywords();
        System.out.println("Search " + value + " Cost: " + cost);
        return result;
    }

    public Set<String> findKeywordsV2(String value){
        long st = System.nanoTime();
        if(value == null || value.isEmpty())
            return null;
        AnyTrieNode start = root;
        for(int i=0;i<value.length();i++){
            Character index = value.charAt(i);
            if(start.children.containsKey(index)){
                start = start.children.get(index);
            }else{
                return null;
            }
        }
        long cost = System.nanoTime() - st;
        HashSet<String> result = new HashSet<String>();
        start.getChildrenKeywords(result);
        System.out.println("Search " + value + " Cost: " + cost);
        return result;
    }

    public static void main(String[] args){
        AnyTrie test = new AnyTrie();
        String[] keywords = {"beijing", "haidian", "dongcheng", "feb", "oct",
                "skydrive", "dropbox", "flickr", "yantai", "china","japan", "korean",
                "sanfrancisco", "qerdsdadafsadfsafsadfsadfsafsafsadafadf"};

        long buildStart = System.nanoTime();
        for(String s:keywords){
            test.insert(s);
            for(int i=1; i<s.length();i++){
                String partial = s.substring(i);
                test.insert(partial, s);
            }
        }
        long end = System.nanoTime() - buildStart;
        System.out.println("build cost: " + end);


        Set<String> s1 = test.findKeywords("i");
        print(s1);

        Set<String> s2 = test.findKeywords("ng");
        print(s2);

        Set<String> s3 = test.findKeywords("beijins");
        print(s3);

        Set<String> s4 = test.findKeywords("na");
        print(s4);

        Set<String> s5 = test.findKeywordsV2("i");
        print(s5);

        Set<String> s6 = test.findKeywordsV2("ng");
        print(s6);

        Set<String> s7 = test.findKeywordsV2("beijins");
        print(s7);

        Set<String> s8 = test.findKeywordsV2("na");
        print(s8);
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
}
