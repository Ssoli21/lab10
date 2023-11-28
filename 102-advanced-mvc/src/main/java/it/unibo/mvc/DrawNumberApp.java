package it.unibo.mvc;

import java.io.BufferedReader;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 */
public final class DrawNumberApp implements DrawNumberViewObserver {

    private final DrawNumber model;
    private final List<DrawNumberView> views;

    /**
     * @param config 
     *             the name of the config file
     * @param views
     *            the views to attach
     * @throws IOException
     */
    @SuppressFBWarnings
    public DrawNumberApp(final String config, final DrawNumberView... views) {
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        for (final DrawNumberView view: views) {
            view.setObserver(this);
            view.start();
        }
        final Configuration.Builder configBuilder = new Configuration.Builder();
        try (BufferedReader configFile = new BufferedReader(
            new InputStreamReader(ClassLoader.getSystemResourceAsStream(config)))) {
            for (String line = configFile.readLine(); line != null; line = configFile.readLine()) {
                final StringTokenizer string = new StringTokenizer(line, ": ");
                // CHECKSTYLE: MissingSwitchDefault OFF
                switch (string.nextToken()) {
                    case "minimum" -> {
                        //string.nextToken();
                        configBuilder.setMin(Integer.parseInt(string.nextToken()));
                    }
                    case "maximum" -> {
                        //string.nextToken();
                        configBuilder.setMax(Integer.parseInt(string.nextToken()));
                    }
                    case "attempts" -> {
                        //string.nextToken();
                        configBuilder.setAttempts(Integer.parseInt(string.nextToken()));
                    }
                }
                // CHECKSTYLE: MissingSwitchDefault ON
            } 
        } catch (IOException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        final Configuration configuration = configBuilder.build();
        if (configuration.isConsistent()) {
            this.model = new DrawNumberImpl(configuration);
        } else {
            this.model = new DrawNumberImpl(new Configuration.Builder().build());
        }
    }

    @Override
    public void newAttempt(final int n) {
        try {
            final DrawResult result = model.attempt(n);
            for (final DrawNumberView view: views) {
                view.result(result);
            }
        } catch (IllegalArgumentException e) {
            for (final DrawNumberView view: views) {
                view.numberIncorrect();
            }
        }
    }

    @Override
    public void resetGame() {
        this.model.reset();
    }

    @Override
    @SuppressFBWarnings
    public void quit() {
        /*
         * A bit harsh. A good application should configure the graphics to exit by
         * natural termination when closing is hit. To do things more cleanly, attention
         * should be paid to alive threads, as the application would continue to persist
         * until the last thread terminates.
         */
        System.exit(0); //NOPMD
    }

    /**
     * @param args
     *            ignored
     * @throws FileNotFoundException 
     */
    public static void main(final String... args) throws FileNotFoundException {
        new DrawNumberApp("config.yml",
                            new DrawNumberViewImpl(),
                            new DrawNumberViewImpl(),
                            new PrintStreamView(System.out),
                            new PrintStreamView("output.log"));
    }

}
