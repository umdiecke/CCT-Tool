package de.be.rhi.crypto.util;

import java.math.BigDecimal;

public class ObjectUtil {

	
	public static boolean isBigDecimalZeroOrNull(final BigDecimal bigDecimal) {
		return bigDecimal == null || BigDecimal.ZERO.equals(bigDecimal);
	}

	public static boolean isBigDecimalNotZero(final BigDecimal bigDecimal) {
		return !isBigDecimalZeroOrNull(bigDecimal);
	}
}
