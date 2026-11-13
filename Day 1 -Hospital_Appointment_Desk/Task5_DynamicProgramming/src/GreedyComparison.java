import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GreedyComparison {
    public static List<Appointment> solve(Appointment[] input) {
        Appointment[] appointments = input.clone();
        Arrays.sort(appointments,
                Comparator.comparingInt(Appointment::getEnd)
                        .thenComparingInt(Appointment::getStart)
                        .thenComparing(Appointment::getPatientId));

        List<Appointment> selected = new ArrayList<>();
        int lastEnd = -1;

        for (Appointment appointment : appointments) {
            if (appointment.getStart() >= lastEnd) {
                selected.add(appointment);
                lastEnd = appointment.getEnd();
            }
        }
        return selected;
    }
}
