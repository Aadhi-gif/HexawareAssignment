public class LinearSearch {
    public static Patient findById(Patient[] patients, int targetId) {
        for (Patient patient : patients) {
            if (patient != null && patient.getPatientId() == targetId) {
                return patient;
            }
        }
        return null;
    }
}
