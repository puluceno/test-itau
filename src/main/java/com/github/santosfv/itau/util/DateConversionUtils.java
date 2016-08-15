package com.github.santosfv.itau.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateConversionUtils {

	/**
	 * Converts a LocalDateTime to Date. Before the conversion the zone ID of the dateTime parameter
	 * is set to System Default.
	 *
	 * @param dateTime LocalDateTime to be converted to Date.
	 * @return Date since epoch.
	 */
	public static Date toDate(LocalDateTime dateTime) {
		return Date.from(dateTime.atZone(ZoneOffset.systemDefault()).toInstant());
	}

	/**
	 * Converts a Date to LocalDateTime using System Default as ZoneId.
	 *
	 * @param date to be converted.
	 * @return LocalDateTime based on date parameter.
	 */
	public static LocalDateTime toLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault());
	}

	public static LocalDateTime convertDateAndTruncate(Date date) {
		LocalDateTime dateTime = toLocalDateTime(date);

		return dateTime.truncatedTo(ChronoUnit.HOURS);
	}

}
