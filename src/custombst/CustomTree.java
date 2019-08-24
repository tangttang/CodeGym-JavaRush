package custombst;

import java.io.Serializable;
import java.util.*;

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    private Entry<String> root;
    private LinkedList<Entry<String>> list = new LinkedList<>();
    private int size;

    public CustomTree() {
        this.root = new Entry<>("0");
    }

    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren;
        boolean availableToAddRightChildren;
        Entry<T> parent;
        Entry<T> leftChild;
        Entry<T> rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            this.availableToAddLeftChildren = true;
            this.availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }
    }

    @Override
    public boolean add(String s) {
        if (list.isEmpty()) {
            list.add(root);
        }
        for (Entry<String> element : list) {
            if (element.isAvailableToAddChildren()) {
                Entry<String> newElement = new Entry<>(s);
                if (element.availableToAddLeftChildren) {
                    element.leftChild = newElement;
                    element.availableToAddLeftChildren = false;
                } else {
                    element.rightChild = newElement;
                    element.availableToAddRightChildren = false;
                }
                newElement.parent = element;
                list.add(newElement);
                this.size++;
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }


    public String getParent(String s) {
        for (Entry<String> element : list) {
            if (s.equals(root.elementName)) {
                return null;
            }
            if (element != root && element.parent == null) {
                return null;
            }
            if (element.elementName.equals(s)) {
                return element.parent.elementName;
            }
        }
        return null;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof String)) {
            throw new UnsupportedOperationException();
        }
        String removedElementName = (String) o;
        if (removedElementName.equals(root.elementName)) {
            list.clear();
        } else {
            //traversing through the list, skipping the root
            for (int i = 1; i < size; i++) {
                Entry<String> elementToRemove = list.get(i);
                //found element to remove
                if (elementToRemove.elementName.equals(removedElementName)) {
                    //delete it and its children
                    removeChildren(elementToRemove);
                    list.remove(i);
                    size--;
                    //restore the possibility to add children for a parent node
                    Entry<String> parent = elementToRemove.parent;
                    if (parent.leftChild.elementName == removedElementName) {
                        parent.availableToAddLeftChildren = true;
                        parent.leftChild = null;
                    } else {
                        parent.availableToAddRightChildren = true;
                        parent.rightChild = null;
                    }
                }
            }
        }
        return true;
    }

    private void removeChildren(Entry<String> element) {
        List<Entry> children = new ArrayList<>();
        if (element.leftChild != null) {
            removeChildren(element.leftChild);
            children.add(element.leftChild);
        }
        if (element.rightChild != null) {
            removeChildren(element.rightChild);
            children.add(element.rightChild);

        }
        size -= children.size();
        list.removeAll(children);
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }
}
