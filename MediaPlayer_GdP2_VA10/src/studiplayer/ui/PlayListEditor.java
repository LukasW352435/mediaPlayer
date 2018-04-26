package studiplayer.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;

import studiplayer.audio.AudioFileFactory;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

@SuppressWarnings("serial")
public class PlayListEditor extends JFrame implements ActionListener,
        MouseListener {
    private PlayList playList;

    private Player player;

    private class PlayListTableModel extends AbstractTableModel {

        public int getColumnCount() {
            return 4;
        }

        public int getRowCount() {
            return playList.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return playList.get(rowIndex).fields()[columnIndex];
        }

        public void sortByColumn(final int clm) {
            if (clm == 0)
                playList.sort(SortCriterion.AUTHOR);
            else if (clm == 1)
                playList.sort(SortCriterion.TITLE);
            else if (clm == 2)
                playList.sort(SortCriterion.ALBUM);
            else if (clm == 3)
                playList.sort(SortCriterion.DURATION);
        }
    }

    private static final String AC_ADD = "add";

    private static final String AC_SUB = "sub";

    private static final String AC_RAND = "rand";

    private PlayListTableModel model;

    private JTable table;

    private String lastDir;

    private JButton random;

    private boolean rand = false;

    ImageIcon seqIcon = new ImageIcon("icons/random_no.png");
    ImageIcon randIcon = new ImageIcon("icons/random.png");

    PlayListEditor(Player player, PlayList playList) {
        this.player = player;
        this.playList = playList;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Playlist - " + playList.size() + " Songs");
        model = new PlayListTableModel();
        table = new JTable(model);
        table.setToolTipText("Einfachklick selektiert, Doppelklick spielt");
        table.getColumnModel().getColumn(0).setHeaderValue("Interpret");
        table.getColumnModel().getColumn(1).setHeaderValue("Titel");
        table.getColumnModel().getColumn(2).setHeaderValue("Album");
        table.getColumnModel().getColumn(3).setHeaderValue("Laenge");
        table.addMouseListener(this);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                model.sortByColumn(table.columnAtPoint(evt.getPoint()));
            }
        });

        add(new JScrollPane(table));
        JPanel bottomPanel = new JPanel();
        JButton add = new JButton(new ImageIcon("icons/add.png"));
        add.setActionCommand(AC_ADD);
        add.addActionListener(this);
        add.setToolTipText("Eintrag zur Playlist hinzufuegen");
        JButton sub = new JButton(new ImageIcon("icons/sub.png"));
        sub.setActionCommand(AC_SUB);
        sub.addActionListener(this);
        sub.setToolTipText("Auswahl aus Playlist entfernen");
        random = new JButton(seqIcon);
        random.setActionCommand(AC_RAND);
        random.addActionListener(this);
        random.setToolTipText("Zufallsmodus aus/ein");
        bottomPanel.add(add);
        bottomPanel.add(sub);
        bottomPanel.add(random);
        add(bottomPanel, BorderLayout.SOUTH);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals(AC_ADD)) {
            JFileChooser fileChooser = new JFileChooser(lastDir);
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    String[] knownExts = new String[] { ".wav", ".mp3", ".ogg" };
                    String lc = f.getName().toLowerCase();
                    if (f.isDirectory())
                        return true;
                    for (String s : knownExts)
                        if (lc.endsWith(s))
                            return true;
                    return false;
                }

                public String getDescription() {
                    return "Sound-Dateien";
                }
            });
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    File[] files = fileChooser.getSelectedFiles();
                    for (File f : files) {
                        playList.add(AudioFileFactory.getInstance(f
                                .getAbsolutePath()));
                    }
                } catch (NotPlayableException ex) {
                    ex.printStackTrace();
                }

                lastDir = fileChooser.getSelectedFile().getParent();
            }
            updateTable();
        } else if (cmd.equals(AC_SUB)) {
            int row = table.getSelectedRow();
            if (row < 0)
                return;
            playList.remove(row);
            updateTable();
        } else if (cmd.equals(AC_RAND)) {
            rand = !rand;
            playList.setRandomOrder(rand);
            if (rand) {
                random.setIcon(randIcon);
            } else {
                random.setIcon(seqIcon);
            }
        }
    }

    private void updateTable() {
        model.fireTableDataChanged();
        setTitle("Playlist - " + playList.size() + " Songs");
        playList.saveAsM3U(Player.DEFAULT_PLAYLIST);
    }

    public void mouseClicked(MouseEvent arg0) {
        if (arg0.getClickCount() > 1) {
            playList.setCurrent(table.getSelectedRow());
            player.actionPerformed(new ActionEvent(player, 0, "AC_STOP"));
            player.actionPerformed(new ActionEvent(player, 0, "AC_PLAY"));
        }
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }
}
