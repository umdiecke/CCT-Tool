/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto;

import java.math.BigDecimal;
import java.util.Date;

import de.be.rhi.crypto.util.MathUtil;
import de.be.rhi.crypto.util.ObjectUtil;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
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
	private String referenz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private String marktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal betragBasisWaehrungVorGebuehrMarktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal betragBasisWaehrungNachGebuehrMarktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal betragBasisWaehrungGebuehrMarktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal betragTransaktionsWaehrungNachGebuehrMarktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal betragTransaktionsWaehrungGebuehrMarktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal betragDepotWaehrungVorGebuehrMarktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal betragDepotWaehrungNachGebuehrMarktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal betragDepotWaehrungGebuehrMarktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal kontostandDepotWaehrungMarktplatz;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private BigDecimal eigenanteilTransaktionsgebuehr;

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getKontostandBasisWaehrungCalc() {
		BigDecimal result = BigDecimal.ZERO;
		if (this.kursBasiswaehrung != null && this.kontostandDepotWaehrungMarktplatz != null) {
			result = ObjectUtil.cutZeroFractionDigits(this.kontostandDepotWaehrungMarktplatz.multiply(this.kursBasiswaehrung));
		}
		return result;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getKontostandTransaktionsWaehrungCalc() {
		BigDecimal result = BigDecimal.ZERO;
		if (this.kursTransaktionsWaehrung != null && this.kontostandDepotWaehrungMarktplatz != null) {
			result = ObjectUtil.cutZeroFractionDigits(this.kontostandDepotWaehrungMarktplatz.multiply(this.kursTransaktionsWaehrung));
		}
		return result;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragBasisWaehrungVorGebuehrCalc() {
		return MathUtil.calculateRuleOfThree(this.kursBasiswaehrung, this.betragTransaktionsWaehrung, this.kursTransaktionsWaehrung);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragBasisWaehrungNachGebuehrCalc() {
		return ObjectUtil.cutZeroFractionDigits(getBetragBasisWaehrungVorGebuehrCalc().subtract(getBetragBasisWaehrungGebuehrCalc()));
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragBasisWaehrungGebuehrCalc() {
		BigDecimal result = getBetragBasisWaehrungVorGebuehrCalc();

		if (ObjectUtil.isBigDecimalNotZero(result) && ObjectUtil.isBigDecimalNotZero(getProzentsatzTransactionsGebuehr())) {
			result = MathUtil.calculateRuleOfThree(result, getProzentsatzTransactionsGebuehr(), new BigDecimal("100"));
			result = calcEigenanteilGebuehr(result);
		}

		return ObjectUtil.cutZeroFractionDigits(result);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param result
	 * @return
	 */
	protected BigDecimal calcEigenanteilGebuehr(BigDecimal result) {
		BigDecimal eigenanteil = getEigenanteilTransaktionsgebuehr() != null ? getEigenanteilTransaktionsgebuehr().multiply(new BigDecimal("0.01")) : new BigDecimal("1");
		if (ObjectUtil.isBigDecimalNotZero(result) && eigenanteil != null) {
			result = result.multiply(eigenanteil);
		}
		return result;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragDepotWaehrungVorGebuehrCalc() {
		return MathUtil.calculateRuleOfThree(this.betragTransaktionsWaehrung, new BigDecimal("1"), this.kursTransaktionsWaehrung);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragDepotWaehrungNachGebuehrCalc() {
		return getBetragDepotWaehrungVorGebuehrCalc().subtract(getBetragDepotWaehrungGebuehrCalc());
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragDepotWaehrungGebuehrCalc() {
		BigDecimal result = getBetragDepotWaehrungVorGebuehrCalc();

		if (ObjectUtil.isBigDecimalNotZero(result) && ObjectUtil.isBigDecimalNotZero(getProzentsatzTransactionsGebuehr())) {
			result = MathUtil.calculateRuleOfThree(result, getProzentsatzTransactionsGebuehr(), new BigDecimal("100"));
		}

		return result;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragTransaktionsWaehrungNachGebuehrCalc() {
		BigDecimal result = this.betragTransaktionsWaehrung != null ? this.betragTransaktionsWaehrung : BigDecimal.ZERO;
		return ObjectUtil.cutZeroFractionDigits(result.subtract(getBetragTransaktionsWaehrungGebuehrCalc()));
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public BigDecimal getBetragTransaktionsWaehrungGebuehrCalc() {
		BigDecimal result = this.betragTransaktionsWaehrung != null ? this.betragTransaktionsWaehrung : BigDecimal.ZERO;

		if (ObjectUtil.isBigDecimalNotZero(result) && ObjectUtil.isBigDecimalNotZero(getProzentsatzTransactionsGebuehr())) {
			result = MathUtil.calculateRuleOfThree(result, getProzentsatzTransactionsGebuehr(), new BigDecimal("100"));
			result = calcEigenanteilGebuehr(result);
		}
		return ObjectUtil.cutZeroFractionDigits(result);
	}

	/**
	 * @return the prozentsatzTransactionsGebuehr
	 */
	public BigDecimal getProzentsatzTransactionsGebuehr() {
		BigDecimal base100 = new BigDecimal("100");
		return base100.subtract(MathUtil.calculateRuleOfThree(getBetragDepotWaehrungNachGebuehrMarktplatz(), base100, getBetragDepotWaehrungVorGebuehrMarktplatz()))
				.setScale(MathUtil.DEFAULT_ACCOUNTING_SCALE, MathUtil.DEFAULT_ROUNDING_MODE);
	}

	/**
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return this.transactionDate;
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
		return this.transactionType;
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
		return this.basisWaehrung;
	}

	/**
	 * @param basisWaehrung
	 *           the basisWaehrung to set
	 */
	public void setBasisWaehrung(final Currency basisWaehrung) {
		this.basisWaehrung = basisWaehrung;

		if (this.transaktionsWaehrung == null) {
			this.transaktionsWaehrung = basisWaehrung;
		}
	}

	/**
	 * @return the transaktionsWaehrung
	 */
	public Currency getTransaktionsWaehrung() {
		return this.transaktionsWaehrung;
	}

	/**
	 * @param transaktionsWaehrung
	 *           the transaktionsWaehrung to set
	 */
	public void setTransaktionsWaehrung(final Currency transaktionsWaehrung) {
		this.transaktionsWaehrung = transaktionsWaehrung;

		if (this.basisWaehrung == null) {
			this.basisWaehrung = transaktionsWaehrung;
		}
	}

	/**
	 * @return the depotWaehrung
	 */
	public Currency getDepotWaehrung() {
		return this.depotWaehrung;
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
		return this.kursBasiswaehrung;
	}

	/**
	 * @param kursBasiswaehrung
	 *           the kursBasiswaehrung to set
	 */
	public void setKursBasiswaehrung(final BigDecimal kursBasiswaehrung) {
		this.kursBasiswaehrung = ObjectUtil.cutZeroFractionDigits(kursBasiswaehrung);

		if (this.kursTransaktionsWaehrung == null) {
			this.kursTransaktionsWaehrung = this.kursBasiswaehrung;
		}
	}

	/**
	 * @return the kursTransaktionsWaehrung
	 */
	public BigDecimal getKursTransaktionsWaehrung() {
		return this.kursTransaktionsWaehrung;
	}

	/**
	 * @param kursTransaktionsWaehrung
	 *           the kursTransaktionsWaehrung to set
	 */
	public void setKursTransaktionsWaehrung(final BigDecimal kursTransaktionsWaehrung) {
		this.kursTransaktionsWaehrung = ObjectUtil.cutZeroFractionDigits(kursTransaktionsWaehrung);

		if (this.kursBasiswaehrung == null) {
			this.kursBasiswaehrung = this.kursTransaktionsWaehrung;
		}
	}

	/**
	 * @return the betragTransaktionsWaehrung
	 */
	public BigDecimal getBetragTransaktionsWaehrung() {
		return this.betragTransaktionsWaehrung;
	}

	/**
	 * @param betragTransaktionsWaehrung
	 *           the betragTransaktionsWaehrung to set
	 */
	public void setBetragTransaktionsWaehrung(final BigDecimal betragTransaktionsWaehrung) {
		this.betragTransaktionsWaehrung = ObjectUtil.cutZeroFractionDigits(betragTransaktionsWaehrung);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 */
	public void calcMissingValues() {
		if (baseValuesFound()) {
			if (this.betragDepotWaehrungVorGebuehrMarktplatz == null) {
				this.betragDepotWaehrungVorGebuehrMarktplatz = getBetragDepotWaehrungVorGebuehrCalc();
			}
			if (this.betragDepotWaehrungGebuehrMarktplatz == null) {
				this.betragDepotWaehrungGebuehrMarktplatz = getBetragDepotWaehrungGebuehrCalc();
			}

			if (this.betragTransaktionsWaehrungNachGebuehrMarktplatz == null) {
				this.betragTransaktionsWaehrungNachGebuehrMarktplatz = getBetragTransaktionsWaehrungNachGebuehrCalc();
			}
			if (this.betragTransaktionsWaehrungGebuehrMarktplatz == null) {
				this.betragTransaktionsWaehrungGebuehrMarktplatz = getBetragTransaktionsWaehrungGebuehrCalc();
			}

			if (this.betragBasisWaehrungVorGebuehrMarktplatz == null) {
				this.betragBasisWaehrungVorGebuehrMarktplatz = getBetragBasisWaehrungVorGebuehrCalc();
			}
			if (this.betragBasisWaehrungNachGebuehrMarktplatz == null) {
				this.betragBasisWaehrungNachGebuehrMarktplatz = getBetragBasisWaehrungNachGebuehrCalc();
			}
			if (this.betragBasisWaehrungGebuehrMarktplatz == null) {
				this.betragBasisWaehrungGebuehrMarktplatz = getBetragBasisWaehrungGebuehrCalc();
			}
		}
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	private boolean baseValuesFound() {
		return this.kursBasiswaehrung != null && this.betragTransaktionsWaehrung != null && this.kursTransaktionsWaehrung != null && this.betragDepotWaehrungNachGebuehrMarktplatz != null;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public boolean valide() {
		boolean valide = true;
		if (this.transactionType != TransactionType.INITIALISIERUNG) {
			valide = valuesEqual(getBetragBasisWaehrungVorGebuehrMarktplatz(), getBetragBasisWaehrungVorGebuehrCalc());
			valide = valide && valuesEqual(getBetragBasisWaehrungGebuehrMarktplatz(), getBetragBasisWaehrungGebuehrCalc());
			valide = valide && valuesEqual(getBetragBasisWaehrungNachGebuehrMarktplatz(), getBetragBasisWaehrungNachGebuehrCalc());
			valide = valide && valuesEqual(getBetragTransaktionsWaehrungGebuehrMarktplatz(), getBetragTransaktionsWaehrungGebuehrCalc());
			valide = valide && valuesEqual(getBetragTransaktionsWaehrungNachGebuehrMarktplatz(), getBetragTransaktionsWaehrungNachGebuehrCalc());
			valide = valide && valuesEqual(getBetragDepotWaehrungVorGebuehrMarktplatz(), getBetragDepotWaehrungVorGebuehrCalc());
			valide = valide && valuesEqual(getBetragDepotWaehrungGebuehrMarktplatz(), getBetragDepotWaehrungGebuehrCalc());
			valide = valide && valuesEqual(getBetragDepotWaehrungNachGebuehrMarktplatz(), getBetragDepotWaehrungNachGebuehrCalc());
		}
		return valide;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	private boolean valuesEqual(final BigDecimal v1, final BigDecimal v2) {
		boolean result = false;
		if (v1 != null && v2 != null && v1.compareTo(v2) == 0) {
			result = true;
		}
		return result;
	}

	/**
	 * @return the referenz
	 */
	public String getReferenz() {
		return this.referenz;
	}

	/**
	 * @param referenz
	 *           the referenz to set
	 */
	public void setReferenz(final String referenz) {
		this.referenz = referenz;
	}

	/**
	 * @return the marktplatz
	 */
	public String getMarktplatz() {
		return this.marktplatz;
	}

	/**
	 * @param marktplatz
	 *           the marktplatz to set
	 */
	public void setMarktplatz(final String marktplatz) {
		this.marktplatz = marktplatz;
	}

	/**
	 * @return the betragBasisWaehrungVorGebuehrMarktplatz
	 */
	public BigDecimal getBetragBasisWaehrungVorGebuehrMarktplatz() {
		return ObjectUtil.cutZeroFractionDigits(this.betragBasisWaehrungVorGebuehrMarktplatz);
	}

	/**
	 * @param betragBasisWaehrungVorGebuehrMarktplatz
	 *           the betragBasisWaehrungVorGebuehrMarktplatz to set
	 */
	public void setBetragBasisWaehrungVorGebuehrMarktplatz(final BigDecimal betragBasisWaehrungVorGebuehrMarktplatz) {
		this.betragBasisWaehrungVorGebuehrMarktplatz = betragBasisWaehrungVorGebuehrMarktplatz;
	}

	/**
	 * @return the betragBasisWaehrungNachGebuehrMarktplatz
	 */
	public BigDecimal getBetragBasisWaehrungNachGebuehrMarktplatz() {
		return ObjectUtil.cutZeroFractionDigits(this.betragBasisWaehrungNachGebuehrMarktplatz);
	}

	/**
	 * @param betragBasisWaehrungNachGebuehrMarktplatz
	 *           the betragBasisWaehrungNachGebuehrMarktplatz to set
	 */
	public void setBetragBasisWaehrungNachGebuehrMarktplatz(final BigDecimal betragBasisWaehrungNachGebuehrMarktplatz) {
		this.betragBasisWaehrungNachGebuehrMarktplatz = betragBasisWaehrungNachGebuehrMarktplatz;
	}

	/**
	 * @return the betragBasisWaehrungGebuehrMarktplatz
	 */
	public BigDecimal getBetragBasisWaehrungGebuehrMarktplatz() {
		return ObjectUtil.cutZeroFractionDigits(this.betragBasisWaehrungGebuehrMarktplatz);
	}

	/**
	 * @param betragBasisWaehrungGebuehrMarktplatz
	 *           the betragBasisWaehrungGebuehrMarktplatz to set
	 */
	public void setBetragBasisWaehrungGebuehrMarktplatz(final BigDecimal betragBasisWaehrungGebuehrMarktplatz) {
		this.betragBasisWaehrungGebuehrMarktplatz = betragBasisWaehrungGebuehrMarktplatz;
	}

	/**
	 * @return the betragTransaktionsWaehrungNachGebuehrMarktplatz
	 */
	public BigDecimal getBetragTransaktionsWaehrungNachGebuehrMarktplatz() {
		return ObjectUtil.cutZeroFractionDigits(this.betragTransaktionsWaehrungNachGebuehrMarktplatz);
	}

	/**
	 * @param betragTransaktionsWaehrungNachGebuehrMarktplatz
	 *           the betragTransaktionsWaehrungNachGebuehrMarktplatz to set
	 */
	public void setBetragTransaktionsWaehrungNachGebuehrMarktplatz(final BigDecimal betragTransaktionsWaehrungNachGebuehrMarktplatz) {
		this.betragTransaktionsWaehrungNachGebuehrMarktplatz = betragTransaktionsWaehrungNachGebuehrMarktplatz;
	}

	/**
	 * @return the betragTransaktionsWaehrungGebuehrMarktplatz
	 */
	public BigDecimal getBetragTransaktionsWaehrungGebuehrMarktplatz() {
		return ObjectUtil.cutZeroFractionDigits(this.betragTransaktionsWaehrungGebuehrMarktplatz);
	}

	/**
	 * @param betragTransaktionsWaehrungGebuehrMarktplatz
	 *           the betragTransaktionsWaehrungGebuehrMarktplatz to set
	 */
	public void setBetragTransaktionsWaehrungGebuehrMarktplatz(final BigDecimal betragTransaktionsWaehrungGebuehrMarktplatz) {
		this.betragTransaktionsWaehrungGebuehrMarktplatz = betragTransaktionsWaehrungGebuehrMarktplatz;
	}

	/**
	 * @return the betragDepotWaehrungVorGebuehrMarktplatz
	 */
	public BigDecimal getBetragDepotWaehrungVorGebuehrMarktplatz() {
		return ObjectUtil.cutZeroFractionDigits(this.betragDepotWaehrungVorGebuehrMarktplatz);
	}

	/**
	 * @param betragDepotWaehrungVorGebuehrMarktplatz
	 *           the betragDepotWaehrungVorGebuehrMarktplatz to set
	 */
	public void setBetragDepotWaehrungVorGebuehrMarktplatz(final BigDecimal betragDepotWaehrungVorGebuehrMarktplatz) {
		this.betragDepotWaehrungVorGebuehrMarktplatz = betragDepotWaehrungVorGebuehrMarktplatz;
	}

	/**
	 * @return the betragDepotWaehrungNachGebuehrMarktplatz
	 */
	public BigDecimal getBetragDepotWaehrungNachGebuehrMarktplatz() {
		return ObjectUtil.cutZeroFractionDigits(this.betragDepotWaehrungNachGebuehrMarktplatz);
	}

	/**
	 * @param betragDepotWaehrungNachGebuehrMarktplatz
	 *           the betragDepotWaehrungNachGebuehrMarktplatz to set
	 */
	public void setBetragDepotWaehrungNachGebuehrMarktplatz(final BigDecimal betragDepotWaehrungNachGebuehrMarktplatz) {
		this.betragDepotWaehrungNachGebuehrMarktplatz = betragDepotWaehrungNachGebuehrMarktplatz;
	}

	/**
	 * @return the betragDepotWaehrungGebuehrMarktplatz
	 */
	public BigDecimal getBetragDepotWaehrungGebuehrMarktplatz() {
		return ObjectUtil.cutZeroFractionDigits(this.betragDepotWaehrungGebuehrMarktplatz);
	}

	/**
	 * @param betragDepotWaehrungGebuehrMarktplatz
	 *           the betragDepotWaehrungGebuehrMarktplatz to set
	 */
	public void setBetragDepotWaehrungGebuehrMarktplatz(final BigDecimal betragDepotWaehrungGebuehrMarktplatz) {
		this.betragDepotWaehrungGebuehrMarktplatz = betragDepotWaehrungGebuehrMarktplatz;
	}

	/**
	 * @return the kontostandDepotWaehrungMarktplatz
	 */
	public BigDecimal getKontostandDepotWaehrungMarktplatz() {
		return ObjectUtil.cutZeroFractionDigits(this.kontostandDepotWaehrungMarktplatz);
	}

	/**
	 * @param kontostandDepotWaehrungMarktplatz
	 *           the kontostandDepotWaehrungMarktplatz to set
	 */
	public void setKontostandDepotWaehrungMarktplatz(final BigDecimal kontostandDepotWaehrungMarktplatz) {
		this.kontostandDepotWaehrungMarktplatz = ObjectUtil.cutZeroFractionDigits(kontostandDepotWaehrungMarktplatz);
	}

	/**
	 * @return the eigenanteilTransaktionsgebuehr
	 */
	public BigDecimal getEigenanteilTransaktionsgebuehr() {
		return this.eigenanteilTransaktionsgebuehr;
	}

	/**
	 * @param eigenanteilTransaktionsgebuehr
	 *           the eigenanteilTransaktionsgebuehr to set
	 */
	public void setEigenanteilTransaktionsgebuehr(final BigDecimal eigenanteilTransaktionsgebuehr) {
		this.eigenanteilTransaktionsgebuehr = eigenanteilTransaktionsgebuehr;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Transaction\n");

		sb.append("Referenz: " + getReferenz() + "\n");
		sb.append("Marktplatz: " + getMarktplatz() + "\n");
		sb.append("Transactionsdatum: " + getTransactionDate() + "\n");
		sb.append("Transaktionstype: " + getTransactionType().getLabel() + " ("+ getTransactionType().getDescription() + ")\n");

		sb.append("Basiswaehrung: " + getBasisWaehrung().getLabel() + "\n");
		sb.append("Transaktionswaehrung: " + getTransaktionsWaehrung().getLabel() + "\n");
		sb.append("Depotwaehrung: " + getDepotWaehrung().getLabel() + "\n");

		sb.append("Kurs Basiswaehrung: " + getKursBasiswaehrung() + " " + getBasisWaehrung() + "\n");
		sb.append("Kurs Transaktionswaehrung: " + getKursTransaktionsWaehrung() + " " + getTransaktionsWaehrung() + "\n");
		sb.append("Prozentsatz Transaktionsgebuehr: " + getProzentsatzTransactionsGebuehr() + " %\n");
		sb.append("Eigenanteil Transaktionsgebuehr: " + getEigenanteilTransaktionsgebuehr() + " %\n");

		sb.append("Kontostand Basiswaehrung: " + getKontostandBasisWaehrungCalc() + " " + getBasisWaehrung() + "\n");
		sb.append("Kontostand Transaktionswaehrung: " + getKontostandTransaktionsWaehrungCalc() + " " + getTransaktionsWaehrung() + "\n");
		sb.append("Kontostand Depotwaehrung: " + getKontostandDepotWaehrungMarktplatz() + " " + getDepotWaehrung() + "\n");

		sb.append("Werte Basiswaehrung (Marktplatz):\n");
		sb.append("Betrag vor Gebuehr: " + getBetragBasisWaehrungVorGebuehrMarktplatz() + " " + getBasisWaehrung() + "\n");
		sb.append("Betrag Gebuehr: " + getBetragBasisWaehrungGebuehrMarktplatz() + " " + getBasisWaehrung() + "\n");
		sb.append("Betrag nach Gebuehr: " + getBetragBasisWaehrungNachGebuehrMarktplatz() + " " + getBasisWaehrung() + "\n");

		sb.append("Werte Basiswaehrung (berechnet):\n");
		sb.append("Betrag vor Gebuehr: " + getBetragBasisWaehrungVorGebuehrCalc() + " " + getBasisWaehrung() + "\n");
		sb.append("Betrag Gebuehr: " + getBetragBasisWaehrungGebuehrCalc() + " "	+ getBasisWaehrung() + "\n");
		sb.append("Betrag nach Gebuehr: " + getBetragBasisWaehrungNachGebuehrCalc() + " " + getBasisWaehrung() + "\n");

		sb.append("Werte Transaktionswaehrung (Marktplatz):\n");
		sb.append("Betrag vor Gebuehr: " + getBetragTransaktionsWaehrung() + " " + getTransaktionsWaehrung() + "\n");
		sb.append("Betrag Gebuehr: " + getBetragTransaktionsWaehrungGebuehrMarktplatz() + " " + getTransaktionsWaehrung() + "\n");
		sb.append("Betrag nach Gebuehr: " + getBetragTransaktionsWaehrungNachGebuehrMarktplatz() + " " + getTransaktionsWaehrung() + "\n");

		sb.append("Werte Transaktionswaehrung (berechnet):\n");
		sb.append("Betrag vor Gebuehr: " + getBetragTransaktionsWaehrung() + " " + getTransaktionsWaehrung() + "\n");
		sb.append("Betrag Gebuehr: " + getBetragTransaktionsWaehrungGebuehrCalc() + " " + getTransaktionsWaehrung() + "\n");
		sb.append("Betrag nach Gebuehr: " + getBetragTransaktionsWaehrungNachGebuehrCalc() + " " + getTransaktionsWaehrung() + "\n");

		sb.append("Werte Depotwaehrung (Marktplatz):\n");
		sb.append("Betrag vor Gebuehr: " + getBetragDepotWaehrungVorGebuehrMarktplatz() + " " + getDepotWaehrung() + "\n");
		sb.append("Betrag Gebuehr: " + getBetragDepotWaehrungGebuehrMarktplatz() + " " + getDepotWaehrung() + "\n");
		sb.append("Betrag nach Gebuehr: " + getBetragDepotWaehrungNachGebuehrMarktplatz() + " " + getDepotWaehrung() + "\n");

		sb.append("Werte Depotwaehrung (berechnet):\n");
		sb.append("Betrag vor Gebuehr: " + getBetragDepotWaehrungVorGebuehrCalc() + " " + getDepotWaehrung() + "\n");
		sb.append("Betrag Gebuehr: " + getBetragDepotWaehrungGebuehrCalc() + " " + getDepotWaehrung() + "\n");
		sb.append("Betrag nach Gebuehr: " + getBetragDepotWaehrungNachGebuehrCalc() + " " + getDepotWaehrung() + "\n");

		sb.append("Valide: " + valide() + "\n");

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