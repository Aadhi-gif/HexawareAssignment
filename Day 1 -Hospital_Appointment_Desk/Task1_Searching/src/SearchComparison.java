import java.util.Arrays;
import java.util.Random;

public class SearchComparison {
    private static final int PATIENT_COUNT = 20000;
    private static final int LOOKUP_COUNT = 1000;

    public static void main(String[] args) {
        Patient[] patients = new Patient[PATIENT_COUNT];

        // IDs are unique. The source array is deliberately shuffled.
        for (int i = 0; i < PATIENT_COUNT; i++) {
            patients[i] = new Patient(100001 + i, "Patient" + (i + 1));
        }
        shuffle(patients, new Random(42));

        Patient[] sortedPatients = patients.clone();
        Arrays.sort(sortedPatients,
                (a, b) -> Integer.compare(a.getPatientId(), b.getPatientId()));

        int[] lookupIds = new int[LOOKUP_COUNT];
        Random random = new Random(7);
        for (int i = 0; i < LOOKUP_COUNT; i++) {
            lookupIds[i] = 100001 + random.nextInt(PATIENT_COUNT);
        }

        // Warm-up both methods to reduce first-run/JIT effects.
        for (int id : lookupIds) {
            LinearSearch.findById(patients, id);
            BinarySearch.findById(sortedPatients, id);
        }

        long linearStart = System.nanoTime();
        int linearFound = 0;
        for (int id : lookupIds) {
            if (LinearSearch.findById(patients, id) != null) {
                linearFound++;
            }
        }
        long linearTime = System.nanoTime() - linearStart;

        long binaryStart = System.nanoTime();
        int binaryFound = 0;
        for (int id : lookupIds) {
            if (BinarySearch.findById(sortedPatients, id) != null) {
                binaryFound++;
            }
        }
        long binaryTime = System.nanoTime() - binaryStart;

        System.out.println("PATIENT SEARCH COMPARISON");
        System.out.println("Total patient records: " + PATIENT_COUNT);
        System.out.println("Lookups per algorithm: " + LOOKUP_COUNT);
        System.out.println("\nLinear Search");
        System.out.println("Successful lookups: " + linearFound);
        System.out.println("Elapsed time: " + linearTime + " ns");
        System.out.println("\nBinary Search");
        System.out.println("Successful lookups: " + binaryFound);
        System.out.println("Elapsed time: " + binaryTime + " ns");
        System.out.println("\nNote: Binary Search used a sorted copy. Timings vary by run.");
        System.out.println("Example patient: " + BinarySearch.findById(sortedPatients, lookupIds[0]));
    }

    private static void shuffle(Patient[] array, Random random) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Patient temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
