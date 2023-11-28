package it.unibo.mvc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class implements a view that can write on any PrintStream.
 */
public final class PrintStreamView implements DrawNumberView {

    private final PrintStream out;

    /**
     * Builds a new PrintStreamView.
     *
     * @param stream the {@link PrintStream} where to write
     */
    @SuppressFBWarnings()
    public PrintStreamView(final PrintStream stream) {
        out = stream; // NOPMD suppressed as it is a false positive
    }

    /**
     * Builds a {@link PrintStreamView} that writes on file, given a path.
     * 
     * @param path a file path
     * @throws FileNotFoundException 
     */
    @SuppressFBWarnings
    public PrintStreamView(final String path) throws FileNotFoundException {
        out = new PrintStream(new FileOutputStream(new File(path))); // NOPMD
        // suppressed as it is a false positive
    }

    @Override
    public void setObserver(final DrawNumberViewObserver observer) {
        /*
         * This UI is output only.
         */
    }

    @Override
    public void start() {
        /*
         * PrintStreams are always ready.
         */
    }

    @Override
    public void numberIncorrect() {
        out.println("You must enter a number");
    }

    @Override
    public void displayError(final String message) {
        out.println(message);
    }
    @Override
    public void result(final DrawResult res) {
        out.println(res.getDescription());
    }

}
