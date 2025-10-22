WITH ins_compra AS (
  INSERT INTO compra (total, fecha_compra, estado) VALUES
    (100, '2025-10-01 10:00:00', 'COMPLETADO'),
    (200, '2025-10-05 12:30:00', 'PENDIENTE'),
    (150, '2025-10-10 09:15:00', 'CANCELADO'),
    (300, '2025-10-12 14:45:00', 'COMPLETADO'),
    (50,  '2025-10-13 08:00:00', 'COMPLETADO'),
    (75,  '2025-10-14 09:30:00', 'PENDIENTE'),
    (125, '2025-10-15 11:00:00', 'COMPLETADO'),
    (220, '2025-10-16 16:20:00', 'PENDIENTE'),
    (410, '2025-10-17 18:45:00', 'COMPLETADO'),
    (90,  '2025-10-18 07:10:00', 'CANCELADO')
  RETURNING idcompra
)
SELECT idcompra INTO TEMP TABLE tmp_compras FROM ins_compra;

INSERT INTO empennio (precio, estado, fecha_inicio, fecha_final, interes) VALUES
  (10.50, 'ACTIVO',  '2025-01-01 08:00:00', NULL,               1.25),
  (20.00, 'INACTIVO','2025-02-01 09:00:00', '2025-03-01 09:00:00', 2.00),
  (15.75, 'ACTIVO',  '2025-05-10 10:00:00', NULL,               1.75),
  (50.00, 'VENDIDO', '2025-06-15 11:00:00', '2025-07-15 11:00:00', 3.50),
  (8.99,  'ACTIVO',  '2025-07-01 12:00:00', NULL,               0.99),
  (100.00,'ACTIVO',  '2025-08-20 09:30:00', NULL,               4.00),
  (35.50, 'INACTIVO','2025-03-05 10:30:00', '2025-04-01 10:30:00', 1.50),
  (60.00, 'ACTIVO',  '2025-09-01 14:00:00', NULL,               2.50),
  (12.30, 'ACTIVO',  '2025-09-20 08:15:00', NULL,               1.10),
  (75.25, 'VENDIDO', '2025-04-18 16:45:00', '2025-05-18 16:45:00', 2.75);

INSERT INTO facturacompra (medio_pago, total, compra_idcompra)
SELECT
  CASE WHEN (ROW_NUMBER() OVER (ORDER BY idcompra) % 3) = 0 THEN 'TRANSFERENCIA'
       WHEN (ROW_NUMBER() OVER (ORDER BY idcompra) % 2) = 0 THEN 'TARJETA'
       ELSE 'EFECTIVO' END AS medio_pago,
  (50 + (ROW_NUMBER() OVER (ORDER BY idcompra) * 10)) AS total,
  idcompra
FROM tmp_compras
ORDER BY idcompra
LIMIT 10;

SELECT '--- COMPRAS (10 insertadas) ---' AS info;
SELECT * FROM compra ORDER BY idcompra LIMIT 20;

SELECT '--- EMPENNIOS (10 insertados) ---' AS info;
SELECT * FROM empennio ORDER BY idempennio LIMIT 20;

SELECT '--- FACTURAS COMPRA (10 insertadas) ---' AS info;
SELECT fc.idFacturaCompra, fc.medio_pago, fc.total, fc.compra_idcompra, c.total AS compra_total
FROM facturacompra fc
LEFT JOIN compra c ON c.idcompra = fc.compra_idcompra
ORDER BY fc.idFacturaCompra LIMIT 20;