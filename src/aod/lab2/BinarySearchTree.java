package aod.lab2;

/**
 * Binärt sökträd som implementerar gränssnittet SearchableDataStructure.
 *
 * @param <T> typ som är jämförbar
 */
public class BinarySearchTree<T extends Comparable<? super T>> implements SearchableDataStructure<T> {
    private BSTNode root;
    private int size;

    /**
     * Skapar ett tomt binärt sökträd.
     */
    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Nod i det binära sökträdet.
     */
    private class BSTNode {
        private T item;
        private BSTNode left;
        private BSTNode right;

        /**
         * Skapar en nod med givet värde.
         *
         * @param item värdet i noden
         */
        public BSTNode(T item) {
            this.item = item;
            this.left = null;
            this.right = null;
        }
    }

    /**
     * Lägger till ett element i trädet.
     *
     * @param item elementet att lägga till
     * @throws IllegalArgumentException om item är null
     */
    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null värden är inte tillåtna");
        }

        if (root == null) {
            root = new BSTNode(item);
            size++;
            return;
        }

        BSTNode current = root;
        while (true) {
            int cmp = item.compareTo(current.item);

            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new BSTNode(item);
                    size++;
                    return;
                }
                current = current.left;
            } else if (cmp > 0) {
                if (current.right == null) {
                    current.right = new BSTNode(item);
                    size++;
                    return;
                }
                current = current.right;
            } else {
                // Ignorera dubbletter
                return;
            }
        }
    }

    /**
     * Söker efter ett element i trädet.
     *
     * @param itemToSearchFor elementet att söka efter
     * @return true om elementet finns, annars false
     */
    @Override
    public boolean searchFor(T itemToSearchFor) {
        if (itemToSearchFor == null) {
            return false;
        }

        BSTNode current = root;
        while (current != null) {
            int cmp = itemToSearchFor.compareTo(current.item);

            if (cmp == 0) {
                return true;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    /**
     * Returnerar antal element i trädet.
     *
     * @return antal element
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Tömmer trädet.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Tar bort ett element om det finns.
     *
     * @param itemToRemove elementet att ta bort
     * @return true om elementet togs bort, annars false
     */
    @Override
    public boolean remove(T itemToRemove) {
        if (itemToRemove == null || root == null) {
            return false;
        }

        BSTNode parent = null;
        BSTNode current = root;

        while (current != null) {
            int cmp = itemToRemove.compareTo(current.item);

            if (cmp == 0) {
                break;
            } else if (cmp < 0) {
                parent = current;
                current = current.left;
            } else {
                parent = current;
                current = current.right;
            }
        }

        if (current == null) {
            return false;
        }

        if (current.left == null && current.right == null) {
            if (current == root) {
                root = null;
            } else if (parent.left == current) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        } else if (current.left == null) {
            if (current == root) {
                root = current.right;
            } else if (parent.left == current) {
                parent.left = current.right;
            } else {
                parent.right = current.right;
            }
        } else if (current.right == null) {
            if (current == root) {
                root = current.left;
            } else if (parent.left == current) {
                parent.left = current.left;
            } else {
                parent.right = current.left;
            }
        } else {
            BSTNode successor = current.right;
            BSTNode successorParent = current;

            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            current.item = successor.item;

            if (successorParent == current) {
                successorParent.right = successor.right;
            } else {
                successorParent.left = successor.right;
            }
        }

        size--;
        return true;
    }

    /**
     * Returnerar en strängrepresentation av trädet i in-order.
     *
     * @return sträng med element i ordning
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        inOrderTraversal(root, sb);
        return sb.toString().trim();
    }

    /**
     * Rekursiv, traverserar trädet i in-order och bygger sträng.
     *
     * @param node aktuell nod
     * @param sb StringBuilder att fylla
     */
    private void inOrderTraversal(BSTNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.item).append(" ");
            inOrderTraversal(node.right, sb);
        }
    }
}
