CREATE TABLE offer (
  id              IDENTITY       auto_increment,
  description     VARCHAR(255) NOT NULL,
  expiration_date TIMESTAMP    NOT NULL,
  price           DOUBLE       NOT NULL CHECK (price >= 0),
  status          VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
)
