package gitlet;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

/** Structure to implement all functions of gitlet.
 * @author Adyant */
public class Functions implements Serializable {
    /** The current directory. */
    private File cwd;
    /** Gets the current directory.
     * @return the cwd. */
    File getCwd() {
        return cwd;
    }
    /** Saves the current dir CW. */
    void saveCwd(File cw) {
        this.cwd = cw;
    }
    /** The gitlet directory. */
    private File gitlet;
    /** Gets the current gitlet dir.
     * @return gets the gitlet. */
    File getGitlet() {
        return gitlet;
    }
    /** Saves the current dir GIT. */
    void saveGitlet(File git) {
        this.gitlet = git;
    }
    /**The commits directory. */
    private File commits;
    /** Gets the current commits dir.
     * @return the commits. */
    File getCommits() {
        return commits;
    }
    /** Saves the current dir COMMIS. */
    void saveCommits(File commis) {
        this.commits = commis;
    }
    /**The current graph. */
    private CommitGraph repo;
    /** Gets the current repo.
     * @return the repo. */
    CommitGraph getRepo() {
        return repo;
    }
    /** Saves the current repo REP. */
    void saveRepo(CommitGraph rep) {
        this.repo = rep;
    }
    /**The blobs directory. */
    private File blobs;
    /** Gets the current directory.
     * @return the blobs. */
    File getBlobs() {
        return blobs;
    }
    /** Saves the current dir BLBS. */
    void saveBlobs(File blbs) {
        this.blobs = blbs;
    }
    /**The current stage. */
    private Stage stageArea;
    /** Gets the current directory.
     * @return the stage. */
    Stage getStageArea() {
        return stageArea;
    }
    /** Saves the current stage STGE. */
    void saveStage(Stage stge) {
        this.stageArea = stge;
    }

    /**The current constructor. */
    Functions() {
        cwd = new File(System.getProperty("user.dir"));
        gitlet = null;
        commits = null;
        repo = null;
        blobs = null;
        stageArea = null;
    }

    /**
     Initializing function for gitlet.
     */
    void init() throws IOException {
        if (Utils.join(cwd, ".gitlet").exists()) {
            throw new GitletException("A Gitlet version-control"
                    + " system already exists in the current directory.");
        }
        gitlet = Utils.join(cwd, ".gitlet");
        gitlet.mkdir();
        commits = Utils.join(gitlet, "/commits");
        commits.mkdir();
        blobs = Utils.join(gitlet, "/blobs");
        blobs.mkdir();
        Commit first = new Commit("initial commit", null);
        repo = new CommitGraph(first);
        stageArea = new Stage();
    }

    /** FILENAME.
     * @throws IOException due to serializability.
     * */
    void add(String fileName) throws IOException {
        File check = Utils.join(cwd, fileName);
        if (!check.exists()) {
            throw new GitletException("File does not exist.");
        }
        File file = Utils.join(cwd, fileName);
        String contents = Utils.readContentsAsString(file);
        Blob newFile = new Blob(fileName, contents);

        Commit currentHead = getCommit(repo.head());
        if (currentHead.getBlobs().containsKey(newFile.getName())) {
            if (!currentHead.getBlobs().get(
                    newFile.getName()).equals(newFile.getHash())) {
                stageArea.addToStage(newFile);
            } else {
                if (stageArea.getAdditionBlobs().containsKey(
                        newFile.getName())) {
                    stageArea.removeFromStage(newFile);
                }
            }
        } else {
            stageArea.addToStage(newFile);
        }

        if (stageArea.getRemovalBlobs().containsKey(newFile.getName())) {
            stageArea.getRemovalBlobs().remove(newFile.getName());
        }
    }

