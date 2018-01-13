package de.be.rhi.crypto;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author René Hildebrand
 * @version 1.0
 * @since 13.01.2018, 15:20:15
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
	 * TODO RHildebrand JavaDoc
	 *
	 * @param label
	 * @param description
	 */
	private Currency(final String label, final String description) {
		this.label = label;
		this.description = description;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}