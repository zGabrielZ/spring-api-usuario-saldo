insert into dbo.PERFIL (NOME) values ('ROLE_ADMIN');
insert into dbo.PERFIL (NOME) values ('ROLE_FUNCIONARIO');
insert into dbo.PERFIL (NOME) values ('ROLE_CLIENTE');

alter table dbo.USUARIOS_PERFIL add constraint FAIXA_PERFIL check (PERFIL_ID in (1,2,3));
alter table dbo.SALDO add constraint SALDO_DEPOSITO check (DEPOSITO >= 0.0);
alter table dbo.SAQUE add constraint SALDO_SAQUE check (VALOR >= 0.0);

