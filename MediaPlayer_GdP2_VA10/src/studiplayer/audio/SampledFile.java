package studiplayer.audio;

import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile{
    public SampledFile(){
        super();
    }
    public SampledFile(String s) throws NotPlayableException{
            super(s);
    }
    public void play() throws NotPlayableException {
        try {
            BasicPlayer.play(getPathname());
        }catch (Exception e){
            throw new NotPlayableException(getPathname(),"File is not playable",e);
        }



    }

    public void togglePause() {
        BasicPlayer.togglePause();
    }

    public void stop() {
        BasicPlayer.stop();
    }

    public String getFormattedDuration() {
        return timeFormatter(this.getDuration());
    }

    public String getFormattedPosition() {
        return timeFormatter(studiplayer.basic.BasicPlayer.getPosition());
    }
    public static String timeFormatter(long microtime){
        if(microtime<0){
            throw new RuntimeException("Negativ time value provided");
        }else if(microtime>=6000000000L){
            throw new RuntimeException("Time value exceeds allowed format");
        }else {
            return String.format("%02d:%02d", (int) (microtime / Math.pow(10, 6) / 60), (int) (microtime / Math.pow(10, 6) % 60));
        }
    }

}
