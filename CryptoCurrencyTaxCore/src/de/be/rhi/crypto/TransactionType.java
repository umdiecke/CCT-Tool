/**
 * 
 */
package de.be.rhi.crypto;

/**
 * @author René
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
	KAUF("Kauf", "Kauf von Coins"),
	/**
	 * Verkauf - Verkauf von Coins
	 */
	VERKAUF("Verkauf", "Verkauf von Coins");

	/**
	 * Label
	 */
	private final String label;
	/**
	 * Beschreibung
	 */
	private final String description;

	/**
	 * @param label
	 * @param description
	 */
	private TransactionType(final String label, final String description) {
		this.label = label;
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}
}