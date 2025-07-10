package de.frauas.objects.car.parts;

import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.interfaces.ICarObserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class SensorLogger implements ICarObserver {

    private BufferedWriter writer;
    private static String filename = "logs/output";

    public SensorLogger() {
        reset();
    }
    
    public void reset(){
        
        File file = new File(filename + LocalDateTime.now().toString().replace(":", "_") + ".csv");
        try {
            if (writer != null) {
                writer.close();
            }
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("Timestamp,FrontLeft,FrontMiddle,FrontRight,RearLeft,RearMiddle,RearRight\n");
            writer.write("--------------------------------------------------------------------------------------------------------------\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logMeasurement(int timestamp, int fl, int fm, int fr, int rl, int rm, int rr) {
        try {
            String line = String.format("%d,%d,%d,%d,%d,%d,%d,\n", timestamp, fl, fm, fr, rl, rm, rr);
            writer.write(line);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCarUpdate(CarUpdateInformation info) {
        logMeasurement(
                info.getUsTimestamp(),
                info.getMeasurements().get(0),
                info.getMeasurements().get(1),
                info.getMeasurements().get(2),
                info.getMeasurements().get(3),
                info.getMeasurements().get(4),
                info.getMeasurements().get(5)
        );
    }
}