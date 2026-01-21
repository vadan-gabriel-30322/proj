package lab4_2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class WheatherInput {
    public static void main(String[] args) throws InterruptedException, IOException {
        String basePath = "C:\\Users\\vadan\\Desktop\\DCS\\All_Petri_FW\\PetriInputData\\";
        File plant = new File(basePath + "OutsideTemp.txt");
        Files.deleteIfExists(plant.toPath());
        File RTC = new File(basePath + "RTC.txt");
        Files.deleteIfExists(RTC.toPath());
        File HTC = new File(basePath + "HTC.txt");
        Files.deleteIfExists(HTC.toPath());

        // Scenarios:
        float winterDay[] = new float[] { -12.5F / 25, -15.0F / 25, -17.0F / 25, -20.0F / 25, -21.0F / 25, -19.0F / 25,
                -17.0F / 25, -15.0F / 25, -12.0F / 25, -8.0F / 25, -7.0F / 25, -5.0F / 25, -4.0F / 25, -3.5F / 25,
                -5.0F / 25, -4.0F / 25, -5.0F / 25, -6.0F / 25, -7.5F / 25, -8.5F / 25, -9.0F / 25, -11.0F / 25,
                -11.5F / 25, -12.0F / 25, -12.0F / 25 };

        // -------------------------------
        float roomReference = 24 / 25F;
        float waterReference = 48 / 70F;

        FileWriter fwPlant = new FileWriter(plant.getPath());
        FileWriter fwRTC = new FileWriter(RTC.getPath());
        FileWriter fwHTC = new FileWriter(HTC.getPath());

        for (int i = 0; i < winterDay.length; i++) {
            fwPlant.write("v1:" + winterDay[i] + "F\n");
            fwRTC.write("P1:" + roomReference + "F\n");
            fwHTC.write("P1:" + waterReference + "F\n");
        }

        fwPlant.close();
        fwRTC.close();
        fwHTC.close();
        System.out.println("Done! Input files created in " + basePath);
    }
}
