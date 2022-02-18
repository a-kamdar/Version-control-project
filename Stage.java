package gitlet;
import java.io.Serializable;
import java.util.HashMap;

/** A structure to represent the Stage and its objects.
 * @author Adyant
 */
public class Stage implements Serializable {
    /** All the addition blobs of this stage. */
    private HashMap<String, String> additionBlobs;
    /** All the removal blobs of this stage. */
    private HashMap<String, String> removalBlobs;
    /** All the potential serializable blobs of this stage. */
    private HashMap<String, Blob> potentialBlobs;

    /** Get all the addition blobs of this stage.
     * @return the addition blobs. */
    HashMap<String, String> getAdditionBlobs() {
        return additionBlobs;
    }
    /** Get all the removal blobs of this stage.
     * @return the removal blobs. */
    HashMap<String, String> getRemovalBlobs() {
        return removalBlobs;
    }
    /** Get all the potential blobs of this stage.
     * @return returns the potential blobs. */
    HashMap<String, Blob> getPotentialBlobs() {
        return potentialBlobs;
    }


    /** Construct this stage. */
    public Stage() {
        additionBlobs = new HashMap<>();
        removalBlobs = new HashMap<>();
        potentialBlobs = new HashMap<>();
    }

    /** BLOB.*/
    public void addToStage(Blob blob) {
        additionBlobs.put(blob.getName(), blob.getHash());
        potentialBlobs.put(blob.getName(), blob);
    }

    /** BLOB.*/
    public void addToRemovalStage(Blob blob) {
        removalBlobs.put(blob.getName(), blob.getHash());
    }

    /** BLOB.*/
    public void removeFromStage(Blob blob) {
        additionBlobs.remove(blob.getName());
        potentialBlobs.remove(blob.getName());
    }

    /** BLOB.*/
    public void removeFromRemovalStage(Blob blob) {
        removalBlobs.remove(blob.getName());
    }

    /** Clears the stage. */
    public void clear() {
        additionBlobs.clear();
        removalBlobs.clear();
        potentialBlobs.clear();
    }
}
