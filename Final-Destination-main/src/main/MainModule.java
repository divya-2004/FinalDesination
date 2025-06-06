package com.examly;
import com.examly.entity.*;
import com.examly.exception.*;
import com.examly.service.*;
import com.examly.util.DBConnectionUtil;
import java.util.Date;
import java.sql.*;
import java.util.*;


public class MainModule {
    private static final CustomerService customerService= new CustomerServiceImpl();
    private static final RestaurantService restaurantService = new RestaurantServiceImpl();
    private static final MenuService menuService= new MenuServiceImpl();
    private static final OrderService orderService=new OrderServiceImpl();
    private static final PaymentService paymentService = new PaymentServiceImpl();
    private static final Scanner scanner= new Scanner(System.in);
    
    public static void main(String[] args) {
        while(true){
            displayMenu();
            System.out.print("Choose an option: ");
            int choice=scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: resgisterCustomer();break;
                case 2: createRestaurant();break;
                case 3: createMenuItem();break;
                case 4: viewRestaurants();break;
                case 5: placeOrder();break;
                case 6: viewOrders();break;
                case 7: makePayment();break;
                case 8: System.out.println("Exiting...");System.exit(0);        
                default: System.out.println("Invalid option"); break;
            }
        }
    }            
    private static void displayMenu(){
        System.out.println("\n=== Welcome to the Online Food Delivery System ===");
        System.out.println("1. Register Customer");
        System.out.println("2. Create Restaurant");
        System.out.println("3. Create Menu Item");
        System.out.println("4. View Restaurants");
        System.out.println("5. Place Order");
        System.out.println("6. View Orders");
        System.out.println("7. Make Payment");
        System.out.println("8. Exit");
    }
    private static void resgisterCustomer(){
        System.out.print("Enter customer name: ");
        String name=scanner.nextLine();
        System.out.print("Enter customer email: ");
        String email=scanner.nextLine();
        System.out.print("Enter customer phone number: ");
        String phoneNumber=scanner.nextLine();
        System.out.print("Enter customer password: ");
        String password=scanner.nextLine();

        Customer customer =new Customer(0,name, email, phoneNumber, password);
        try{
            boolean success= customerService.createCustomer(customer);
            System.out.println("Customer registered successfully!");   
        }catch(EmailAlreadyRegisteredException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    private static void createRestaurant(){
        System.out.print("Enter restaurant name: ");
        String name = scanner.nextLine();
        System.out.print("Enter restaurant address: ");
        String address = scanner.nextLine();
        System.out.print("Enter cuisine type: ");
        String cuisineType= scanner.nextLine();
        System.out.print("Enter contact number: ");
        String contactNumber= scanner.nextLine();
        int restaurantId=(int)(Math.random()*1000)+1;

        Restaurant restaurant=new Restaurant(restaurantId, name, address, cuisineType, contactNumber);
        boolean success= restaurantService.createRestaurant(restaurant);
        System.out.println("Restaurant created successfully!");   
        
    }
    private static void createMenuItem(){
        System.out.print("Enter restaurant ID to add menu item: ");
        int restaurantId= scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter menu item name: ");
        String name=scanner.nextLine();
        System.out.print("Enter menu item price: ");
        double price= scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter menu item description: ");
        String description= scanner.nextLine();
        System.out.print("Enter available quantity: ");
        int availableQuantity =scanner.nextInt();
        int itemId=(int)(Math.random()*1000)+1;

        MenuItem menuItem=new MenuItem(itemId, restaurantId, name, price, description, availableQuantity);
        try{
            boolean success= menuService.createMenuItem(menuItem);
            System.out.println("Menu item created successfully!");   
        }catch(RestaurantNotFoundException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    private static void viewRestaurants(){
        List<Restaurant> restaurants=restaurantService.getAllRestaurants();
        if(restaurants.isEmpty()){
            System.out.println("No restaurants available.");
        }else{
            System.out.println("\n=== List of Restaurants ===");
            for(Restaurant r: restaurants){
                System.out.println("Restaurant ID: "+r.getRestaurantId());
                System.out.println("Name: "+r.getName());
                System.out.println("Address: "+r.getAddress());
                System.out.println("Cuisine Type: "+r.getCuisineType());
                System.out.println("Contact Number: "+r.getContactNumber());
                System.out.println();
            }
        }
    }
    private static void placeOrder(){
        System.out.print("Enter customer ID: ");
        int customerId=scanner.nextInt();
        if(!isCustomerExists(customerId)){System.out.println("Error: Customer Id is invalid");return;}
        System.out.print("Enter restaurant ID to place order: ");
        int restaurantId=scanner.nextInt();

        List<MenuItem> menuItems=menuService.getMenuItemsByRestaurant(restaurantId);
        if(menuItems.isEmpty()){
            System.out.println("No menu items available for this restaurant.");
            return;
        }
        System.out.println("\n=== Menu Items ===");
        for(MenuItem item:menuItems){
            System.out.println("Item ID: "+item.getItemId());
            System.out.println("Name: "+item.getName());
            System.out.println("Price: "+item.getPrice());
            System.out.println("Description: "+item.getDescription());
            System.out.println("Avalaible Quantity: "+item.getAvailableQuantity());
            System.out.println();
        }
        System.out.print("Enter item ID to order: ");
        int itemId=scanner.nextInt();
        System.out.print("Enter quantity: ");
        int quantity=scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter delivery address: ");
        String deliveryAddress=scanner.nextLine();
        MenuItem selectedItem=menuItems.stream().filter(item->item.getItemId()==itemId).findFirst().orElse(null);
        if(selectedItem==null || selectedItem.getAvailableQuantity()<quantity){
            System.out.println("Invalid item or insufficient quantity");
            return;
        }
        int orderId=(int)(Math.random()*1000)+1;
        double totalPrice=selectedItem.getPrice()*quantity;
        Order order=new Order(orderId, customerId, restaurantId, "Pending", totalPrice, deliveryAddress);
        List<OrderItem> orderedItems=new ArrayList<>();
        orderedItems.add(new OrderItem(orderId, itemId, quantity));
        if(orderService.createOrder(order, orderedItems)){
            System.out.println("Order place successfully!");
        }else{
            System.out.println("Failed to place order.");
        }
    }
    private static boolean isCustomerExists(int customerId){
        try(Connection conn=DBConnectionUtil.getConnection()){
            String sql="select count(*) from customer where customerId=?";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1)>0;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }return false;
    }
    private static void viewOrders(){
        System.out.print("Enter customer ID to view orders: ");
        int customerId=scanner.nextInt();
        List<com.examly.entity.Order> orders=orderService.getOrdersByCustomer(customerId);
        if(orders.isEmpty()){
            System.out.println("No orders found.");
        }else{
            System.out.println("\n=== List of Orders ===");
            for(com.examly.entity.Order o:orders){
                System.out.println("\nOrder ID: "+o.getOrderId());
                System.out.println("Restaurant ID "+o.getRestaurantId());
                System.out.println("Total: "+o.getTotalPrice());
                System.out.println("Status: "+o.getOrderStatus());
                System.out.println("Address: "+o.getDeliveryAddress());
            }
        }
    }
    private static void makePayment(){
        System.out.print("Enter order ID to make payment: ");
        int orderId= scanner.nextInt();
        System.out.print("Enter amount to pay: ");
        double amountPaid= scanner.nextDouble();
        Order order=orderService.getOrderById(orderId);
        if(order==null){
            System.out.println("Order not found");
            return;
        }
        if(order.getTotalPrice()!=amountPaid){
            System.out.println("Invalid amount. Expected: "+order.getTotalPrice());
            return;
        }
        int paymentId=(int)(Math.random()*1000)+1;
        Payment payment= new Payment(0, orderId,  new Date(), "Completed", amountPaid);
        boolean success=paymentService.processPayment(payment);
        if(success){
            System.out.println("Payment successful! Order is now confirmed.");
            order.setOrderStatus("Confirmed");
        }else{
            System.out.println("Payment Failed");
        }
    }

}














