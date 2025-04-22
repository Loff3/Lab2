package aod.lab2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class BinarySearchTreeTest {
    public static void main(String[] args) {
        // Kör alla funktionalitetstester
        testAll();

        // Tidskomplexitetstest
        System.out.println("\n--- Tidskomplexitetstest ---");
        performTimeComplexityTests();
    }

    private static void performTimeComplexityTests() {
        try {
            // Tester med slumpade heltal
            performRandomTests();

            // Tester med sorterade heltal
            performSortedTests();

            System.out.println("CSV-filer skapade: random_bst_timings.csv och sorted_bst_timings.csv");
        } catch (IOException e) {
            System.err.println("Kunde inte skriva till fil: " + e.getMessage());
        }
    }

    private static void performRandomTests() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("random_bst_timings.csv"));
        writer.println("Storlek,Genomsnittstid(ns)");

        int[] sizes = {20000, 40000, 80000, 160000, 320000, 640000};
        Random random = new Random(42); // Fast seed för reproducerbarhet

        for (int size : sizes) {
            BinarySearchTree<Integer> tree = new BinarySearchTree<>();

            // Lägg till slumpade element
            for (int i = 0; i < size; i++) {
                tree.add(random.nextInt(size * 10));
            }

            // Mät söktid
            double avgTime = measureSearchTime(tree, size, true);
            writer.println(size + "," + avgTime);
            System.out.println("Random: Storlek " + size + " - Genomsnittstid: " + avgTime + " ns");
        }

        writer.close();
    }

    private static void performSortedTests() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("sorted_bst_timings.csv"));
        writer.println("Storlek,Genomsnittstid(ns)");

        int[] sizes = {1000, 2000, 4000, 8000, 16000, 32000}; // Mindre storlekar för sorterade

        for (int size : sizes) {
            BinarySearchTree<Integer> tree = new BinarySearchTree<>();

            // Lägg till sorterade element
            for (int i = 0; i < size; i++) {
                tree.add(i);
            }

            // Mät söktid
            double avgTime = measureSearchTime(tree, size, false);
            writer.println(size + "," + avgTime);
            System.out.println("Sorted: Storlek " + size + " - Genomsnittstid: " + avgTime + " ns");
        }

        writer.close();
    }

    private static double measureSearchTime(BinarySearchTree<Integer> tree, int size, boolean randomSearch) {
        Random random = new Random(42);
        int searches = 1000; // Antal sökningar
        long totalTime = 0;

        for (int i = 0; i < searches; i++) {
            int searchValue;
            if (randomSearch) {
                // Sök både efter existerande och icke-existerande värden
                searchValue = random.nextInt(size * 20);
            } else {
                // För sorterade träd, sök i rimligt intervall
                searchValue = random.nextInt(size + size/2);
            }

            long startTime = System.nanoTime();
            tree.searchFor(searchValue);
            long endTime = System.nanoTime();

            totalTime += (endTime - startTime);
        }

        return (double) totalTime / searches;
    }


    private static void testAll() {
        testAdd();
        testSearchFor();
        testSize();
        testClear();
        testRemove();
        testToString();
        testAddDuplicates();
        testRemoveNonExistent();
        testEmptyTree();

        System.out.println("Alla tester passerade!");
    }

    private static void testAdd() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(3);
        tree.add(7);

        assert tree.size() == 3 : "Add: Fel storlek efter att ha lagt till 3 element";
        assert tree.searchFor(5) : "Add: Kunde inte hitta roten";
        assert tree.searchFor(3) : "Add: Kunde inte hitta vänster barn";
        assert tree.searchFor(7) : "Add: Kunde inte hitta höger barn";
    }

    private static void testSearchFor() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(3);
        tree.add(7);

        assert tree.searchFor(5) : "SearchFor: Kunde inte hitta befintligt element (5)";
        assert tree.searchFor(3) : "SearchFor: Kunde inte hitta befintligt element (3)";
        assert tree.searchFor(7) : "SearchFor: Kunde inte hitta befintligt element (7)";
        assert !tree.searchFor(10) : "SearchFor: Hittade element som inte finns (10)";
        assert !tree.searchFor(1) : "SearchFor: Hittade element som inte finns (1)";
    }

    private static void testSize() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        assert tree.size() == 0 : "Size: Fel storlek på tomt träd";

        tree.add(1);
        assert tree.size() == 1 : "Size: Fel storlek efter ett element";

        tree.add(2);
        tree.add(3);
        assert tree.size() == 3 : "Size: Fel storlek efter tre element";
    }

    private static void testClear() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(1);
        tree.add(2);
        tree.add(3);

        tree.clear();
        assert tree.size() == 0 : "Clear: Trädet är inte tomt efter clear";
        assert !tree.searchFor(1) : "Clear: Hittade element efter clear";
    }

    private static void testRemove() {
        // Test med nod som är löv
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(3);
        tree.add(7);

        assert tree.remove(3) : "Remove: Kunde inte ta bort löv";
        assert !tree.searchFor(3) : "Remove: Lövet finns kvar efter borttagning";
        assert tree.size() == 2 : "Remove: Fel storlek efter borttagning av löv";

        // Test med nod som har ett barn
        tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(3);
        tree.add(7);
        tree.add(6);

        assert tree.remove(7) : "Remove: Kunde inte ta bort nod med ett barn";
        assert !tree.searchFor(7) : "Remove: Nod med ett barn finns kvar";
        assert tree.searchFor(6) : "Remove: Barnet togs bort med föräldern";

        // Test med nod som har två barn
        tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(3);
        tree.add(7);
        tree.add(2);
        tree.add(4);

        assert tree.remove(3) : "Remove: Kunde inte ta bort nod med två barn";
        assert !tree.searchFor(3) : "Remove: Nod med två barn finns kvar";
        assert tree.searchFor(2) : "Remove: Vänster barn togs bort";
        assert tree.searchFor(4) : "Remove: Höger barn togs bort";

        // Test med rot
        tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(3);
        tree.add(7);

        assert tree.remove(5) : "Remove: Kunde inte ta bort roten";
        assert !tree.searchFor(5) : "Remove: Roten finns kvar";
        assert tree.size() == 2 : "Remove: Fel storlek efter borttagning av rot";
    }

    private static void testToString() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(3);
        tree.add(7);
        tree.add(1);
        tree.add(9);

        assert tree.toString().equals("1 3 5 7 9") : "ToString: Fel ordning, förväntade '1 3 5 7 9' men fick '" + tree.toString() + "'";
    }

    private static void testAddDuplicates() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(5);

        assert tree.size() == 1 : "AddDuplicates: Dubbletter borde ignoreras";
        assert tree.searchFor(5) : "AddDuplicates: Värdet saknas";
    }

    private static void testRemoveNonExistent() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);

        assert !tree.remove(10) : "RemoveNonExistent: remove returnerade true för element som inte finns";
        assert tree.size() == 1 : "RemoveNonExistent: Storleken ändrades";
    }

    private static void testEmptyTree() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();

        assert !tree.searchFor(5) : "EmptyTree: Hittade element i tomt träd";
        assert !tree.remove(5) : "EmptyTree: remove returnerade true i tomt träd";
        assert tree.toString().equals("") : "EmptyTree: toString på tomt träd borde vara tom sträng";
    }
}