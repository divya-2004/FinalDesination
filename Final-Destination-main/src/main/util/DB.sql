create DATABASE appdb;
use appdb;
create table customer(
    customerId int primary key auto_increment,
    name varchar(100) not null,
    email varchar(100) unique not null,
    phoneNumber varchar(15) not null,
    password varchar(255) not null
);
create table restaurant(
    restaurantId int primary key auto_increment,
    name varchar(100) not null,
    address varchar(255) not null ,
    cuisineType varchar(55),
    contactNumber varchar(15)
);
create table menuItem(
    itemId int primary key auto_increment,
    restaurantId int,
    name varchar(100) not null,
    price double not null,
    description varchar(255),
    availableQuantity int not null,
    Foreign Key (restaurantId) REFERENCES restaurant(restaurantId)
);
create table `order`(
    orderId INT primary key auto_increment,
    customerId int,
    restaurantId int,
    orderStatus varchar(35),
    totalPrice double not null,
    deliveryAddress VARCHAR(45),
    Foreign Key (customerId) REFERENCES customer(customerId),
    Foreign Key (restaurantId) REFERENCES restaurant(restaurantId)
);
create table orderItem(
    orderId INT,
    itemId INT,
    quantity INT not null,
    PRIMARY KEY (orderId, itemId),
    FOREIGN KEY (orderId) REFERENCES `order`(orderId),
    FOREIGN KEY (itemId) REFERENCES menuItem(itemId)
);
create table payment(
    paymentId int primary key,
    orderId int,
    paymentDate datetime not null,
    paymentStatus varchar(35),
    amountPaid DOUBLE not null,
    Foreign Key (orderId) REFERENCES `order`(orderId)
);
