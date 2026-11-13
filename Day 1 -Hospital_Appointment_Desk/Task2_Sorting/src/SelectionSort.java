import java.util.Comparator;

public class SelectionSort {
    public static long sort(Appointment[] array, Comparator<Appointment> comparator) {
        long swaps = 0;
        for (int i = 0; i < array.length - 1; i++) {
            int best = i;
            for (int j = i + 1; j < array.length; j++) {
                if (comparator.compare(array[j], array[best]) < 0) {
                    best = j;
                }
            }
            if (best != i) {
                Appointment temp = array[i];
                array[i] = array[best];
                array[best] = temp;
                swaps++;
            }
        }
        return swaps;
    }
}
