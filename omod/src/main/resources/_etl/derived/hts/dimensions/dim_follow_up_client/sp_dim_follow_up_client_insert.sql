-- $BEGIN

INSERT INTO
    mamba_dim_follow_up_client (
        client_id,
        identifier,
        date_of_birth,
        age_num,
        sex,
        county,
        sub_county,
        ward,
        given_name,
        middle_name,
        family_name
    )
SELECT
 DISTINCT (f.client_id),
    pi.identifier,
    p.birthdate,
    DATE_FORMAT(
        FROM_DAYS(DATEDIFF(NOW(), p.birthdate)),
        '%Y'
    ) + 0 AS age,
    p.gender,
    pa.country,
    pa.city_village as sub_country,
    pa.address1 as worda,
    pn.given_name,
    pn.middle_name,
    pn.family_name
from
    mamba_flat_encounter_follow_up as f
    INNER JOIN person as p on p.person_id = f.client_id
    INNER JOIN person_name as pn on pn.person_id = f.client_id
    INNER JOIN patient_identifier as pi on pi.patient_id = f.client_id AND pi.identifier_type =3
    LEFT JOIN person_address as pa on pa.person_id = f.client_id;

-- $END
