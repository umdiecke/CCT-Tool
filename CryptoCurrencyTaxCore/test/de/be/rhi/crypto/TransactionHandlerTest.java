package de.be.rhi.crypto;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.be.rhi.crypto.io.CsvTransactionInputReader;
import de.be.rhi.crypto.io.TransactionInputReader;

public class TransactionHandlerTest {

	@Test
	public void testTransactionHandlerTransactionList() {
		TransactionHandler transactionHandler = new TransactionHandler();
		transactionHandler.addTransaction(createTransaction1());
		transactionHandler.addTransaction(createTransaction2());
		transactionHandler.addTransaction(createTransaction3());
		transactionHandler.addTransaction(createTransaction4());

		for (Transaction transaction : transactionHandler.getTransactionList()) {
			System.out.println(transaction + "\n");
		}
	}

	@Test
	public void testTransactionHandlerTransactionListWithCsvImport() {

		File csvFileBtc = new File("U:\\git\\CCT-Tool\\CryptoCurrencyTaxCore\\ressources\\account_statement20110101-20180116.csv");
		File csvFileEth = new File("U:\\git\\CCT-Tool\\CryptoCurrencyTaxCore\\ressources\\account_statement20170601-20180116.csv");
		File csvFileBch = new File("U:\\git\\CCT-Tool\\CryptoCurrencyTaxCore\\ressources\\account_statement20170701-20180116.csv");

		Set<File> files = new HashSet<>();
		files.add(csvFileBtc);
		files.add(csvFileEth);
		files.add(csvFileBch);

		CsvTransactionInputReader csvTransactionInputReader = new CsvTransactionInputReader(files);

		Set<TransactionInputReader> inputReader = new HashSet<>();
		inputReader.add(csvTransactionInputReader);

		TransactionHandler transactionHandler = new TransactionHandler();
		transactionHandler.loadNewTransactions(inputReader);

		for (Transaction transaction : transactionHandler.getTransactionList()) {
			System.out.println(transaction + "\n");
		}
	}

	private Transaction createTransaction1() {
		Transaction transaction = new Transaction();
		transaction.setTransactionDate(Calendar.getInstance().getTime());
		transaction.setTransactionType(TransactionType.KAUF);

		transaction.setBasisWaehrung(Currency.EUR);
		transaction.setTransaktionsWaehrung(Currency.ETH);
		transaction.setDepotWaehrung(Currency.BTC);

		transaction.setKursBasiswaehrung(new BigDecimal("10000"));
		transaction.setKursTransaktionsWaehrung(new BigDecimal("40"));

		transaction.setBetragTransaktionsWaehrung(new BigDecimal("100"));

		transaction.setProzentsatzTransactionsGebuehr(new BigDecimal("0.05"));

		transaction.setReferenz("rhi12356");
		transaction.setBeschreibung("Transaktion 1");
		return transaction;
	}

	private Transaction createTransaction2() {
		Transaction transaction = new Transaction();
		transaction.setTransactionDate(Calendar.getInstance().getTime());
		transaction.setTransactionType(TransactionType.KAUF);

		transaction.setBasisWaehrung(Currency.EUR);
		transaction.setDepotWaehrung(Currency.BTC);

		transaction.setKursBasiswaehrung(new BigDecimal("7895"));

		transaction.setBetragTransaktionsWaehrung(new BigDecimal("540"));

		transaction.setProzentsatzTransactionsGebuehr(new BigDecimal("0.2"));

		transaction.setReferenz("rhi12356");
		transaction.setBeschreibung("Transaktion 2");
		return transaction;
	}

	private Transaction createTransaction3() {
		Transaction transaction = new Transaction();
		transaction.setTransactionDate(Calendar.getInstance().getTime());
		transaction.setTransactionType(TransactionType.VERKAUF);

		transaction.setBasisWaehrung(Currency.EUR);
		transaction.setTransaktionsWaehrung(Currency.ETH);
		transaction.setDepotWaehrung(Currency.BTC);

		transaction.setKursBasiswaehrung(new BigDecimal("10150.87"));
		transaction.setKursTransaktionsWaehrung(new BigDecimal("41"));

		transaction.setBetragTransaktionsWaehrung(new BigDecimal("1500"));

		transaction.setProzentsatzTransactionsGebuehr(new BigDecimal("0.05"));

		transaction.setReferenz("rhi12356");
		transaction.setBeschreibung("Transaktion 3");
		return transaction;
	}


	private Transaction createTransaction4() {
		Transaction transaction = new Transaction();
		transaction.setTransactionDate(Calendar.getInstance().getTime());
		transaction.setTransactionType(TransactionType.KAUF);



		transaction.setBasisWaehrung(Currency.EUR);
		transaction.setTransaktionsWaehrung(Currency.BTC);
		transaction.setDepotWaehrung(Currency.ETH);


		transaction.setKursBasiswaehrung(new BigDecimal("10150.87"));
		transaction.setKursTransaktionsWaehrung(new BigDecimal("44.75"));

		transaction.setBetragTransaktionsWaehrung(new BigDecimal("30.25"));

		transaction.setProzentsatzTransactionsGebuehr(new BigDecimal("2.0"));

		transaction.setReferenz("rhi12356");
		transaction.setBeschreibung("Transaktion 4");
		return transaction;
	}
}
