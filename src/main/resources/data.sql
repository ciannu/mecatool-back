-- Insert roles
INSERT INTO roles (name, description) VALUES ('ADMIN', 'Administrator role with full access');
INSERT INTO roles (name, description) VALUES ('MECHANIC', 'Mechanic role with limited access');

-- Insert admin user
INSERT INTO users (first_name, last_name, email, password, role_id, created_at, updated_at)
VALUES (
    'Admin',
    'User',
    'admin@mecatool.com',
    '$2a$10$aIiTQ65HLLuVEuPWl98V0.lpjdaGU6iov6dBZ7NFCCGEG.hWPeBiC', -- Fresh BCrypt hash for 'admin123' with strength 10
    1, -- role_id for ADMIN
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Insert sample mechanics
INSERT INTO mechanics (first_name, last_name, email, phone) VALUES ('Juan', 'Perez', 'juan.perez@example.com', '123-456-7890');
INSERT INTO mechanics (first_name, last_name, email, phone) VALUES ('Maria', 'Gomez', 'maria.gomez@example.com', '098-765-4321');

-- Insert sample clients
INSERT INTO clients (first_name, last_name, email, phone, address) VALUES ('Carlos', 'Ruiz', 'carlos.ruiz@example.com', '111-222-3333', 'Calle Falsa 123');
INSERT INTO clients (first_name, last_name, email, phone, address) VALUES ('Ana', 'Diaz', 'ana.diaz@example.com', '444-555-6666', 'Avenida Siempre Viva 742');

-- Insert sample vehicles
INSERT INTO vehicles (client_id, brand, model, year, license_plate) VALUES (1, 'Toyota', 'Corolla', 2020, 'ABC-123');
INSERT INTO vehicles (client_id, brand, model, year, license_plate) VALUES (2, 'Honda', 'Civic', 2018, 'DEF-456');

-- Insert sample inventory items
INSERT INTO inventory_items (name, category, quantity, min_stock, price, description) VALUES ('Aceite de Motor', 'Lubricantes', 50, 10, 15.99, 'Aceite sintético 5W-30');
INSERT INTO inventory_items (name, category, quantity, min_stock, price, description) VALUES ('Filtro de Aire', 'Filtros', 20, 5, 8.50, 'Filtro de aire para motor');

-- Insert sample work orders (assuming vehicle_id 1 and mechanic_id 1 exist)
INSERT INTO work_orders (vehicle_id, description, status, start_date, end_date, total) VALUES (1, 'Cambio de aceite y filtro', 'COMPLETED', '2025-06-01', '2025-06-01', 30.00);
INSERT INTO work_orders (vehicle_id, description, status, start_date, end_date, total) VALUES (2, 'Revisión general', 'PENDING', '2025-06-15', NULL, 0.00); 