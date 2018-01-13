package de.be.rhi.crypto.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
	
	public static int DEFAUL_SCALE = 50;
	public static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

	/**
	 * Berechung des Dreisatzes.<br>
	 * a * b / c
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static BigDecimal calculateRuleOfThree(final BigDecimal a, final BigDecimal b, final BigDecimal c){
		BigDecimal result = BigDecimal.ZERO;
		
		if(ObjectUtil.isBigDecimalNotZero(a) && ObjectUtil.isBigDecimalNotZero(b) && ObjectUtil.isBigDecimalNotZero(c)) {
			result = a.multiply(b).divide(c, DEFAUL_SCALE, DEFAULT_ROUNDING_MODE);
		}
		
		return result;
	}
}
