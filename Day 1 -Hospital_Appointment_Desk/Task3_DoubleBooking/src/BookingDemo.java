public class BookingDemo {
    public static void main(String[] args) {
        BookingManager manager = new BookingManager();

        tryBooking(manager, "P101", "2026-11-11 10:00");
        tryBooking(manager, "P102", "2026-11-11 10:00");
        tryBooking(manager, "P101", "2026-11-11 10:00"); // duplicate
        tryBooking(manager, "P101", "2026-11-11 11:00"); // different slot

        System.out.println("Total accepted bookings: " + manager.getBookingCount());
    }

    private static void tryBooking(BookingManager manager, String patientId, String slot) {
        if (manager.book(patientId, slot)) {
            System.out.println("BOOKING ACCEPTED: " + patientId + " @ " + slot);
        } else {
            System.out.println("DUPLICATE REJECTED: " + patientId + " @ " + slot);
        }
    }
}
