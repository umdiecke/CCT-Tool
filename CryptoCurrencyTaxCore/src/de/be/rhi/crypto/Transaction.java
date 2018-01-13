/**
 *
 */
package de.be.rhi.crypto;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import de.be.rhi.crypto.util.MathUtil;
import de.be.rhi.crypto.util.ObjectUtil;

/**
 * @author René
 *
 */
public class Transaction implements Comparable<Transaction> {

	private Date transactionDate;
	private TransactionType transactionType;
	private Currency basisWaehrung;
	private Currency transaktionsWaehrung;
	private Currency depotWaehrung;
	private BigDecimal kursBasiswaehrung;
	private BigDecimal kursTransaktionsWaehrung;
	private BigDecimal betragTransaktionsWaehrung;
	private BigDecimal prozentsatzTransactionsGebuehr;
	private String referenz;
	private String beschreibung;

	public BigDecimal getBetragBasisWaehrungVorGebuehr() {
		return MathUtil.calculateRuleOfThree(kursBasiswaehrung,
				betragTransaktionsWaehrung, kursTransaktionsWaehrung);
	}

	public BigDecimal getBetragBasisWaehrungNachGebuehr() {
		return getBetragBasisWaehrungVorGebuehr().subtract(
				getBetragBasisWaehrungGebuehr());
	}

	public BigDecimal getBetragBasisWaehrungGebuehr() {
		BigDecimal result = getBetragBasisWaehrungVorGebuehr();

		if (ObjectUtil.isBigDecimalNotZero(result)
				&& ObjectUtil
				.isBigDecimalNotZero(prozentsatzTransactionsGebuehr)) {
			result = MathUtil.calculateRuleOfThree(result,
					prozentsatzTransactionsGebuehr, new BigDecimal("100"));
		}

		return result;
	}

	public BigDecimal getBetragDepotWaehrungVorGebuehr() {
		return MathUtil.calculateRuleOfThree(betragTransaktionsWaehrung,
				new BigDecimal("1"), kursTransaktionsWaehrung);
	}

	public BigDecimal getBetragDepotWaehrungNachGebuehr() {
		return getBetragDepotWaehrungVorGebuehr().subtract(
				getBetragDepotWaehrungGebuehr());
	}

	public BigDecimal getBetragDepotWaehrungGebuehr() {
		BigDecimal result = getBetragDepotWaehrungVorGebuehr();

		if (ObjectUtil.isBigDecimalNotZero(result)
				&& ObjectUtil
				.isBigDecimalNotZero(prozentsatzTransactionsGebuehr)) {
			result = MathUtil.calculateRuleOfThree(result,
					prozentsatzTransactionsGebuehr, new BigDecimal("100"));
		}

		return result;
	}

	public BigDecimal getBetragTransaktionsWaehrungNachGebuehr() {
		BigDecimal result = betragTransaktionsWaehrung != null ? betragTransaktionsWaehrung
				: BigDecimal.ZERO;
		return result.subtract(getBetragTransaktionsWaehrungGebuehr());
	}

	public BigDecimal getBetragTransaktionsWaehrungGebuehr() {
		BigDecimal result = betragTransaktionsWaehrung != null ? betragTransaktionsWaehrung
				: BigDecimal.ZERO;

		if (ObjectUtil.isBigDecimalNotZero(result)
				&& ObjectUtil
				.isBigDecimalNotZero(prozentsatzTransactionsGebuehr)) {
			result = MathUtil.calculateRuleOfThree(result,
					prozentsatzTransactionsGebuehr, new BigDecimal("100"));
		}
		return result;
	}

	/**
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(final Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the transactionType
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(final TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the basisWaehrung
	 */
	public Currency getBasisWaehrung() {
		return basisWaehrung;
	}

	/**
	 * @param basisWaehrung
	 *            the basisWaehrung to set
	 */
	public void setBasisWaehrung(final Currency basisWaehrung) {
		this.basisWaehrung = basisWaehrung;

		if (transaktionsWaehrung == null) {
			transaktionsWaehrung = basisWaehrung;
		}
	}

	/**
	 * @return the transaktionsWaehrung
	 */
	public Currency getTransaktionsWaehrung() {
		return transaktionsWaehrung;
	}

	/**
	 * @param transaktionsWaehrung
	 *            the transaktionsWaehrung to set
	 */
	public void setTransaktionsWaehrung(final Currency transaktionsWaehrung) {
		this.transaktionsWaehrung = transaktionsWaehrung;

		if (basisWaehrung == null) {
			basisWaehrung = transaktionsWaehrung;
		}
	}

	/**
	 * @return the depotWaehrung
	 */
	public Currency getDepotWaehrung() {
		return depotWaehrung;
	}

	/**
	 * @param depotWaehrung
	 *            the depotWaehrung to set
	 */
	public void setDepotWaehrung(final Currency depotWaehrung) {
		this.depotWaehrung = depotWaehrung;
	}

	/**
	 * @return the kursBasiswaehrung
	 */
	public BigDecimal getKursBasiswaehrung() {
		return kursBasiswaehrung;
	}

	/**
	 * @param kursBasiswaehrung
	 *            the kursBasiswaehrung to set
	 */
	public void setKursBasiswaehrung(final BigDecimal kursBasiswaehrung) {
		this.kursBasiswaehrung = kursBasiswaehrung;

		if (kursTransaktionsWaehrung == null) {
			kursTransaktionsWaehrung = kursBasiswaehrung;
		}
	}

