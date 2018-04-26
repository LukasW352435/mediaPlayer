package studiplayer.audio;

import studiplayer.basic.TagReader;

import java.util.Map;


public class TaggedFile extends SampledFile {
    private String album = "";

    public TaggedFile(){
        super();
    }

    public TaggedFile(String s) throws NotPlayableException{
        super(s);
        readAndStoreTags(this.getPathname());

    }

    public String[] fields() {
        String[] as ={this.getAuthor(),this.getTitle(),this.getAlbum(),this.getFormattedDuration()};
        return as;
    }

    public void readAndStoreTags(String pathname) throws NotPlayableException{
        try {
            Map<String,Object> tag_map = TagReader.readTags(pathname);
        for(String key: tag_map.keySet()) {
            if(key.equals("title") && !tag_map.get(key).equals("")) {
                this.setTitle(((String)tag_map.get(key)).trim());
            }
            if(key.equals("author") && !tag_map.get(key).equals("")){
                this.setAuthor(((String)tag_map.get(key)).trim());
            }
            if(key.equals("album")){
                this.setAlbum(((String)tag_map.get(key)).trim());
            }
            if(key.equals("duration")){
                this.setDuration((long)tag_map.get(key));
            }
        }
        }catch (Exception e){
            throw new NotPlayableException(pathname,"Tags not readable",e);
        }
    }

    public String toString(){
        if(this.getAlbum().equals("")) {
            return super.toString()+" - "+this.getFormattedDuration();
        }else {
            return super.toString()+" - "+this.getAlbum()+" - "+this.getFormattedDuration();
        }

    }

    public String getAlbum() {
        return this.album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
}
