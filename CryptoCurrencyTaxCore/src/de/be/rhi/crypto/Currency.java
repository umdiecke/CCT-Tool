/**
 * 
 */
package de.be.rhi.crypto;

/**
 * @author René
 * FIXME RHI ENUM ersetzen durch eine Property-Datei o.ä.
 *
 */
public enum Currency {

	/**
	 * Euro
	 */
	EUR("Euro", "Euro"),
	/**
	 * Bitcoin
	 */
	BTC("Bitcoin", "Bitcoin"),
	/**
	 * Bitcoin Cash
	 */
	BCH("Bitcoin Cash", "Bitcoin Cash"),
	/**
	 * Ether
	 */
	ETH("Ether", "Ether");

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
	private Currency(final String label, final String description) {
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