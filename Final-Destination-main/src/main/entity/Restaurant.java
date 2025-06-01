public class Restaurant {
    private int restaurantId;
    private String name;
    private String address;
    private String cuisineType;
    private String contactNumber;
    
        //CONSTRUCTOR
        public Restaurant() {}
    
        public Restaurant(int restaurantId, String name, String address, String cuisineType, String contactNumber){
            this.restaurantId = restaurantId;
            this.name = name;
            this.address = address;
            this.cuisineType = cuisineType;
            this.contactNumber = contactNumber;
        }
    
    //GETTERS AND SEETTERS FOR RESTAURANT.JAVA
    public int getRestaurantId() {return restaurantId;}
    public void getRestaurantId(int restaurantId) {this.restaurantId = restaurantId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    public String getCuisineType() {return cuisineType;}
    public void setCuisineType(String cuisineType) {this.cuisineType = cuisineType;}

    public String getContactNumber() {return contactNumber;}
    public void setContactNumber(String contactNumber) {this.contactNumber = contactNumber;}
}
