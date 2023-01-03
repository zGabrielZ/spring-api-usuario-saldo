insert into dbo.PERFIL (NOME, DESCRICAO) values ('ROLE_ADMIN','Administrador');
insert into dbo.PERFIL (NOME, DESCRICAO) values ('ROLE_FUNCIONARIO','Funcionário');
insert into dbo.PERFIL (NOME, DESCRICAO) values ('ROLE_CLIENTE', 'Cliente');

ALTER TABLE dbo.USUARIO ADD CONSTRAINT EMAIL_UNIQUE UNIQUE (EMAIL);
ALTER TABLE dbo.USUARIO ADD CONSTRAINT CPF_UNIQUE UNIQUE (CPF);
alter table dbo.USUARIOS_PERFIL add constraint FAIXA_PERFIL check (PERFIL_ID in (1,2,3));
alter table dbo.SALDO add constraint SALDO_DEPOSITO check (DEPOSITO >= 0.0);
alter table dbo.SAQUE add constraint SALDO_SAQUE check (VALOR >= 0.0);

insert into dbo.usuario (cpf, data_nascimento, email, nome,saldo_total,senha)
values ('57252647005','1997-12-26','usuariomastergabriel@gmail.com','Gabriel Ferreira',0.0,'$2a$10$aXn3.Hn6MbnsdEbvBh/OhehvXBo3HMyPIqGoBVRuU2yxxNxqY0tWu');
insert into dbo.usuarios_perfil (usuario_id, perfil_id) values ((select id from dbo.usuario order by id desc limit 1), (select id from dbo.perfil where nome = 'ROLE_ADMIN'));

