package de.be.rhi.crypto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.be.rhi.crypto.io.TransactionInputReader;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author Ren� Hildebrand
 * @version 1.0
 * @since 13.01.2018, 15:22:55
 *
 */
public class TransactionHandler {

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private final Logger LOGGER = LogManager.getLogger();

	/**
	 * TODO RHildebrand JavaDoc
	 */
	private List<Transaction> transactionList;
	/**
	 * TODO RHildebrand JavaDoc
	 */
	private Map<Currency, List<Transaction>> depotTransactionMap;

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 */
	public TransactionHandler() {
		this.transactionList = new ArrayList<>();
		this.depotTransactionMap = new EnumMap<>(Currency.class);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param inputReader
	 */
	public void loadNewTransactions(final Set<TransactionInputReader> inputReader) {
		for (TransactionInputReader transactionInputReader : inputReader) {
			addTransactionList(transactionInputReader.readTransactionList());
		}
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param depotCurrency
	 * @return
	 */
	public List<Transaction> rtvDepotTransactionList(final Currency depotCurrency) {
		List<Transaction> depotTransactionList;
		if (this.depotTransactionMap.containsKey(depotCurrency)) {

			depotTransactionList = this.depotTransactionMap.get(depotCurrency);
			if(depotTransactionList==null) {
				depotTransactionList = new ArrayList<>();
				this.depotTransactionMap.put(depotCurrency, depotTransactionList);
			}

		} else {
			depotTransactionList = new ArrayList<>();
			this.depotTransactionMap.put(depotCurrency, depotTransactionList);
		}
		return depotTransactionList;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public Set<Currency> rtvDepotCurrencies() {
		return this.depotTransactionMap.keySet();
	}


	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	private Map<Currency, List<Transaction>> createDepotTransactionMap() {
		Collections.sort(this.transactionList);
		for (Transaction transaction : this.transactionList) {

			List<Transaction> depotTransactionList = rtvDepotTransactionList(transaction.getDepotWaehrung());

			if(!depotTransactionList.contains(transaction)){
				depotTransactionList.add(transaction);
				Collections.sort(depotTransactionList);
			}
		}

		return this.depotTransactionMap;
	}

	/**
	 * @return the transactionList
	 */
	public List<Transaction> getTransactionList() {
		return this.transactionList;
	}


	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transaction
	 */
	public void addTransaction(final Transaction transaction) {
		if (transaction != null) {
			this.transactionList.add(transaction);
			createDepotTransactionMap();
		}
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transactionList
	 */
	public void addTransactionList(final List<Transaction> transactionList) {
		if (transactionList != null && !transactionList.isEmpty()) {
			this.transactionList.addAll(transactionList);
			createDepotTransactionMap();
		}
	}
}