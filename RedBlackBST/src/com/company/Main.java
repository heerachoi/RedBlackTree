package com.company;

import java.util.*;
import java.io.*;

/**
 * RedBlackBST class
 */
class RedBlackBST<Key extends Comparable<Key>, Value> {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;
    Node root;

    public class Node {
        Key key;
        Value val;
        Node left, right;
        boolean color;
        int N;

        public Node(Key key, Value val, boolean color, int N) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.N = N;
        }
    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        return (x.color == RED);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.N;
    }

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public RedBlackBST() {
        this.root = null;
    }

    public void insert(Key key, Value val) {
        root = insert(root, key, val);
        root.color = BLACK;
    }

    private Node insert(Node node, Key key, Value val) {
        if(node == null) {
            return new Node(key, val, RED, 1);
        }
        int cmp = key.compareTo(node.key);
        if(cmp>0) {
            node.right = insert(node.right, key, val);
        }
        else if(cmp<0) {
            node.left = insert(node.left, key, val);
        }
        else {
            node.val = val;
        }
        if(isRed(node.right)) {
            node = rotateLeft(node);
        }
        if(isRed(node.left) && isRed(node.left.left)) {
            node = splitNode(node);
        }
        node.N = 1 + size(node.right) + size(node.left);
        return node;
    }

    private Node splitNode(Node node){
        node = rotateRight(node);
        flipColors(node);
        return node;
    }

    public Value get(Key key){
        Node x = root;
        while (x!=null){
            int compare = key.compareTo(x.key);
            if (compare>0) {
                x = x.right;
            }
            else if (compare<0) {
                x = x.left;
            }
            else {
                return x.val;
            }
        }
        return null;
    }

    private Node deleteMin(Node node){
        if(node.left == null) {
            return null;
        }
        if(!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return fixUp(node);
    }

    public void delete(Key key) {
        if (!contains(key))  {
            return;
        }
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = delete(root, key);
        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    private Node delete(Node node, Key key){
        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            node.left = delete(node.left, key);
        }
        else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (key.compareTo(node.key) == 0 && (node.right == null)) {
                return null;
            }
            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            if (key.compareTo(node.key) == 0) {
                node.val = get(deleteMin(node.right).key); node.key = deleteMin(node.right).key;
                node.right = deleteMin(node.right);
                matchFound++;
            }
            else {
                node.right = delete(node.right, key);
            }
        }
        node.N = 1 + size(node.right) + size(node.left);
        return fixUp(node);
    }

    private Node fixUp (Node h) {
        if (isRed(h.right)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }
        return h;
    }

    public Value search(Key key) {
        Node temp = root;
        while (temp != null) {
            int cmp = key.compareTo(temp.key);
            if (cmp == 0) {
                return temp.val;
            }
            else if (cmp < 0) {
                temp = temp.left;
            }
            else if (cmp > 0) {
                temp = temp.right;
            }
        }
        return null;
    }
    public boolean contains(Key key) {
        return (search(key) != null);
    }

    public int height() { return height(root); }
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    int tempK = 0;
    int keyFound = 0;
    Key foundNode;
    public Key getValByRank(int k) {
        tempK = -1;
        keyFound = 0;
        foundNode = getVal(root, k);
        return foundNode;
    }
    int noMatch =0;
    Node returnNode = null;
    private Key getVal(Node node, int k) {
        noMatch = 0;
        if (node == null) {
            return null;
        }
        getVal(node.left, k);
        tempK++;
        if (tempK == k) {
            keyFound++;
            returnNode = node;
        }
        getVal(node.right, k);
        if (keyFound == 1) {
            noMatch++;
            return returnNode.key;
        }
        return null;
    }

    int count = -1;
    int matchFound = 0;
    int checkValidKey = 0;
    int savedLargerNode = 0;
    int countNode = 0;
    int saveCountNode = 0;
    int entered= 0;
    Node lessNode = null;
    public int rank(Key key) {
        int result = 0;
        count =-1;
        matchFound = 0;
        checkValidKey = 0;
        savedLargerNode = 0;
        countNode = -1;
        saveCountNode = 0;
        lessNode = null;
        entered = 0;
        result = rank(root,key);
        return result;
    }

    private int rank(Node node, Key key) {
        if (node == null) {
            return 0;
        }
        rank(node.left, key);

        int checkKey = key.compareTo(node.key);
        if (count >= root.N) {
            return 0;
        }
        if (matchFound == 0) {
            checkValidKey++;
            countNode++;
            count++;
        }
        if (checkKey == 0) {
            matchFound++;
            return count;
        }
        if (checkKey == -1) {
            savedLargerNode ++;
            if (savedLargerNode == 1 ) {
                lessNode = node;
                saveCountNode = countNode;
                entered++;
                return saveCountNode;
            }
        }
        rank(node.right, key);

        if (matchFound == 1) {
            return count;
        }
        if (entered == 1) {
            return saveCountNode;
        }
        if ((checkValidKey == (root.N)) && (checkKey == 1)) {
            return root.N;
        }
        return count;
    }

    public List<Key> getElements(int a, int b){
        start = -1;
        list = new LinkedList<>();

        if (root == null) {
            return list;
        }
        if (a >= root.N || b >= root.N || a < 0 || b < 0) {
            return list;
        }
        else {
            list = getElements(root, a, b);
            return list;
        }
    }

    int start = -1;
    LinkedList <Key> list = new LinkedList<>();
    public LinkedList<Key> getElements(Node node, int a, int b) {
        if (node == null) {
            return list;
        }
        if ((a == 0) && (b == 0)) {
            return list;
        }
        getElements(node.left, a, b);
        start++;
        if (start >= a && start <= b) {
            list.add(node.key);
            if (start == b) {
                return list;
            }
        }
        getElements(node.right, a, b);
        return list;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.N = h.N;
        h.N = size(h.left) + size(h.right) + 1;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.N = h.N;
        h.N = size(h.left) + size(h.right) + 1;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (isRed(h.right))                      h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);

        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    public static void main(String[] args) {

        Scanner readerTest = null;

        try {
            readerTest = new Scanner(new File("sampletest.txt"));
        } catch (IOException e) {
            System.out.println("Reading Oops");
        }

        RedBlackBST<Integer, Integer> test = new RedBlackBST<>();

        while(readerTest.hasNextLine()){
            String[] input  =readerTest.nextLine().split(" ");

            for(String x: input){
                System.out.print(x+" ");
            }

            switch (input[0]){
                case "insert":
                    Integer key = Integer.parseInt(input[1]);
                    Integer val = Integer.parseInt(input[2]);
                    test.insert(key,val);
                    printTree(test.root);
                    break;

                case "delete":
                    Integer key1 = Integer.parseInt(input[1]);
                    test.delete(key1);
                    printTree(test.root);
                    break;

                case "search":
                    Integer key2 = Integer.parseInt(input[1]);
                    Integer ans2 = test.search(key2);
                    System.out.println(ans2);
                    System.out.println();
                    break;

                case "getval":
                    Integer key3 = Integer.parseInt(input[1]);
                    Integer ans21 = test.getValByRank(key3);
                    break;

                case "rank":
                    Integer key4 = Integer.parseInt(input[1]);
                    Object ans22 = test.rank(key4);
                    break;

                case "getelement":
                    Integer low = Integer.parseInt(input[1]);
                    Integer high = Integer.parseInt(input[2]);
                    List<Integer> testList = test.getElements(low,high);
                    break;

                default:
                    System.out.println("Error, Invalid test instruction! "+input[0]);
            }
        }

    }

    /*************************************************************************
     *  Prints the tree
     *************************************************************************/
    public static void printTree(RedBlackBST.Node node){

        if (node == null){
            return;
        }
        printTree(node.left);
        System.out.print(((node.color == true)? "Color: Red; ":"Color: Black; ") + "Key: " + node.key + " Size:: " + node.N+ "\n");
        printTree(node.right);
    }
}