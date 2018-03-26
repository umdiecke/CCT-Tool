package de.be.rhi.crypto;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.junit.Test;

public class TransactionTest {

	@Test
	public void testTransactionToString() {
		Transaction transaction = createTransaction1();

		System.out.println(transaction);
	}

	private Transaction createTransaction1() {
		Transaction transaction = new Transaction();
		transaction.setTransactionDate(new DateTime());
		transaction.setTransactionType(TransactionType.KAUF);

		transaction.setBasisWaehrung(Currency.EUR);
		transaction.setTransaktionsWaehrung(Currency.ETH);
		transaction.setDepotWaehrung(Currency.BTC);

		transaction.setKursBasiswaehrung(new BigDecimal("10000"));
		transaction.setKursTransaktionsWaehrung(new BigDecimal("40"));

		transaction.setBetragTransaktionsWaehrung(new BigDecimal("100"));


		transaction.setReferenz("rhi12356");
		transaction.setWalletDescription("bitcoin.de");
		return transaction;
	}

}
