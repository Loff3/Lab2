package aod.lab2;
public interface SearchableDataStructure<T> {
    public void add(T item);
    public boolean searchFor(T itemToSearchFor);
    public int size();
    public void clear();
    public boolean remove(T itemToRemove);
}