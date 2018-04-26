package studiplayer.audio;// to test the code on Windows
// isWindows() needs to return true;
// sepchar needs to return '\\';
import java.io.File;

public abstract class AudioFile {

	private String filename = "";
	private String pathname = "";
	private String author = "";
	private String title = "";
	private long duration = 0L;

	private boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
		// for testing
		//		return true;
	}

	public AudioFile() {

	}

	public AudioFile(String s) throws NotPlayableException {
		this.parsePathname(s);
		this.parseFilename(this.filename);

		// create File objekt for canRead
		File f = new File(getPathname());
		if (f.canRead()) {
			// file is OK
		} else {
			throw new NotPlayableException(getPathname(),"File is not readable");
		}
	}

	public void parsePathname(String s) {
		char sepchar = java.io.File.separatorChar;
		// only for testing on Linux
		//sepchar = '\\';
		if (!s.equals("")) {
			s = s.replace('\\', sepchar);
			s = s.replace('/', sepchar);
			// clear multiple sepchar
			boolean schleife = true;
			while (schleife) {
				schleife = false;
				for (int i = 1; i < s.length(); i++) {
					if ((s.charAt(i - 1) == sepchar) && (s.charAt(i) == sepchar)) {
						s = s.replace("" + sepchar + sepchar + "", "" + sepchar + "");
						schleife = true;
					}
				}
			}

			if (!isWindows()) {
				if (s.length() >= 3 && s.charAt(1) == ':') {
					s = sepchar + s.substring(0, 1) + sepchar + s.substring(3);
				}
			}  // fehlt noch
			// may be nothing to do


			this.pathname = s;
			if (s.contains("" + sepchar + "")) {
				int i = s.lastIndexOf(sepchar);
				this.filename = s.substring(i + 1);
			} else {
				this.filename = s;
			}
		}
		// System.out.println(pathname);
		// System.out.println(filename);

	}

	public void parseFilename(String s) {
		if (!s.equals("")) {
			if (s.contains(" - ")) {
				int i = s.indexOf(" - ");
				this.author = s.substring(0, i);
				this.title = s.substring(i + 3);

			} else {
				this.author = "";
				this.title = s;
			}
			if (this.title.contains(".")) {
				int i = this.title.lastIndexOf('.');
				this.title = this.title.substring(0, i);
			}
			if (this.title.length() > 0) {
				while ((this.title.charAt(this.title.length() - 1) == ' ') && this.title.length() > 1) {
					this.title = this.title.substring(0, this.title.length() - 1);
				}
				while (this.title.charAt(0) == ' ') {
					if (!(this.title.length() == 1)) {
						this.title = this.title.substring(1);
					} else {
						this.title = "";
						break;
					}
				}
			}
			if (this.author.length() > 0) {
				while ((this.author.charAt(this.author.length() - 1) == ' ') && this.author.length() > 1) {
					this.author = this.author.substring(0, this.author.length() - 1);
				}
				while (this.author.charAt(0) == ' ') {
					if (!(this.author.length() == 1)) {
						this.author = this.author.substring(1);
					} else {
						this.author = "";
						break;
					}
				}
			}
		}
	}

	public abstract void play() throws NotPlayableException;
	public abstract void togglePause();
	public abstract void stop();
	public abstract String getFormattedDuration();
	public abstract String getFormattedPosition();
	public abstract String[] fields();

	public String toString() {
		String ret;
		if (this.author.equals("")) {
			ret = this.title;
		} else if (this.author.equals("") && this.title.equals("")) {
			ret = "";
		} else {
			ret = this.author + " - " + this.title;
		}
		return ret;
	}


	public String getFilename() {
		return this.filename;
	}
	public String getPathname() {
		return this.pathname;
	}
	public String getAuthor() {
		return this.author;
	}
	public String getTitle() {
		return this.title;
	}

	public long getDuration() {
		return this.duration;
	}
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public void setPathname(String pathname) {
        this.pathname = pathname;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setTitle(String title) {
        this.title = title;
    }

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
