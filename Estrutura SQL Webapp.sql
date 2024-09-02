CREATE SCHEMA webapp;

-- CRIACAO DAS TABLES

CREATE TABLE webapp.loja (
    cnpj CHAR(14) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    contato VARCHAR(15),
    hr_funcionamento VARCHAR(50),
    endereco VARCHAR(200) NOT NULL,
    email VARCHAR(100),
    CONSTRAINT pk_loja PRIMARY KEY (cnpj)
);

CREATE TABLE webapp.cardapio (
    id_card SERIAL,
    nome VARCHAR(100) NOT NULL,
    loja_cnpj CHAR(14),
    CONSTRAINT pk_cardapio PRIMARY KEY (id_card),
    CONSTRAINT fk_cardapio_loja FOREIGN KEY (loja_cnpj)
        REFERENCES webapp.loja (cnpj)
);

CREATE TABLE webapp.secao (
    nome VARCHAR(100) NOT NULL,
    cardapio_id_card INT,
    CONSTRAINT pk_secao PRIMARY KEY (nome),
    CONSTRAINT fk_secao_cardapio FOREIGN KEY (cardapio_id_card)
        REFERENCES webapp.cardapio (id_card)
);

CREATE TABLE webapp.subsecao (
    nome VARCHAR(100) NOT NULL,
    secao_nome VARCHAR(100),
    CONSTRAINT pk_subsecao PRIMARY KEY (nome),
    CONSTRAINT fk_subsecao_secao FOREIGN KEY (secao_nome)
        REFERENCES webapp.secao (nome)
);

CREATE TABLE webapp.produto (
    id_prod SERIAL,
    nome VARCHAR(100) NOT NULL,
    preco NUMERIC(10, 2),
    qtd_disponivel INT,
    descricao VARCHAR(255),
    subsecao_nome VARCHAR(100),
    CONSTRAINT pk_produto PRIMARY KEY (id_prod),
    CONSTRAINT fk_produto_subsecao FOREIGN KEY (subsecao_nome)
        REFERENCES webapp.subsecao (nome),
    CONSTRAINT ck_produto_preco CHECK (preco >= 0.00),
    CONSTRAINT ck_produto_qtd_disponivel CHECK (qtd_disponivel >= 0)
);

CREATE TABLE webapp.pedido (
    id_pedi SERIAL,
    data_pedi DATE,
    valor_total NUMERIC(10, 2),
    CONSTRAINT pk_pedido PRIMARY KEY (id_pedi),
    CONSTRAINT ck_pedido_valor_total CHECK (valor_total >= 0.00)
);

CREATE TABLE webapp.item_pedido (
    preco NUMERIC(10, 2),
    quantidade INT,
    produto_id_prod INT,
    pedido_id_pedi INT,
    CONSTRAINT pk_item_pedido PRIMARY KEY (produto_id_prod, pedido_id_pedi),
    CONSTRAINT fk_item_pedido_produto FOREIGN KEY (produto_id_prod)
        REFERENCES webapp.produto (id_prod)
        ON DELETE CASCADE,
    CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (pedido_id_pedi)
        REFERENCES webapp.pedido (id_pedi)
        ON DELETE CASCADE,
    CONSTRAINT ck_item_pedido_preco CHECK (preco >= 0.00),
    CONSTRAINT ck_item_pedido_quantidade CHECK (quantidade >= 0)
);

CREATE TABLE webapp.cliente (
    cpf CHAR(11) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    contato VARCHAR(15),
    CONSTRAINT pk_cliente PRIMARY KEY (cpf)
);

CREATE TABLE webapp.endereco_cliente (
    cliente_cpf CHAR(11),
    endereco VARCHAR(100),
    CONSTRAINT pk_endereco_cliente PRIMARY KEY (cliente_cpf, endereco),
    CONSTRAINT fk_endereco_cliente_cliente FOREIGN KEY (cliente_cpf)
        REFERENCES webapp.cliente (cpf)
);

