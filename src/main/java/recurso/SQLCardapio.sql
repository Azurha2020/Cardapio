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
create table Pratos(
	id int primary key,
    nome varchar(100),
    preparo text,
    porcoes int,
    tempo int
);
create table if not exists Ingrediente_Prato(
	ingrediente int,
    prato int,
    constraint fk_Ing foreign key (ingrediente) references Ingredientes(id),
    constraint fk_prato foreign key (prato) references Pratos(id)
);
