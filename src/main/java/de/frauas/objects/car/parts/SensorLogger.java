package de.frauas.objects.car.parts;

import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.interfaces.ICarObserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SensorLogger implements ICarObserver {

    private BufferedWriter writer;

    public SensorLogger(String filename, int firstUSTimestamp) {
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write("Timestamp,FrontLeft,FrontMiddle,FrontRight,RearLeft,RearMiddle,RearRight\n");
            writer.write("--------------------------------------------------------------------------------------------------------------\n");
            String line = String.format("%d,0,0,0,0,0,0,\n",firstUSTimestamp);
            writer.write(line);

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

    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCarUpdate(CarUpdateInformation info) {
        logMeasurement(info.getUsTimestamp(), info.getMeasurements().get(0), info.getMeasurements().get(1),info.getMeasurements().get(2),info.getMeasurements().get(3),info.getMeasurements().get(4), info.getMeasurements().get(5));
       System.out.println(info.getUsTimestamp());
    }
}