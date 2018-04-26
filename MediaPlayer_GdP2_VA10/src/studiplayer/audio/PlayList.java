package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static studiplayer.audio.SortCriterion.*;

public class PlayList extends LinkedList<AudioFile> {
    private int current;
    private boolean randomOrder;


    public PlayList(){
        this.setCurrent(0);
        this.setRandomOrder(false);
    }
    public PlayList( String m3uFile){
        new PlayList();
        this.loadFromM3U(m3uFile);
    }

    public AudioFile getCurrentAudioFile(){
        try {
            return this.get(this.current);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }
    public void changeCurrent(){
        this.current++;
        if(getCurrentAudioFile()==null){
            this.current = 0;
            if(this.randomOrder) {
                Collections.shuffle(this);
            }
        }

    }
    public void setRandomOrder(boolean randomOrder){
        this.randomOrder = randomOrder;
        if(randomOrder){
            Collections.shuffle(this);
        }
    }
    public void saveAsM3U(String pathname){
        // date time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter df;
        // end date time
        FileWriter writer = null;
        String linesep = System.getProperty("line.separator");
        try{
            writer = new FileWriter(pathname);
            // date time
            df = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");
            writer.write("#"+now.format(df)+linesep+linesep);
            // end date time
            int i=0;
            while ((this.size()>=1)&&(this.size()-1>=i)){
                writer.write((this.get(i)).getPathname()+linesep);
                i++;
            }
        }catch (IOException e){
            throw new RuntimeException("Unable to write to file "+pathname+":"+e.getMessage());
        }finally {
            try {
                writer.close();
            }catch (Exception e){
                // Just swallow any excaption caused by finally block
            }
        }
    }
    public void loadFromM3U(String pathname){
        this.clear();
        Scanner scanner = null;
        String line;

        try {
            scanner = new Scanner(new File(pathname));
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if((line.length()>=4)){
                    if((line.charAt(0)!= '#')&&((line.trim()).length()>0)){
                        try {
                            AudioFile af;
                            af = AudioFileFactory.getInstance(line);
                            if(af != null){
                                this.add(af);
                            }
                        }catch (NotPlayableException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                scanner.close();
            }catch (Exception e){
                // Just swallow any excaption
            }
        }
    }
    public void sort(SortCriterion order){
        if(order == AUTHOR){
            Collections.sort(this,new AuthorComparator());
        }else if (order == TITLE){
            Collections.sort(this,new TitleComparator());
        }else if (order == ALBUM){
            Collections.sort(this,new AlbumComparator());
        }else if (order == DURATION){
            Collections.sort(this,new DurationComparator());
        }
    }

    // get
    public int getCurrent() {
        return this.current;
    }
    // set
    public void setCurrent(int currentPosition) {
        this.current = currentPosition;
    }

}
