import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Main {
    public static void main(String[] args) {
        String[] array = {"the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"};
        List<String> aList = new MyArrayList<>();
        List<String> lList = new MyLinkedList<>();
        MyBinaryTree<String> tree = new MyBinaryTree<>();
        for (String s : array){
            aList.add(s);
            lList.add(s);
            tree.add(s);
        }
        output(aList, lList, tree);

        aList.remove("lazy");
        aList.add(7, "fluffy");
        aList.set(2, "red");

        System.out.println(lList.remove("quick"));
        lList.add(1, "tricky");
        lList.set(8, "bunny");

        System.out.println(tree.remove("fox"));
        tree.add("vixen");

        output(aList, lList, tree);

        System.out.println(aList.indexOf("the"));
        System.out.println(lList.lastIndexOf("the"));

        aList.removeAll(tree);

        ListIterator iterator = lList.listIterator();
        while (iterator.hasNext()){
            if (iterator.next().equals("the")){
                iterator.add("same");
            }
        }

        output(aList, lList, tree);

        Object[] arr = tree.toArray(new Object[3]);
        for (Object o: arr){
            System.out.print(o + " ");
        }
    }
    public static void output(List aList, List lList, MyBinaryTree tree){
        System.out.println("Array list:");
        Iterator aIterator = aList.iterator();
        while (aIterator.hasNext()){
            System.out.print(aIterator.next() + " ");
        }
        System.out.println("\nSize:" + aList.size());
        System.out.println("Linked list:");
        Iterator lIterator = lList.iterator();
        while (lIterator.hasNext()){
            System.out.print(lIterator.next() + " ");
        }
        System.out.println("\nSize:" + lList.size());
        System.out.println("Tree");
        Iterator tIterator = tree.postfixIterator();
        while (tIterator.hasNext()){
            System.out.print(tIterator.next()+ " ");
        }
        System.out.println("\nSize:" + tree.size());
    }

}
