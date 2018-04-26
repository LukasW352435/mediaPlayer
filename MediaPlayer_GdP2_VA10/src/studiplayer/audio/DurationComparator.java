package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {
    public int compare(AudioFile af1, AudioFile af2) {
        int ret;
        if (af1 == null || af2 == null) {
            if (af1 == null && af2 == null) {
                ret = 0;
            } else if (af1 == null) {
                ret = -1;
            } else if (af2 == null) {
                ret = 1;
            }
            throw new NullPointerException("DurationComparator has an Null Object");
        } else {
            ret = af1.getFormattedDuration().compareTo(af2.getFormattedDuration());
        }
        return ret;
    }
}
