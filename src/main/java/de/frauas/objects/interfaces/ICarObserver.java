package de.frauas.objects.interfaces;

import de.frauas.objects.CarUpdateInformation;

// Interface for all Teams, implements the Observer pattern and notifys Observers, if any Information from the Cars Parts changes
public interface ICarObserver {
    void onCarUpdate(CarUpdateInformation info);
}
