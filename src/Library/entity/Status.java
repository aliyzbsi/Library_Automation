package Library.entity;

public enum Status {
    AVAILABLE("Mevcut"),
    BORROWED("Kiralanmış"),
    PURCHASED("Satın Alınmış");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
