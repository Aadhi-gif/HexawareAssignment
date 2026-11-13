class Task5_TwoPointers {

    public static void main(String[] args) {

        int[] orders = {
                10, 20, 30, 40, 50,
                60, 70, 80, 90
        };

        int target = 100;

        int left = 0;
        int right = orders.length - 1;

        System.out.println("Matching Pairs:");

        while (left < right) {

            int sum = orders[left] + orders[right];

            if (sum == target) {

                System.out.println(
                        orders[left] + " + " + orders[right]
                );

                left++;
                right--;
            }

            else if (sum < target) {
                left++;
            }

            else {
                right--;
            }
        }
    }
}