    /** MESSAGE.
     * @throws IOException due to serializability.*/
    void commit(String message) throws IOException {
        if (stageArea.getAdditionBlobs().keySet().isEmpty()
                && stageArea.getRemovalBlobs().keySet().isEmpty()) {
            throw new GitletException("No changes added to the commit.");
        }
        if (message == null || message.isEmpty()) {
            throw new GitletException("Please enter a commit message.");
        }
        Commit currentHead = getCommit(repo.head());
        Commit nextCommit = commitClone(currentHead);
        for (String key : stageArea.getAdditionBlobs().keySet()) {
            if (nextCommit.getBlobs().containsKey(key)) {
                String oldCommitBlobHash = nextCommit.getBlobs().get(key);
                String stageAreaBlobHash = stageArea.getAdditionBlobs().
                        get(key);
                if (!oldCommitBlobHash.equals(stageAreaBlobHash)) {
                    nextCommit.getBlobs().put(key, stageAreaBlobHash);
                }
            } else {
                nextCommit.getBlobs().put(key,
                        stageArea.getAdditionBlobs().get(key));
            }
        }
        for (String key : stageArea.getRemovalBlobs().keySet()) {
            if (nextCommit.getBlobs().containsKey(key)) {
                nextCommit.getBlobs().remove(key);
            }
        }
        nextCommit.saveMessage(message);
        nextCommit.saveParent(repo.head());
        nextCommit.saveHash(Utils.sha1(Utils.serialize(nextCommit)));
        for (String name : stageArea.getPotentialBlobs().keySet()) {
            Blob current  = stageArea.getPotentialBlobs().get(name);
            current.saveBlob();
        }
        repo.addToGraph(nextCommit);
        stageArea.clear();
    }

    /** FILENAME.
     * */
    void rm(String fileName) {
        Commit headCommit = getCommit(repo.head());
        if (!stageArea.getAdditionBlobs().containsKey(fileName)
                && !headCommit.getBlobs().containsKey(fileName)) {
            throw new GitletException("No reason to remove the file.");
        }
        if (stageArea.getAdditionBlobs().containsKey(fileName)) {
            stageArea.getAdditionBlobs().remove(fileName);
            stageArea.getPotentialBlobs().remove(fileName);
        }
        if (headCommit.getBlobs().containsKey(fileName)) {
            String blobHashCode = headCommit.getBlobs().get(fileName);
            stageArea.getRemovalBlobs().put(fileName, blobHashCode);
            Utils.restrictedDelete(fileName);
        }
    }

    /** The log function.
     * */
    void log() {
        SimpleDateFormat f = new SimpleDateFormat("EE MMM dd HH:mm:ss yyyy Z");
        f.setTimeZone(TimeZone.getDefault());
        Commit head = getCommit(repo.head());
        Commit pointer = head;

        while (pointer != null) {
            System.out.println("===");
            System.out.println("commit " + pointer.getHash());
            System.out.println("Date: " + f.format(pointer.getCreation()
                    .getTime()));
            if (pointer.getParent() != null) {
                System.out.println(pointer.getMessage() + "\n");
                pointer = getCommit(pointer.getParent());
            } else {
                System.out.println(pointer.getMessage());
                break;
            }
        }
    }

    /** The global log function.
     * */
    void globalLog() {
        SimpleDateFormat f = new SimpleDateFormat("EE MMM dd HH:mm:ss yyyy Z");
        f.setTimeZone(TimeZone.getDefault());
        File[] allCommits = commits.listFiles();
        if (allCommits == null) {
            throw new GitletException("No commits seem to have been made yet.");
        }
        for (File file : allCommits) {
            String fileHash = file.getName();
            Commit current = getCommit(fileHash);

            System.out.println("===");
            System.out.println("commit " + current.getHash());
            System.out.println("Date: " + f.format(current.getCreation().
                    getTime()));
            System.out.println(current.getMessage() + "\n");
        }
    }

