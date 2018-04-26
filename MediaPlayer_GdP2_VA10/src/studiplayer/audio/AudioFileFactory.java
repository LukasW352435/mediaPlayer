package studiplayer.audio;

public class AudioFileFactory {
    public static AudioFile getInstance(String pathname) throws NotPlayableException{

            AudioFile af = null;

            if ((pathname.toLowerCase()).lastIndexOf(".mp3") == pathname.length() - 4) {
                af = new TaggedFile(pathname);
            } else if ((pathname.toLowerCase()).lastIndexOf(".ogg") == pathname.length() - 4) {
                af = new TaggedFile(pathname);
            } else if ((pathname.toLowerCase()).lastIndexOf(".wav") == pathname.length() - 4) {
                af = new WavFile(pathname);
            } else {
                throw new NotPlayableException(pathname, "Unknow suffix for studiplayer.audio.AudioFile");
            }
            return af;

    }
}
