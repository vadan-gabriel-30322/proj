package lab4_3;

import java.io.FileNotFoundException;
import lab4_2.HeaterTankPlant;
import lab4_2.RoomPlant;
import lab4_2.RTC;

public class RunLab4_3_System {
    public static void main(String[] args) {
        // Scenario: ORC + HTC PI + Base Plant + Base Room + Base RTC
        // Note: ORC sends to HTC PI. HTC PI sends to HeaterTankPlant.

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

        // 4. Start HTC PI (Lab 4_3 Exercise 2) (Port 1083)
        new Thread(() -> {
            try {
                System.out.println("Starting HTC PI (Lab 4_3)...");
                HTC_PI.main(new String[] {});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // 5. Start ORC (Lab 4_3 Exercise 3) (Port 1084)
        new Thread(() -> {
            try {
                System.out.println("Starting ORC (Lab 4_3)...");
                ORC.main(new String[] {});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