    /** MESSAGE.
     * The find function.
     * */
    void find(String message) {
        File[] allCommits = commits.listFiles();
        if (allCommits == null) {
            throw new GitletException("Found no commit with that message.");
        }
        boolean existed = false;
        for (File file : allCommits) {
            String fileHash = file.getName();
            Commit current = getCommit(fileHash);
            if (current.getMessage().equals(message)) {
                System.out.println(current.getHash());
                existed = true;
            }
        }
        if (!existed) {
            throw new GitletException("Found no commit with that message.");
        }
    }

    /** The status function.
     * */
    void status() {
        System.out.println("=== Branches ===");
        Set<String> branches = repo.getBranchLabels();
        TreeSet<String> temp = new TreeSet<>();
        temp.addAll(branches);
        for (String branch : temp) {
            if (branch.equals(repo.getCurrentBranch())) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        temp.clear();
        System.out.println("");
        System.out.println("=== Staged Files ===");
        HashMap<String, String> nottemp = stageArea.getAdditionBlobs();
        Set<String> files = stageArea.getAdditionBlobs().keySet();
        temp.addAll(files);
        for (String file : temp) {
            System.out.println(file);
        }
        temp.clear();
        System.out.println("");
        System.out.println("=== Removed Files ===");
        Set<String> removeFiles = stageArea.getRemovalBlobs().keySet();
        temp.addAll(removeFiles);
        for (String file : temp) {
            System.out.println(file);
        }
        temp.clear();
        System.out.println("");
        System.out.println("=== Modifications Not Staged For Commit ===");
        Set<String> helped = statusModHelper(temp);
        temp.addAll(helped);
        for (String file : temp) {
            System.out.println(file);
        }
        temp.clear();
        System.out.println("");
        System.out.println("=== Untracked Files ===");
        Set<String> aight = statusUntrackHelper(temp);
        temp.addAll(aight);
        for (String file : temp) {
            System.out.println(file);
        }
        temp.clear();
        System.out.println("");
    }

    /**
     * Helper func for exrta credit.
     * @param tree is the treeset.
     * @return the treeset.
     */
    private Set<String> statusModHelper(Set<String> tree) {
        Commit headCommit = getCommit(repo.getHead());
        for (String blob : headCommit.getBlobs().keySet()) {
            if (Utils.join(cwd, blob).exists()) {
                File file = Utils.join(cwd, blob);
                String contents = Utils.readContentsAsString(file);
                Blob newFile = new Blob(blob, contents);
                String lastHash = headCommit.getBlobs().get(blob);
                String currentHash = newFile.getHash();
                if (!lastHash.equals(currentHash)) {
                    if (!stageArea.getAdditionBlobs().containsKey(blob)) {
                        tree.add(blob + " (modified)");
                    }
                }
            } else {
                if (!stageArea.getRemovalBlobs().containsKey(blob)) {
                    tree.add(blob + " (deleted)");
                }
            }
        }
        for (String blob : stageArea.getAdditionBlobs().keySet()) {
            if (Utils.join(cwd, blob).exists()) {
                File file = Utils.join(cwd, blob);
                String contents = Utils.readContentsAsString(file);
                Blob newFile = new Blob(blob, contents);
                String stageHash = stageArea.getAdditionBlobs().get(blob);
                if (!newFile.getHash().equals(stageHash)) {
                    tree.add(blob + " (modified)");
                }
            } else {
                tree.add(blob + " (deleted)");
            }
        }
        return tree;
    }

    /**
     * Helper func for exrta credit.
     * @param tree is the treeset.
     * @return the treeset.
     */
    private Set<String> statusUntrackHelper(Set<String> tree) {
        List<String> temp = Utils.plainFilenamesIn(cwd);
        Commit headCommit = getCommit(repo.getHead());
        if (temp == null) {
            return tree;
        }
        for (String blob : temp) {
            if (!headCommit.getBlobs().containsKey(blob)
                    && !stageArea.getAdditionBlobs().containsKey(blob)) {
                tree.add(blob);
            }
        }
        return tree;
    }

    /** TYPE. ARGS.
     * */
    void checkout(int type, String... args) {
        if (type == 1) {
            String fileName = args[0];
            Commit head = getCommit(repo.getHead());
            checkFile(head, fileName);
        }
        if (type == 2) {
            String commitId = args[0];
            String fileName = args[1];
            Commit found = null;
            boolean exists = false;
            for (String comId : repo.getAllCommits()) {
                if (comId.indexOf(commitId) == 0) {
                    found = getCommit(comId);
                    exists = true;
                }
            }
            if (!exists) {
                throw new GitletException("No commit with that id exists.");
            }
            if (found != null) {
                checkFile(found, fileName);
            }
        }
        if (type == 3) {
            String branchName = args[0];
            if (!repo.getBranches().containsKey(branchName)) {
                throw new GitletException("No such branch exists.");
            }
            if (branchName.equals(repo.getCurrentBranch())) {
                throw new GitletException("No need to checkout current branch");
            }

            Commit checkoutCommit = getCommit(repo.getBranches().
                    get(branchName));
            Commit currentCommit = getCommit(repo.getHead());
            branchCheckout(currentCommit, checkoutCommit);
            repo.saveCurrentBranch(branchName);
        }
    }

    /** CURRENTCOMMIT. CHECKOUTCOMMIT.
     * Helper to checkout a branch.
     * */
    private void branchCheckout(Commit currentCommit, Commit checkoutCommit) {
        for (String blob : checkoutCommit.getBlobs().keySet()) {
            if (Utils.join(cwd, blob).exists()) {
                File file = Utils.join(cwd, blob);
                String contents = Utils.readContentsAsString(file);
                Blob newFile = new Blob(blob, contents);

                String lastHash = currentCommit.getBlobs().get(blob);
                String currentHash = newFile.getHash();
                String checkoutHash = checkoutCommit.getBlobs().get(blob);
                if (currentCommit.getBlobs().containsKey(blob)) {
                    if (!lastHash.equals(currentHash)) {
                        throw new GitletException("There is an untracked "
                                + "file in the way; delete it, or"
                                + " add and commit it first.");
                    }
                } else {
                    if (!checkoutHash.equals(currentHash)) {
                        throw new GitletException("There is an untracked "
                                + "file in the way; delete it, or"
                                + " add and commit it first.");
                    }
                }
            }
        }
        for (String blob : checkoutCommit.getBlobs().keySet()) {
            checkFile(checkoutCommit, blob);
        }
        for (String blob : currentCommit.getBlobs().keySet()) {
            if (!checkoutCommit.getBlobs().containsKey(blob)) {
                rm(blob);
            }
        }
        stageArea.clear();
        repo.saveHead(checkoutCommit.getHash());
    }

    /** HEAD. FILENAME.
     * Helper to check if the file exists.
     * */
    private void checkFile(Commit head, String fileName) {
        if (!head.getBlobs().containsKey(fileName)) {
            throw new GitletException("File does not exist in that commit.");
        }
        File toChange = Utils.join(cwd, fileName);
        String fileHash = head.getBlobs().get(fileName);
        Blob oldFile = getBlob(fileHash);
        Utils.writeContents(toChange, oldFile.getContents());
    }

    /** BRANCHNAME.
     * Gets the branch with the name.
     * */
    void branch(String branchName) {
        if (repo.getBranches().containsKey(branchName)) {
            throw new GitletException("A branch with that"
                    + " name already exists.");
        }
        repo.getBranches().put(branchName, repo.getHead());
    }

    /** BRANCHNAME.
     * Function to remove branch.
     * */
    void rmbranch(String branchName) {
        if (!repo.getBranches().containsKey(branchName)) {
            throw new GitletException("A branch with that "
                    + "name does not exist.");
        }
        if (repo.getCurrentBranch().equals(branchName)) {
            throw new GitletException("Cannot remove the current branch.");
        }
        repo.getBranches().remove(branchName);
    }

    /** RESET function that resets the state to
     * COMMITID param.
     */
    void reset(String commitId) {
        Commit found = null;
        boolean exists = false;
        for (String comId : repo.getAllCommits()) {
            if (comId.indexOf(commitId) == 0) {
                found = getCommit(comId);
                exists = true;
            }
        }
        if (!exists) {
            throw new GitletException("No commit with that id exists.");
        }
        Commit currentCommit = getCommit(repo.getHead());
        branchCheckout(currentCommit, found);
        repo.getBranches().put(repo.getCurrentBranch(), found.getHash());
    }

    /** Function to merge.
     * BRANCHNAME.
     */
    void merge(String branchName) {
        mergeErrors(branchName);
        Commit splitCommit = findSplit(branchName);
        Commit branchCommit = getCommit(repo.getBranches().get(branchName));
        Commit currentCommit = getCommit(repo.head());
        if (splitCommit.getHash().equals(branchCommit.getHash())) {
            throw new GitletException("Given branch is an "
                    + "ancestor of the current branch.");
        }
        if (splitCommit.getHash().equals(currentCommit.getHash())) {
            reset(branchCommit.getHash());
            throw new GitletException("Current branch fast forwarded.");
        }
        for (String fileName : branchCommit.getBlobs().keySet()) {
            File check = new File(fileName);
            if (!currentCommit.getBlobs().containsKey(fileName)
                    && check.exists()) {
                throw new GitletException("There is an untracked file "
                        + "in the way; delete it or add it first.");
            }
        }
    }

    /** Helper method to find split point.
     *
     * @param branchName name of BRANCHNAME.
     * @return the relevant COMMIT.
     */
    private Commit findSplit(String branchName) {
        Commit mergeHead = getCommit(repo.getBranches().get(branchName));
        Commit currentHead = getCommit(repo.getHead());
        Commit pointerMerge = mergeHead;
        Commit pointerCurrent = currentHead;

        while (!pointerMerge.equals(pointerCurrent)) {
            pointerMerge = getCommit(pointerMerge.getParent());
            pointerCurrent = getCommit(pointerCurrent.getParent());
        }
        return pointerCurrent;
    }

    /** Helper method to find errors.
     *
     * @param branchName name of BRANCHNAME.
     */
    private void mergeErrors(String branchName) {
        if (!stageArea.getAdditionBlobs().isEmpty()
                || !stageArea.getRemovalBlobs().isEmpty()) {
            throw new GitletException("You have uncommitted changes.");
        }
        if (repo.getBranches().containsKey(branchName)) {
            throw new GitletException("A branch with that name"
                    + " does not exist.");
        }
        if (repo.getCurrentBranch().equals(branchName)) {
            throw new GitletException("Cannot merge a branch"
                    + " with itself.");
        }
    }

    /** Helper method to find commit.
     *
     * @param hash name of HASH.
     * @return the relevant COMMIT.
     */
    private Commit getCommit(String hash) {
        File com = Utils.join(commits, hash);
        assert com.exists() : "File does not exist in commits.";
        return Utils.readObject(com, Commit.class);
    }

    /** Helper method to find commit.
     *
     * @param hash name of HASH.
     * @return the relevant BLOB.
     */
    private Blob getBlob(String hash) {
        File com = Utils.join(blobs, hash);
        assert com.exists() : "File does not exist in blobs.";
        return Utils.readObject(com, Blob.class);
    }

    /**
     * @param commit The commit object to be cloned.
     * @return Returns a new commit object that is a clone of the one passed in.
     */
    private Commit commitClone(Commit commit) {
        assert commit != null : "Current head value is null";
        Commit newCommit = new Commit("not null", "not null");
        HashMap<String, String> temp = new HashMap<>();
        newCommit.saveBlobs((HashMap<String, String>)
                commit.getBlobs().clone());
        newCommit.saveHash(commit.getHash());
        newCommit.saveMessage(commit.getMessage());
        newCommit.saveParent(commit.getParent());
        return newCommit;
    }

}
