/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import de.be.rhi.crypto.util.MathUtil;
import de.be.rhi.crypto.util.ObjectUtil;
import de.umdiecke.protocol.CsvProtocolExport;
import de.umdiecke.protocol.ProtocolElement;
import de.umdiecke.util.DataFormatter;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 * @version 1.0
 * @since 29.01.2018, 13:55:37
 *
 */
public class DepotJournal {

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private final Logger LOGGER = LogManager.getLogger();

	private TransactionHandler transactionHandler;
	private Currency depotWaehrung;

	private Currency basisWaehrung;
	private List<DepotJournalLine> journalEintraege;
	private BigDecimal betragDepotWaehrung;
	private BigDecimal gewinnVerlustBasisWaehrungGesamt;
	private HashMap<Integer, BigDecimal> gewinnVerlustBasisWaehrungVorSteuernJahrMap;

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transactionHandler
	 * @param currency
	 */
	public DepotJournal(final TransactionHandler transactionHandler, final Currency depotWaehrung) {
		this.transactionHandler = transactionHandler;
		this.depotWaehrung = depotWaehrung;
		this.gewinnVerlustBasisWaehrungVorSteuernJahrMap = new HashMap<>();
		initializeDepotJournal();
	}

	private void initializeDepotJournal() {

		this.journalEintraege = new ArrayList<>();
		this.betragDepotWaehrung = BigDecimal.ZERO;
		this.gewinnVerlustBasisWaehrungGesamt = BigDecimal.ZERO;

		if (this.depotWaehrung != null) {

			List<Transaction> transactionList = this.transactionHandler.rtvDepotTransactionList(this.depotWaehrung);
			List<Transaction> kaufListe = createKaufListe(transactionList);
			Transaction kaufBak = null;

			for (Transaction transaction : transactionList) {

				if (transaction.getTransactionType() == TransactionType.VERKAUF) {
					Transaction verkauf = new Transaction(transaction);

					while (verkauf.getBetragDepotWaehrungVorGebuehrMarktplatz().compareTo(BigDecimal.ZERO) > 0) {
						DepotJournalLine journalLine = new DepotJournalLine(transaction);
						Transaction kauf = kaufListe.get(0);

						if (kaufBak == null || !kaufBak.getReferenz().equals(kauf.getReferenz())) {
							kaufBak = new Transaction(kauf);
						}

						journalLine.setKaufReferenz(kauf.getReferenz());
						journalLine.setKaufDatum(kauf.getTransactionDate());
						journalLine.setVerkaufReferenz(verkauf.getReferenz());
						journalLine.setVerkaufDatum(verkauf.getTransactionDate());

						if (kauf.getBetragDepotWaehrungNachGebuehrMarktplatz().compareTo(verkauf.getBetragDepotWaehrungVorGebuehrMarktplatz()) > 0) {
							journalLine.setZuAbgang(verkauf.getBetragDepotWaehrungVorGebuehrMarktplatz().negate());
							kauf.setBetragDepotWaehrungNachGebuehrMarktplatz(kauf.getBetragDepotWaehrungNachGebuehrMarktplatz().subtract(verkauf.getBetragDepotWaehrungVorGebuehrMarktplatz()));
							verkauf.setBetragDepotWaehrungVorGebuehrMarktplatz(BigDecimal.ZERO);
						} else if (kauf.getBetragDepotWaehrungNachGebuehrMarktplatz().compareTo(verkauf.getBetragDepotWaehrungVorGebuehrMarktplatz()) < 0) {
							journalLine.setZuAbgang(kauf.getBetragDepotWaehrungNachGebuehrMarktplatz().negate());
							verkauf.setBetragDepotWaehrungVorGebuehrMarktplatz(verkauf.getBetragDepotWaehrungVorGebuehrMarktplatz().subtract(kauf.getBetragDepotWaehrungNachGebuehrMarktplatz()));
							kaufListe.remove(0);
						} else {
							journalLine.setZuAbgang(verkauf.getBetragDepotWaehrungVorGebuehrMarktplatz().negate());
							verkauf.setBetragDepotWaehrungVorGebuehrMarktplatz(BigDecimal.ZERO);
							kaufListe.remove(0);
						}

						journalLine.setBetragEinkaufBasisWaehrung(
								MathUtil.calculateRuleOfThree(journalLine.getZuAbgang().abs(), kaufBak.getBetragBasisWaehrungVorGebuehrMarktplatz(), kaufBak.getBetragDepotWaehrungNachGebuehrMarktplatz()));
						journalLine.setBetragVerkaufBasisWaehrung(MathUtil.calculateRuleOfThree(journalLine.getZuAbgang().abs(), transaction.getBetragBasisWaehrungVorGebuehrMarktplatz(),
								transaction.getBetragDepotWaehrungVorGebuehrMarktplatz()));

						this.betragDepotWaehrung = this.betragDepotWaehrung.add(journalLine.getZuAbgang());
						journalLine.setBetragDepotWaehrung(this.betragDepotWaehrung);

						// Set Gewinn und Verlust in Basiswährung für das entsprechende Steuerjahr
						BigDecimal gewinnVerlustBasisWaehrungVorSteuern = journalLine.getBetragGewinnVerlustBasisWaehrung().add(getZuVersteuerndenGewinnVerlust(journalLine.getTransactionDate().getYear()));
						if (ObjectUtil.isBigDecimalNotZero(gewinnVerlustBasisWaehrungVorSteuern) && StringUtils.isNotBlank(journalLine.getSteuerjahr())) {
							this.gewinnVerlustBasisWaehrungVorSteuernJahrMap.put(journalLine.getTransactionDate().getYear(), gewinnVerlustBasisWaehrungVorSteuern);
							journalLine.setGewinnVerlustBasisWaehrungSteuerjahr(gewinnVerlustBasisWaehrungVorSteuern);
						} else {
							journalLine.setGewinnVerlustBasisWaehrungSteuerjahr(BigDecimal.ZERO);
						}

						// Set Gewinn und Verlust in Basiswährung insgesammt
						this.gewinnVerlustBasisWaehrungGesamt = this.gewinnVerlustBasisWaehrungGesamt.add(journalLine.getBetragGewinnVerlustBasisWaehrung());
						journalLine.setGewinnVerlustBasisWaehrungGesamt(this.gewinnVerlustBasisWaehrungGesamt);

						this.journalEintraege.add(journalLine);
					}
				} else {
					DepotJournalLine journalLine = new DepotJournalLine(transaction);
					journalLine.setKaufReferenz(transaction.getReferenz());
					journalLine.setKaufDatum(transaction.getTransactionDate());
					journalLine.setVerkaufReferenz(null);
					journalLine.setVerkaufDatum(null);
					journalLine.setBetragEinkaufBasisWaehrung(BigDecimal.ZERO);
					journalLine.setBetragVerkaufBasisWaehrung(BigDecimal.ZERO);

					BigDecimal gewinnVerlustBasisWaehrungVorSteuern = journalLine.getBetragGewinnVerlustBasisWaehrung().add(getZuVersteuerndenGewinnVerlust(journalLine.getTransactionDate().getYear()));
					if (ObjectUtil.isBigDecimalNotZero(gewinnVerlustBasisWaehrungVorSteuern) && StringUtils.isNotBlank(journalLine.getSteuerjahr())) {
						this.gewinnVerlustBasisWaehrungVorSteuernJahrMap.put(journalLine.getTransactionDate().getYear(), gewinnVerlustBasisWaehrungVorSteuern);
						journalLine.setGewinnVerlustBasisWaehrungSteuerjahr(gewinnVerlustBasisWaehrungVorSteuern);
					} else {
						journalLine.setGewinnVerlustBasisWaehrungSteuerjahr(BigDecimal.ZERO);
					}

					this.gewinnVerlustBasisWaehrungGesamt = this.gewinnVerlustBasisWaehrungGesamt.add(journalLine.getBetragGewinnVerlustBasisWaehrung());
					journalLine.setGewinnVerlustBasisWaehrungGesamt(this.gewinnVerlustBasisWaehrungGesamt);

					if (transaction.getBetragDepotWaehrungNachGebuehrMarktplatz() != null) {
						journalLine.setZuAbgang(transaction.getBetragDepotWaehrungNachGebuehrMarktplatz());
						this.betragDepotWaehrung = this.betragDepotWaehrung.add(journalLine.getZuAbgang());
						journalLine.setBetragDepotWaehrung(this.betragDepotWaehrung);
					}

					this.journalEintraege.add(journalLine);

				}
			}
		}
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param jahr
	 * @return
	 */
	public BigDecimal getZuVersteuerndenGewinnVerlust(final Integer jahr) {
		BigDecimal gewinn = BigDecimal.ZERO;

		if (this.gewinnVerlustBasisWaehrungVorSteuernJahrMap.containsKey(jahr)) {
			gewinn = this.gewinnVerlustBasisWaehrungVorSteuernJahrMap.get(jahr);
		}

		// if (jahr != null) {
		// for (DepotJournalLine depotJournalLine : this.journalEintraege) {
		//
		// if (depotJournalLine.getTransactionDate().getYear() == jahr.intValue() && depotJournalLine.isInEinJahresRegel()) {
		// gewinn = gewinn.add(depotJournalLine.getBetragGewinnVerlustBasisWaehrung());
		// }
		// }
		// }

		return ObjectUtil.cutZeroFractionDigits(gewinn);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 */
	private List<Transaction> createKaufListe(final List<Transaction> transactionList) {
		List<Transaction> kaufListe = new ArrayList<>();
		this.basisWaehrung = null;

		for (Transaction transaction : transactionList) {
			if(this.basisWaehrung == null) {
				this.basisWaehrung = transaction.getBasisWaehrung();
			}

			if ((transaction.getTransactionType() == TransactionType.INITIALISIERUNG || transaction.getTransactionType() == TransactionType.KAUF)
					&& transaction.getBetragDepotWaehrungNachGebuehrMarktplatz() != null) {
				kaufListe.add(new Transaction(transaction));
			} else if (transaction.getTransactionType() != TransactionType.VERKAUF) {
				this.LOGGER.warn("Der Transaktionstyp '" + transaction.getTransactionType() + "' ist nicht definiert.");
			}
		}
		return kaufListe;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public int rtvJahrErsteTransaktion() {
		List<Transaction> transaktionen = getTransactionHandler().rtvDepotTransactionList(getDepotWaehrung());
		int anfangsJahr = !transaktionen.isEmpty() ? transaktionen.get(0).getTransactionDate().getYear() : new DateTime().getYear();
		return anfangsJahr;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public Set<Currency> rtvDepotCurrencies() {
		return this.transactionHandler.rtvDepotCurrencies();
	}


	/**
	 * @param depotWaehrung
	 *           the depotWaehrung to set
	 */
	public void setDepotWaehrung(final Currency depotWaehrung) {
		if (this.depotWaehrung != depotWaehrung) {
			this.depotWaehrung = depotWaehrung;
			initializeDepotJournal();
		}
	}

	/**
	 * @return the currency
	 */
	public Currency getDepotWaehrung() {
		return this.depotWaehrung;
	}

	/**
	 * @return the basisWaehrung
	 */
	public Currency getBasisWaehrung() {
		return this.basisWaehrung;
	}

	/**
	 * @return the journalEintraege
	 */
	public List<DepotJournalLine> getJournalEintraege() {
		return this.journalEintraege;
	}

	/**
	 * @return the transactionHandler
	 */
	public TransactionHandler getTransactionHandler() {
		return this.transactionHandler;
	}

	/**
	 * @return the betragDepotWaehrung
	 */
	public BigDecimal getBetragDepotWaehrung() {
		return ObjectUtil.cutZeroFractionDigits(this.betragDepotWaehrung);
	}

	/**
	 * @return the gewinnVerlustBasisWaehrungGesamt
	 */
	public BigDecimal getGewinnVerlustBasisWaehrungGesamt() {
		return ObjectUtil.cutZeroFractionDigits(this.gewinnVerlustBasisWaehrungGesamt);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param depotJournal
	 * @param fileAllgemein
	 * @param fileDetail
	 * @param jahr
	 * @param depotWaehrung
	 */
	public void exportJournalToCsv(final File fileAllgemein, final File fileDetail) {
		CsvProtocolExport csvProtocolExport = new CsvProtocolExport(fileAllgemein, fileDetail, false);
		ProtocolElement commonProtocolElement = new ProtocolElement("Allgemeine Inhalte");
		List<ProtocolElement> protocolElementList = new ArrayList<>();
		try {
			commonProtocolElement.addContentValue("Depot", getDepotWaehrung().getLabel());
			commonProtocolElement.addContentValue("Aktueller Bestand", getBetragDepotWaehrung() + " " + getDepotWaehrung());
			commonProtocolElement.addContentValue("Bisheriger Gewinn / Verlust", getGewinnVerlustBasisWaehrungGesamt() + " " + getBasisWaehrung());

			int anfangsJahr = rtvJahrErsteTransaktion();
			int aktuellesJahr = new DateTime().getYear();

			for (int i = anfangsJahr; i <= aktuellesJahr; i++) {
				commonProtocolElement.addContentValue("Zu versteuernder Gewinn / Verlust (" + i + ")", getZuVersteuerndenGewinnVerlust(i) + " " + getBasisWaehrung());
			}

			int i = 0;
			for (DepotJournalLine depotJournalLine : getJournalEintraege()) {
				ProtocolElement lineProtocolElement = new ProtocolElement(i++ + "");
				lineProtocolElement.addContentValue("Transaktionstyp", depotJournalLine.getTransactionType().getLabel());
				lineProtocolElement.addContentValue("Kaufreferenz", depotJournalLine.getKaufReferenz());
				lineProtocolElement.addContentValue("Kaufdatum", DataFormatter.convertDateTime(depotJournalLine.getKaufDatum(), null));
				lineProtocolElement.addContentValue("Kaufzeit", DataFormatter.convertDateTime(depotJournalLine.getKaufDatum(), "HH:mm:ss"));
				lineProtocolElement.addContentValue("Verkaufreferenz", depotJournalLine.getVerkaufReferenz());
				lineProtocolElement.addContentValue("Verkaufdatum", depotJournalLine.getVerkaufDatum() != null ? DataFormatter.convertDateTime(depotJournalLine.getVerkaufDatum(), null) : "");
				lineProtocolElement.addContentValue("Verkaufzeit", depotJournalLine.getVerkaufDatum() != null ? DataFormatter.convertDateTime(depotJournalLine.getVerkaufDatum(), "HH:mm:ss") : "");
				lineProtocolElement.addContentValue("Einjahresregel", depotJournalLine.isInEinJahresRegel());
				lineProtocolElement.addContentValue("Steuerjahr", depotJournalLine.getSteuerjahr());
				lineProtocolElement.addContentValue("Zu- / Abgang (" + depotJournalLine.getDepotWaehrung() + ")", depotJournalLine.getZuAbgang());
				lineProtocolElement.addContentValue("Wert bei Kauf (" + depotJournalLine.getBasisWaehrung() + ")", depotJournalLine.getBetragEinkaufBasisWaehrung());
				lineProtocolElement.addContentValue("Wert bei Verkauf (" + depotJournalLine.getBasisWaehrung() + ")", depotJournalLine.getBetragVerkaufBasisWaehrung());
				lineProtocolElement.addContentValue("Gewinn / Verlust (" + depotJournalLine.getBasisWaehrung() + ")", depotJournalLine.getBetragGewinnVerlustBasisWaehrung());
				lineProtocolElement.addContentValue("Kontostand (" + depotJournalLine.getDepotWaehrung() + ")", depotJournalLine.getBetragDepotWaehrung());
				lineProtocolElement.addContentValue("Steuerjahr Gewinn / Verlust (" + depotJournalLine.getBasisWaehrung() + ")", depotJournalLine.getGewinnVerlustBasisWaehrungSteuerjahr());
				lineProtocolElement.addContentValue("Gesamt Gewinn / Verlust (" + depotJournalLine.getBasisWaehrung() + ")", depotJournalLine.getGewinnVerlustBasisWaehrungGesamt());

				protocolElementList.add(lineProtocolElement);
			}

			csvProtocolExport.doExport(commonProtocolElement, protocolElementList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Depot (" + getDepotWaehrung().getLabel() + ")\n\n");

		sb.append("Aktueller Bestand: " + getBetragDepotWaehrung() + " " + getDepotWaehrung() + "\n");
		sb.append("Bisheriger Gewinn / Verlust: " + getGewinnVerlustBasisWaehrungGesamt() + " " + getBasisWaehrung() + "\n");

		int aktuellesJahr = new DateTime().getYear();
		int anfangsJahr = rtvJahrErsteTransaktion();

		for (int i = anfangsJahr; i <= aktuellesJahr; i++) {
			sb.append("Zu versteuernder Gewinn / Verlust (" + i + "): " + getZuVersteuerndenGewinnVerlust(i) + " " + getBasisWaehrung() + "\n");
		}

		sb.append("\n");

		for (DepotJournalLine depotJournalLine : getJournalEintraege()) {
			sb.append(depotJournalLine);
		}

		return sb.toString();
	}
}