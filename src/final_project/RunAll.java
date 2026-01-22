package final_project;

public class RunAll {

    public static void main(String[] args) {
        // Start Intersection 1
        new Thread(() -> {
            try {
                Intersection1.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start Controller 1
        new Thread(() -> {
            try {
                Controller1.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start Intersection 2
        new Thread(() -> {
            try {
                Intersection2.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start Controller 2
        new Thread(() -> {
            try {
                Controller2.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start Bus Station
        new Thread(() -> {
            try {
                BusStation.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start Taxi Station
        new Thread(() -> {
            try {
                TaxiStation.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start Roundabout
        new Thread(() -> {
            try {
                Roundabout.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start Pedestrian Controller
        new Thread(() -> {
            try {
                PedestrianController.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Start Universal Input GUI
        new Thread(() -> {
            try {
                UniversalInput.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("RunAll: All components and Universal Input GUI started.");
    }
}
