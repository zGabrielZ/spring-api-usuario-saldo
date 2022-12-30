insert into dbo.usuario (cpf, data_nascimento, email, nome,saldo_total,senha)
values ('57252647005','1997-12-26','usuariomastergabriel@gmail.com','Gabriel Ferreira',0.0,'$2a$10$aXn3.Hn6MbnsdEbvBh/OhehvXBo3HMyPIqGoBVRuU2yxxNxqY0tWu');
insert into dbo.usuarios_perfil (usuario_id, perfil_id) values ((select id from dbo.usuario order by id desc limit 1), (select id from dbo.perfil where nome = 'ROLE_ADMIN'));
