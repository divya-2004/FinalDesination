public class OrderItem {
    private int orderId;
    private int itemId;
    private int quantity;

    // CONSTRUCTOR
    public OrderItem() {
    }

    public OrderItem(int orderId, int itemId, int quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    // GETTERS AND SEETTERS FOR ORDERITEM.JAVA
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
