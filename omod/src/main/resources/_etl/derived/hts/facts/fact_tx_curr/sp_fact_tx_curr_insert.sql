-- $BEGIN

INSERT INTO
    mamba_fact_tx_curr (
        encounter_id,
        client_id,
        patient_status,
        regiment,
        treatment_end_date
    )
SELECT
    fu.encounter_id,
    fu.client_id ,
    fu.patient_status,
    fu.regiment,
    CAST(fu.treatment_end_date as DATETIME) AS treatment_end_date
FROM mamba_flat_encounter_follow_up as fu;

-- $END