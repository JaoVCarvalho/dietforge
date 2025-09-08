-- Cria tabela de dietas
CREATE TABLE IF NOT EXISTS diets (
  id          BINARY(16)      NOT NULL,
  name        VARCHAR(120)    NOT NULL,
  created_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_diets_name (name) -- opcional; remova se quiser nomes repetidos
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Adiciona coluna diet_id em meals e ajusta unicidade de nome
ALTER TABLE meals
  ADD COLUMN diet_id BINARY(16) NULL AFTER name;

-- Caso tenha criado antes a restrição única por name, removemos:
ALTER TABLE meals
  DROP INDEX uk_meals_name;

-- Opcional: unicidade do nome de refeição por dieta
ALTER TABLE meals
  ADD CONSTRAINT uk_meals_diet_name UNIQUE (diet_id, name);

ALTER TABLE meals
  ADD CONSTRAINT fk_meals_diet
    FOREIGN KEY (diet_id) REFERENCES diets(id)
    ON DELETE CASCADE;

CREATE INDEX idx_meals_diet_id ON meals(diet_id);
