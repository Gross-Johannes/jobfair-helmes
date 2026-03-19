CREATE TABLE benefit_calculation_inputs
(
    id              UUID           NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    gross_salary    DECIMAL(10, 2) NOT NULL,
    baby_birth_date date           NOT NULL,
    CONSTRAINT pk_benefit_calculation_inputs PRIMARY KEY (id)
);