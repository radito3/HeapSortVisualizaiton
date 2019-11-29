package com.sap.exercise;

import java.util.concurrent.TimeUnit;

public class AlgorithmExercise {

    public static class LinkedList {

        private static class Node {
            int value;
            Node prev;
            Node next;

            Node(int value, Node prev, Node next) {
                this.value = value;
                this.prev = prev;
                this.next = next;
            }

            Node() {
            }
        }

        private Node head;
        private Node tail;
        private int size;

        public void insertBack(int val) {
            Node newNode = new Node(val, tail.prev, tail);
            tail.prev.next = newNode;
            tail.prev = newNode;
            size++;
        }

        public void insertFront(int val) {
            Node newNode = new Node(val, head, head.next);
            head.next.prev = newNode;
            head.next = newNode;
            size++;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public void clear() {
            head = null;
            tail = null;
            size = 0;
        }

        public void insertAfter(int index, int val) {
            if (index == 0) {
                insertFront(val);
                return;
            }
            if (index >= size) {
                insertBack(val);
                return;
            }
            Node node = head.next;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            Node newNode = new Node(val, node, node.next);
            node.next.prev = newNode;
            node.next = newNode;
            size++;
        }

        public int get(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
            return getNode(index).value;
        }

        private Node getNode(int index) {
            if (index < (size >> 1)) {
                Node node = head.next;
                for (int i = 0; i < index; i++) {
                    node = node.next;
                }
                return node;
            } else {
                Node node = tail.prev;
                for (int i = size - 1; i > index; i--) {
                    node = node.prev;
                }
                return node;
            }
        }

        public void remove(int val) {
            Node node = head.next;
            for (int i = 0; i < size; i++) {
                if (node.value == val) {
                    break;
                }
                node = node.next;
            }
            unlinkNode(node);
            size--;
        }

        private void unlinkNode(Node node) {
            node.next.prev = node.prev;
            node.prev = node.next;
        }

        public void removeAt(int index) {
            unlinkNode(getNode(index));
            size--;
        }

        public LinkedList() {
            head = new Node();
            tail = new Node();
            head.next = tail;
            tail.prev = head;
        }

    }

    static void sortArray(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j -= 1;
            }
            arr[j + 1] = key;
        }
    }

    static void sortList(LinkedList list) {
        int n = list.size();
        for (int i = 1; i < n; ++i) {
            int key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j) > key) {
                list.insertAfter(j, list.get(j));
                j -= 1;
            }
            list.insertAfter(j, key);
        }
    }

    static void printList(LinkedList list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    static void printArray(int[] array) {
        for (int value : array) {
            System.out.println(value);
        }
    }

    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        int[] arr = new int[10001];
        for (int i = 10000; i >= 0; i--) {
            list.insertBack(i);
        }
        for (int i = 0; i < 10001; i++) {
            arr[i] = list.get(i);
        }

        long start = System.nanoTime();
        sortArray(arr);
        long end = System.nanoTime();
        System.out.println(TimeUnit.NANOSECONDS.toMillis(end - start));

        System.out.println();

        start = System.nanoTime();
        sortList(list);
        end = System.nanoTime();
        System.out.println(TimeUnit.NANOSECONDS.toMillis(end - start));

        System.out.println();

//        printArray(arr);
//        System.out.println();
//        printList(list);
    }

}
