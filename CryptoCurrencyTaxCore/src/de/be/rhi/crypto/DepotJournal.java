/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import de.be.rhi.crypto.util.MathUtil;
import de.be.rhi.crypto.util.ObjectUtil;

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

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transactionHandler
	 * @param currency
	 */
	public DepotJournal(final TransactionHandler transactionHandler, final Currency depotWaehrung) {
		this.transactionHandler = transactionHandler;
		this.depotWaehrung = depotWaehrung;
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

						this.gewinnVerlustBasisWaehrungGesamt = this.gewinnVerlustBasisWaehrungGesamt.add(journalLine.getBetragGewinnVerlustBasisWaehrung());
						journalLine.setGewinnVerlustBasisWaehrungGesamt(this.gewinnVerlustBasisWaehrungGesamt);
						this.journalEintraege.add(journalLine);
					}
				} else {
					DepotJournalLine journalLine = new DepotJournalLine(transaction);
					journalLine.setKaufReferenz(transaction.getReferenz());
					journalLine.setKaufDatum(transaction.getTransactionDate());
					journalLine.setBetragEinkaufBasisWaehrung(BigDecimal.ZERO);
					journalLine.setBetragVerkaufBasisWaehrung(BigDecimal.ZERO);
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

		if (jahr != null) {
			for (DepotJournalLine depotJournalLine : this.journalEintraege) {

				if (depotJournalLine.getTransactionDate().getYear() == jahr.intValue() && depotJournalLine.isInEinJahresRegel()) {
					gewinn = gewinn.add(depotJournalLine.getBetragGewinnVerlustBasisWaehrung());
				}
			}
		}

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Depot (" + getDepotWaehrung().getLabel() + ")\n\n");

		sb.append("Aktueller Bestand: " + getBetragDepotWaehrung() + " " + getDepotWaehrung() + "\n");
		sb.append("Bisheriger Gewinn / Verlust: " + getGewinnVerlustBasisWaehrungGesamt() + " " + getBasisWaehrung() + "\n");

		int aktuellesJahr = new DateTime().getYear();
		List<Transaction> transaktionen = getTransactionHandler().rtvDepotTransactionList(getDepotWaehrung());
		int anfangsJahr = !transaktionen.isEmpty() ? transaktionen.get(0).getTransactionDate().getYear() : aktuellesJahr;

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