import java.util.*;

public class MyBinaryTree<T> implements Set<T> {

    TreeNode root;
    int size;

    MyBinaryTree(){
        root = null; size = 0;}

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return find(root, (T)o)!=null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iter<T>(root);
    }

    public Iterator<T> prefixIterator() {
        return new PrefixIter<T>(root);
    }

    public Iterator<T> postfixIterator() {
        return new PostfixIter<T>(root);
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Iterator iterator = iterator();
        int i=0;
        while (iterator.hasNext()){
            array[i]=iterator.next();
            i++;
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (size<=a.length){
            Iterator iterator = iterator();
            int i=0;
            while (iterator.hasNext()){
                a[i]=(T1)iterator.next();
                i++;
            }
            if (size < a.length) {
                a[size] = null;
            }
            return a;
        } else {
            return (T1[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
    }

    @Override
    public boolean add(T t) {
        if (!(t instanceof Comparable)){ return false;}//here an exception needs to be thrown
        if (root == null){
            TreeNode node = new TreeNode(t);
            root = node;
            size++;
            return true;}
        else{
            if (appendToNode(root, t)){
                size++;
                return true;
            }
            return false;
        }
    }

    private boolean appendToNode (TreeNode node, T element){
        if (element.equals(node.element)){return false;}
        if (((Comparable)element).compareTo(node.element)>0) {//node.element is smaller, append to right
            if (!node.hasRightChild()) {
                node.appendRight(new TreeNode(element));
                return true;
            }
            else {return appendToNode(node.rightChild, element);}
        }
        else {
            if (!node.hasLeftChild()) {
                node.appendLeft(new TreeNode(element));
                return true;
            }
            else {return appendToNode(node.leftChild, element);}
        }
    }

    private TreeNode find(TreeNode node, T element){
        if (element.equals(node.element)){return node;}
        if (((Comparable)element).compareTo(node.element)>0) {//node.element is greater, search to right
            if (!node.hasRightChild()) {
                return null;
            }
            else {return find(node.rightChild, element);}
        }
        else {
            if (!node.hasLeftChild()) {
                return null;
            }
            else {return find(node.leftChild, element);}
        }
    }

    @Override
    public boolean remove(Object o) {
        TreeNode removed = find(root,(T)o);
        if (removed == null){return false;}
        removeNode(removed);
        size--;
        return true;
    }

    private void removeNode(TreeNode removed){
        if (!removed.hasRightChild()){
            if (!removed.hasLeftChild()){
                if (removed.parent == null){root = null;}//no parents, no child, only node
                else if (removed.parent.leftChild==removed){removed.parent.leftChild = null;}
                else {removed.parent.rightChild = null;}
            }
            else {//only left child
                if (removed.parent == null){root = removed.leftChild;} //remove root with one child
                else if (removed.parent.leftChild==removed){removed.parent.leftChild = removed.leftChild;}
                else {removed.parent.rightChild = removed.leftChild;}}
        }
        else if (!removed.hasLeftChild()){//only right child
            if (removed.parent == null){root = removed.rightChild;} //remove root with one child
            else if (removed.parent.leftChild==removed){removed.parent.leftChild = removed.rightChild;}
            else {removed.parent.rightChild = removed.rightChild;}
        }
        else if (!removed.rightChild.hasLeftChild()){//has both children, right has no left, pull right up and append left to it
            if (removed.parent == null){root = removed.rightChild;}
            else if (removed.parent.leftChild==removed){removed.parent.leftChild = removed.rightChild;}
            else {removed.parent.rightChild = removed.rightChild;}
            removed.rightChild.parent = removed.parent;
            removed.rightChild.leftChild = removed.leftChild;
            removed.leftChild.parent = removed.rightChild;
        }
        else{//right has left, find leftmost of right, move the value up, delete leftmost of right
            TreeNode current = removed.rightChild;
            while (current.hasLeftChild()){current = current.leftChild;}
            removed.element = current.element;
            removeNode(current);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c){
            if (!contains(o)){return false;}
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean added = false;
        for (Object o : c){
            if (add((T)o)){added = true;} //if at least one element was added aka wasn't equal to any present ones
        }
        return added;
    }

    @Override
    public boolean removeAll(Collection<?> c) {//an object in the tree is unique.
        boolean removed = false;//one object in the collection has to be removed only once from the tree
        for (Object element : c){
            if (remove(element)){removed = true;}
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {//since an object in the tree is unique,
        MyBinaryTree<T> newTree = new MyBinaryTree<>();//it's easier to make a new one of objects from the collection
        for (Object element : c){
            if (contains(element)){
                newTree.add((T)element);
            }
        }
        root = newTree.root;
        return false;
    }

    @Override
    public void clear() {
        root = null;
    }

    private class TreeNode{
        T element;
        TreeNode parent;
        TreeNode leftChild;
        TreeNode rightChild;
        TreeNode(T t){
            element = t;
        }
        boolean hasLeftChild(){return leftChild != null;}
        boolean hasRightChild(){return rightChild != null;}
        void appendLeft(TreeNode node){
            leftChild = node;
            node.parent = this;
        }
        void appendRight(TreeNode node){
            rightChild = node;
            node.parent = this;
        }
    }

    private class Iter<T> implements Iterator<T>{
        TreeNode current;
        Iter(TreeNode root){
            current = root;
            if (root != null){
                current = leftmost(root);
            }
        }

        private TreeNode leftmost (TreeNode from){
            while (from.hasLeftChild()){
                from = from.leftChild;
            }
            return from;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {throw new NoSuchElementException();}
            TreeNode node = current;
            if (current.hasRightChild()){//if there is a right child, go to leftmost child of it
                current = current.rightChild;
                current = leftmost(current);

            } else {//otherwise it has no children, go to parent
                while (current.parent!=null && current.parent.leftChild!=current){//until come from the left or reach the root
                current = current.parent;
                }
                current = current.parent;//if root is reached from the right, it's the end
            }
            return (T)node.element;
        }
    }
    private class PrefixIter<T> implements Iterator<T>{
        TreeNode current;
        PrefixIter(TreeNode root){
            current = root;}

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {throw new NoSuchElementException();}
            TreeNode node = current;
            if (current.hasLeftChild()){
                current = current.leftChild;
            }
            else if (current.hasRightChild()){
                current = current.rightChild;
            }
            else if (current.parent != null && current.parent.leftChild == current && current.parent.hasRightChild()){
                current = current.parent.rightChild;
            }
            else{
                while (current.parent!=null && !(current.parent.leftChild==current && current.parent.hasRightChild())){
                    current = current.parent;
                }
                if (current.parent != null) {current = current.parent.rightChild;}
                else {current = null;}
            }
            return (T)node.element;
        }
    }
    private class PostfixIter<T> implements Iterator<T>{
        TreeNode current;
        PostfixIter(TreeNode root){
            current = leftmost(root);
            while (current.hasRightChild()){
                current = current.rightChild;
                current = leftmost(current);
            }
        }
        private TreeNode leftmost (TreeNode from){
            while (from.hasLeftChild()){
                from = from.leftChild;
            }
            return from;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }
        @Override
        public T next() {
            if (!hasNext()) {throw new NoSuchElementException();}
            TreeNode node = current;
            if (current.parent==null){ current = null;}
            else if (current.parent.leftChild == current){
                if (current.parent.hasRightChild()){
                    current = current.parent;
                    while (current.hasRightChild()){
                        current = current.rightChild;
                        current = leftmost(current);
                    }
                }
                else {current = current.parent;}
            }
            else {current = current.parent;}
            return (T)node.element;
        }

        }

    }
