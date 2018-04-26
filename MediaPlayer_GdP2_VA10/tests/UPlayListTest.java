import org.junit.Ignore;
import org.junit.Test;
import studiplayer.audio.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UPlayListTest {
    @Ignore
    public void test_loadFromM3U_02() throws Exception{
        String m3u_pathname = "playlist.m3u";
        String mp3_pathname = "corrupt.mp3";

        // Create the M3U file with one entry for a non-existent mp3 file
        FileWriter writer = null;
        try {
            // Create a FileWriter
            writer = new FileWriter(m3u_pathname);
            writer.write(mp3_pathname+System.getProperty("line.separator"));
        }catch (IOException e){
            throw new RuntimeException("Unable to stor M3U file: "+m3u_pathname,e);
        }finally {
            try {
                writer.close();
            }catch (IOException e){
                // Just swallow
            }
        }
        // Ok , the playlist for testing is in place
        PlayList pl = new PlayList();
        // The next statement will cause a stack trace to be printed onto
        // the console. However, execution is not terminated with an error
        // since we catch the exception in studiplayer.audio.PlayList.loadFromM3U()
        // The test succeds.
        pl.loadFromM3U(m3u_pathname);
        // cleanup
        new File(m3u_pathname).delete();
    }
    @Test
    public void test_sort_byTitle_01() throws Exception{
        PlayList pl1 = new PlayList();
        // Populate the playlist
        pl1.add(new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
        pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
        pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
        pl1.add(new TaggedFile("audiofiles/tanom p2 journey.mp3"));
        pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
        // Sort the playlist by title
        pl1.sort(SortCriterion.TITLE);
        // Store the toString() strings of the sorted play lists into an array
        // and compare the arrays
        // Note: "T*" < "t*"
        String exp[] = new String[]{
                "Eisbach - Deep Snow - The Sea, the Sky - 03:18",
                "Eisbach - Rock 812 - The Sea, the Sky - 05:31",
                "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55",
                "Wellenmeister - TANOM Part II: Journey - TheAbsoluteNecessityOfMeaning - 02:52",
                "wellenmeister - tranquility - 02:21"
        };
        String sorted[] = new String[5];
        int i = 0;
        for(AudioFile af : pl1){
            sorted[i] = af.toString();
            i++;
        }
        assertArrayEquals("Wrong sorting by title",exp,sorted);

    }
    @Test
    public void test_sort_byDuration_01() throws Exception{
        PlayList pl1 = new PlayList();
        // Populate the playlist
        pl1.add(new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
        pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
        pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
        pl1.add(new TaggedFile("audiofiles/tanom p2 journey.mp3"));
        pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
        // Sort the playlist by title
        pl1.sort(SortCriterion.DURATION);
        // Store the toString() strings of the sorted play lists into an array
        // and compare the arrays
        // Note: "T*" < "t*"
        String exp[] = new String[]{
                "wellenmeister - tranquility - 02:21",
                "Wellenmeister - TANOM Part II: Journey - TheAbsoluteNecessityOfMeaning - 02:52",
                "Eisbach - Deep Snow - The Sea, the Sky - 03:18",
                "Eisbach - Rock 812 - The Sea, the Sky - 05:31",
                "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55"
        };
        String sorted[] = new String[5];
        int i = 0;
        for(AudioFile af : pl1){
            sorted[i] = af.toString();
            i++;
        }
        assertArrayEquals("Wrong sorting by title",exp,sorted);

    }
}