import java.util.Arrays;
import java.util.Comparator;

public class SortComparison {
    private static final Comparator<Appointment> PRIORITY_ORDER =
            Comparator.comparingInt((Appointment a) -> urgencyRank(a.getUrgency()))
                    .thenComparingInt(Appointment::getArrivalMinutes)
                    .thenComparing(Appointment::getPatientId);

    public static void main(String[] args) {
        Appointment[] input = {
            new Appointment("P101", "Low", 630),
            new Appointment("P102", "Emergency", 570),
            new Appointment("P103", "High", 540),
            new Appointment("P104", "Emergency", 510),
            new Appointment("P105", "Medium", 550),
            new Appointment("P106", "High", 530)
        };

        Appointment[] selectionData = input.clone();
        Appointment[] quickData = input.clone();

        long selectionStart = System.nanoTime();
        long selectionSwaps = SelectionSort.sort(selectionData, PRIORITY_ORDER);
        long selectionTime = System.nanoTime() - selectionStart;

        QuickSort quickSort = new QuickSort();
        long quickStart = System.nanoTime();
        long quickSwaps = quickSort.sort(quickData, PRIORITY_ORDER);
        long quickTime = System.nanoTime() - quickStart;

        System.out.println("Original appointments:");
        print(input);

        System.out.println("\nSelection Sort result:");
        print(selectionData);
        System.out.println("Swaps: " + selectionSwaps);
        System.out.println("Time: " + selectionTime + " ns");

        System.out.println("\nQuick Sort result:");
        print(quickData);
        System.out.println("Swaps: " + quickSwaps);
        System.out.println("Time: " + quickTime + " ns");

        System.out.println("\nBoth algorithms produce same order: "
                + Arrays.equals(selectionData, quickData));
        System.out.println("Priority: Emergency > High > Medium > Low; "
                + "ties use earlier arrival time.");
    }

    private static int urgencyRank(String urgency) {
        switch (urgency) {
            case "Emergency": return 1;
            case "High": return 2;
            case "Medium": return 3;
            case "Low": return 4;
            default: throw new IllegalArgumentException("Unknown urgency");
        }
    }

    private static void print(Appointment[] appointments) {
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }
}
