/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto.io.csv;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.be.rhi.crypto.Transaction;
import de.be.rhi.crypto.io.TransactionInputReader;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 * @version 1.0
 * @since 16.01.2018, 19:19:43
 *
 */
abstract class CsvTransactionInputReader implements TransactionInputReader {

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private final Logger LOGGER = LogManager.getLogger();

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
				String[] headLine = null;
				Iterator<String> lineIterator = stream.iterator();
				while (lineIterator.hasNext()) {
					String line = lineIterator.next();

					line = line.replace("\"", "");
					String[] splitedLine = line.split(";");

					if (isHeadline) {
						headLine = splitedLine;
						isHeadline = false;
					} else {
						Transaction transaction = new Transaction();
						for (int i = 0; i < splitedLine.length; i++) {

							String rowName = headLine != null ? headLine[i].trim() : "";
							String lineRow = splitedLine[i].trim();

							if (StringUtils.isNotBlank(lineRow)) {
								processLineRow(i, rowName, lineRow, transaction);
							}
						}


						transactionList.add(transaction);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Fehler beim Einlesen der CsvDatei '" + file.getAbsolutePath() + "'!", e);
			}
		}
		return transactionList;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param rowNumber
	 * @param rowName
	 * @param rowContent
	 * @param transaction
	 * @throws Exception
	 */
	protected abstract void processLineRow(int rowNumber, String rowName, String rowContent, Transaction transaction) throws Exception;

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transaction
	 */
	protected abstract void postProcessingTransaction(final Transaction transaction);
}