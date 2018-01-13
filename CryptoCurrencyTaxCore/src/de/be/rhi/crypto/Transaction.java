package de.be.rhi.crypto;

import java.math.BigDecimal;
import java.util.Date;

import de.be.rhi.crypto.util.MathUtil;
import de.be.rhi.crypto.util.ObjectUtil;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author Ren� Hildebrand
 * @version 1.0
 * @since 13.01.2018, 15:21:20
 *
 */
public class Transaction implements Comparable<Transaction> {

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private Date transactionDate;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private TransactionType transactionType;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private Currency basisWaehrung;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private Currency transaktionsWaehrung;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private Currency depotWaehrung;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal kursBasiswaehrung;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal kursTransaktionsWaehrung;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal betragTransaktionsWaehrung;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal prozentsatzTransactionsGebuehr;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private String referenz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private String beschreibung;

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragBasisWaehrungVorGebuehr() {
		return MathUtil.calculateRuleOfThree(kursBasiswaehrung, betragTransaktionsWaehrung, kursTransaktionsWaehrung);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragBasisWaehrungNachGebuehr() {
		return getBetragBasisWaehrungVorGebuehr().subtract(getBetragBasisWaehrungGebuehr());
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragBasisWaehrungGebuehr() {
		BigDecimal result = getBetragBasisWaehrungVorGebuehr();

		if (ObjectUtil.isBigDecimalNotZero(result) && ObjectUtil.isBigDecimalNotZero(prozentsatzTransactionsGebuehr)) {
			result = MathUtil.calculateRuleOfThree(result, prozentsatzTransactionsGebuehr, new BigDecimal("100"));
		}

		return result;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragDepotWaehrungVorGebuehr() {
		return MathUtil.calculateRuleOfThree(betragTransaktionsWaehrung, new BigDecimal("1"), kursTransaktionsWaehrung);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragDepotWaehrungNachGebuehr() {
		return getBetragDepotWaehrungVorGebuehr().subtract(getBetragDepotWaehrungGebuehr());
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragDepotWaehrungGebuehr() {
		BigDecimal result = getBetragDepotWaehrungVorGebuehr();

		if (ObjectUtil.isBigDecimalNotZero(result) && ObjectUtil.isBigDecimalNotZero(prozentsatzTransactionsGebuehr)) {
			result = MathUtil.calculateRuleOfThree(result, prozentsatzTransactionsGebuehr, new BigDecimal("100"));
		}

		return result;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragTransaktionsWaehrungNachGebuehr() {
		BigDecimal result = betragTransaktionsWaehrung != null ? betragTransaktionsWaehrung : BigDecimal.ZERO;
		return result.subtract(getBetragTransaktionsWaehrungGebuehr());
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragTransaktionsWaehrungGebuehr() {
		BigDecimal result = betragTransaktionsWaehrung != null ? betragTransaktionsWaehrung : BigDecimal.ZERO;

		if (ObjectUtil.isBigDecimalNotZero(result) && ObjectUtil.isBigDecimalNotZero(prozentsatzTransactionsGebuehr)) {
			result = MathUtil.calculateRuleOfThree(result, prozentsatzTransactionsGebuehr, new BigDecimal("100"));
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
	 *           the transactionDate to set
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
	 *           the transactionType to set
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
	 *           the basisWaehrung to set
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
	 *           the transaktionsWaehrung to set
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
	 *           the depotWaehrung to set
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
	 *           the kursBasiswaehrung to set
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
	 *           the kursTransaktionsWaehrung to set
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
	 *           the betragTransaktionsWaehrung to set
	 */
	public void setBetragTransaktionsWaehrung(final BigDecimal betragTransaktionsWaehrung) {
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
	 *           the prozentsatzTransactionsGebuehr to set
	 */
	public void setProzentsatzTransactionsGebuehr(final BigDecimal prozentsatzTransactionsGebuehr) {
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
	 *           the referenz to set
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
	 *           the beschreibung to set
	 */
	public void setBeschreibung(final String beschreibung) {
		this.beschreibung = beschreibung;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Transaction\n");

		sb.append("Referenz: " + getReferenz() + "\n");
		sb.append("Beschreibung: " + getBeschreibung() + "\n");
		sb.append("Transactionsdatum: " + getTransactionDate() + "\n");
		sb.append("Transaktionstype: " + getTransactionType().getLabel() + " ("+ getTransactionType().getDescription() + ")\n");

		sb.append("Prozentsatz Transaktionsgebuehr: " + getProzentsatzTransactionsGebuehr() + " %\n");

		sb.append("Basiswaehrung: " + getBasisWaehrung().getLabel() + "\n");
		sb.append("Transaktionswaehrung: " + getTransaktionsWaehrung().getLabel() + "\n");
		sb.append("Depotwaehrung: " + getDepotWaehrung().getLabel() + "\n");

		sb.append("Werte Basiswaehrung:\n");
		sb.append("Betrag vor Gebuehr: " + getBetragBasisWaehrungVorGebuehr() + " " + getBasisWaehrung() + "\n");
		sb.append("Betrag Gebuehr: " + getBetragBasisWaehrungGebuehr() + " "	+ getBasisWaehrung() + "\n");
		sb.append("Betrag nach Gebuehr: " + getBetragBasisWaehrungNachGebuehr() + " " + getBasisWaehrung() + "\n");

		sb.append("Werte Transaktionswaehrung:\n");
		sb.append("Betrag vor Gebuehr: " + getBetragTransaktionsWaehrung() + " " + getTransaktionsWaehrung() + "\n");
		sb.append("Betrag Gebuehr: " + getBetragTransaktionsWaehrungGebuehr() + " " + getTransaktionsWaehrung() + "\n");
		sb.append("Betrag nach Gebuehr: " + getBetragTransaktionsWaehrungNachGebuehr() + " " + getTransaktionsWaehrung() + "\n");

		sb.append("Werte Depotwaehrung:\n");
		sb.append("Betrag vor Gebuehr: " + getBetragDepotWaehrungVorGebuehr() + " " + getDepotWaehrung() + "\n");
		sb.append("Betrag Gebuehr: " + getBetragDepotWaehrungGebuehr() + " " + getDepotWaehrung() + "\n");
		sb.append("Betrag nach Gebuehr: " + getBetragDepotWaehrungNachGebuehr() + " " + getDepotWaehrung() + "\n");

		return sb.toString();
	}

	@Override
	public int compareTo(final Transaction transaction) {

		if (getTransactionDate() == null || transaction.getTransactionDate() == null) {
			return 0;
		}
		return getTransactionDate().compareTo(transaction.getTransactionDate());
	}
}