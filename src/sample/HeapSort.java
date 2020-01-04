package sample;

import java.util.LinkedList;

public class HeapSort {

    public enum EventType { CHANGE, COMPARISON }

    public static class Event {
        private int[] elems;
        private EventType eventType;

        Event(int[] elems, EventType eventType) {
            this.elems = elems;
            this.eventType = eventType;
        }

        public int[] getElems() {
            return elems;
        }

        public EventType getEventType() {
            return eventType;
        }
    }

    public static LinkedList<Event> events = new LinkedList<>();

    public static void sort(int[] arr) {
        int n = arr.length;

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        // One by one extract an element from heap
        for (int i = n - 1; i >= 0; i--) {
            events.add(new Event(new int[] { arr[0], arr[i] }, EventType.CHANGE));
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

        // If left child is larger than root
        if (left < n) {
            events.add(new Event(new int[] { arr[left], arr[largest] }, EventType.COMPARISON));
            if (arr[left] > arr[largest])
                largest = left;
        }
        // If right child is larger than largest so far
        if (right < n) {
            events.add(new Event(new int[] { arr[right], arr[largest] }, EventType.COMPARISON));
            if (arr[right] > arr[largest])
                largest = right;
        }

        // If largest is not root
        if (largest != i) {
            events.add(new Event(new int[] { arr[i], arr[largest] }, EventType.CHANGE));
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
        }
    }
}

