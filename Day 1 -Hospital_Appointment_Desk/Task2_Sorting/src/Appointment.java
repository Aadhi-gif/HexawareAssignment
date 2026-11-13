public class Appointment {
    private final String patientId;
    private final String urgency;
    private final int arrivalMinutes;

    public Appointment(String patientId, String urgency, int arrivalMinutes) {
        if (!urgency.equals("Emergency") && !urgency.equals("High")
                && !urgency.equals("Medium") && !urgency.equals("Low")) {
            throw new IllegalArgumentException("Unknown urgency: " + urgency);
        }
        this.patientId = patientId;
        this.urgency = urgency;
        this.arrivalMinutes = arrivalMinutes;
    }

    public String getPatientId() { return patientId; }
    public String getUrgency() { return urgency; }
    public int getArrivalMinutes() { return arrivalMinutes; }

    @Override
    public String toString() {
        return patientId + " | " + urgency + " | " + formatTime(arrivalMinutes);
    }

    public static String formatTime(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }
}
