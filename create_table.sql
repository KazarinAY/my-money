  CREATE TABLE operations (
      op_id INT NOT NULL AUTO_INCREMENT,
      op_sum NUMERIC(10,2) NOT NULL,      
      op_date DATE NOT NULL,
      op_description VARCHAR(255),
      op_tags VARCHAR(255),
      PRIMARY KEY (op_id)
);
