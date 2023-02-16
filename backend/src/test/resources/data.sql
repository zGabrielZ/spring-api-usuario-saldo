insert into dbo.PERFIL (NOME, DESCRICAO) values ('ROLE_ADMIN','Administrador');
insert into dbo.PERFIL (NOME, DESCRICAO) values ('ROLE_FUNCIONARIO','Funcionário');
insert into dbo.PERFIL (NOME, DESCRICAO) values ('ROLE_CLIENTE', 'Cliente');

ALTER TABLE dbo.USUARIO ADD CONSTRAINT EMAIL_UNIQUE UNIQUE (EMAIL);
ALTER TABLE dbo.USUARIO ADD CONSTRAINT CPF_UNIQUE UNIQUE (CPF);
alter table dbo.USUARIOS_PERFIL add constraint FAIXA_PERFIL check (PERFIL_ID in (1,2,3));
alter table dbo.SALDO add constraint SALDO_DEPOSITO check (DEPOSITO >= 0.0);
alter table dbo.SAQUE add constraint SALDO_SAQUE check (VALOR >= 0.0);

