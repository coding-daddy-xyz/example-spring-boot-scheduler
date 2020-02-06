package xyz.codingdaddy.domain;

/**
 * Time-based aggregation types
 *
 * @author sboychen
 */
public enum Aggregation {
    ONE_MINUTE(1),
    FIVE_MINUTES(2),
    FIFTEEN_MINUTES(3);

    private final int identifier;
    private final int index;

    Aggregation(int identifier) {
        this.identifier = identifier;
        this.index = identifier - 1;
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getIndex() {
        return index;
    }
}
