package DataService;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private T root;
    private List<Node<T>> children;
    public Tree(T rootData) {
        root = rootData;
        children = new ArrayList<Node<T>>();
    }

    public void add(Node<T> item){
        children.add(item);
    }

    public T getRoot()
    {
        return root;
    }


    public List<Node<T>> getChildren()
    {
        return children;
    }

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;
        public Node(T item)
        {
            data = item;
            children = new ArrayList<Node<T>>();
        }
        public void addChildren(Node<T> item)
        {
            item.parent = this;
            children.add(item);
        }
        public List<Node<T>> getChildren()
        {
            return children;
        }

        public T getData()
        {
            return data;
        }
    }
}
