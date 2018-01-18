/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto.io;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.be.rhi.crypto.Transaction;
import de.be.rhi.crypto.io.csv.BitcoinDeCsvTransactionInputReader;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 * @version 1.0
 * @since 16.01.2018, 19:59:52
 *
 */
public class CsvTransactionInputReaderTest {

	@Test
	public void testReadTransactionList() {

		File csvFileBtc = new File("U:\\git\\CCT-Tool\\CryptoCurrencyTaxCore\\ressources\\account_statement20110101-20180116.csv");
		File csvFileEth = new File("U:\\git\\CCT-Tool\\CryptoCurrencyTaxCore\\ressources\\account_statement20170601-20180116.csv");
		File csvFileBch = new File("U:\\git\\CCT-Tool\\CryptoCurrencyTaxCore\\ressources\\account_statement20170701-20180116.csv");

		Set<File> files = new HashSet<>();
		files.add(csvFileBtc);
		files.add(csvFileEth);
		files.add(csvFileBch);

		BitcoinDeCsvTransactionInputReader csvTransactionInputReaderTest = new BitcoinDeCsvTransactionInputReader(files);
		List<Transaction> transactionList = csvTransactionInputReaderTest.readTransactionList();

		for (Transaction transaction : transactionList) {
			System.out.println(transaction);
			System.out.println("");
		}
		Assert.assertNotNull(transactionList);

	}

}
