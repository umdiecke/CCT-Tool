/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto.io;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.be.rhi.crypto.Currency;
import de.be.rhi.crypto.Transaction;
import de.be.rhi.crypto.TransactionType;
import de.be.rhi.crypto.util.MathUtil;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 * @version 1.0
 * @since 16.01.2018, 19:19:43
 *
 */
public class CsvTransactionInputReader implements TransactionInputReader {

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private final Logger LOGGER = LogManager.getLogger();

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private Set<File> csvFiles;

	public CsvTransactionInputReader(final File csvFile) {
		csvFiles = new HashSet<>();
		csvFiles.add(csvFile);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param csvFiles
	 */
	public CsvTransactionInputReader(final Set<File> csvFiles) {
		this.csvFiles = csvFiles;
	}

	/* (non-Javadoc)
	 * @see de.be.rhi.crypto.io.TransactionInputReader#readTransactionList()
	 */
	@Override
	public List<Transaction> readTransactionList() {
		return readInCsvFiles();
	}

	private List<Transaction> readInCsvFiles() {
		List<Transaction> transactionList = new ArrayList<>();
		for (File file : csvFiles) {
			// read file into stream, try-with-resources
			try (Stream<String> stream = Files.lines(file.toPath())) {

				boolean isHeadline = true;
				Iterator<String> lineIterator = stream.iterator();
				while (lineIterator.hasNext()) {
					String line = lineIterator.next();

					line = line.replace("\"", "");
					String[] splitedLine = line.split(";");

					if (isHeadline) {
						// FIXME RHildebrand Nutzer die Headlines um die Felder der Lines zu mappen, ggf. mit einer Mapping-Datei
						// for (String string : splitedLine) {
						// System.out.println(string);
						// }
						isHeadline = false;
					} else {
						Transaction transaction = new Transaction();
						for (int i = 0; i < splitedLine.length; i++) {

							String lineRow = splitedLine[i].trim();

							if (StringUtils.isNotBlank(lineRow)) {
								switch (i) {
								case 0:
									transaction.setTransactionDate(sdf.parse(lineRow));
									break;
								case 1:
									transaction.setTransactionType(TransactionType.valueOf("Registrierung".equals(lineRow) ? "Initialisierung".toUpperCase() : lineRow.toUpperCase()));
									break;
								case 2:
									setCurrencies(transaction, lineRow);
									break;
								case 3:
									transaction.setReferenz(lineRow);
									break;
								case 4:
									transaction.setKursBasiswaehrung(new BigDecimal(lineRow));
									break;
								case 5:
									// .getBetragDepotWaehrungVorGebuehr()(new BigDecimal(lineRow));
									break;
								case 6:
									transaction.setBetragTransaktionsWaehrung(new BigDecimal(lineRow));
									break;
								case 8:
									setProzentsatzGebuehr(transaction, lineRow);
									break;

								default:
									break;
								}
							}
						}
						transactionList.add(transaction);
					}
				}
			} catch (IOException e) {
				LOGGER.error("Fehler beim Einlesen der CsvDatei '" + file.getAbsolutePath() + "'!", e);
			} catch (ParseException e) {
				LOGGER.error("Fehler beim verarbeiten eines Datums-Strings. Erwartet '" + sdf.toPattern() + "'", e);
			}
		}
		return transactionList;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transaction
	 * @param lineRow
	 */
	private void setProzentsatzGebuehr(Transaction transaction, String lineRow) {
		BigDecimal betragTransaktionswaehrungNachGebuehr = new BigDecimal(lineRow);
		BigDecimal betragTransaktionsWaehrung = transaction.getBetragTransaktionsWaehrung();
		BigDecimal prozentsatzGebuehr = new BigDecimal("100")
				.subtract(MathUtil.calculateRuleOfThree(betragTransaktionswaehrungNachGebuehr, new BigDecimal("100"), betragTransaktionsWaehrung));
		transaction.setProzentsatzTransactionsGebuehr(prozentsatzGebuehr);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transaction
	 * @param lineRow
	 */
	private void setCurrencies(Transaction transaction, String lineRow) {
		if (lineRow.equalsIgnoreCase("BTC") || lineRow.equalsIgnoreCase("ETH") || lineRow.equalsIgnoreCase("BCH")) {
			transaction.setDepotWaehrung(Currency.valueOf(lineRow.toUpperCase()));
			transaction.setTransaktionsWaehrung(Currency.EUR);
		} else if (lineRow.equalsIgnoreCase("BTC / EUR")) {
			transaction.setDepotWaehrung(Currency.BTC);
			transaction.setTransaktionsWaehrung(Currency.EUR);
		} else if (lineRow.equalsIgnoreCase("ETH / EUR")) {
			transaction.setDepotWaehrung(Currency.ETH);
			transaction.setTransaktionsWaehrung(Currency.EUR);
		} else if (lineRow.equalsIgnoreCase("BCH / EUR")) {
			transaction.setDepotWaehrung(Currency.BCH);
			transaction.setTransaktionsWaehrung(Currency.EUR);
		}
	}
}