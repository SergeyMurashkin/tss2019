
DROP DATABASE IF EXISTS onlineshop;
CREATE DATABASE onlineshop DEFAULT CHARSET utf8;
USE onlineshop;

CREATE TABLE users (
id INT NOT NULL AUTO_INCREMENT,
firstName VARCHAR(250) NOT NULL,
lastName VARCHAR(250) NOT NULL,
patronymic VARCHAR(250) DEFAULT NULL,
userType VARCHAR(250) NOT NULL,
login VARCHAR(250) NOT NULL,
password VARCHAR(250) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY login (login)
);

CREATE TABLE admins (
id INT NOT NULL,
position VARCHAR(250) NOT NULL,
foreign key (id) references users(id) ON DELETE CASCADE,
PRIMARY KEY (id)
);

CREATE TABLE clients (
id INT NOT NULL,
email VARCHAR(250) NOT NULL,
address VARCHAR(250) NOT NULL,
phone VARCHAR(250) NOT NULL,
foreign key (id) references users(id) ON DELETE CASCADE,
PRIMARY KEY (id)
);

CREATE TABLE deposits (
id INT NOT NULL,
deposit INT DEFAULT 0,
version INT DEFAULT 0,
foreign key (id) references clients(id) ON DELETE CASCADE,
PRIMARY KEY (id)
);

CREATE TABLE sessions (
id INT NOT NULL,
cookie VARCHAR(250) DEFAULT NULL,
foreign key (id) references users(id) ON DELETE CASCADE,
PRIMARY KEY (id),
UNIQUE KEY cookie (cookie)
);

CREATE TABLE categories (
id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(250) NOT NULL,
parentId INT DEFAULT NULL,
foreign key (parentId) references categories(id) ON DELETE CASCADE,
PRIMARY KEY (id),
UNIQUE KEY name (name)
);


CREATE TABLE products (
id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(250) NOT NULL,
price INT NOT NULL,
count INT DEFAULT 0,
version INT DEFAULT 0,
isDeleted boolean DEFAULT false,
PRIMARY KEY (id)
);

CREATE TABLE products_categories (
productId INT NOT NULL,
categoryId INT NOT NULL,
foreign key (productId) references products(id) ON DELETE CASCADE,
foreign key (categoryId) references categories(id) ON DELETE CASCADE,
UNIQUE KEY productId_categoryId (productId, categoryId)
);

CREATE TABLE purchases (
id INT NOT NULL AUTO_INCREMENT,
clientId INT NOT NULL,
productId INT NOT NULL,
name VARCHAR(250) NOT NULL,
price INT NOT NULL,
count INT NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE basketProducts (
clientId INT NOT NULL,
productId INT NOT NULL,
count INT NOT NULL,
foreign key (clientId) references clients(id) ON DELETE CASCADE,
foreign key (productId) references products(id) ON DELETE CASCADE,
UNIQUE KEY clientId_productId (clientId, productId)
);
