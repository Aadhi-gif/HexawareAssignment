import java.util.Comparator;

public class QuickSort {
    private long swaps;

    public long sort(Appointment[] array, Comparator<Appointment> comparator) {
        swaps = 0;
        quickSort(array, 0, array.length - 1, comparator);
        return swaps;
    }

    private void quickSort(Appointment[] array, int low, int high,
                           Comparator<Appointment> comparator) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, comparator);
            quickSort(array, low, pivotIndex - 1, comparator);
            quickSort(array, pivotIndex + 1, high, comparator);
        }
    }

    private int partition(Appointment[] array, int low, int high,
                          Comparator<Appointment> comparator) {
        Appointment pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private void swap(Appointment[] array, int i, int j) {
        if (i != j) {
            Appointment temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            swaps++;
        }
    }
}
