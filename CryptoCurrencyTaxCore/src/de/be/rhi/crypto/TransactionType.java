package de.be.rhi.crypto;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author René Hildebrand
 * @version 1.0
 * @since 13.01.2018, 15:23:39
 *
 */
public enum TransactionType {

	/**
	 * Initialisierung - Beschreibt die Kontoerstellung bzw. einen durch einen
	 * Hardfork entstandenen Bestand
	 */
	INITIALISIERUNG(
			"Initialisierung",
			"Beschreibt die Kontoerstellung bzw. einen durch einen Hardfork entstandenen Bestand"),
	/**
	 * Kauf - Kauf von Coins
	 */
	KAUF("Kauf", "Kauf von Depot-Waehrung"),
	/**
	 * Verkauf - Verkauf von Coins
	 */
	VERKAUF("Verkauf", "Verkauf von Depot-Waehrung"),
	/**
	 * Transfer - Transfer von einem Wallet / Exchanger auf ein anderes / anderen Exchanger
	 */
	TRANSFER("Transfer", "Transfer von einem Wallet auf ein anderes");

	/**
	 * Label
	 */
	private final String label;
	/**
	 * Beschreibung
	 */
	private final String description;

	/**
	 * TODO RHildebrand JavaDoc
	 *
	 * @param label
	 * @param description
	 */
	private TransactionType(final String label, final String description) {
		this.label = label;
		this.description = description;

	}


	/**
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
}