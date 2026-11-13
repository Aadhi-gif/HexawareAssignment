import java.util.HashSet;
import java.util.Set;

public class BookingManager {
    private final Set<BookingKey> bookings = new HashSet<>();

    // Returns true when the booking is new; false when it is a duplicate.
    public boolean book(String patientId, String slot) {
        BookingKey key = new BookingKey(patientId, slot);
        if (bookings.contains(key)) {
            return false;
        }
        bookings.add(key);
        return true;
    }

    public int getBookingCount() {
        return bookings.size();
    }
}
