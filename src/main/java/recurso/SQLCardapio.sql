drop database cardapio;
create database if not exists Cardapio;
use Cardapio;
create table if not exists Grupos(
	id int primary key auto_increment,
    nome varchar(100)
);
create table if not exists Ingredientes(
	id int primary key auto_increment,
    nome varchar(100),
    calorias int,
    quantidade double,
    grupo int,
    constraint fkGrupo foreign key (grupo) references Grupos(id) ON DELETE CASCADE
);
create table if not exists Pratos(
	id int primary key auto_increment,
    nome varchar(100),
    preparo text,
    porcoes int,
    calorias int,
    pronto boolean,
    tempo int
);
CREATE TABLE IF NOT EXISTS Ingrediente_Prato (
    ingrediente INT,
    prato INT,
    quantidade double,
    PRIMARY KEY (ingrediente, prato),
    CONSTRAINT fk_Ing FOREIGN KEY (ingrediente) REFERENCES Ingredientes(id) ON DELETE CASCADE,
    CONSTRAINT fk_prato FOREIGN KEY (prato) REFERENCES Pratos(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Refeicoes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tempottl INT,
    tempomaximo INT,
    num_pratos INT,
    caloriattl INT,
    caloriamax INT
);
CREATE TABLE IF NOT EXISTS Prato_Refeicao (
    prato INT,
    refeicao INT,
    PRIMARY KEY (prato, refeicao),
    CONSTRAINT fk_pr FOREIGN KEY (prato) REFERENCES Pratos(id) ON DELETE CASCADE,
    CONSTRAINT fk_ref FOREIGN KEY (refeicao) REFERENCES Refeicoes(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Grupo_Refeicao (
    grupo INT,
    refeicao INT,
    PRIMARY KEY (grupo, refeicao),
    CONSTRAINT fk_grupo_ref FOREIGN KEY (grupo) REFERENCES Grupos(id) ON DELETE CASCADE,
    CONSTRAINT fk_refeicao_grp FOREIGN KEY (refeicao) REFERENCES Refeicoes(id) ON DELETE CASCADE
);
-- 1. Inserindo Grupos
INSERT INTO Grupos (nome) VALUES 
('Proteínas'),
('Carboidratos'),
('Vegetais e Verduras'),
('Laticínios');

-- 2. Inserindo Ingredientes (referenciando Grupos)
INSERT INTO Ingredientes (nome, calorias, quantidade, grupo) VALUES 
('Peito de Frango', 165, 1000.0, 1),
('Arroz Integral', 110, 1000.0, 2),
('Brócolis', 34, 1000.0, 3),
('Queijo Muçarela', 280, 1000.0, 4),
('Batata Doce', 86, 1000.0, 2);

-- 3. Inserindo Pratos
INSERT INTO Pratos (nome, preparo, porcoes, calorias, pronto, tempo) VALUES 
('Frango Grelhado com Brócolis', 'Tempere o peito de frango e grelhe. Cozinhe o brócolis no vapor.', 2, 400, TRUE, 25),
('Escondidinho de Batata Doce', 'Faça um purê com a batata doce, recheie com o frango desfiado e cubra com queijo.', 4, 650, TRUE, 45),
('Salada Proteica Express', 'Corte o frango grelhado em cubos e misture com o brócolis cozido.', 1, 250, FALSE, 15);

-- 4. Relacionando Ingredientes e Pratos (Ingrediente_Prato)
INSERT INTO Ingrediente_Prato (ingrediente, prato, quantidade) VALUES 
(1, 1, 200.0), -- Peito de Frango no Frango Grelhado
(3, 1, 150.0), -- Brócolis no Frango Grelhado
(1, 2, 300.0), -- Peito de Frango no Escondidinho
(5, 2, 400.0), -- Batata Doce no Escondidinho
(4, 2, 100.0), -- Queijo Muçarela no Escondidinho
(1, 3, 100.0), -- Peito de Frango na Salada
(3, 3, 100.0); -- Brócolis na Salada

-- 5. Inserindo Refeições
INSERT INTO Refeicoes (tempottl, tempomaximo, num_pratos, caloriattl, caloriamax) VALUES 
(25, 30, 1, 400, 500),  -- Almoço Rápido
(60, 90, 2, 1050, 1200); -- Jantar em Família

-- 6. Relacionando Pratos e Refeições (Prato_Refeicao)
INSERT INTO Prato_Refeicao (prato, refeicao) VALUES 
(1, 1), -- Frango Grelhado no Almoço Rápido
(1, 2), -- Frango Grelhado no Jantar
(2, 2); -- Escondidinho no Jantar

-- 7. Relacionando Grupos e Refeições (Grupo_Refeicao)
INSERT INTO Grupo_Refeicao (grupo, refeicao) VALUES 
(1, 1), -- Proteínas no Almoço Rápido
(3, 1), -- Vegetais no Almoço Rápido
(1, 2), -- Proteínas no Jantar
(2, 2), -- Carboidratos no Jantar
(4, 2); -- Laticínios no Jantar
SELECT 
    p.nome AS prato,
    i.nome AS ingrediente,
    ip.quantidade
FROM Pratos p
INNER JOIN Ingrediente_Prato ip ON p.id = ip.prato
INNER JOIN Ingredientes i ON ip.ingrediente = i.id
ORDER BY p.nome, i.nome;
select * from ingredientes;
SELECT i.id, i.nome, i.calorias, i.quantidade, i.grupo, g.nome AS nome_grupo
                FROM Ingredientes i 
INNER JOIN Grupos g ON i.grupo = g.id 
                order by i.id;