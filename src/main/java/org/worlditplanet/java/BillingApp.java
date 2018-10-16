package org.worlditplanet.java;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.cli.Option.builder;

import static java.util.Objects.requireNonNull;

/**
 * Приложение для выставления счетов абонентам IT-Planet Mobile.
 *
 * @author Sergey Morgunov {@literal <smorgunov@at-consulting.ru>}
 */
public class BillingApp {

    private final Options options;

    private final BillingEngine engine;

    BillingApp(BillingEngine engine) {
        this.options = commandLineOptions();
        this.engine = requireNonNull(engine);
    }

    private static Options commandLineOptions() {
        Options options = new Options();
        options.addOption(
                builder("t")
                        .longOpt("tariffs")
                        .required(true)
                        .hasArg(true)
                        .argName("file")
                        .optionalArg(false)
                        .desc("File with description of tariffs IT-Planet Mobile")
                        .build()
        ).addOption(
                builder("s")
                        .longOpt("subscribers")
                        .required(true)
                        .hasArg(true)
                        .argName("file")
                        .optionalArg(false)
                        .desc("File with IT-Planet Mobile subscribers")
                        .build()
        ).addOption(
                builder("a")
                        .longOpt("actions")
                        .required(true)
                        .hasArg(true)
                        .argName("file")
                        .optionalArg(false)
                        .desc("File with actions of IT-Planet Mobile subscribers")
                        .build()
        ).addOption(
                builder("i")
                        .longOpt("invoices")
                        .required(true)
                        .hasArg(true)
                        .argName("file")
                        .optionalArg(false)
                        .desc("File with invoices of IT-Planet Mobile subscribers")
                        .build()
        );
        return options;
    }

    void process(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            process(parser.parse(options, args));
        } catch (ParseException e) {
            System.err.println("Parsing command line options failed.  Reason: " + e.getMessage());
            help();
        }
    }

    private void process(CommandLine cmd) throws ParseException {
        Path tariffs = Paths.get(cmd.getOptionValue("t"));
        Path subscribers = Paths.get(cmd.getOptionValue("s"));
        Path actions = Paths.get(cmd.getOptionValue("a"));
        Path invoices = Paths.get(cmd.getOptionValue("i"));
        engine.billing(tariffs, subscribers, actions, invoices);
    }

    private void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("billing", options);
    }

    @SuppressWarnings("checkstyle:javadocmethod")
    public static void main(String[] args) {
        BillingApp app = new BillingApp(new BillingEngineImpl());
        app.process(args);
    }
}
