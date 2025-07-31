package de.frauas.objects.car.parts;

import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.interfaces.ICarObserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Done by Ultrasonic Team
 *
 * The SensorLogger class implements a logging system for ultrasonic sensor measurements of a vehicle.
 *
 * Sensor values are written to a CSV file every time the car is updated.
 */

public class SensorLogger implements ICarObserver {

    private BufferedWriter writer;
    private static String filename = "logs/output";

    public SensorLogger() {
        reset();
    }

    /**
     *Done by Ultrasonic Team
     *
     * Creates a new log file with the current timestamp.
     *
     * If a writer is already open, it will be closed first.
     * The new file includes a header line describing the columns.
     */

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
    /**
     * Done by Ultrasonic Team
     *
     * Logs a single measurement entry to the CSV file.
     *
     * @param timestamp The timestamp of the measurement
     * @param fl        Distance from the front left sensor
     * @param fm        Distance from the front middle sensor
     * @param fr        Distance from the front right sensor
     * @param rl        Distance from the rear left sensor
     * @param rm        Distance from the rear middle sensor
     * @param rr        Distance from the rear right sensor
     */

    public void logMeasurement(int timestamp, int fl, int fm, int fr, int rl, int rm, int rr) {
        try {
            String line = String.format("%d,%d,%d,%d,%d,%d,%d,\n", timestamp, fl, fm, fr, rl, rm, rr);
            writer.write(line);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Done by Ultrasonic Team
     *
     * This method is called whenever the car updates its state.
     *
     * Extracts the sensor values from the CarUpdateInformation object and logs them.
     *
     * @param info Object containing the latest timestamp and ultrasonic measurements
     */

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