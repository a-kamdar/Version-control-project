package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Structure that will represent the commit graph, which will be a
 * directed acyclic graph.
 * @author Adyant
 */
class CommitGraph implements Serializable {
    /** Representation of the acyclic GRAPH. */
    private HashMap<String, HashSet<String>> graph;
    /** A hashmap of BRANCHES from their names to the
     * hashcode of the last commit made in them. */
    private HashMap<String, String> branches;
    /** ALLCOMMITS. */
    private HashSet<String> allCommits;
    /** HEAD. */
    private String head;
    /** Gets the head of the repo.
     * @return the head. */
    String getHead() {
        return head;
    }
    /** CURRENTBRANCH. */
    private String currentBranch;

    /**
     * INITIALCOMMIT.
     * @throws IOException because the repo must be serializable.
     */
    CommitGraph(Commit initialCommit) throws IOException {
        graph = new HashMap<>();
        branches = new HashMap<>();
        allCommits = new HashSet<>();
        head = "";
        currentBranch = "";
        addToGraph(initialCommit);
    }

    /** COMMIT. */
    void addToGraph(Commit commit) throws IOException {
        String parentHash = commit.getParent();
        if (parentHash != null) {
            if (graph.containsKey(parentHash)) {
                HashSet<String> children = graph.get(parentHash);
                children.add(commit.getHash());

                graph.put(commit.getHash(), new HashSet<String>());
                allCommits.add(commit.getHash());
                branches.put(currentBranch, commit.getHash());
                head = commit.getHash();
                commit.saveHash(commit.getHash());
                commit.saveCommit();
            } else {
                throw new GitletException("Incorrect parent input.");
            }
        } else {
            String hash = commit.getHash();
            graph.put(commit.getHash(), new HashSet<>());
            allCommits.add(commit.getHash());
            branches.put("master", commit.getHash());
            currentBranch = "master";
            head = commit.getHash();
            commit.saveCommit();
        }
    }

    /** Gets the current branch.
     * @return the current branch. */
    String getCurrentBranch() {
        return currentBranch;
    }

    /** Saves the new branch BRANCH. */
    void saveCurrentBranch(String branch) {
        this.currentBranch = branch;
    }

    /** Saves the new head HD. */
    void saveHead(String hd) {
        this.head = hd;
    }

    /** Gets the branch labels.
     * @return the current branch labels. */
    Set<String> getBranchLabels() {
        return branches.keySet();
    }

    /** Returns the HEAD of the graph rn.
     * @return the current head. */
    String head() {
        return head;
    }

    /** Returns a hashmap of all the commits.
     * @return all the commmits. */
    HashSet<String> getAllCommits() {
        return allCommits;
    }

    /** BRANCHES.
     * @return the branches */
    HashMap<String, String> getBranches() {
        return branches;
    }
}
