package Tree;

import java.util.*;

/**
 * Created by jimmy on 2/28/14.
 */
public class MySuffixTree {

   // private List<StringBuilder> lists = new ArrayList<StringBuilder>();
   private Map<StringBuilder, Integer> lists = new HashMap<StringBuilder, Integer>();

    private Map<String, StringBuilder> patternTpSB = new HashMap<String, StringBuilder>();

    private Map<String, TreeNode> sbToLastNode = new HashMap<String, TreeNode>();
    private TreeNode root = new TreeNode();
    private char empty = '.';

    public MySuffixTree() {
        root.children = new HashMap<Character, TreeNode>();
    }

    private class TreeNode {
        public StringBuilder sb;
        public int start;
        public int end;
        public int len;
        public Map<Character, TreeNode> children;
        public TreeNode link;
        public Set<String> sameSuffix;
        public TreeNode parent;

        public Set<StringBuilder> sameSuffixBuilder;
        public TreeNode() {
            this.start = -1;
            this.end = -1;
            len = 0;

        }

        /*public TreeNode(StringBuilder sb, int start, int end, int len){
            this.sb = sb;
            this.start = start;
            this.end= end;
            this.len=len;
        }*/

        public TreeNode(StringBuilder sb, int start, int end, TreeNode parent) {
            this.sb = sb;
            this.start = start;
            this.end = end;
            this.len = end - start + 1;
            this.parent = parent;
        }

        public String getString() {
            if (this.start == -1)
                return null;
            if (end == sb.length() - 1) {
                return sb.substring(start);
            }
            //System.out.println("ToString: " + this + ", sb: " + sb);
            return sb.substring(start, end + 1);
        }

        public String toString() {
            return "[" + start + ", " +
                    end + "]";
        }

        public Set<String> getMatched() {
            System.out.println("Searched");
            Set<String> result = new HashSet<String>();
            if (sameSuffix != null)
                result.addAll(sameSuffix);
            result.add(sb.toString());
            if (children != null) {
                for (TreeNode tn : children.values()) {
                    result.addAll(tn.getMatched());
                }
            }
            return result;
        }

        public Set<StringBuilder> getMatchedSB(){
            Set<StringBuilder> result = new HashSet<StringBuilder>();
            if (sameSuffixBuilder != null)
                result.addAll(sameSuffixBuilder);
            result.add(sb);
            if (children != null) {
                for (TreeNode tn : children.values()) {
                    result.addAll(tn.getMatchedSB());
                }
            }
            return result;
        }
    }

    private class State {
        TreeNode active;
        char value = empty;
        int activeLen = 0;
    }

    public Set<String> getMatched(String pattern) {
        if (pattern == null || pattern.isEmpty())
            return null;
        int len = pattern.length();
        int matched = 0;
        Character first = pattern.charAt(0);
        TreeNode tn = root.children.get(first);

        while (tn != null && tn.len < (len - matched)) {
            for (int i = 0; i < tn.len; i++) {
                if (tn.sb.charAt(tn.start + i) != pattern.charAt(matched + i))
                    return null;
            }
            matched += tn.len;
            tn = tn.children == null ? null : tn.children.get(pattern.charAt(matched));
        }
        if (tn == null)
            return null;
        else {
            int remaining = len - matched;
            if (remaining > 0) {
                for (int i = 0; i < remaining; i++) {
                    if (tn.sb.charAt(tn.start + i) != pattern.charAt(matched + i)) {
                        return null;
                    }
                }
                //if go to here means that tn can match the pattern
                return tn.getMatched();
            } else {
                return tn.getMatched();
            }
        }
    }

    public Set<StringBuilder> getMatchedSB(String pattern) {
        if (pattern == null || pattern.isEmpty())
            return null;
        int len = pattern.length();
        int matched = 0;
        char first = pattern.charAt(0);
        TreeNode tn = root.children.get(first);

        while (tn != null && tn.len < (len - matched)) {
            for (int i = 0; i < tn.len; i++) {
                if (tn.sb.charAt(tn.start + i) != pattern.charAt(matched + i))
                    return null;
            }
            matched += tn.len;
            tn = tn.children == null ? null : tn.children.get(pattern.charAt(matched));
        }
        if (tn == null)
            return null;
        else {
            int remaining = len - matched;
            if (remaining > 0) {
                for (int i = 0; i < remaining; i++) {
                    if (tn.sb.charAt(tn.start + i) != pattern.charAt(matched + i)) {
                        return null;
                    }
                }
                //if go to here means that tn can match the pattern
                return tn.getMatchedSB();
            } else {
                return tn.getMatchedSB();
            }
        }
    }

