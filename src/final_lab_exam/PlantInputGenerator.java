package final_lab_exam;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import DataObjects.DataFuzzy;
import DataOnly.Fuzzy;
import DataOnly.FuzzyVector;
import Utilities.DataOverNetwork;

public class PlantInputGenerator {

    public static void main(String[] args) {
        // Send initial input 'u' = 0.0 to Plant (Port 1085)
        sendInput("u", 0.0F, 1085);

        // Example: Send sequence of inputs to test Plant response
        // sendInput("u", 0.5F, 1085);
        // sendInput("u", -0.5F, 1085);
    }

    public static void sendInput(String placeName, float value, int port) {
        try {
            Socket s = new Socket(InetAddress.getByName("localhost"), port);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

            DataOverNetwork dataToSend = new DataOverNetwork();
            DataFuzzy dataFuzzy = new DataFuzzy();
            dataFuzzy.SetName(placeName);
            dataFuzzy.SetValue(new Fuzzy(new FuzzyVector(0f, 0f, 0f, 0f, 0f), value));

            dataToSend.petriObject = dataFuzzy;
            dataToSend.NetWorkPort = port;

            oos.writeObject(dataToSend);
            System.out.println("Sent input: " + placeName + " = " + value + " to Port: " + port);

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
