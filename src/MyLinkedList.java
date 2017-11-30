import java.util.*;
public class MyLinkedList<T> extends AbstractSequentialList<T> implements List<T> {
    ListNode start;
    int size;

    public MyLinkedList() {
        start = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public T get(int i) {
        if (i >= size) {
            throw new IndexOutOfBoundsException();
        }
        ListNode current = start;
        for (int j = 0; j < i; j++) {
            current = current.next;
        }
        return (T) current.obj;
    }

    public T set(int index, T element) {
        if (index >= size || index == 0) {
            throw new IndexOutOfBoundsException();
        }
        ListNode current = start;
        for (int j = 0; j < index; j++) {
            current = current.next;
        }
        T old = (T) current.obj;
        current.obj = element;
        return old;
    }

    public Object[] toArray() {
        ListNode current = start;
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = current.obj;
            current = current.next;
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (size <= a.length) {
            ListNode current = start;
            for (int i = 0; i < size; i++) {
                a[i] = (T1) current.obj;
                current = current.next;
            }
            if (size < a.length) {
                a[size] = null;
            }
            return a;
        } else {
            return (T1[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
    }

    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        ListNode<T> node = new ListNode<>(element);
        if (index == size) {
            add(element);
        }else if (index==0){
            addFirst(element);
        }else {
            ListNode current = start;
            for (int j = 0; j < index; j++) {
                current = current.next;
            }
            node.prev = current.prev;
            node.next = current;
            current.prev.next = node;
            current.prev = node;
            size++;
        }
    }

    @Override
    public boolean add(T element) {
        if (start == null){return addFirst(element);}
        ListNode<T> node = new ListNode<>(element);
        ListNode current = start;
        while (current.next != null) {
            current = current.next;
        }
        current.next = node;
        node.prev = current;
        size++;
        return true;
    }

    public boolean addFirst(T element) {
        ListNode<T> node = new ListNode<>(element);
        node.next=start;
        start = node;
        size++;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Object[] newArray = c.toArray();
        int amount = newArray.length;
        if (amount == 0) {
            return false;
        }
        ListNode current = start;
        if (start == null){
            addFirst((T)newArray[0]);
            current = start;
        }
        else{
        while (current.next != null) {
            current = current.next;
        }
            ListNode<T> node = new ListNode<>((T)newArray[0]);
            current.next = node;
            node.prev = current;
            current = node;
        }
        for (int i=1; i<c.size(); i++) {
            ListNode<T> node = new ListNode<>((T) newArray[i]);
            current.next = node;
            node.prev = current;
            current = node;
        }
        size += amount;
        return true;
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        Object[] newArray = c.toArray();
        int amount = newArray.length;
        if (amount == 0) {
            return false;
        }
        if (index == size) {
            addAll(c);
        } else {
            ListNode current = start;
            if (index == 0){
                addFirst((T)newArray[0]);
            }
            else {
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }
                ListNode<T> node = new ListNode<>((T) newArray[0]);
                node.prev = current.prev;
                node.next = current;
                current.prev.next = node;
                current.prev = node;
            }
            for (Object element : newArray) {
                ListNode<T> node = new ListNode<>((T) element);
                node.prev = current.prev;
                node.next = current;
                current.prev.next = node;
                current.prev = node;
            }
        }
        size += amount;
        return true;
    }

    @Override
    public T remove(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index==0){ return removeFirst();}
        ListNode current = start;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        T old = (T) current.obj;
        if (current.next!=null) {
            current.next.prev = current.prev;
        }
        current.prev.next = current.next;
        size--;
        return old;
    }

    public T removeFirst(){
        T old = (T)start.obj;
        start.next.prev=null;
        start=start.next;
        size--;
        return old;
    }

    @Override
    public boolean remove(Object o) {
        ListNode current = start;
        for (int i = 0; i < size; i++) {
            if (current.obj.equals(o)) {
                if (i==0){removeFirst(); return true;}
                if (current.next!=null) {
                    current.next.prev = current.prev;
                }
                current.prev.next = current.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        ListNode current = start;
        boolean removed = false;
        int sz = size;
        for (int i = 0; i < sz; i++) {
            if (c.contains(current.obj)) {
                if (current == start) {
                    removeFirst();
                } else {
                    if (current.next != null) {
                        current.next.prev = current.prev;
                    }
                    current.prev.next = current.next;
                    size--;
                }
                removed = true;
            }
            current = current.next;
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        ListNode current = start;
        int sz = size;
        boolean removed = false;
        for (int i = 0; i < sz; i++) {
            if (!c.contains(current.obj)) {
                if (current == start) {
                    removeFirst();
                } else {
                    if (current.next != null) {
                        current.next.prev = current.prev;
                    }
                    current.prev.next = current.next;
                    size--;
                }
                removed = true;
            }
            current = current.next;
        }
        return removed;
    }

    @Override
    public void clear() {
        start = null;
    }

    public int indexOf(Object o) {
        ListNode current = start;
        for (int i = 0; i < size; i++) {
            if (o.equals(current.obj)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        ListNode current = start;
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(current.obj)) {
                index = i;
            }
            current = current.next;
        }
        return index;
    }

    @Override
    public boolean contains(Object o) {
        return (indexOf(o) != -1);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (indexOf(obj) == -1) {
                return false;
            }
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

    @Override
    public ListIterator<T> listIterator(int index) {
        return new Iter(index);
    }

    private class ListNode<T> {
        ListNode prev;
        ListNode next;
        T obj;

        ListNode(T object) {
            obj = object;
        }

        ListNode(T object, ListNode p, ListNode n) {
            obj = object;
            prev = p;
            next = n;
        }
    }

    private class Iter implements ListIterator<T> {
        int index = 0;
        /*int last = -1;*/
        ListNode current;
        ListNode last;
        ListNode end;

        Iter() {
            current = start;
        }

        Iter(int ind){
            if (ind < 0 || ind > size){throw new IndexOutOfBoundsException();}
            current = start;
            for (int i = 0; i < ind; i++){
                if (current.next == null){end = current;}
                current = current.next;
            }

        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        public boolean hasPrevious() {
            return (current.prev != null);
        }

        @Override
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            last = current;
            if (current.next==null){
                end = current;
            }
            current = current.next;
            index++;
            return (T) last.obj;
        }

        @Override
        public T previous() {
            if (current == null) {
            current=end;
            }
            if (current.prev == null) {
                throw new NoSuchElementException();
            }

            current = current.prev;
            index--;
            last = current;
            return (T) last.obj;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }
            if (last == start){
                removeFirst();
            }
            else {

                if (last.next == null) {
                    end = last;
                }//if the last, save it
                else {
                    last.next.prev = last.prev;
                } //change prev of next only if not last
                last.prev.next = last.next;
                current = last.next;
                last = null;
            }
        }

        @Override
        public void set(T t) {
            if (last == null) {
                throw new IllegalStateException();
            }
            last.obj = t;
        }

        @Override
        public void add(T t) {
            ListNode<T> node = new ListNode<>(t);
            if (current == start){
                addFirst(t);
                if (current == null){end=start;}//if the only
            }
            //if the first
            else if (current == null){
                end.next = node; node.prev = end; end = node; size++;//if the last
            }
            else {
                    node.prev = current.prev;
                    current.prev.next = node;
                    current.prev = node;
                    size++;
            }
            node.next = current;
            index++;
            last = null;
        }
    }

    /*private class SubList<T> extends MyLinkedList<T>{
        MyLinkedList<T> superList;
        SubList(MyLinkedList<T> list, int st, int e){
            superList = list;
            if (st<0 || st<e || e>list.size){ throw new IndexOutOfBoundsException();}
            if (list.isEmpty()) {start = null; size = 0;}
            else{
            ListNode current = list.start;
            for (int i = 0; i < st; i++){ current = current.next; }
            start = current;
                size = e-st;
            }
        }
        private SubList(){}

        @Override
        public boolean add(T element) {
            if (isEmpty()){
                return addFirst(element);
            }
            ListNode<T> node = new ListNode<>(element);
            ListNode current = start;
            for (int i = 0; i < size-1; i++){ current = current.next; }
            if (current.next!=null){current.next.prev = node; node.next = current.next;}
            current.next = node;
            node.prev = current;
            superList.size++;
            size++;
            return true;
        }

        @Override
        public boolean addFirst(T element) {
            ListNode<T> node = new ListNode<>(element);
            node.next=start;
            if (this.start == superList.start){superList.start = node;}
            start = node;
            size++;
            superList.size++;
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            Object[] newArray = c.toArray();
            int amount = newArray.length;
            if (amount == 0) {
                return false;
            }
            ListNode current = start;
            if (start == null){
                addFirst((T)newArray[0]);
                current = start;
            }
            else{
                for (int i = 0; i < size-1; i++) {
                    current = current.next;
                }
                ListNode<T> node = new ListNode<>((T)newArray[0]);
                if (current.next!=null){current.next.prev = node; node.next = current.next;}
                current.next = node;
                node.prev = current;
                current = node;
            }
            for (int i=1; i<c.size(); i++) {
                ListNode<T> node = new ListNode<>((T) newArray[i]);
                if (current.next!=null){current.next.prev = node; node.next = current.next;}
                current.next = node;
                node.prev = current;
                current = node;
            }
            size += amount;
            superList.size+=amount;
            return true;
        }
    }*/
}
