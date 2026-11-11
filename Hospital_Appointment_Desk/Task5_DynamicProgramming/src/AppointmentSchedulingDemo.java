import java.util.List;

public class AppointmentSchedulingDemo {
    public static void main(String[] args) {
        // Times are minutes after midnight. 09:00 = 540, 10:00 = 600, etc.
        Appointment[] appointments = {
            new Appointment("A1", 540, 600), // 09:00-10:00
            new Appointment("A2", 570, 660), // 09:30-11:00
            new Appointment("A3", 600, 660), // 10:00-11:00
            new Appointment("A4", 660, 720), // 11:00-12:00
            new Appointment("A5", 690, 780), // 11:30-13:00
            new Appointment("A6", 780, 840)  // 13:00-14:00
        };

        AppointmentDP.Result dpResult = AppointmentDP.solve(appointments);
        List<Appointment> greedyResult = GreedyComparison.solve(appointments);

        System.out.println("DYNAMIC PROGRAMMING");
        System.out.println("Maximum appointments: " + dpResult.maximumCount);
        for (Appointment appointment : dpResult.selected) {
            System.out.println(appointment);
        }

        System.out.println("\nGREEDY (EARLIEST FINISH TIME)");
        System.out.println("Maximum appointments: " + greedyResult.size());
        for (Appointment appointment : greedyResult) {
            System.out.println(appointment);
        }

        System.out.println("\nBoth methods agree on maximum count: "
                + (dpResult.maximumCount == greedyResult.size()));
        System.out.println("DP complexity: O(n log n) including sorting and predecessor search.");
        System.out.println("Greedy complexity: O(n log n) including sorting.");
    }
}