CREATE TABLE webapp.cliente_realiza_pedido (
    pedido_id_pedi INT,
    cliente_cpf CHAR(11),
    loja_cnpj CHAR(14),
    CONSTRAINT pk_cliente_realiza_pedido PRIMARY KEY (pedido_id_pedi, cliente_cpf, loja_cnpj),
    CONSTRAINT fk_cliente_realiza_pedido_pedido FOREIGN KEY (pedido_id_pedi)
        REFERENCES webapp.pedido (id_pedi),
    CONSTRAINT fk_cliente_realiza_pedido_cliente FOREIGN KEY (cliente_cpf)
        REFERENCES webapp.cliente (cpf),
    CONSTRAINT fk_cliente_realiza_pedido_loja FOREIGN KEY (loja_cnpj)
        REFERENCES webapp.loja (cnpj)
);

-- INSERCAO DE DADOS NAS TABLES

INSERT INTO webapp.loja (cnpj, nome, contato, hr_funcionamento, endereco, email) VALUES
    ('12345678900001', 'Loja A', '43999000001', '08:00-18:00', 'Rua A, 568', 'contatolojaA@gmail.com'),
    ('12345678900002', 'Loja B', '43999000002', '12:00-23:59', 'Avenida B, 1046', 'emaillojaB@hotmail.com');


WITH inserted_cardapio AS (
    INSERT INTO webapp.cardapio (nome, loja_cnpj) VALUES
    ('Cardapio Barato', '12345678900001'),
    ('Cardapio Luxuoso', '12345678900002')
    RETURNING id_card, nome
),

inserted_secao AS (
    INSERT INTO webapp.secao (nome, cardapio_id_card)
    SELECT 'Bebidas', id_card FROM inserted_cardapio WHERE nome = 'Cardapio Barato'
    UNION ALL
    SELECT 'Lanches', id_card FROM inserted_cardapio WHERE nome = 'Cardapio Barato'
    UNION ALL
    SELECT 'Massas', id_card FROM inserted_cardapio WHERE nome = 'Cardapio Luxuoso'
    RETURNING nome
)

INSERT INTO webapp.subsecao (nome, secao_nome) VALUES
    ('Refrigerante', 'Bebidas'),
    ('Cerveja', 'Bebidas'),
    ('Hot dog', 'Lanches'),
    ('hambúrguer', 'Lanches'),
    ('Espaguete', 'Massas'),
    ('Nhoque', 'Massas');


WITH inserted_produto AS (
    INSERT INTO webapp.produto (nome, preco, qtd_disponivel, descricao, subsecao_nome) VALUES
    ('Coca-cola lata', 5.50, 10, 'Refrigerante com 350ml', 'Refrigerante'),
    ('Skol Long Neck', 6.00, 15, 'Desce redondo. Ahh!!', 'Cerveja'),
    ('Dog frango', 12.50, 20, 'Hot dog com frango desfiado', 'Hot dog'),
    ('X-Salada', 10.25, 5, 'Hamburger saudavel', 'hambúrguer'),
    ('Espaguete Bolonesa', 27.00, 6, 'Molho vermelho com carne moida', 'Espaguete'),
    ('Nhoque de molho branco', 30.00, 4, 'Melhor Nhoque da cidade, delicia!!', 'Nhoque')
    RETURNING id_prod, nome
)

INSERT INTO webapp.cliente (cpf, nome, contato) VALUES
    ('12345678910', 'Jonas da Silva', '43988006611'),
    ('98765432198', 'Don Pedro', '43966587410');

INSERT INTO webapp.endereco_cliente (cliente_cpf, endereco) VALUES
    ('12345678910', 'Rua Z, 200'),
    ('98765432198', 'Rodovia, 6965');

-- SIMULANDO UM PEDIDO

-- Reverter qualquer transação anterior que tenha falhado
ROLLBACK;
BEGIN;
-- Declarar uma variável para armazenar o ID do pedido
DO $$
DECLARE
    id_pedido_inserido INTEGER;
