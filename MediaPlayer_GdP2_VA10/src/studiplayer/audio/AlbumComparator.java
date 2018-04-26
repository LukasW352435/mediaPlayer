package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {
    public int compare(AudioFile af1, AudioFile af2) {
        int ret;
        if(af1 instanceof TaggedFile && af2 instanceof TaggedFile ) {
            if (af1 == null || af2 == null) {

                if (af1 == null && af2 == null) {
                    ret = 0;
                } else if (af1 == null) {
                    ret = -1;
                } else if (af2 == null) {
                    ret = 1;
                }
                throw new NullPointerException("AlbumComparator has an Null Object");
            } else {
                ret = af1.getAuthor().compareTo(af2.getAuthor());
            }
        }else if(af1 instanceof TaggedFile && !(af2 instanceof TaggedFile)){
            ret = 1;
        }else if(!(af1 instanceof TaggedFile)&& af2 instanceof TaggedFile){
            ret = -1;
        }else{
            ret =0;
        }
        return ret;
    }
}
