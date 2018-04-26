import org.junit.Test;
import studiplayer.audio.AudioFileFactory;
import studiplayer.audio.NotPlayableException;

import static org.junit.Assert.*;

public class UAudioFileFactoryTest {
    @Test
    public void test_getInstance_01() throws Exception{
        try {
            AudioFileFactory.getInstance("unknown.xxx");
            fail("Unknow suffix; expecting exception");
        }catch (NotPlayableException e){
            // Expected
        }
    }
    @Test
    public void test_getInstance_02() throws Exception{
        try {
            AudioFileFactory.getInstance("nonexistent.mp3");
            fail("File is not readable; expecting exception");
        }catch (NotPlayableException e){
            // Expected
        }
    }
}