	/**
	 * @return the kursTransaktionsWaehrung
	 */
	public BigDecimal getKursTransaktionsWaehrung() {
		return kursTransaktionsWaehrung;
	}

	/**
	 * @param kursTransaktionsWaehrung
	 *            the kursTransaktionsWaehrung to set
	 */
	public void setKursTransaktionsWaehrung(final BigDecimal kursTransaktionsWaehrung) {
		this.kursTransaktionsWaehrung = kursTransaktionsWaehrung;

		if (kursBasiswaehrung == null) {
			kursBasiswaehrung = kursTransaktionsWaehrung;
		}
	}

	/**
	 * @return the betragTransaktionsWaehrung
	 */
	public BigDecimal getBetragTransaktionsWaehrung() {
		return betragTransaktionsWaehrung;
	}

	/**
	 * @param betragTransaktionsWaehrung
	 *            the betragTransaktionsWaehrung to set
	 */
	public void setBetragTransaktionsWaehrung(
			final BigDecimal betragTransaktionsWaehrung) {
		this.betragTransaktionsWaehrung = betragTransaktionsWaehrung;
	}

	/**
	 * @return the prozentsatzTransactionsGebuehr
	 */
	public BigDecimal getProzentsatzTransactionsGebuehr() {
		return prozentsatzTransactionsGebuehr;
	}

	/**
	 * @param prozentsatzTransactionsGebuehr
	 *            the prozentsatzTransactionsGebuehr to set
	 */
	public void setProzentsatzTransactionsGebuehr(
			final BigDecimal prozentsatzTransactionsGebuehr) {
		this.prozentsatzTransactionsGebuehr = prozentsatzTransactionsGebuehr;
	}

	/**
	 * @return the referenz
	 */
	public String getReferenz() {
		return referenz;
	}

	/**
	 * @param referenz
	 *            the referenz to set
	 */
	public void setReferenz(final String referenz) {
		this.referenz = referenz;
	}

	/**
	 * @return the beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * @param beschreibung
	 *            the beschreibung to set
	 */
	public void setBeschreibung(final String beschreibung) {
		this.beschreibung = beschreibung;
	}

	@Override
	public String toString() {
		return "Transaction [" + "\ngetBetragBasisWaehrungVorGebuehr()="
				+ getBetragBasisWaehrungVorGebuehr() + " " + getBasisWaehrung()
				+ ", \ngetBetragBasisWaehrungNachGebuehr()="
				+ getBetragBasisWaehrungNachGebuehr() + " "
				+ getBasisWaehrung() + ", \ngetBetragBasisWaehrungGebuehr()="
				+ getBetragBasisWaehrungGebuehr() + " " + getBasisWaehrung()
				+ ", \ngetBetragDepotWaehrungVorGebuehr()="
				+ getBetragDepotWaehrungVorGebuehr() + " " + getDepotWaehrung()
				+ ", \ngetBetragDepotWaehrungNachGebuehr()="
				+ getBetragDepotWaehrungNachGebuehr() + " "
				+ getDepotWaehrung() + ", \ngetBetragDepotWaehrungGebuehr()="
				+ getBetragDepotWaehrungGebuehr() + " " + getDepotWaehrung()
				+ ", \ngetBetragTransaktionsWaehrung()="
				+ getBetragTransaktionsWaehrung() + " "
				+ getTransaktionsWaehrung()
				+ ", \ngetBetragTransaktionsWaehrungNachGebuehr()="
				+ getBetragTransaktionsWaehrungNachGebuehr() + " "
				+ getTransaktionsWaehrung()
				+ ", \ngetBetragTransaktionsWaehrungGebuehr()="
				+ getBetragTransaktionsWaehrungGebuehr() + " "
				+ getTransaktionsWaehrung() + ", \ngetTransactionDate()="
				+ getTransactionDate() + ", \ngetTransactionType()="
				+ getTransactionType() + ", \ngetBasisWaehrung()="
				+ getBasisWaehrung() + ", \ngetTransaktionsWaehrung()="
				+ getTransaktionsWaehrung() + ", \ngetDepotWaehrung()="
				+ getDepotWaehrung() + ", \ngetKursBasiswaehrung()="
				+ getKursBasiswaehrung() + " " + getBasisWaehrung()
				+ ", \ngetKursTransaktionsWaehrung()="
				+ getKursTransaktionsWaehrung() + " "
				+ getTransaktionsWaehrung()
				+ ", \ngetProzentsatzTransactionsGebuehr()="
				+ getProzentsatzTransactionsGebuehr() + "%"
				+ ", \ngetReferenz()=" + getReferenz()
				+ ", \ngetBeschreibung()=" + getBeschreibung() + "]";
	}

	@Override
	public int compareTo(final Transaction transaction) {

		if (getTransactionDate() == null
				|| transaction.getTransactionDate() == null) {
			return 0;
		}
		return getTransactionDate().compareTo(
				transaction.getTransactionDate());
	}

	public static final Comparator<Transaction> transactionComparator = new Comparator<>() {

		@Override
		public int compare(final Transaction transaction1, final Transaction transaction2) {
			return transaction1.compareTo(transaction2);
		}
	};
}