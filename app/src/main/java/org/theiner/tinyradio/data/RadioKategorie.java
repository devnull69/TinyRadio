package org.theiner.tinyradio.data;

/**
 * Created by TTheiner on 08.03.2017.
 */

public enum RadioKategorie {
    Metal("Metal"), Achtziger("80er"), Neunziger("90er"), Sender("Sender");

    private final String description;

    private RadioKategorie(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
