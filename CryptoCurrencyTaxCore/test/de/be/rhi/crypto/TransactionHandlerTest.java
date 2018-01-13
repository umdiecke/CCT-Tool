package de.be.rhi.crypto;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;

public class TransactionHandlerTest {

	@Test
	public void testTransactionHandler() {
		TransactionHandler transactionHandler = new TransactionHandler();
		transactionHandler.addTransaction(createTransaction1());
		transactionHandler.addTransaction(createTransaction2());
		transactionHandler.addTransaction(createTransaction3());
		transactionHandler.addTransaction(createTransaction4());
		
		
		for (Transaction transaction : transactionHandler.getTransactionList()) {
			System.out.println(transaction.toString());
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
