-- MySQL 8.4 / InnoDB / utf8mb4
CREATE TABLE IF NOT EXISTS foods (
  id            BINARY(16)         NOT NULL,
  name          VARCHAR(120)       NOT NULL,
  unit_type     ENUM('GRAM','ML','UNIT') NOT NULL,
  base_amount   DECIMAL(10,2)      NOT NULL,
  kcal          DECIMAL(10,2)      NOT NULL,
  protein_g     DECIMAL(10,2)      NOT NULL,
  carbs_g       DECIMAL(10,2)      NOT NULL,
  fat_g         DECIMAL(10,2)      NOT NULL,
  created_at    TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_foods_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
