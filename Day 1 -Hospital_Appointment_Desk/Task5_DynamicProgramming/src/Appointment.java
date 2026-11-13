public class Appointment {
    private final String patientId;
    private final int start;
    private final int end;

    public Appointment(String patientId, int start, int end) {
        if (start < 0 || end <= start) {
            throw new IllegalArgumentException("Appointment must have end > start >= 0");
        }
        this.patientId = patientId;
        this.start = start;
        this.end = end;
    }

    public String getPatientId() { return patientId; }
    public int getStart() { return start; }
    public int getEnd() { return end; }

    @Override
    public String toString() {
        return patientId + " [" + format(start) + "-" + format(end) + "]";
    }

    public static String format(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }
}