    public Set<Integer> getMatchResult(String pattern){
        Set<StringBuilder> sbs = getMatchedSB(pattern);
        Set<Integer> result = new HashSet<Integer>();
        if(sbs != null){
            for(StringBuilder sb:sbs){
                result.add(lists.get(sb));
            }
        }
        return result;
    }

    public void buildSuffixTree(String value, int id){
        StringBuilder sb = buildSuffixTree(value + "^");
        lists.put(sb, id);
        patternTpSB.put(value, sb);
    }

    public StringBuilder buildSuffixTree(String value) {
        StringBuilder sb = new StringBuilder();
        int len = value.length();
        List<TreeNode> leaves = new LinkedList<TreeNode>();

        State state = new State();
        state.active = root;
        int remaining = 1;
        for (int i = 0; i < len; i++) {
            char wd = value.charAt(i);
            for (TreeNode leaf : leaves) {
                if (leaf.sb == sb) {
                    leaf.end++;
                    leaf.len++;
                }
            }

            sb.append(wd);

            //add new
            TreeNode tn;
            char cat;
            if (state.value == empty) {
                if (state.active == root) {
                    tn = state.active.children.get(wd);
                    if (tn == null) {
                        tn = new TreeNode(sb, i, i, state.active);
                        state.active.children.put(wd, tn);
                        leaves.add(tn);
                    } else {
                        //found existed first time
                        state.value = wd;
                        state.activeLen++;
                        if (tn.len == state.activeLen) {
                            state.active = tn;
                            state.value = empty;
                            state.activeLen = 0;
                        }
                        remaining++;
                       // System.out.println("Add 1 Remaining: " + remaining + " ,char: " + wd);
                       // System.out.println("Active-iterate: (" + wd + "): [" + (state.active.sb == null ? "" : state.active.sb.toString()) + "]--" + state.active + ", Value: " + state.value + ", ActiveLen: " + state.activeLen + ", Remaining: " + remaining);
                    }
                    continue;
                }
            } else {
                tn = state.active.children.get(state.value);
                cat = tn.sb.charAt(tn.start + state.activeLen);
                if (cat == wd) {
                    state.activeLen++;
                    if (tn.len == state.activeLen) {
                        if(sb != state.active.sb){
                            if(state.active.sameSuffixBuilder==null)
                                state.active.sameSuffixBuilder = new HashSet<StringBuilder>();
                            state.active.sameSuffixBuilder.add(sb);
                        }
                        state.active = tn;
                        state.value = empty;
                        state.activeLen = 0;



                    }
                    remaining++;
                    //System.out.println("Add 2 Remaining: " + remaining + ", Char: " + cat);
                   // System.out.println("Active-iterate: (" + wd + "): [" + (state.active.sb == null ? "" : state.active.sb.toString()) + "]--" + state.active + ", Value: " + state.value + ", ActiveLen: " + state.activeLen + ", Remaining: " + remaining);
                    continue;
                }
            }
            TreeNode newInserted = null;
            boolean isdone = false;
            //need split here
            while (remaining > 1) {
                if (state.value == empty) {
                    if (state.active.children != null) {
                        tn = state.active.children.get(wd);
                        if (tn != null) {
                            isdone = true;
                            remaining++;
                            state.value = wd;
                            state.activeLen++;
                            if(tn.len == state.activeLen){
                                state.active = tn;
                                state.value = empty;
                                state.activeLen = 0;
                            }
                            break;
                        }
                    }
                    TreeNode txn = new TreeNode(sb, i, i, state.active);
                    if (state.active.children == null) {
                        leaves.remove(state.active);
                        state.active.children = new HashMap<Character, TreeNode>();
                    }
                    state.active.children.put(wd, txn);
                    leaves.add(txn);
                    if (newInserted != null) {
                        if (newInserted.link == null) {
                            newInserted.link = state.active;
                           // System.out.println("Set Link from: " + newInserted + " to: " + state.active);
                        }
                    }
                    remaining--;


                    if (state.active.link != null) {
                        state.active = state.active.link;
                        //System.out.println("Reset toLink-1: [" + (state.active.sb == null ? "" : state.active.sb.toString()) + "]--" + state.active + ", Value: " + state.value + ", ActiveLen: " + state.activeLen);

                    } else {
                        if (state.active == root) {
                            state.activeLen--;
                            state.value = sb.charAt(sb.length() - remaining);
                        } else {
                            state.active = root;
                                state.value = sb.charAt(sb.length() - remaining);
                                state.activeLen = remaining - 1;
                            TreeNode nxt = state.active.children.get(state.value);
                            if (nxt != null) {
                                if (nxt.len == state.activeLen) {
                                    if(sb != state.active.sb){
                                        if(state.active.sameSuffixBuilder==null)
                                            state.active.sameSuffixBuilder = new HashSet<StringBuilder>();
                                        state.active.sameSuffixBuilder.add(sb);
                                    }
                                    state.active = nxt;
                                    state.value = empty;
                                    state.activeLen = 0;
                                }
                            }
                        }
                    }
                } else {
                    tn = state.active.children.get(state.value);
                    if (tn != null) {
                        cat = tn.sb.charAt(tn.start + state.activeLen);
                        if (cat == wd) {
                            isdone = true;
                            remaining++;
                            //state.value = wd;
                            state.activeLen++;
                          //  System.out.println("Done process with: " + wd);

                            if(tn.len == state.activeLen){
                                if(sb != state.active.sb){
                                    if(state.active.sameSuffixBuilder==null)
                                        state.active.sameSuffixBuilder = new HashSet<StringBuilder>();
                                    state.active.sameSuffixBuilder.add(sb);
                                }
                                state.active = tn;
                                state.value = empty;
                                state.activeLen = 0;
                            }
                            break;
                        }

                        if (tn.end - tn.start + 1 > state.activeLen) {//do split
                            TreeNode internal = new TreeNode(tn.sb, tn.start, tn.start + state.activeLen - 1, tn.parent);
                            state.active.children.put(state.value, internal);
                            internal.children = new HashMap<Character, TreeNode>();
                            tn.start = internal.end + 1;
                           // System.out.println("Split at: [" + (internal.sb == null ? "" : internal.sb.toString()) + "]" + internal.end);
                            tn.len = tn.end - tn.start + 1;
                            tn.parent = internal;
                            internal.children.put(cat, tn);
                            tn = internal;

                        } else {//no need to split, direct add
                           // System.out.println("No Split, direct add!");
                            if (tn.children == null) {
                                leaves.remove(tn);
                                tn.children = new HashMap<Character, TreeNode>();
                            }
                        }
                        TreeNode ct = new TreeNode(sb, i, i, tn);
                        tn.children.put(wd, ct);
                        remaining--;
                        //System.out.println("Split Remaining: " + remaining);
                        // leaves.add(newsplit);
                        leaves.add(ct);
                        if (newInserted != null) {
                            if (newInserted.link == null) {
                                newInserted.link = tn;
                               // System.out.println("Set Link from: " + newInserted + " to: " + tn);
                            }
                        }
                        newInserted = tn;
                        if (state.active.link != null) {
                            state.active = state.active.link;
                            //System.out.println("Reset toLink-2: [" + (state.active.sb == null ? "" : state.active.sb.toString()) + "]--" + state.active + ", Value: " + state.value + ", ActiveLen: " + state.activeLen);

                        } else {
                            if (state.active == root) {
                                state.activeLen--;
                                state.value = sb.charAt(sb.length() - remaining);
                            } else {
                                state.active = root;
                                //if (state.activeLen != remaining - 1) {
                                    state.activeLen = remaining - 1;
                                    state.value = sb.charAt(sb.length() - remaining);
                               // }
                            }
                        }
                    }
                }
                tn = state.active.children.get(state.value);
                if (tn != null) {
                    while (tn.len < state.activeLen) {
                        int count = state.activeLen - tn.len;
                        char ca  = sb.charAt(sb.length() - count-1);
                        TreeNode next = tn.children.get(ca);
                        if (next != null) {
                            if(sb != state.active.sb){
                                if(state.active.sameSuffixBuilder==null)
                                    state.active.sameSuffixBuilder = new HashSet<StringBuilder>();
                                state.active.sameSuffixBuilder.add(sb);
                            }
                            state.active = tn;
                            state.value = ca;
                            state.activeLen = count;
                        } else {
                            System.out.println("Error occurred: " + value);
                        }
                        tn = next;
                    }

                    if (tn.len == state.activeLen) {
                        if(sb != state.active.sb){
                            if(state.active.sameSuffixBuilder==null)
                                state.active.sameSuffixBuilder = new HashSet<StringBuilder>();
                            state.active.sameSuffixBuilder.add(sb);
                        }
                        state.active = tn;
                        state.value = empty;
                        state.activeLen = 0;
                    }
                }
              //  System.out.println("Active-internal: [" + (state.active.sb == null ? "" : state.active.sb.toString()) + "]--" + state.active + ", Value: " + state.value + ", ActiveLen: " + state.activeLen + ", Remaining: " + remaining);
            }

            if (!isdone) {
                tn = state.active.children.get(state.value);
                if (tn == null) {
                   // System.out.println("Char: " + wd + " not exist! create new node");
                    tn = new TreeNode(sb, i, i, state.active);
                    state.active.children.put(wd, tn);
                    leaves.add(tn);
                    state.value = empty;
                } else {
                    state.activeLen++;
                    // state.
                    remaining++;
                    if (tn.len == state.activeLen) {
                        state.active = tn;
                        state.value = empty;
                        state.activeLen = 0;
                    }
                }
            }
          //  System.out.println("Active-iterate: (" + wd + "): [" + (state.active.sb == null ? "" : state.active.sb.toString()) + "]--" + state.active + ", Value: " + state.value + ", ActiveLen: " + state.activeLen + ", Remaining: " + remaining);
        }

        // processRemaining(sb,state, remaining, len-1, leaves);
        //process remaining when handling more than 1 text
        if (remaining > 2) {
            TreeNode node;
            if (state.value != empty)
                node = state.active.children.get(state.value);
            else {
                node = state.active;
            }
            if(sb != node.sb){
                if(state.active.sameSuffixBuilder==null)
                    state.active.sameSuffixBuilder = new HashSet<StringBuilder>();
                state.active.sameSuffixBuilder.add(sb);
            }
            //processRemaining(sb, state, remaining, sb.length()-1, leaves);
           /* TreeNode node;
            if (state.value != empty)
                node = state.active.children.get(state.value);
            else {
                node = state.active;
            }

            if(remaining != state.activeLen + 1 && remaining != (state.active.len + state.activeLen + 2)){
                System.out.println("Met Not only 1 left: " + sb + "(Remaining: " + remaining + ", state.len: " + state.active.len + ", state.activelen: " + state.activeLen + ")");
            }

            if(node.sameSuffixBuilder == null){
                TreeNode tn = node;
                while(tn != root && sbToLastNode.containsKey(tn.getString())){
                    TreeNode next = sbToLastNode.get(node.getString());
                    if(tn != next){
                        tn = next;
                        if(node.sameSuffixBuilder != null)
                            break;
                    }else
                        break;
                }

                if(tn.sameSuffixBuilder == null){
                    //not found, create and update old one
                    node.sameSuffixBuilder = new HashSet<StringBuilder>();
                    sbToLastNode.put(node.getString(), node);
                }else{
                    node = tn;
                }
            }
            node.sameSuffixBuilder.add(sb);
            sbToLastNode.put(node.getString(), node);
           // System.out.println("link add: " + value);
        }else{
            TreeNode node;
            if (state.value != empty)
                node = state.active.children.get(state.value);
            else {
                node = state.active;
            }
            sbToLastNode.put(node.getString(), node);
           // System.out.println("Direct add: " + value);*/
        }
       // lists.add(sb);
        return sb;
    }

