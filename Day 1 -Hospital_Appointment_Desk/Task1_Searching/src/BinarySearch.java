public class BinarySearch {
    // The array must be sorted by patient ID in ascending order.
    public static Patient findById(Patient[] sortedPatients, int targetId) {
        int low = 0;
        int high = sortedPatients.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midId = sortedPatients[mid].getPatientId();

            if (midId == targetId) {
                return sortedPatients[mid];
            } else if (midId < targetId) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }
}
