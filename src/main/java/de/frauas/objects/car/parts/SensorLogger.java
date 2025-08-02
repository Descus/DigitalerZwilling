package de.frauas.objects.car.parts;

import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.interfaces.ICarObserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * The SensorLogger class implements a logging system for ultrasonic sensor measurements of a vehicle.
 * <p>
 * Sensor values are written to a CSV file every time the car is updated.
 * <p>
 * @author Ultrasonic
 */

public class SensorLogger implements ICarObserver {

    /**
     * BufferedWriter used for writing log data to a CSV file.
     * This writer facilitates recording sensor measurements
     * or other relevant data in a structured format.
     * It is initialized or reset within the context of the SensorLogger class.
     * The writer ensures that data is consistently appended to the currently active file
     * and handles output management for the logging process.
     */
    private BufferedWriter writer;
    /**
     * Stores the default file path for the output log file used to record sensor data.
     * <p>
     * This variable is shared across all instances of the SensorLogger class and is used when
     * creating or resetting the log file. The default value defines the relative path to the
     * log file where measurement data will be stored.
     * <p>
     * Modifying this variable affects all operations involving file handling within the SensorLogger class.
     */
    private static final String filename = "logs/output";

    /**
     * Part of Ultrasonic Team
     * Initializes a new instance of the SensorLogger class and prepares it for use.
     * The constructor calls the reset method, which creates a new log file with
     * the current timestamp. If an existing writer is open, it will be closed,
     * and a new writer will be initialized. Additionally, a header line describing
     * the sensor measurement columns is added to the file.
     */
    public SensorLogger() {
        reset();
    }

    /**
     *Part of Ultrasonic Team
     * Creates a new log file with the current timestamp.
     * <p>
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
     * Part of Ultrasonic Team
     * Logs a single measurement entry to the CSV file.
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
     * Part of Ultrasonic Team
     * This method is called whenever the car updates its state.
     * Extracts the sensor values from the CarUpdateInformation object and logs them.
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