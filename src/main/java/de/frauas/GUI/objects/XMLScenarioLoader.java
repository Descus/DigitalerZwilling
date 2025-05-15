package de.frauas.GUI.objects;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.awt.geom.Point2D;
import java.io.File;


public class XMLScenarioLoader {
    public static void loadScenario(String xmlFilePath, AxisPanel panel) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("C:\\Users\\Asus\\Documents\\digitalerzwilling\\src\\main\\java\\de\\frauas\\scenario_example.xml"));
            System.out.println(doc);
            doc.getDocumentElement().normalize();

            // Load StartPosition (Car)
            Element startElem = (Element) doc.getElementsByTagName("StartPosition").item(0);
            int x = Integer.parseInt(startElem.getElementsByTagName("xPosition").item(0).getTextContent());
            int y = Integer.parseInt(startElem.getElementsByTagName("yPosition").item(0).getTextContent());
            double heading = Double.parseDouble(startElem.getElementsByTagName("heading").item(0).getTextContent());

            Car car = new Car();
            car.setPositionPoint(x, y);
            car.setHeadingDegree(heading);
            panel.addCar(car);

            // Load path points
            NodeList pointList = doc.getElementsByTagName("Point");
            for (int i = 0; i < pointList.getLength(); i++) {
                Element pointElem = (Element) pointList.item(i);
                double px = Double.parseDouble(pointElem.getElementsByTagName("xPosition").item(0).getTextContent());
                double py = Double.parseDouble(pointElem.getElementsByTagName("yPosition").item(0).getTextContent());
                panel.addPoint(new Point2D.Double(px, py));
            }

            // Load obstacles
            NodeList objectList = doc.getElementsByTagName("Object");
            for (int i = 0; i < objectList.getLength(); i++) {
                Element objElem = (Element) objectList.item(i);
                int x1 = Integer.parseInt(objElem.getElementsByTagName("xPositionStart").item(0).getTextContent());
                int y1 = Integer.parseInt(objElem.getElementsByTagName("yPositionStart").item(0).getTextContent());
                int x2 = Integer.parseInt(objElem.getElementsByTagName("xPositionEnd").item(0).getTextContent());
                int y2 = Integer.parseInt(objElem.getElementsByTagName("yPositionEnd").item(0).getTextContent());
                int heightVal = Integer.parseInt(objElem.getElementsByTagName("height").item(0).getTextContent());

                panel.addObstacle(new Obstacle(x1, y1, x2, y2, heightVal));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
