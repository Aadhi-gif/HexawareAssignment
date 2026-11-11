import java.util.Objects;

public class BookingKey {
    private final String patientId;
    private final String slot;

    public BookingKey(String patientId, String slot) {
        this.patientId = patientId;
        this.slot = slot;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BookingKey)) return false;
        BookingKey other = (BookingKey) object;
        return Objects.equals(patientId, other.patientId)
                && Objects.equals(slot, other.slot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, slot);
    }

    @Override
    public String toString() {
        return patientId + " @ " + slot;
    }
}
