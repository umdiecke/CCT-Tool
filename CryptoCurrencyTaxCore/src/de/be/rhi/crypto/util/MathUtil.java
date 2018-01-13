/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto.util;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * TODO RHildebrand JavaDoc
 *
 * @author René Hildebrand
 * @version 1.0
 * @since 13.01.2018, 15:40:57
 *
 */
public class MathUtil {

	public static final int DEFAULT_CALCULATION_SCALE = 50;
	public static final int DEFAULT_ACCOUNTING_SCALE = 2;
	public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;


	/**
	 * TODO RHildebrand JavaDoc
	 *
	 */
	private MathUtil() {
		super();
	}

	/**
	 * Berechung des Dreisatzes.<br>
	 * a * b / c
	 *
	 * @param a
	 *           TODO RHildebrand JavaDoc
	 * @param b
	 * @param c
	 * @return
	 */
	public static BigDecimal calculateRuleOfThree(final BigDecimal a, final BigDecimal b, final BigDecimal c){
		BigDecimal result = BigDecimal.ZERO;

		if(ObjectUtil.isBigDecimalNotZero(a) && ObjectUtil.isBigDecimalNotZero(b) && ObjectUtil.isBigDecimalNotZero(c)) {
			result = a.multiply(b).divide(c, MathUtil.DEFAULT_CALCULATION_SCALE, MathUtil.DEFAULT_ROUNDING_MODE);
		}

		return result;
	}
}
