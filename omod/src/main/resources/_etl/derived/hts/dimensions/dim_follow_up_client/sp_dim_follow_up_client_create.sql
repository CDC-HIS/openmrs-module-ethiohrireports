-- $BEGIN
CREATE TABLE IF NOT EXISTS mamba_dim_follow_up_client
(
    id            INT AUTO_INCREMENT,
    client_id     INT           NULL,
    identifier    NVARCHAR(255) NULL,
    date_of_birth DATE          NULL,
    age_num     INT           NULL,
    sex           NVARCHAR(50)  NULL,
    county        NVARCHAR(255) NULL,
    sub_county    NVARCHAR(255) NULL,
    ward          NVARCHAR(255) NULL,
    given_name       NVARCHAR(255) NULL,
    middle_name   NVARCHAR(255) NULL,
    family_name  NVARCHAR(255) NULL,
    PRIMARY KEY (id)
);

CREATE INDEX
   mamba_dim_follow_up_client_client_id_index ON mamba_dim_follow_up_client (client_id);

-- $END

