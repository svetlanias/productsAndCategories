ALTER TABLE IF EXISTS category_attributes DROP CONSTRAINT IF EXISTS fk_cat_attr_category;
ALTER TABLE IF EXISTS category_attributes DROP CONSTRAINT IF EXISTS fk_cat_attr_attribute;
ALTER TABLE IF EXISTS products DROP CONSTRAINT IF EXISTS fk_product_category;
ALTER TABLE IF EXISTS categories DROP CONSTRAINT IF EXISTS fk_category_parent;

DROP TABLE IF EXISTS category_attributes CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS attributes CASCADE;
DROP TABLE IF EXISTS categories CASCADE;

DROP SEQUENCE IF EXISTS attribute_seq;
DROP SEQUENCE IF EXISTS category_seq;
DROP SEQUENCE IF EXISTS product_seq;

CREATE SEQUENCE attribute_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE category_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE attributes (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('attribute_seq'),
    name VARCHAR(255) NOT NULL,
    short_name VARCHAR(255) NOT NULL,
    a_type VARCHAR(255) NOT NULL,
    string_values JSONB
);

CREATE TABLE categories (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('category_seq'),
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT
);

CREATE TABLE category_attributes (
    category_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,
    required BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (category_id, attribute_id)
);

CREATE TABLE products (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('product_seq'),
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10,2) NOT NULL CHECK (price > 0),
    description TEXT,
    image_path VARCHAR(255),
    maker VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    category_id BIGINT NOT NULL,
    attributes JSONB NOT NULL DEFAULT '{}'
);

ALTER TABLE categories
    ADD CONSTRAINT fk_category_parent
    FOREIGN KEY (parent_id) REFERENCES categories (id);

ALTER TABLE category_attributes
    ADD CONSTRAINT fk_cat_attr_category
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE;

ALTER TABLE category_attributes
    ADD CONSTRAINT fk_cat_attr_attribute
    FOREIGN KEY (attribute_id) REFERENCES attributes (id) ON DELETE CASCADE;

ALTER TABLE products
    ADD CONSTRAINT fk_product_category
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT;

CREATE INDEX idx_products_attributes_gin ON products USING GIN (attributes);
CREATE INDEX idx_products_category ON products (category_id);
CREATE INDEX idx_categories_parent ON categories (parent_id);