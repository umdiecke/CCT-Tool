package de.be.rhi.crypto.util;

import java.math.BigDecimal;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author René Hildebrand
 * @version 1.0
 * @since 13.01.2018, 15:19:56
 *
 */
public class ObjectUtil {


	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param bigDecimal
	 * @return
	 */
	public static boolean isBigDecimalZeroOrNull(final BigDecimal bigDecimal) {
		return bigDecimal == null || BigDecimal.ZERO.equals(bigDecimal);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param bigDecimal
	 * @return
	 */
	public static boolean isBigDecimalNotZero(final BigDecimal bigDecimal) {
		return !ObjectUtil.isBigDecimalZeroOrNull(bigDecimal);
	}
}
