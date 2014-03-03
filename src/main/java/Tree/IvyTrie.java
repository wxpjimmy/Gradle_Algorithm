package Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jimmy on 2/26/14.
 */
public class IvyTrie {
    private TrieNode root = new TrieNode(null);
    private char start='a';

    public class TrieNode{
        public char value;
        public TrieNode parent;
        public TrieNode[] children;
        //public boolean isWord = false;
        public Set<String> keyword;
        public int childSize = 0;

        public TrieNode(TrieNode parent){
            this.parent = parent;
            children = new TrieNode[26];
            for(int i=0;i<26;i++){
                children[i] = null;
            }
        }

        public Set<String> getChildrenKeywords(){
            Set<String> result = new HashSet<String>();
            if(keyword != null)
                result.addAll(keyword);
            for(int i=0;i<26;i++){
                if(children[i]!=null){
                    Set<String> cR = children[i].getChildrenKeywords();
                    if(cR != null)
                        result.addAll(cR);
                }
            }
            return result;
        }
    }

    public void insert(String value){
        insert(value,value);
    }

    public void insert(String value, String keyword){
        if(value == null || value.isEmpty())
            return;
        TrieNode ro = root;
        for(int i=0;i<value.length();i++){
            char wd = value.charAt(i);
            int index = wd-start;
            if(ro.children[index]==null){
                ro.children[index] = new TrieNode(ro);
                ro.children[index].value = wd;
                ro.childSize++;
            }
            ro = ro.children[index];
        }
        //ro.isWord = true;
        if(ro.keyword == null){
            ro.keyword = new HashSet<String>();
        }
        ro.keyword.add(keyword);
    }

    public boolean remove(String value){
        if(value == null || value.isEmpty())
            return true;
        TrieNode ro = root;

        for(int i=0;i<value.length();i++){
            char wd = value.charAt(i);
            int index = wd-start;
            if(ro.children[index]!=null){
                ro = ro.children[index];
            }else{
                return false;
            }
        }

        if( ro.childSize > 0){
            ro.keyword = null;
        }else{
            TrieNode parent = ro.parent;
            int index = ro.value-'a';
            parent.children[index] = null;
            parent.childSize--;
            while(parent != null && parent.childSize==0 && parent.keyword == null){
                if(parent.parent!=null){
                    int idx = parent.value-'a';
                    parent = parent.parent;
                    parent.children[idx]=null;
                    parent.childSize--;
                }else
                    break;
            }
        }
        return true;
    }

    public boolean find(String value){
        if(value == null || value.isEmpty())
            return true;
        TrieNode start = root;
        for(int i=0;i<value.length();i++){
            int index = value.charAt(i)-'a';
            if(start.children[index] != null){
                start = start.children[index];
            }else{
                return false;
            }
        }
        return start.keyword != null;
    }

    public Set<String> findKeywords(String value){
        long st = System.nanoTime();
        if(value == null || value.isEmpty())
            return null;
        TrieNode start = root;
        for(int i=0;i<value.length();i++){
            int index = value.charAt(i)-'a';
            if(start.children[index] != null){
                start = start.children[index];
            }else{
                return null;
            }
        }
        long cost = System.nanoTime() - st;
        System.out.println("Search " + value + " Cost: " + cost);
        return start.getChildrenKeywords();
    }

    public static void main(String[] args){
        IvyTrie test = new IvyTrie();
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
