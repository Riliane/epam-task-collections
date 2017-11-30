import java.util.*;

public class MyArrayList<T> extends AbstractList<T> implements List<T> {
    Object[] array;
    int size;

    MyArrayList() {
        array = new Object[10];
        size = 0;
    }
    MyArrayList(int capacity){
        if (capacity<0) {throw new IllegalArgumentException();}
        array = new Object[capacity];
        size=0;
    }
    MyArrayList(Collection<? extends T> c){
        array = new Object[0];
        size = 0;
        addAll(c);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    public T get(int i) {
        if (i >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (T) array[i];
    }

    @Override
    public T set(int index, T element) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T old = (T)array[index];
        array[index]=element;
        return old;
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (size <= a.length){
            System.arraycopy(array,0, a, 0, size);
            if (size < a.length){a[size]=null;}
            return a;
        }
        else return (T1[])Arrays.copyOf(array, size, a.getClass());
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {throw new IndexOutOfBoundsException();}

            if (size + 1 > array.length) {
                expand(size + 1);
            }
            int amountRight = size - index;
            if (amountRight > 0) {
                System.arraycopy(array, index, array, index + 1, amountRight);
            }
            array[index] = element;
            size++;
    }
    @Override
    public boolean add(T element){
        add(size, element);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Object[] newArray = c.toArray();
        int amount = newArray.length;
        if (amount == 0) {
            return false;
        }
        if (size + amount < array.length) {
            expand(size + amount);
        }
        System.arraycopy(newArray, 0, array, size, amount);
        size += amount;
        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> c) {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException();
        }
        Object[] newArray = c.toArray();
        int amount = newArray.length;
        if (amount == 0) {
            return false;
        }
        if (size + amount > array.length) {
            expand(size + amount);
        }
        int amountRight = size - i;
        if (amountRight > 0) {
            System.arraycopy(array, i, array, i + amount, amountRight);
        }
        System.arraycopy(newArray, 0, array, size, amount);
        size += amount;
        return true;
    }

    @Override
    public T remove(int index) {
        T old = (T)array[index];
        int amountLeft = size - index - 1;
        if (amountLeft > 0) {
            System.arraycopy(array, index+1, array, index, amountLeft);
        }
        array[size-1] = null;
        size--;
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index==-1){ return false; }
        remove(index);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removed = false;
        for (int i = 0; i<size; i++){
            if (c.contains(array[i])){
                remove(i);
                i--;
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean removed = false;
        for (int i = 0; i<size; i++){
            if (!c.contains(array[i])){
                remove(i);
                i--;
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public void clear() {
        array = new Object[10];
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(array[i])){
                return i;
            }
        }
        return -1;
    }
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size-1; i >=0; i--) {
            if (o.equals(array[i])){
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return (indexOf(o)!=-1);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c){
            if (indexOf(obj)==-1){return false;}
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==this) {return true;}
        if (!(obj instanceof List)){return false;}
        if (this.size!=((List) obj).size()){return false;}
        Iterator iter1 = iterator();
        Iterator iter2 = ((List) obj).iterator();
        while (iter1.hasNext()&&iter2.hasNext()){
            if (!iter1.next().equals(iter2.next())){return false;}
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iter();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new Iter();
    }

    private void expand(int capacity) {
        array = Arrays.copyOf(array, capacity);
    }

    private void expand() {
        expand(size + 1);
    }

    private class Iter implements ListIterator<T>{
        int index = 0;
        int last = -1;

        @Override
        public boolean hasNext() {
            return (index != size);
        }
        public boolean hasPrevious(){
            return (index != 0);
        }

        @Override
        public T next() {
            if (index >= size){throw new NoSuchElementException();}
            last = index;
            index++;
            return (T)array[last];
        }

        @Override
        public T previous() {
            if (index<=0){throw new NoSuchElementException();}
            index--;
            last = index;
            return (T)array[last];
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index-1;
        }

        @Override
        public void remove() {
            if (last==-1){throw new IllegalStateException();}
            MyArrayList.this.remove(last);
            index = last;
            last=-1;
        }

        @Override
        public void set(T t) {
            if (last==-1){throw new IllegalStateException();}
            MyArrayList.this.set(last, t);
        }

        @Override
        public void add(T t) {
            MyArrayList.this.add(index, t);
            index++;
            last=-1;
        }
    }
}
