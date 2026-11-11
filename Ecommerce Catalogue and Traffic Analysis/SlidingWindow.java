class Task4_SlidingWindow {

    public static void main(String[] args) {

        int[] requests = new int[1440];

        // Sample traffic data
        for (int i = 0; i < 1440; i++) {
            requests[i] = 100;
        }

        // Peak traffic between 10 AM and 1 PM
        for (int i = 600; i < 780; i++) {
            requests[i] = 500;
        }

        int window = 180;

        int sum = 0;

        // First 3-hour window
        for (int i = 0; i < window; i++) {
            sum += requests[i];
        }

        int max = sum;
        int start = 0;

        // Sliding window
        for (int i = window; i < 1440; i++) {

            sum = sum + requests[i] - requests[i - window];

            if (sum > max) {
                max = sum;
                start = i - window + 1;
            }
        }

        System.out.println("Maximum Requests: " + max);

        System.out.println("Busiest Window: "
                + start / 60 + ":00 to "
                + (start + window) / 60 + ":00");
    }
}