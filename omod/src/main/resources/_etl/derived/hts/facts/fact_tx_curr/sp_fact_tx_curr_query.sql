DELIMITER //

DROP PROCEDURE IF EXISTS sp_fact_tx_curr_query;

CREATE PROCEDURE sp_fact_tx_curr_query(
    IN art_end_date DATETIME
) 

BEGIN 

	SELECT txc.patient_status, txc.regiment, txc.treatment_end_date 
    FROM mamba_fact_tx_curr txc
	    WHERE txc.treatment_end_date >= art_end_date;
END //

DELIMITER ;