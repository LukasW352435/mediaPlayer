package studiplayer.ui;

import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Player extends JFrame implements ActionListener {
    private PlayList playList;
    private PlayListEditor playListEditor;
    private boolean editorVisible;
    public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
    //public static final String DEFAULT_PLAYLIST = "playlists/playList.cert.m3u";

    private String no_title = "Studiplayer: empty play list";
    private String no_songDescription = "no current song";
    private String no_playTime = "--:--";

    private JButton bplay;
    private JButton bpause;
    private JButton bstop;
    private JLabel songDescription;
    private JLabel playTime;

    private volatile boolean stopped;

    public Player(PlayList playList) {
        this.playList = playList;
        playListEditor = new PlayListEditor(this,this.playList);
        editorVisible = false;
        // Initialize the main frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            e.printStackTrace();
        }
        // Create GUI components

        // Create the buttons
        JPanel buttons = new JPanel();
        bplay = new JButton(new ImageIcon("icons/play.png"));
        bplay.setActionCommand("AC_PLAY");
        bplay.addActionListener(this);
        buttons.add(bplay);
        bpause = new JButton(new ImageIcon("icons/pause.png"));
        bpause.setActionCommand("AC_PAUSE");
        bpause.addActionListener(this);
        buttons.add(bpause);
        bstop = new JButton(new ImageIcon("icons/stop.png"));
        bstop.setActionCommand("AC_STOP");
        bstop.addActionListener(this);
        buttons.add(bstop);
        JButton bnext = new JButton(new ImageIcon("icons/next.png"));
        bnext.setActionCommand("AC_NEXT");
        bnext.addActionListener(this);
        buttons.add(bnext);
        JButton bpl_editor = new JButton(new ImageIcon("icons/pl_editor.png"));
        bpl_editor.setActionCommand("AC_EDITOR");
        bpl_editor.addActionListener(this);
        buttons.add(bpl_editor);

        // Create the Labels
        songDescription = new JLabel();
        playTime = new JLabel();
        // Add components to frame
        this.add(buttons,BorderLayout.EAST);
        this.add(songDescription,BorderLayout.NORTH);
        this.add(playTime,BorderLayout.WEST);

        // initialize all labels and buttons
        if(playList.size()>0) {
            updateSongInfo(playList.getCurrentAudioFile());
        }else {
            updateSongInfo(null);
        }
        bplay.setEnabled(true);
        bpause.setEnabled(false);
        bstop.setEnabled(false);
        bnext.setEnabled(true);
        bpl_editor.setEnabled(true);

        // initialize attributs
        this.stopped = true;

        // Activate GUI
        this.pack();
        this.setVisible(true);
    }
    public static void main(String[] args){
        PlayList pl = new PlayList();
        if (args.length >1){
            pl.loadFromM3U(args[1]);
        }else {
            pl.loadFromM3U(DEFAULT_PLAYLIST);
        }
        new Player(pl);
    }
    public void actionPerformed(ActionEvent e){
        AudioFile af;
        String cmd = e.getActionCommand();
        if(cmd.equals("AC_PLAY")){
            if(playList.size()>0) {
                bplay.setEnabled(false);
                bpause.setEnabled(true);
                bstop.setEnabled(true);
                playCurrentSong();
            }
        }else if(cmd.equals("AC_PAUSE")){
            if(playList.size() > 0) {
                bplay.setEnabled(false);
                bpause.setEnabled(true);
                bstop.setEnabled(true);
                playList.getCurrentAudioFile().togglePause();
                updateSongInfo(playList.getCurrentAudioFile());
            }
        }else if(cmd.equals("AC_STOP")){
            bplay.setEnabled(true);
            bpause.setEnabled(false);
            bstop.setEnabled(false);
            stopCurrentSong();
        }else if(cmd.equals("AC_NEXT")){
            if(playList.size()>0) {
                bplay.setEnabled(false);
                bpause.setEnabled(true);
                bstop.setEnabled(true);
                //System.out.println("Switching to next audio file");
                if (!stopped) {
                    // We are playing
                    // Stop playing the last song
                    stopCurrentSong();
                }
                // Now, we are stopped and not playing
                // Move on to the next song in the playlist
                this.playList.changeCurrent();
                // Play the next song
                playCurrentSong();
            /*
            // For Info: Get the current song from the list
            af = playList.getCurrentAudioFile();
            if(af != null){
                System.out.println("Switched to next audio file");
            }else {
                System.out.println("PlayList is empty");
            }
            System.out.println("");
            */
            }
        } else if(cmd.equals("AC_EDITOR")){
            if(editorVisible){
                editorVisible=false;
            }else {
                editorVisible = true;
            }
            playListEditor.setVisible(editorVisible);
        }
    }
    public void updateSongInfo(AudioFile af){
        if (af == null){
            setTitle(no_title);
            songDescription.setText(no_songDescription);
            playTime.setText(no_playTime);
        }else {
            setTitle("Current song: " + af.toString());
            songDescription.setText(af.toString());
            if (!stopped) {
                playTime.setText(af.getFormattedPosition());
            }else {
                playTime.setText("00:00");
            }
        }
    }
    private void playCurrentSong(){
        stopped = false;
        updateSongInfo(playList.getCurrentAudioFile());
        if(playList.getCurrentAudioFile() != null){
            (new TimerThread()).start();
            (new PlayerThread()).start();
        }
    }
    private void stopCurrentSong(){
        stopped = true;
        if(playList.getCurrentAudioFile() != null ){
            playList.getCurrentAudioFile().stop();
        }
        updateSongInfo(playList.getCurrentAudioFile());
    }

    private class TimerThread extends Thread{
        public void run(){
            while (!stopped && playList.size()>0){
                playTime.setText((playList.getCurrentAudioFile()).getFormattedPosition());
                try {
                    sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    private class PlayerThread extends Thread{
        public void run(){
            while (!stopped && playList.size()>0){
                try {
                    (playList.getCurrentAudioFile()).play();
                }catch (NotPlayableException e){
                    e.printStackTrace();
                }
                if(!stopped){
                    playList.changeCurrent();
                    updateSongInfo(playList.getCurrentAudioFile());
                }
            }
        }
    }
}
