package me.eranik.ftp;

/**
 * Describes available query types for client-server interaction.
 */
public enum QueryType {
    listQuery(1), getQuery(2);

    private final int id;

    QueryType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}