UPDATE dbo.USUARIO SET USUARIO_INCLUSAO_ID = (SELECT ID FROM dbo.USUARIO WHERE email = 'usuariomastergabriel@gmail.com')
, USUARIO_ALTERACAO_ID = (SELECT ID FROM dbo.USUARIO WHERE email = 'usuariomastergabriel@gmail.com')
, DATA_INCLUSAO = '2023-01-16 21:00:00.000'
, DATA_ALTERACAO = '2023-01-16 21:00:00.000'
WHERE ID = (SELECT ID FROM dbo.USUARIO WHERE email = 'usuariomastergabriel@gmail.com');