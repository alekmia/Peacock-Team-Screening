public class AnswerBundle {
    int groupAmount;
    long seconds;

    public AnswerBundle(int groupAmount, long seconds) {
        this.groupAmount = groupAmount;
        this.seconds = seconds;
    }

    public void print() {
        System.out.println(STR."Number of Groups: \{groupAmount}");
        System.out.println(STR."Seconds: \{seconds}");
    }
}
