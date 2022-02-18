package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * The class representation of a Blob in this gitlet function.
 * @author Adyant
 */
public class Blob implements Serializable {
    /** Name of this file. */
    private String name = "";
    /** Contents of this file. */
    private String contents = "";
    /** Hash of this file. */
    private String hash = "";

    /** THENAME. Gets name of this file.
     * @return returns the name */
    public String getName() {
        return name;
    }

    /** THECONTENTS. Gets contents of this file.
     * @return returns the contents */
    public String getContents() {
        return contents;
    }

    /** @param nme is the name.
     *  @param cntents CONTENTS. */
    public Blob(String nme, String cntents) {
        this.name = nme;
        this.contents = cntents;
        this.hash = Utils.sha1((Object) Utils.serialize(this));
    }

    /** Gets hash of this file.
     * @returns the hash */
    String getHash() {
        return this.hash;
    }

    /** Saves Blob.
     * @throws IOException as this is a serialization function. */
    public void saveBlob() throws IOException {
        File cwd = new File(System.getProperty("user.dir"));
        File blobs = Utils.join(cwd, ".gitlet/blobs");
        String path = Utils.join(blobs, this.getHash()).getAbsolutePath();
        File blobFile = new File(path);
        Utils.writeObject(blobFile, this);
    }

}
