package lab4_2;

import java.io.FileNotFoundException;

public class RunExercise3 {
    public static void main(String[] args) {
        // Scenario: PID HTC (Exercise 3) + Base Plant + Base Room + Base RTC

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

        // 4. Start PID HTC (Exercise 3) (Port 1083)
        new Thread(() -> {
            try {
                System.out.println("Starting PID HTC (Exercise 3)...");
                Exercise3.main(new String[] {});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
