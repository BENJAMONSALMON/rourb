drop database if exists rourb_in5bv;
create database rourb_in5bv;
use rourb_in5bv;

create table Users(
    id_user int auto_increment primary key,
    user_code int,
    username varchar(45),
    encrypted_password varchar(100),
    email varchar(60),
    rol enum('ADMIN','SELLER','CLIENT'),
    state boolean
);


create table Clients(
    id_client int auto_increment primary key,
    client_dpi int,
    client_name varchar(50),
    client_last_name varchar(50),
    address varchar(100),
    state enum('ACTIVE','INACTIVE')
);

create table Products(
    id_product int auto_increment primary key,
    product_code int,
    product_name varchar(60),
    price decimal(10,2),
    stock int,
    image_url varchar(500),
    state_product enum('CONTINUED','DISCONTINUED')
);

create table Sales(
    id_sale int auto_increment primary key,
    sale_code bigint,
    sale_date date,
    total decimal(10,2),
    state_sale enum('ACTIVE','IN_PROCESS'),
    clients_client_id int,
    users_user_id int,
    foreign key (clients_client_id) references Clients(id_client),
    foreign key (users_user_id) references Users(id_user)
);

create table SaleDetails(
    id_sale_detail int auto_increment primary key,
    detail_sale_code int,
    amount int,
    unitary_price decimal(10,2),
    subtotal decimal(10,2),
    products_product_id int,
    sales_sale_id int,
    foreign key (products_product_id) references Products(id_product),
    foreign key (sales_sale_id) references Sales(id_sale)
);

