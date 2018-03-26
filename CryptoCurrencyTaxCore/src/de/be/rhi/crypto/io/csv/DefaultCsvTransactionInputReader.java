/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto.io.csv;

import java.io.File;
import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.be.rhi.crypto.Currency;
import de.be.rhi.crypto.Transaction;
import de.be.rhi.crypto.TransactionType;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 * @version 1.0
 * @since 26.03.2018, 22:07:06
 *
 */
public class DefaultCsvTransactionInputReader extends CsvTransactionInputReader {

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private DateTimeFormatter formatter = DateTimeFormat.forPattern(DefaultCsvTransactionInputReader.PATTERN_DATETIME);

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param csvFile
	 */
	public DefaultCsvTransactionInputReader(final File csvFile) {
		super(csvFile);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param csvFiles
	 */
	public DefaultCsvTransactionInputReader(final Set<File> csvFiles) {
		super(csvFiles);
	}


	@Override
	protected void processLineRow(final int rowNumber, final String rowName, final String rowContent, final Transaction transaction) throws Exception {
		switch (rowName) {
		case "Referenz":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setReferenz(rowContent);
			}
			break;
		case "Wallet":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setWalletDescription(rowContent);
			}
			break;
		case "Datum":
			if (StringUtils.isNotBlank(rowContent)) {
				try {
					transaction.setTransactionDate(this.formatter.parseDateTime(rowContent));
				} catch (IllegalArgumentException e) {
					System.err.println("Feld 'Datum' muss im Datetime-Format '" + DefaultCsvTransactionInputReader.PATTERN_DATETIME + "' formatiert sein!"); // FIXME RHI Implement Logging
					throw e;
				}
			}
			break;
		case "Typ":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setTransactionType(TransactionType.valueOf("Registrierung".equals(rowContent) ? "Initialisierung".toUpperCase() : rowContent.toUpperCase()));
			}
			break;
		case "Basiswaehrung B-W":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setBasisWaehrung(Currency.valueOf(rowContent.toUpperCase()));
			}
			break;
		case "Transaktionswaehrung T-W":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setTransaktionsWaehrung(Currency.valueOf(rowContent.toUpperCase()));
			}
			break;
		case "Depotwaehrung D-W":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setDepotWaehrung(Currency.valueOf(rowContent.toUpperCase()));
			}
			break;
		case "Kurs B-D-W":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setKursBasiswaehrung(new BigDecimal(rowContent));
			}
			break;
		case "Kurs T-D-W":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setKursTransaktionsWaehrung(new BigDecimal(rowContent));
			}
			break;
		case "Value B-W v.G.":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setBetragBasisWaehrungVorGebuehrMarktplatz(new BigDecimal(rowContent));
			}
			break;
		case "Value T-W v.G.":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setBetragTransaktionsWaehrung(new BigDecimal(rowContent));
			}
			break;
		case "Value D-W v.G.":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setBetragDepotWaehrungVorGebuehrMarktplatz(new BigDecimal(rowContent));
			}
			break;
		case "Value B-W n.G.":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setBetragBasisWaehrungNachGebuehrMarktplatz(new BigDecimal(rowContent));
			}
			break;
		case "Value T-W n.G.":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setBetragTransaktionsWaehrungNachGebuehrMarktplatz(new BigDecimal(rowContent));
			}
			break;
		case "Value D-W n.G.":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setBetragDepotWaehrungNachGebuehrMarktplatz(new BigDecimal(rowContent));
			}
			break;
		case "Eigenanteil G":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setEigenanteilTransaktionsgebuehr(new BigDecimal(rowContent));
			}
			break;
		case "Kontostand D-W":
			if (StringUtils.isNotBlank(rowContent)) {
				transaction.setKontostandDepotWaehrungMarktplatz(new BigDecimal(rowContent));
			}
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.be.rhi.crypto.io.csv.CsvTransactionInputReader#postProcessingTransaction(de.be.rhi.crypto.Transaction)
	 */
	@Override
	protected void postProcessingTransaction(final Transaction transaction) {
		transaction.calcMissingValues();
	}
}