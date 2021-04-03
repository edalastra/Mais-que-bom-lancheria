
CREATE TABLE IF NOT EXISTS users (
    id serial PRIMARY KEY,
    email varchar(100) UNIQUE NOT NULL,
    password varchar(255) not NULL,
    is_admin boolean NOT NULL,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) not NULL,
    telephone varchar(12) not NULL check(telephone ~ '^[0-9]{11,12}'),
    salary DECIMAL NOT NULL,
    address text NOT NULL,
    zipcode char(8) NOT NULL check(zipcode ~ '^[0-9]{8}$')
    );


CREATE TABLE if NOT EXISTS bartable(
   id serial PRIMARY KEY ,
   capacity integer not NULL
 );
 
 CREATE TABLE IF NOT EXISTS bartable_worker(
   user_id integer not NULL,
   bartable_id integer NOT NULL,
   FOREIGN KEY (user_id) REFERENCES users(id),
   FOREIGN KEY (bartable_id) REFERENCES bartable(id),
   PRIMARY KEY(user_id, bartable_id)
  );

CREATE TABLE if NOT EXISTS orders(
	id serial PRIMARY KEY,
  	bartable_id integer NOT NULL,
  	open_at timestamp not NULL DEFAULT current_timestamp,
    close_at timestamp,
    FOREIGN KEY (bartable_id) REFERENCES bartable(id)
);

 CREATE TABLE if not EXISTS nutritional(
   id serial PRIMARY KEY ,
   portion TEXT not NULL,
   proteins real not NULL,
   carb real not  NULL,
   fats real not null
  );

CREATE TABLE if NOT EXISTS item(
  id serial PRIMARY KEY,
  price DECIMAL not NULL,
  cost DECIMAL not NULL,
  category varchar(10) NOT NULL check(category in ('bebida', 'comida')),
  description VARCHAR(255) NOT NULL,
  maker varchar(100) not NULL,
  nutritional_id Integer,
  FOREIGN KEY (nutritional_id) REFERENCES nutritional(id)
  );

CREATE TABLE if NOT EXISTS order_item(
  order_id integer not NULL,
  item_id Integer Not NULL,
  quantity integer not NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id),
  FOREIGN KEY (item_id) REFERENCES item(id),
  PRIMARY KEY (order_id, item_id)
);

CREATE TABLE if NOT EXISTS order_item_history(
    order_id integer not NULL,
    item_id Integer Not NULL,
    quantity integer not NULL,
    consumption_time time NOT NULL,
    user_id integer NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (item_id) REFERENCES item(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    PRIMARY KEY (order_id, item_id)
);
