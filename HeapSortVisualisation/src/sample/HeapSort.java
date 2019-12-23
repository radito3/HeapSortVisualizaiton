package sample;

import java.util.LinkedList;

class HeapSort {

    static class Event {
        int[] elems;

        Event(int[] elems) {
            this.elems = elems;
        }
    }

    static LinkedList<Event> events = new LinkedList<>();

    static void sort(int[] arr) {
        int n = arr.length;

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        // One by one extract an element from heap
        for (int i = n - 1; i >= 0; i--) {
            events.add(new Event(new int[] { arr[0], arr[i] }));
            // Move current root to end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // call max heapify on the reduced heap
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i; // Initialize largest as root
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        events.add(new Event(new int[] { arr[left], arr[largest] }));
        // If left child is larger than root
        if (left < n && arr[left] > arr[largest])
            largest = left;

        events.add(new Event(new int[] { arr[right], arr[largest] }));
        // If right child is larger than largest so far
        if (right < n && arr[right] > arr[largest])
            largest = right;

        // If largest is not root
        if (largest != i) {
            events.add(new Event(new int[] { arr[i], arr[largest] }));
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
        }
    }
}

