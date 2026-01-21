package lab4_2;

import java.io.FileNotFoundException;

public class RunExercise2 {
    public static void main(String[] args) {
        // Scenario: Base HTC + Optimized Plant (Exercise 2) + Base Room + Base RTC

        // 1. Start Room Plant (Port 1080)
        new Thread(() -> {
            try {
                System.out.println("Starting Room Plant...");
                RoomPlant.main(new String[] {});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // 2. Start Optimized Heater Tank Plant (Exercise 2) (Port 1081)
        new Thread(() -> {
            try {
                System.out.println("Starting Optimized Heater Tank Plant (Exercise 2)...");
                Exercise2.main(new String[] {});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // 3. Start RTC (Port 1082)
        new Thread(() -> {
            try {
                System.out.println("Starting RTC...");
                RTC.main(new String[] {});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // 4. Start Base HTC (Port 1083)
        new Thread(() -> {
            try {
                System.out.println("Starting Base HTC...");
                HTC.main(new String[] {});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
