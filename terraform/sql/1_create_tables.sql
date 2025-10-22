CREATE TABLE IF NOT EXISTS compra (
  idcompra SERIAL PRIMARY KEY,
  total INTEGER NOT NULL,
  fecha_compra TIMESTAMP NOT NULL,
  estado VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS empennio (
  idempennio SERIAL PRIMARY KEY,
  precio NUMERIC(12,2) NOT NULL,
  estado VARCHAR(50) NOT NULL,
  fecha_inicio TIMESTAMP NOT NULL,
  fecha_final TIMESTAMP NULL,
  interes NUMERIC(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS facturacompra (
  idFacturaCompra SERIAL PRIMARY KEY,
  medio_pago VARCHAR(45) NOT NULL,
  total INTEGER NOT NULL,
  compra_idcompra INTEGER NOT NULL,
  CONSTRAINT fk_factura_compra
    FOREIGN KEY (compra_idcompra)
    REFERENCES compra(idcompra)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);