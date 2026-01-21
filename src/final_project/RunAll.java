package final_project;

public class RunAll {

    public static void main(String[] args) {
        // Start Intersection 1
        new Thread(() -> {
            Intersection1.main(args);
        }).start();

        // Start Controller 1
        new Thread(() -> {
            Controller1.main(args);
        }).start();

        // Start Intersection 2
        new Thread(() -> {
            Intersection2.main(args);
        }).start();

        // Start Controller 2
        new Thread(() -> {
            Controller2.main(args);
        }).start();

        // Start Bus Station
        new Thread(() -> {
            BusStation.main(args);
        }).start();

        // Start Taxi Station
        new Thread(() -> {
            TaxiStation.main(args);
        }).start();

        // Start Roundabout
        new Thread(() -> {
            Roundabout.main(args);
        }).start();

        // Start Pedestrian
        new Thread(() -> {
            PedestrianController.main(args);
        }).start();

        System.out.println("All components started.");
    }
}
