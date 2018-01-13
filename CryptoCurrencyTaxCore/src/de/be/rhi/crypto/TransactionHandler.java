/**
 * 
 */
package de.be.rhi.crypto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author René
 *
 */
public class TransactionHandler {

	private List<Transaction> transactionList;

	private Map<Currency, List<Transaction>> depotTransactionMap;

	public TransactionHandler() {
		transactionList = new ArrayList<>();
		depotTransactionMap = new HashMap<>();
	}

	

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
	
	private Map<Currency, List<Transaction>> createDepotTransactionMap() {
		for (Transaction transaction : transactionList) {
			
			List<Transaction> depotTransactionList = rtvDepotTransactionList(transaction.getDepotWaehrung());
			
			if(!depotTransactionList.contains(transaction)){
				depotTransactionList.add(transaction);
			}
			
			 Collections.sort(depotTransactionList);
		}

		return depotTransactionMap;
	}

	/**
	 * @return the transactionList
	 */
	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	public void addTransaction(final Transaction transaction) {
		if (transaction != null) {
			transactionList.add(transaction);
			createDepotTransactionMap();
		}
	}

	/**
	 * @param transactionList
	 *            the transactionList to set
	 */
	public void addTransactionList(final List<Transaction> transactionList) {
		if (transactionList != null && !transactionList.isEmpty()) {
			this.transactionList.addAll(transactionList);
			createDepotTransactionMap();
		}
	}
}