package de.frauas.objects.car.parts;

import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.interfaces.CarObserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SensorLogger implements CarObserver {

    private BufferedWriter writer;

    public SensorLogger(String filename) {
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write("Timestamp,FrontLeft,FrontMiddle,FrontRight,RearLeft,RearMiddle,RearRight\n");
            writer.write("--------------------------------------------------------------------------------------------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logMeasurement(long timestamp, int fl, int fm, int fr, int rl, int rm, int rr) {
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
        for (Integer i : info.getMeasurements()) {

        }
    }
}