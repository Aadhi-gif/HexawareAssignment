import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AppointmentDP {
    public static class Result {
        public final int maximumCount;
        public final List<Appointment> selected;

        Result(int maximumCount, List<Appointment> selected) {
            this.maximumCount = maximumCount;
            this.selected = selected;
        }
    }

    private static final Comparator<Appointment> BY_END =
            Comparator.comparingInt(Appointment::getEnd)
                    .thenComparingInt(Appointment::getStart)
                    .thenComparing(Appointment::getPatientId);

    public static Result solve(Appointment[] input) {
        Appointment[] appointments = input.clone();
        Arrays.sort(appointments, BY_END);
        int n = appointments.length;

        // predecessor[i] is the last index j < i with end[j] <= start[i].
        int[] predecessor = new int[n];
        for (int i = 0; i < n; i++) {
            predecessor[i] = findLastCompatible(appointments, i);
        }

        // dp[i] stores the best answer using the first i sorted appointments.
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int currentIndex = i - 1;
            int take = 1 + dp[predecessor[currentIndex] + 1];
            int skip = dp[i - 1];
            dp[i] = Math.max(take, skip);
        }

        List<Appointment> selected = new ArrayList<>();
        int i = n;
        while (i > 0) {
            int currentIndex = i - 1;
            int take = 1 + dp[predecessor[currentIndex] + 1];

            if (take > dp[i - 1]) {
                selected.add(appointments[currentIndex]);
                i = predecessor[currentIndex] + 1;
            } else {
                i--;
            }
        }

        // Reconstruct in chronological order.
        List<Appointment> chronological = new ArrayList<>();
        for (int j = selected.size() - 1; j >= 0; j--) {
            chronological.add(selected.get(j));
        }
        return new Result(dp[n], chronological);
    }

    private static int findLastCompatible(Appointment[] appointments, int current) {
        int low = 0;
        int high = current - 1;
        int answer = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (appointments[mid].getEnd() <= appointments[current].getStart()) {
                answer = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return answer;
    }
}
