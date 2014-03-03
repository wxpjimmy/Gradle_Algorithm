package Tree;

/**
 * Created by Administrator on 2/25/14.
 */
public class Trie {
    private TrieNode root = new TrieNode(null);
    private char start='a';

    public class TrieNode{
        public char value;
        public TrieNode parent;
        public TrieNode[] children;
        public boolean isWord = false;
        public int childSize = 0;

        public TrieNode(TrieNode parent){
            this.parent = parent;
            children = new TrieNode[26];
            for(int i=0;i<26;i++){
                children[i] = null;
            }
        }
    }

    public void Insert(String value){
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
        ro.isWord = true;
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
            ro.isWord = false;
        }else{
            TrieNode parent = ro.parent;
            int index = ro.value-'a';
            parent.children[index] = null;
            parent.childSize--;
            while(parent != null && parent.childSize==0 && !parent.isWord){
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
        return start.isWord;
    }
}
