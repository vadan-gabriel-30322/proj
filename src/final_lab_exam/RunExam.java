package final_lab_exam;

public class RunExam {
    public static void main(String[] args) {
        // Start Plant
        new Thread(() -> {
            try {
                Plant.main(new String[] {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start Controller
        new Thread(() -> {
            try {
                Controller.main(new String[] {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start InputFuzzy
        new Thread(() -> {
            try {
                InputFuzzy.main(new String[] {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
