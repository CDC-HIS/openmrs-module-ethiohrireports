-- $BEGIN

CREATE TABLE
    mamba_fact_tx_curr (
        id INT AUTO_INCREMENT,
        encounter_id INT NOT NULL,
        client_id INT NOT NULL,
        patient_status CHAR(255) CHARACTER SET UTF8MB4 NULL,
        regiment CHAR(255) CHARACTER SET UTF8MB4 NULL,
        treatment_end_date DATETIME  NULL,
        PRIMARY KEY (id)
    );

CREATE INDEX
    mamba_fact_tx_curr_trmt_end_date_index ON mamba_fact_tx_curr (treatment_end_date);

CREATE INDEX
    mamba_fact_tx_curr_client_id_index ON mamba_fact_tx_curr (client_id);

CREATE INDEX
    mamba_fact_tx_curr_patient_status_index ON mamba_fact_tx_curr (patient_status);

-- $END