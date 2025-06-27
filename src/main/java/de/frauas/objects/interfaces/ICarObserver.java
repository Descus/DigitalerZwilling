package de.frauas.objects.interfaces;

import de.frauas.objects.CarUpdateInformation;

public interface ICarObserver {
    void onCarUpdate(CarUpdateInformation info);
}
