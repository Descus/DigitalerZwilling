package de.frauas.objects.interfaces;

import de.frauas.objects.CarUpdateInformation;

public interface CarObserver {
    void onCarUpdate(CarUpdateInformation info);
}
