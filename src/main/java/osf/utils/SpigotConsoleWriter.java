package osf.utils;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.pmw.tinylog.Configuration;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.LogEntry;
import org.pmw.tinylog.writers.LogEntryValue;
import org.pmw.tinylog.writers.Writer;

import java.io.PrintStream;
import java.util.EnumSet;
import java.util.Set;

public class SpigotConsoleWriter implements Writer {
    private static final String LOGGER_PREFIX = "[OSF]";
    private final PrintStream err;
    private final PrintStream out;

    /** */
    public SpigotConsoleWriter() {
        err = System.err;
        out = System.out;
    }

    /**
     * @param stream
     *            Print stream for outputting log entries
     */
    public SpigotConsoleWriter(final PrintStream stream) {
        err = stream;
        out = stream;
    }


    SpigotConsoleWriter(final String stream) {
        if (stream == null) {
            err = System.err;
            out = System.out;
        } else if ("err".equalsIgnoreCase(stream)) {
            err = System.err;
            out = System.err;
        } else if ("out".equalsIgnoreCase(stream)) {
            err = System.out;
            out = System.out;
        } else {
            throw new IllegalArgumentException("Stream must be \"out\" or \"err\", \"" + stream + "\" is not a valid stream name");
        }
    }

    @Override
    public Set<LogEntryValue> getRequiredLogEntryValues() {
        return EnumSet.of(LogEntryValue.LEVEL, LogEntryValue.RENDERED_LOG_ENTRY);
    }

    @Override
    public void init(final Configuration configuration) {
        // Do nothing
    }

    @Override
    public void write(final LogEntry logEntry) {
        ChatColor color = getChatColorForLoggingLevel(logEntry.getLevel());

        Bukkit.getLogger().log(convertToJavaLoggingLevel(logEntry.getLevel()), LOGGER_PREFIX + " " + color + logEntry.getRenderedLogEntry().substring(0, logEntry.getRenderedLogEntry().length()-2));
    }

    @Override
    public void flush() {
        // Do nothing
    }

    @Override
    public void close() {
        // Do nothing
    }

    private PrintStream getPrintStream(final Level level) {
        if (level == Level.ERROR || level == Level.WARNING) {
            return err;
        } else {
            return out;
        }
    }

    public static java.util.logging.Level convertToJavaLoggingLevel(Level level) {
        switch(level) {
            default:
                return java.util.logging.Level.INFO;
            case DEBUG:
                return java.util.logging.Level.INFO;
            case WARNING:
                return java.util.logging.Level.WARNING;
            case ERROR:
                return java.util.logging.Level.SEVERE;
        }
    }

    public static ChatColor getChatColorForLoggingLevel(Level level) {
        switch(level) {
            case DEBUG:
                return ChatColor.GREEN;
            case INFO:
                return ChatColor.WHITE;
            case WARNING:
                return ChatColor.YELLOW;
            case ERROR:
                return ChatColor.DARK_RED;
            default:
                return ChatColor.GRAY;
        }
    }
}