    public void print() {
        for (TreeNode tn : root.children.values()) {
            print(0, tn);
        }
    }

    public void print(int indent, TreeNode tn) {
        for (int i = 0; i < indent; i++) {
            System.out.print("    ");
        }
        System.out.println(tn.getString() + (tn.link == null ? "" : tn.link));
        if (tn.children != null) {
            for (TreeNode t : tn.children.values()) {
                print(indent + 1, t);
            }
        }
    }

    public static void main(String[] args) {
        MySuffixTree mst = new MySuffixTree();

        /*mst.buildSuffixTree("abcabxabcd^");
        mst.buildSuffixTree("abc^");
        mst.buildSuffixTree("abxab^");
        mst.buildSuffixTree("xabcd^");
        mst.buildSuffixTree("ivy^");*/
        //mst.buildSuffixTree("abcdexyabcde^");
        // mst.buildSuffixTree("aaaaaaaab^");
        //mst.buildSuffixTree("mississippi^");
        mst.buildSuffixTree("20140112175858upload^");
        mst.buildSuffixTree("olddata1232^");
        mst.buildSuffixTree("img20140225161453^");
        mst.buildSuffixTree("olddata1231^");

        mst.buildSuffixTree("baitian2007102220^");
        mst.buildSuffixTree("4225237221e895def2d6o^");
        mst.buildSuffixTree("img20140121182150^");
        mst.buildSuffixTree("olddata1032^");
        mst.buildSuffixTree("olddata1031^");
        mst.buildSuffixTree("img20140206191529^");
        mst.buildSuffixTree("7955032096d29593870ao^");
        mst.buildSuffixTree("img20140220111207^");//

        mst.print();

        Set<String> res = mst.getMatched("img");
        print(res);
    }

    public static void print(Set<String> set) {
        if (set == null) {
            System.out.println("No result!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : set) {
            sb.append(s).append(",");
        }
        System.out.println(sb.toString());
    }
}
