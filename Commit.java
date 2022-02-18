package gitlet;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * A structure to represent a commit object in this gitlet
 * project.
 * @author Adyant
 */
public class Commit implements Serializable {
    /** Date of making of commit. */
    private String date;

    /** Message of commit. */
    private String message;
    /** Parent of commit. */
    private String parent;
    /** Hash of commit. */
    private String hash;
    /** Blobs of commit. */
    private HashMap<String, String> blobs = new HashMap<>();
    /** Calendar of commit. */
    private Calendar creation;

    /** File of user. */
    private File cwd = new File(System.getProperty("user.dir"));
    /** File of serialized commits. */
    private File _commits = Utils.join(cwd, ".gitlet/commits");
    /** Path to _commits file. */
    private String commitPaths = _commits.getAbsolutePath();

    /** @param mssage is the MESSAGE.
     *  @param prent  is the PARENT.*/
    public Commit(String mssage, String prent) {
        this.parent = prent;
        this.message = mssage;
        if (this.parent == null) {
            this.date = "00:00:00 UTC, Thursday, 1 January 1970";
            getOriginalDate();
        } else {
            this.date = getFirstDate();
        }
        this.hash = Utils.sha1((Object) Utils.serialize(this));
    }

    /** Gets date of commit.
     * @return the date*/
    private String getFirstDate() {
        Calendar calendar = Calendar.getInstance();
        this.creation = calendar;
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss z"
                + ", EEEE, dd MMMM yyyy");
        return f.format(calendar.getTime());
    }

    /** Gets original date of commit. */
    private void getOriginalDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(0);
        this.creation = calendar;
    }

    /** Gets parent of commit.
     * @return the parent. */
    String getParent() {
        return this.parent;
    }
    /** Gets hash of commit.
     * @return the hash. */
    String getHash() {
        return this.hash;
    }

    /** Saves the new hsh as the old HSH. */
    void saveHash(String hsh) {
        this.hash = hsh;
    }

    /** Saves the new MSG as the old MSG. */
    void saveMessage(String msg) {
        this.message = msg;
    }

    /** Saves the new PRNT as the old PRNT. */
    void saveParent(String prnt) {
        this.parent = prnt;
    }

    /** Saves the new BLBS as the old BLBS. */
    void saveBlobs(HashMap<String, String> blbs) {
        this.blobs = blbs;
    }

    /** Gets blobs of commit.
     * @return the blobs. */
    HashMap<String, String> getBlobs() {
        return this.blobs;
    }

    /** Gets the creation of the commit.
     * @return the calendar. */
    Calendar getCreation() {
        return this.creation;
    }

    /** Gets message of commit.
     * @return the message. */
    String getMessage() {
        return this.message;
    }

    /** Gets date of commit.
     * @return the date. */
    String getDate() {
        return this.date;
    }

    /** Saves commit.
     * @throws IOException because this is a serialization function. */
    void saveCommit() throws IOException {
        String path = Utils.join(commitPaths, this.getHash()).getAbsolutePath();
        File commitFile = new File(path);
        Utils.writeObject(commitFile, this);
    }

    /** Adds BLOB to commit. */
    public void addBlob(Blob blob) {
        blobs.put(blob.getName(), blob.getHash());
    }
}