BEGIN

    INSERT INTO webapp.pedido (data_pedi, valor_total)
    VALUES ('2024-09-01', 0.00)
    RETURNING id_pedi INTO id_pedido_inserido;

    INSERT INTO webapp.item_pedido (preco, quantidade, produto_id_prod, pedido_id_pedi)
    VALUES 
        (5.50, 2, (SELECT id_prod FROM webapp.produto WHERE nome = 'Coca-cola lata'), id_pedido_inserido),
        (6.00, 1, (SELECT id_prod FROM webapp.produto WHERE nome = 'Skol Long Neck'), id_pedido_inserido),
        (12.50, 1, (SELECT id_prod FROM webapp.produto WHERE nome = 'Dog frango'), id_pedido_inserido);

    UPDATE webapp.pedido
    SET valor_total = 33.00 -- Define o valor total diretamente
    WHERE id_pedi = id_pedido_inserido;

    -- Reduzir o estoque dos produtos
    UPDATE webapp.produto
    SET qtd_disponivel = qtd_disponivel - (
        SELECT SUM(quantidade)
        FROM webapp.item_pedido
        WHERE produto_id_prod = webapp.produto.id_prod
          AND pedido_id_pedi = id_pedido_inserido
    )
    WHERE id_prod IN (
        SELECT produto_id_prod
        FROM webapp.item_pedido
        WHERE pedido_id_pedi = id_pedido_inserido
    );

    -- Associar o pedido ao cliente e loja
    INSERT INTO webapp.cliente_realiza_pedido (pedido_id_pedi, cliente_cpf, loja_cnpj)
    VALUES (id_pedido_inserido, '12345678910', '12345678900001');
END $$;
COMMIT;

-- ATUALIZACAO DOS DADOS

UPDATE webapp.produto
 SET preco = 6.00
 WHERE nome = 'Coca-cola lata';

UPDATE webapp.produto
 SET qtd_disponivel = 10
 WHERE nome = 'Espaguete Bolonesa';

UPDATE webapp.loja
 SET contato = '43966862348'
 WHERE cnpj = '12345678900001';

UPDATE webapp.endereco_cliente
 SET endereco = 'Avenida L, 500'
 WHERE cliente_cpf = '12345678910';
 

-- VIZUALIAR TABELAS
-- lojas
SELECT * FROM webapp.loja;
-- cardápios
SELECT * FROM webapp.cardapio;
-- seções
SELECT * FROM webapp.secao;
-- subseções
SELECT * FROM webapp.subsecao;
-- produtos
SELECT * FROM webapp.produto;
-- pedidos
SELECT * FROM webapp.pedido;
-- itens pedido
SELECT * FROM webapp.item_pedido;
-- clientes
SELECT * FROM webapp.cliente;
-- endereco dos clientes
SELECT * FROM webapp.endereco_cliente;
-- pedidos realizados por clientes
SELECT * FROM webapp.cliente_realiza_pedido;


-- Excluir todas as tabelas do schema 'webapp' para testes
DROP TABLE IF EXISTS webapp.item_pedido CASCADE;
DROP TABLE IF EXISTS webapp.cliente_realiza_pedido CASCADE;
DROP TABLE IF EXISTS webapp.endereco_cliente CASCADE;
DROP TABLE IF EXISTS webapp.cliente CASCADE;
DROP TABLE IF EXISTS webapp.produto CASCADE;
DROP TABLE IF EXISTS webapp.subsecao CASCADE;
DROP TABLE IF EXISTS webapp.secao CASCADE;
DROP TABLE IF EXISTS webapp.pedido CASCADE;
DROP TABLE IF EXISTS webapp.cardapio CASCADE;
DROP TABLE IF EXISTS webapp.loja CASCADE;

-- Excluir o schema 'webapp' para testes
DROP SCHEMA IF EXISTS webapp CASCADE;