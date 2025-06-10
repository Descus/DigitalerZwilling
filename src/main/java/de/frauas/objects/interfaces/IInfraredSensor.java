package de.frauas.objects.interfaces;

import de.frauas.IDrawable;

import javax.sound.midi.Track;

public interface IInfraredSensor extends IDrawable {
    boolean isOnTrack(Track track);
}
