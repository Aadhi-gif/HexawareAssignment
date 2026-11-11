public class Patient {
    private final int patientId;
    private final String name;

    public Patient(int patientId, String name) {
        this.patientId = patientId;
        this.name = name;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Patient{id=" + patientId + ", name='" + name + "'}";
    }
}
