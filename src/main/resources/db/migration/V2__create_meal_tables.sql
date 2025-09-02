CREATE TABLE IF NOT EXISTS meals (
  id          BINARY(16)      NOT NULL,
  name        VARCHAR(120)    NOT NULL,
  created_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_meals_name (name)  -- opcional (remova se quiser permitir nomes repetidos)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS meal_items (
  id             BINARY(16)      NOT NULL,
  meal_id        BINARY(16)      NOT NULL,
  food_id        BINARY(16)      NOT NULL,
  unit_type_used ENUM('GRAM','ML','UNIT') NOT NULL,
  quantity_used  DECIMAL(10,2)   NOT NULL,

  kcal_resolved    DECIMAL(10,2) NOT NULL,
  protein_resolved DECIMAL(10,2) NOT NULL,
  carbs_resolved   DECIMAL(10,2) NOT NULL,
  fat_resolved     DECIMAL(10,2) NOT NULL,

  created_at     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  KEY idx_meal_items_meal_id (meal_id),
  KEY idx_meal_items_food_id (food_id),
  CONSTRAINT fk_meal_items_meal FOREIGN KEY (meal_id) REFERENCES meals(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_meal_items_food FOREIGN KEY (food_id) REFERENCES foods(id)
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
