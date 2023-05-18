DELIMITER //

DROP PROCEDURE IF EXISTS sp_fact_tx_curr_query;

CREATE PROCEDURE sp_fact_tx_curr_query(IN ART_END_DATE 
DATETIME) BEGIN 
	SELECT
	    mp.identifier,
	    CONCAT(
	        mp.given_name,
	        " ",
	        mp.middle_name,
	        " ",
	        mp.family_name
	    ) as full_name,
	    case
	        WHEN mp.sex = 'M' THEN "Male"
	        ELSE "Female"
	    END as gender,
	      mp.age_num as `age`,
	    txc.treatment_end_date,
	    txc.patient_status,
	    txc.regiment
	FROM mamba_fact_tx_curr txc
	    INNER JOIN mamba_dim_follow_up_client as mp
		on txc.client_id = mp.client_id
	WHERE
	    txc.treatment_end_date >= art_end_date;
	END // 


DELIMITER ;