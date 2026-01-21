package lab4_3;

import java.io.FileNotFoundException;

public class RunExercise2 {
    public static void main(String[] args) {
        // Scenario: HTC PI (Exercise 2) + Base Plant + Base Room + Base RTC

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

        // 2. Start Heater Tank Plant (Port 1081)
        new Thread(() -> {
            try {
                System.out.println("Starting Heater Tank Plant...");
                HeaterTankPlant.main(new String[] {});
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

        // 4. Start HTC PI (Exercise 2) (Port 1083)
        new Thread(() -> {
            try {
                System.out.println("Starting HTC PI (Exercise 2)...");
                Exercise2.main(new String[] {});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
