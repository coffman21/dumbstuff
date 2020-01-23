import java.util.*;

class FriendsMeeting {
    public static void main(String[] args) {
        solve(3, 10, 3);
    }

    public static void solve(int count, int maxTime, int maxWeight) {
        Friend[] friends = new Friend[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int bound = random.nextInt(maxTime - 1) + 1;
            Friend friend = new Friend(
                    random.nextInt(bound),
                    bound + random.nextInt(maxTime - bound),
                    random.nextInt(maxWeight)
            );
            assert friend.to <= maxTime;
            friends[i] = friend;
            System.out.println(friend);
        }

        int[] deltas = new int[maxTime];
        for (Friend f : friends) {
            deltas[f.from] = (f.weight + 1); // +1 vote for himself
            deltas[f.to] = -(f.weight + 1);
        }
        System.out.println(Arrays.toString(deltas));

        int from = 0;
        int to = 0;
        int weight = 0;

        boolean flag = false; // enable when weight is at current maximum
        for (int i = 0, acc = 0; i < maxTime; i++) {
            acc += deltas[i];
            if (acc > from) {
                // weight at current time is max counting from beginning
                from = i;
                weight = acc;
                flag = true;
            }
            if (flag && acc < from) {
                // a period is over
                flag = false;
                to = i - 1; // unsure
            }
        }
        System.out.println("weight: " + weight
                + ", from: " + from
                + ", to: " + to);
    }

    static class Friend {

        public Friend(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        int from;
        int to;
        int weight;

        @Override
        public String toString() {
            return "Friend{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }

}
