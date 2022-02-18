package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Adyant
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        try {
            if (args.length == 0) {
                throw new GitletException("Please enter a command.");
            } else if (args[0].equals("init")) {
                initHelper(args);
            } else if (!Utils.join(System.getProperty("user.dir"),
                    ".gitlet").exists()) {
                throw new GitletException("Not in an initialized"
                        + " Gitlet directory.");
            } else if (args[0].equals("add")) {
                addHelper(args);
            } else if (args[0].equals("commit")) {
                commitHelper(args);
            } else if (args[0].equals("rm")) {
                rmHelper(args);
            } else if (args[0].equals("log")) {
                logHelper(args);
            } else if (args[0].equals("global-log")) {
                globalLogHelper(args);
            } else if (args[0].equals("find")) {
                findHelper(args);
            } else if (args[0].equals("status")) {
                statusHelper(args);
            } else if (args[0].equals("checkout")) {
                checkoutHelper(args);
            } else if (args[0].equals("branch")) {
                branchHelper(args);
            } else if (args[0].equals("rm-branch")) {
                rmBranchHelper(args);
            } else if (args[0].equals("reset")) {
                resetHelper(args);
            } else if (args[0].equals("merge")) {
                mergeHelper(args);
            } else {
                throw new GitletException("No command with that name exists.");
            }
        } catch (GitletException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    /** ARGS. */
    private static void initHelper(String... args) throws IOException {
        if (args.length == 1) {
            Functions current = new Functions();
            current.init();

            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void addHelper(String... args) throws IOException {
        if (args.length == 2) {
            String fileName = args[1];
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.add(fileName);

            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void commitHelper(String... args) throws IOException {
        if (args.length == 2) {
            String message = args[1];
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.commit(message);
            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void rmHelper(String... args) {
        if (args.length == 2) {
            String fileName = args[1];
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.rm(fileName);
            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void logHelper(String... args) {
        if (args.length == 1) {
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.log();

            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void globalLogHelper(String... args) {
        if (args.length == 1) {
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.globalLog();

            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void findHelper(String... args) {
        if (args.length == 2) {
            String commit = args[1];
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.find(commit);
            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void statusHelper(String... args) {
        if (args.length == 1) {
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.status();

            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void checkoutHelper(String... args) {
        File cwd = new File(System.getProperty("user.dir"));
        File gitlet = Utils.join(cwd, ".gitlet");
        File path = Utils.join(gitlet, "Functions");
        Functions current = new Functions();
        Functions copyFunction = Utils.readObject(path, Functions.class);

        current.saveCwd(copyFunction.getCwd());
        current.saveCommits(copyFunction.getCommits());
        current.saveStage(copyFunction.getStageArea());
        current.saveRepo(copyFunction.getRepo());
        current.saveBlobs(copyFunction.getBlobs());
        current.saveGitlet(copyFunction.getGitlet());

        if (args.length == 3) {
            if (args[1].equals("--")) {
                current.checkout(1, args[2]);
            } else {
                throw new GitletException("Incorrect operands.");
            }
        } else if (args.length == 4) {
            if (args[2].equals("--")) {
                current.checkout(2, args[1], args[3]);
            } else {
                throw new GitletException("Incorrect operands.");
            }
        } else {
            if (args.length == 2) {
                current.checkout(3, args[1]);
            } else {
                throw new GitletException("Incorrect operands.");
            }

        }
        Utils.writeObject(path, current);
    }

    /** ARGS. */
    private static void branchHelper(String... args) {
        if (args.length == 2) {
            String branchName = args[1];
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.branch(branchName);

            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void rmBranchHelper(String... args) {
        if (args.length == 2) {
            String branchName = args[1];
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.rmbranch(branchName);

            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void resetHelper(String... args) {
        if (args.length == 2) {
            String commitId = args[1];
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.reset(commitId);

            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

    /** ARGS. */
    private static void mergeHelper(String... args) {
        if (args.length == 2) {
            String branchName = args[1];
            File cwd = new File(System.getProperty("user.dir"));
            File gitlet = Utils.join(cwd, ".gitlet");
            File path = Utils.join(gitlet, "Functions");
            Functions current = new Functions();
            Functions copyFunction = Utils.readObject(path, Functions.class);

            current.saveCwd(copyFunction.getCwd());
            current.saveCommits(copyFunction.getCommits());
            current.saveStage(copyFunction.getStageArea());
            current.saveRepo(copyFunction.getRepo());
            current.saveBlobs(copyFunction.getBlobs());
            current.saveGitlet(copyFunction.getGitlet());

            current.merge(branchName);

            Utils.writeObject(path, current);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }
}
