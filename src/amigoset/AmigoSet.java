package amigoset;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class AmigoSet<E> extends AbstractSet<E> implements Serializable, Cloneable, Set<E> {

    private static final Object PRESENT = new Object();

    private transient HashMap<E, Object> map;

    public AmigoSet() {
        map = new HashMap<>();
    }

    public AmigoSet(Collection<? extends E> collection) {
        int capacity = Math.max(16, (int) Math.floor(collection.size() / .75f) + 1);
        map = new HashMap<>(capacity);
        for (E element : collection) {
            map.put(element, PRESENT);
        }
    }

    private void writeObject(ObjectOutputStream oos) throws Exception {
        oos.defaultWriteObject();

        oos.writeInt(HashMapReflectionHelper.callHiddenMethod(map, "capacity"));
        oos.writeFloat(HashMapReflectionHelper.callHiddenMethod(map, "loadFactor"));
        oos.writeInt(map.size());

        for (E e : map.keySet()) oos.writeObject(e);


    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        int capacity = ois.readInt();
        float loadFactor = ois.readFloat();
        int size = ois.readInt();

        map = new HashMap<>(capacity, loadFactor);

        for (int i = 0; i < size; i++) {
            E e = (E) ois.readObject();
            map.put(e, PRESENT);
        }

    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator iterator() {
        return map.keySet().iterator();
    }

    @Override
    public Object[] toArray() {
        return this.map.keySet().toArray();
    }

    @Override
    public boolean add(E e) {
        return null == map.put(e, PRESENT);
    }


    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.map.keySet().toArray(a);
    }

    @Override
    public Object clone() {
        try {
            AmigoSet<E> set = (AmigoSet<E>) super.clone();
            set.map = (HashMap<E, Object>) map.clone();
            return set;
        } catch (Exception e) {
            throw new InternalError();
        }
    }
}
