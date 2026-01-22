package final_project;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import DataObjects.DataCar;
import DataOnly.Car;
import Utilities.DataOverNetwork;

public class RunTest {

    public static void main(String[] args) {
        System.out.println("[RunTest] Starting System via RunAll...");
        RunAll.main(args);

        // Test Scenario Thread
        new Thread(() -> {
            try {
                System.out.println("[RunTest] Waiting 5 seconds for system initialization...");
                Thread.sleep(5000);

                // Test 1: Inject Car into Intersection 1
                System.out.println(
                        "[RunTest] Executing Test 1: Inject 'TestCar1' into Intersection 1 (Port 1081, P_a1)...");
                sendCar("P_a1", "Car", "TestCar1", 1081);

                // Optional: Wait and send another
                // Thread.sleep(2000);
                // sendCar("P_a2", "Bus", "TestBus1", 1081);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void sendCar(String placeName, String model, String number, int port) {
        try {
            Socket s = new Socket(InetAddress.getByName("localhost"), port);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

            DataOverNetwork packet = new DataOverNetwork();
            DataCar dataCar = new DataCar();
            dataCar.SetName(placeName);

            // Create Car with empty targets list to avoid null pointer issues
            dataCar.SetValue(new Car(model, number, new ArrayList<String>()));

            packet.petriObject = dataCar;
            packet.NetWorkPort = port;

            oos.writeObject(packet);
            System.out.println("[RunTest] Sent " + model + " (" + number + ") to Port " + port + " Place " + placeName);

            s.close();
        } catch (IOException e) {
            System.err.println("[RunTest] Failed to connect to port " + port);
            e.printStackTrace();
        }
    }
}
