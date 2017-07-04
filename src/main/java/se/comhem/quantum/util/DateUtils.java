package se.comhem.quantum.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtils {

    public static LocalDateTime fromDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Europe/Stockholm"));
    }
}
