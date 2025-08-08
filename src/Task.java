public class Task {
    String text;
    boolean completed;

    Task(String text) {
        this.text = text;
        this.completed = false;
    }

    @Override
    public String toString() {
        return text;
    }
}
