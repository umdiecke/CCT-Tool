/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto.io;

import java.util.List;

import de.be.rhi.crypto.Transaction;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 * @version 1.0
 * @since 14.01.2018, 17:52:00
 *
 */
public interface TransactionInputReader {


	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @return
	 */
	public List<Transaction> readTransactionList();

}
