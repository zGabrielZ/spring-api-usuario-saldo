alter table dbo.PERFIL ADD COLUMN DESCRICAO VARCHAR(255);
update dbo.PERFIL set DESCRICAO = 'Adminstrador' where NOME = 'ROLE_ADMIN';
update dbo.PERFIL set DESCRICAO = 'Funcionário' where NOME = 'ROLE_FUNCIONARIO';
update dbo.PERFIL set DESCRICAO = 'Cliente' where NOME = 'ROLE_CLIENTE';