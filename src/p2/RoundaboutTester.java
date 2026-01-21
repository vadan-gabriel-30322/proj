package p2;

import java.util.ArrayList;

import Components.PetriNet;
import Components.PetriNetWindow;
import DataObjects.DataCar;
import DataOnly.Car;

public class RoundaboutTester {

	public static void main(String[] args) throws InterruptedException {
		Roundabout roundabout = new Roundabout();
		roundabout.Delay = 100; // Faster for testing
		
		// Start the Petri Net in a separate thread
		Thread t = new Thread(roundabout);
		t.start();
		
		// Wait for initialization
		Thread.sleep(1000);
		
		System.out.println("Sending Car...");
		
		// Send a car
		Car car = new Car("TestCar", "1", new String[] { "t1", "t6" });
		DataCar dataCar = new DataCar();
		dataCar.SetName("P5");
		dataCar.SetValue(car);
		
		int index = roundabout.util.GetIndexByName("P5", roundabout.PlaceList);
		roundabout.PlaceList.get(index).SetValue(car);
		
		// Monitor for a few seconds
		for (int i = 0; i < 20; i++) {
			Thread.sleep(500);
			roundabout.PrintPetri();
		}
		
		roundabout.Stop();
	}
}
