create database if not exists Cardapio;
use Cardapio;
create table if not exists Grupos(
	id int primary key,
    nome varchar(100)
);
create table if not exists Ingredientes(
	id int primary key,
    nome varchar(100),
    calorias int,
    quantidade double,
    grupo int,
    constraint fkGrupo foreign key (grupo) references Grupos(id)
);
create TABLE IF NOT EXISTS Pratos(
	id int primary key,
    nome varchar(100),
    preparo text,
    porcoes int,
    tempo int
);
CREATE TABLE IF NOT EXISTS Refeicoes(
id int PRIMARY KEY auto increment,
tempo_max int,
tempo_ttl int,
caloria_Max int,
caloria_ttl int
);
create table if not exists Ingrediente_Prato(
	ingrediente int,
    prato int,
    constraint fk_Ing foreign key (ingrediente) references Ingredientes(id),
    constraint fk_prato foreign key (prato) references Pratos(id)
);
CREATE TABLE IF NOT EXISTS Pratos_refeicao(
	prato int,
	refeicao int,
	CONSTRAINT fk_prato FOREIGN KEY (prato) REFERENCES Pratos(id),
	CONSTRAINT fk_refeicao FOREIGN KEY (refeicao) REFERENCES Refeicoes(id)
);
