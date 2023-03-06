create schema if not exists dbo;

create table dbo.USUARIO_MOVIMENTACAO(
    ID INT NOT NULL,
    USUARIO_ID INT NOT NULL,
    SALDO_ATUAL NUMERIC DEFAULT 0,
    DATA_NASCIMENTO DATE NOT NULL,
    QUANTIDADE_INFORMADA NUMERIC NOT NULL,
    DESCRICAO VARCHAR(255) NOT NULL,
    SITUACAO_ID INT NOT NULL,
    CONSTRAINT USUARIO_MOVIMENTACAO_PK PRIMARY KEY(ID)
);

CREATE SEQUENCE dbo.USUARIO_MOVIMENTACAO_SEQUENCE_ID
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

ALTER TABLE dbo.USUARIO_MOVIMENTACAO ALTER COLUMN ID SET DEFAULT NEXTVAL('dbo.USUARIO_MOVIMENTACAO_SEQUENCE_ID'::regclass);

ALTER TABLE dbo.USUARIO_MOVIMENTACAO ADD FOREIGN KEY (USUARIO_ID) REFERENCES dbo.USUARIO(ID);
ALTER TABLE dbo.USUARIO_MOVIMENTACAO ADD FOREIGN KEY (SITUACAO_ID) REFERENCES dbo.SITUACAO(ID);