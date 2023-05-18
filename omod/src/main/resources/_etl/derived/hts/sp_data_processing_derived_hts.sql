-- $BEGIN
-- CALL sp_dim_client_hiv_hts;
-- CALL sp_fact_encounter_hiv_hts;

CALL sp_dim_follow_up_client;
CALL sp_fact_tx_curr;
-- $END