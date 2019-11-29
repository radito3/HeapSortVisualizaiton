package com.sap.exercise;

import javafx.application.Application;
import javafx.collections.*;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    public static void main(String[] args) throws InterruptedException {
        int[] arr = { 9, 8, 7, 6, 5, 4, 3, 2, 1 };

        class ChangeListener implements ArrayChangeListener<ObservableIntegerArray> {

            @Override
            public void onChanged(ObservableIntegerArray array, boolean sizeChanged, int from, int to) {
                int[] copy = new int[array.size()];
                changes.add(new ArrayChange(array.toArray(copy), sizeChanged, from, to));

                System.out.println(Arrays.toString(array.toArray(copy)));
            }

            @Override
            public int hashCode() {
                return 1;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof ChangeListener;
            }
        }

        ObservableIntegerArray intArr = FXCollections.observableIntegerArray(arr);
        intArr.addListener(new ChangeListener());
        HeapSort.sort(intArr);

        for (int i = 0; i < changes.size(); i++) {
            ArrayChange change = changes.poll();
            System.out.println(Arrays.toString(change.array));
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        }

    }

    private static Queue<ArrayChange> changes = new LinkedList<>();

    static class ArrayChange {
        int[] array;
        boolean sizeChanged;
        int from;
        int to;

        ArrayChange(int[] array, boolean sizeChanged, int from, int to) {
            this.array = array;
            this.sizeChanged = sizeChanged;
            this.from = from;
            this.to = to;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
