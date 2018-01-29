/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto.io.csv;

import java.io.File;
import java.math.BigDecimal;
import java.util.Set;

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
 * @since 18.01.2018, 05:40:55
 *
 */
public class BitcoinDeCsvTransactionInputReader extends CsvTransactionInputReader {

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param csvFile
	 */
	public BitcoinDeCsvTransactionInputReader(final File csvFile) {
		super(csvFile);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param csvFiles
	 */
	public BitcoinDeCsvTransactionInputReader(final Set<File> csvFiles) {
		super(csvFiles);
	}


	@Override
	protected void processLineRow(final int rowNumber, final String rowName, final String rowContent, final Transaction transaction) throws Exception {
		switch (rowNumber) {
		case 0:
			transaction.setTransactionDate(this.formatter.parseDateTime(rowContent));
			break;
		case 1:
			transaction.setTransactionType(TransactionType.valueOf("Registrierung".equals(rowContent) ? "Initialisierung".toUpperCase() : rowContent.toUpperCase()));
			break;
		case 2:
			setCurrencies(transaction, rowContent);
			break;
		case 3:
			transaction.setReferenz(rowContent);
			break;
		case 4:
			transaction.setKursBasiswaehrung(new BigDecimal(rowContent));
			break;
		case 5:
			transaction.setBetragDepotWaehrungVorGebuehrMarktplatz(new BigDecimal(rowContent));
			break;
		case 6:
			transaction.setBetragTransaktionsWaehrung(new BigDecimal(rowContent));
			break;
		case 7:
			transaction.setBetragDepotWaehrungNachGebuehrMarktplatz(new BigDecimal(rowContent));
			break;
		case 8:
			transaction.setBetragTransaktionsWaehrungNachGebuehrMarktplatz(new BigDecimal(rowContent));
			break;
		case 9:
			// do nothing - Zu- / Abgang
			break;
		case 10:
			transaction.setKontostandDepotWaehrungMarktplatz(new BigDecimal(rowContent));
			break;

		default:
			break;
		}
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transaction
	 * @param rowContent
	 */
	private void setCurrencies(final Transaction transaction, final String rowContent) {
		if (rowContent.equalsIgnoreCase("BTC") || rowContent.equalsIgnoreCase("ETH") || rowContent.equalsIgnoreCase("BCH")) {
			transaction.setDepotWaehrung(Currency.valueOf(rowContent.toUpperCase()));
			transaction.setTransaktionsWaehrung(Currency.EUR);
		} else if (rowContent.equalsIgnoreCase("BTC / EUR")) {
			transaction.setDepotWaehrung(Currency.BTC);
			transaction.setTransaktionsWaehrung(Currency.EUR);
		} else if (rowContent.equalsIgnoreCase("ETH / EUR")) {
			transaction.setDepotWaehrung(Currency.ETH);
			transaction.setTransaktionsWaehrung(Currency.EUR);
		} else if (rowContent.equalsIgnoreCase("BCH / EUR")) {
			transaction.setDepotWaehrung(Currency.BCH);
			transaction.setTransaktionsWaehrung(Currency.EUR);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.be.rhi.crypto.io.csv.CsvTransactionInputReader#postProcessingTransaction(de.be.rhi.crypto.Transaction)
	 */
	@Override
	protected void postProcessingTransaction(final Transaction transaction) {
		transaction.setMarktplatz("bitcoin.de");
		transaction.setEigenanteilTransaktionsgebuehr(new BigDecimal("50"));
		transaction.calcMissingValues();
	}
}