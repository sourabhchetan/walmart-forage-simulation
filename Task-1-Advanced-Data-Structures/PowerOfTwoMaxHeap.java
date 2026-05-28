import java.util.ArrayList;
import java.util.List;

public class PowerOfTwoMaxHeap {

    private final List<Integer> heap;
    private final int childExponent;
    private final int childrenPerNode;

    // Constructor
    public PowerOfTwoMaxHeap(int childExponent) {
        if (childExponent < 0) {
            throw new IllegalArgumentException("childExponent must be non-negative");
        }

        this.childExponent = childExponent;
        this.childrenPerNode = 1 << childExponent;
        this.heap = new ArrayList<>();
    }

    // Insert a value into the heap
    public void insert(int value) {
        heap.add(value);
        heapifyUp(heap.size() - 1);
    }

    // Remove and return the maximum value
    public int popMax() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        int maxValue = heap.get(0);
        int lastIndex = heap.size() - 1;

        // Move last element to root
        heap.set(0, heap.get(lastIndex));
        heap.remove(lastIndex);

        // Restore heap property if heap is not empty
        if (!heap.isEmpty()) {
            heapifyDown(0);
        }

        return maxValue;
    }

    // Heapify upward after insertion
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = getParentIndex(index);

            if (heap.get(parentIndex) >= heap.get(index)) {
                break;
            }

            swap(parentIndex, index);
            index = parentIndex;
        }
    }

    // Heapify downward after removing max
    private void heapifyDown(int index) {
        int size = heap.size();

        while (true) {
            int largestIndex = index;

            int firstChildIndex = getFirstChildIndex(index);

            // Check all children
            for (int offset = 0; offset < childrenPerNode; offset++) {
                int childIndex = firstChildIndex + offset;

                if (childIndex >= size) {
                    break;
                }

                if (heap.get(childIndex) > heap.get(largestIndex)) {
                    largestIndex = childIndex;
                }
            }

            // Heap property satisfied
            if (largestIndex == index) {
                break;
            }

            swap(index, largestIndex);
            index = largestIndex;
        }
    }

    // Get parent index
    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / childrenPerNode;
    }

    // Get first child index
    private int getFirstChildIndex(int parentIndex) {
        return parentIndex * childrenPerNode + 1;
    }

    // Swap two elements in heap
    private void swap(int firstIndex, int secondIndex) {
        int temp = heap.get(firstIndex);
        heap.set(firstIndex, heap.get(secondIndex));
        heap.set(secondIndex, temp);
    }

    // Utility method for printing heap
    public void printHeap() {
        System.out.println(heap);
    }

    // Main method for testing
    public static void main(String[] args) {

        // childExponent = 1
        // childrenPerNode = 2^1 = 2 (Binary Heap)
        PowerOfTwoMaxHeap binaryHeap = new PowerOfTwoMaxHeap(1);

        binaryHeap.insert(10);
        binaryHeap.insert(40);
        binaryHeap.insert(15);
        binaryHeap.insert(60);
        binaryHeap.insert(90);
        binaryHeap.insert(70);

        System.out.println("Binary Heap:");
        binaryHeap.printHeap();

        System.out.println("Pop Max: " + binaryHeap.popMax());
        binaryHeap.printHeap();

        // childExponent = 2
        // childrenPerNode = 2^2 = 4 (4-ary Heap)
        PowerOfTwoMaxHeap fourAryHeap = new PowerOfTwoMaxHeap(2);

        fourAryHeap.insert(12);
        fourAryHeap.insert(55);
        fourAryHeap.insert(23);
        fourAryHeap.insert(99);
        fourAryHeap.insert(31);
        fourAryHeap.insert(77);

        System.out.println("\n4-ary Heap:");
        fourAryHeap.printHeap();

        System.out.println("Pop Max: " + fourAryHeap.popMax());
        fourAryHeap.printHeap();
    }
}
