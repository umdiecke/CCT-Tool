package de.be.rhi.crypto.util;

import java.math.BigDecimal;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author Ren� Hildebrand
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
		return bigDecimal == null || bigDecimal.signum() == 0;
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

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param decimal
	 * @return
	 */
	public static BigDecimal cutZeroFractionDigits(final BigDecimal decimal) {
		return ObjectUtil.cutZeroFractionDigits(decimal, 2);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param decimal
	 * @param minScale
	 * @return
	 */
	public static BigDecimal cutZeroFractionDigits(final BigDecimal decimal, final int minScale) {
		BigDecimal result = decimal;

		if (result != null) {
			result = new BigDecimal(result.stripTrailingZeros().toPlainString());
			if (minScale >= 0 && result.scale() < minScale) {
				result = result.setScale(minScale, MathUtil.DEFAULT_ROUNDING_MODE);
			}
		}

		return result;
	}
}
