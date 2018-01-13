package de.be.rhi.crypto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
		transactionList = new ArrayList<>();
		depotTransactionMap = new EnumMap<>(Currency.class);
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param depotCurrency
	 * @return
	 */
	public List<Transaction> rtvDepotTransactionList(final Currency depotCurrency) {
		List<Transaction> depotTransactionList;
		if (depotTransactionMap.containsKey(depotCurrency)) {

			depotTransactionList = depotTransactionMap.get(depotCurrency);
			if(depotTransactionList==null) {
				depotTransactionList = new ArrayList<>();
				depotTransactionMap.put(depotCurrency, depotTransactionList);
			}

		} else {
			depotTransactionList = new ArrayList<>();
			depotTransactionMap.put(depotCurrency, depotTransactionList);
		}
		return depotTransactionList;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	private Map<Currency, List<Transaction>> createDepotTransactionMap() {
		Collections.sort(transactionList);
		for (Transaction transaction : transactionList) {

			List<Transaction> depotTransactionList = rtvDepotTransactionList(transaction.getDepotWaehrung());

			if(!depotTransactionList.contains(transaction)){
				depotTransactionList.add(transaction);
				Collections.sort(depotTransactionList);
			}
		}

		return depotTransactionMap;
	}

	/**
	 * @return the transactionList
	 */
	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param transaction
	 */
	public void addTransaction(final Transaction transaction) {
		if (transaction != null) {
			transactionList.add(transaction);
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