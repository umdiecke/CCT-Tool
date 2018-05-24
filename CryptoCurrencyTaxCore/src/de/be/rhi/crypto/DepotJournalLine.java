/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.Days;

import de.be.rhi.crypto.util.ObjectUtil;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 * @version 1.0
 * @since 29.01.2018, 13:55:49
 *
 */
public class DepotJournalLine extends Transaction {

	private String kaufReferenz;
	private DateTime kaufDatum;
	private String verkaufReferenz;
	private DateTime verkaufDatum;
	private Boolean kauf;
	private BigDecimal zuAbgang;
	private BigDecimal betragEinkaufBasisWaehrung;
	private BigDecimal betragVerkaufBasisWaehrung;
	private BigDecimal betragDepotWaehrung;
	private BigDecimal gewinnVerlustBasisWaehrungGesamt;

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transaction
	 */
	public DepotJournalLine(final Transaction transaction) {
		super(transaction);
	}

	public boolean isInEinJahresRegel() {
		boolean result = false;
		if (this.kaufDatum != null && getTransactionDate() != null) {
			result = Days.daysBetween(this.kaufDatum, getTransactionDate()).getDays() <= 365;
		}
		return result;
	}

	/**
	 * @return the betragGewinnVerlustBasisWaehrung
	 */
	public BigDecimal getBetragGewinnVerlustBasisWaehrung() {
		BigDecimal einkaufswert = this.betragEinkaufBasisWaehrung != null ? this.betragEinkaufBasisWaehrung : BigDecimal.ZERO;
		BigDecimal verkaufswert = this.betragVerkaufBasisWaehrung != null ? this.betragVerkaufBasisWaehrung : BigDecimal.ZERO;

		return ObjectUtil.cutZeroFractionDigits(verkaufswert.subtract(einkaufswert));
	}

	/**
	 * @return the kaufReferenz
	 */
	public String getKaufReferenz() {
		return this.kaufReferenz;
	}

	/**
	 * @param kaufReferenz
	 *           the kaufReferenz to set
	 */
	public void setKaufReferenz(final String kaufReferenz) {
		this.kaufReferenz = kaufReferenz;
	}

	/**
	 * @return the kaufDatum
	 */
	public DateTime getKaufDatum() {
		return this.kaufDatum;
	}

	/**
	 * @param kaufDatum
	 *           the kaufDatum to set
	 */
	public void setKaufDatum(final DateTime kaufDatum) {
		this.kaufDatum = kaufDatum;
	}

	/**
	 * @return the verkaufReferenz
	 */
	public String getVerkaufReferenz() {
		return this.verkaufReferenz;
	}

	/**
	 * @param verkaufReferenz
	 *           the verkaufReferenz to set
	 */
	public void setVerkaufReferenz(final String verkaufReferenz) {
		this.verkaufReferenz = verkaufReferenz;
	}

	/**
	 * @return the verkaufDatum
	 */
	public DateTime getVerkaufDatum() {
		return this.verkaufDatum;
	}

	/**
	 * @param verkaufDatum
	 *           the verkaufDatum to set
	 */
	public void setVerkaufDatum(final DateTime verkaufDatum) {
		this.verkaufDatum = verkaufDatum;
	}

	/**
	 * @return the kauf
	 */
	public Boolean getKauf() {
		return this.kauf;
	}

	/**
	 * @param kauf
	 *           the kauf to set
	 */
	public void setKauf(final Boolean kauf) {
		this.kauf = kauf;
	}

	/**
	 * @return the zuAbgang
	 */
	public BigDecimal getZuAbgang() {
		return ObjectUtil.cutZeroFractionDigits(this.zuAbgang);
	}

	/**
	 * @param zuAbgang
	 *           the zuAbgang to set
	 */
	public void setZuAbgang(final BigDecimal zuAbgang) {
		this.zuAbgang = zuAbgang;
	}

	/**
	 * @return the betragEinkaufBasisWaehrung
	 */
	public BigDecimal getBetragEinkaufBasisWaehrung() {
		return ObjectUtil.cutZeroFractionDigits(this.betragEinkaufBasisWaehrung);
	}

	/**
	 * @param betragEinkaufBasisWaehrung
	 *           the betragEinkaufBasisWaehrung to set
	 */
	public void setBetragEinkaufBasisWaehrung(final BigDecimal betragEinkaufBasisWaehrung) {
		this.betragEinkaufBasisWaehrung = betragEinkaufBasisWaehrung;
	}

	/**
	 * @return the betragVerkaufBasisWaehrung
	 */
	public BigDecimal getBetragVerkaufBasisWaehrung() {
		return ObjectUtil.cutZeroFractionDigits(this.betragVerkaufBasisWaehrung);
	}

	/**
	 * @param betragVerkaufBasisWaehrung
	 *           the betragVerkaufBasisWaehrung to set
	 */
	public void setBetragVerkaufBasisWaehrung(final BigDecimal betragVerkaufBasisWaehrung) {
		this.betragVerkaufBasisWaehrung = betragVerkaufBasisWaehrung;
	}

	/**
	 * @return the betragDepotWaehrung
	 */
	public BigDecimal getBetragDepotWaehrung() {
		return ObjectUtil.cutZeroFractionDigits(this.betragDepotWaehrung);
	}

	/**
	 * @param betragDepotWaehrung
	 *           the betragDepotWaehrung to set
	 */
	public void setBetragDepotWaehrung(final BigDecimal betragDepotWaehrung) {
		this.betragDepotWaehrung = betragDepotWaehrung;
	}

	/**
	 * @return the gewinnVerlustBasisWaehrungGesamt
	 */
	public BigDecimal getGewinnVerlustBasisWaehrungGesamt() {
		return ObjectUtil.cutZeroFractionDigits(this.gewinnVerlustBasisWaehrungGesamt);
	}

	/**
	 * @param gewinnVerlustBasisWaehrungGesamt
	 *           the gewinnVerlustBasisWaehrungGesamt to set
	 */
	public void setGewinnVerlustBasisWaehrungGesamt(final BigDecimal gewinnVerlustBasisWaehrungGesamt) {
		this.gewinnVerlustBasisWaehrungGesamt = gewinnVerlustBasisWaehrungGesamt;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());

		sb.append("\nJournalLine\n");

		sb.append("Kaufreferenz: " + getKaufReferenz() + "\n");
		sb.append("Kaufdatum: " + getKaufDatum() + "\n");
		sb.append("In Einjahresregel: " + isInEinJahresRegel() + "\n");
		sb.append("Zu- / Abgang: " + getZuAbgang() + " " + getDepotWaehrung() + "\n");
		sb.append("Wert bei Kauf: " + getBetragEinkaufBasisWaehrung() + " " + getBasisWaehrung() + "\n");
		sb.append("Wert bei Verkauf: " + getBetragVerkaufBasisWaehrung() + " " + getBasisWaehrung() + "\n");
		sb.append("Gewinn / Verlust: " + getBetragGewinnVerlustBasisWaehrung() + " " + getBasisWaehrung() + "\n");
		sb.append("Kontostand: " + getBetragDepotWaehrung() + " " + getDepotWaehrung() + "\n");
		sb.append("Gewinn / Verlust (gesamt): " + getGewinnVerlustBasisWaehrungGesamt() + " " + getBasisWaehrung() + "\n\n");

		return sb.toString();
	}
}
