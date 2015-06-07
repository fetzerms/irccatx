package de.fetzerms.irccatx.util;

import org.pircbotx.Colors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2015 - Matthias Fetzer <br>
 * <p/>
 * Classdescription here...
 *
 * @author Matthias Fetzer - matthias [at] fetzerms.de
 */
public class ColorMap {

    // Hacky way to get current class for logger.
    private static Class thisClass = new Object() {
    }.getClass().getEnclosingClass();
    private static Logger LOG = LoggerFactory.getLogger(thisClass);

    /**
     * Taken from fm.last.irccat
     */
    public static String colorize(String m) {

        LOG.debug("Replacing colors for message \"{}\"", m);

        Map<String, String> colorReplacementMap = new HashMap<>();
        colorReplacementMap.put("NORMAL", Colors.NORMAL);
        colorReplacementMap.put("BOLD", Colors.BOLD);
        colorReplacementMap.put("UNDERLINE", Colors.UNDERLINE);
        colorReplacementMap.put("REVERSE", Colors.REVERSE);
        colorReplacementMap.put("WHITE", Colors.WHITE);
        colorReplacementMap.put("BLACK", Colors.BLACK);
        colorReplacementMap.put("DBLUE", Colors.DARK_BLUE);
        colorReplacementMap.put("DGREEN", Colors.DARK_GREEN);
        colorReplacementMap.put("RED", Colors.RED);
        colorReplacementMap.put("BROWN", Colors.BROWN);
        colorReplacementMap.put("PURPLE", Colors.PURPLE);
        colorReplacementMap.put("ORANGE", Colors.OLIVE);
        colorReplacementMap.put("YELLOW", Colors.YELLOW);
        colorReplacementMap.put("GREEN", Colors.GREEN);
        colorReplacementMap.put("TEAL", Colors.TEAL);
        colorReplacementMap.put("CYAN", Colors.CYAN);
        colorReplacementMap.put("BLUE", Colors.BLUE);
        colorReplacementMap.put("PINK", Colors.MAGENTA);
        colorReplacementMap.put("DGRAY", Colors.DARK_GRAY);
        colorReplacementMap.put("GRAY", Colors.LIGHT_GRAY);

        for (Map.Entry<String, String> e : colorReplacementMap.entrySet()) {
            // Support #COLOR or %COLOR
            // either format can be confusing, depending on context.
            m = m.replaceAll("%" + e.getKey(), e.getValue());
            m = m.replaceAll("#" + e.getKey(), e.getValue());
        }
        return m;
    }
}
