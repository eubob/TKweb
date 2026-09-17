CREATE DATABASE IF NOT EXISTS tkweb
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE tkweb;

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuarios_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS habilidades (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    tipo ENUM('OFERECE', 'DESEJA') NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    usuario_id BIGINT NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_habilidades_usuario (usuario_id),
    KEY idx_habilidades_tipo (tipo),
    KEY idx_habilidades_categoria (categoria),
    KEY idx_habilidades_criado_em (criado_em),
    CONSTRAINT fk_habilidades_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS curtidas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    habilidade_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    criada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_curtidas_habilidade_usuario (habilidade_id, usuario_id),
    KEY idx_curtidas_usuario (usuario_id),
    CONSTRAINT fk_curtidas_habilidade
        FOREIGN KEY (habilidade_id)
        REFERENCES habilidades(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_curtidas_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS comentarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    habilidade_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    texto VARCHAR(500) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_comentarios_habilidade (habilidade_id),
    KEY idx_comentarios_usuario (usuario_id),
    KEY idx_comentarios_criado_em (criado_em),
    CONSTRAINT fk_comentarios_habilidade
        FOREIGN KEY (habilidade_id)
        REFERENCES habilidades(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_comentarios_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS trocas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    solicitante_id BIGINT NOT NULL,
    receptor_id BIGINT NOT NULL,
    habilidade_solicitada_id BIGINT NOT NULL,
    habilidade_oferecida_id BIGINT NOT NULL,
    status ENUM('PENDENTE', 'ACEITA', 'RECUSADA', 'FINALIZADA') NOT NULL DEFAULT 'PENDENTE',
    criada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_trocas_solicitante (solicitante_id),
    KEY idx_trocas_receptor (receptor_id),
    KEY idx_trocas_status (status),
    KEY idx_trocas_habilidade_solicitada (habilidade_solicitada_id),
    KEY idx_trocas_habilidade_oferecida (habilidade_oferecida_id),
    KEY idx_trocas_atualizada_em (atualizada_em),
    CONSTRAINT fk_trocas_solicitante
        FOREIGN KEY (solicitante_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_trocas_receptor
        FOREIGN KEY (receptor_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_trocas_habilidade_solicitada
        FOREIGN KEY (habilidade_solicitada_id)
        REFERENCES habilidades(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_trocas_habilidade_oferecida
        FOREIGN KEY (habilidade_oferecida_id)
        REFERENCES habilidades(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS avaliacoes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    troca_id BIGINT NOT NULL,
    avaliador_id BIGINT NOT NULL,
    avaliado_id BIGINT NOT NULL,
    nota TINYINT NOT NULL,
    comentario VARCHAR(500),
    criada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_avaliacoes_troca_avaliador (troca_id, avaliador_id),
    KEY idx_avaliacoes_avaliador (avaliador_id),
    KEY idx_avaliacoes_avaliado (avaliado_id),
    KEY idx_avaliacoes_troca (troca_id),
    CONSTRAINT fk_avaliacoes_troca
        FOREIGN KEY (troca_id)
        REFERENCES trocas(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_avaliacoes_avaliador
        FOREIGN KEY (avaliador_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_avaliacoes_avaliado
        FOREIGN KEY (avaliado_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT ck_avaliacoes_nota CHECK (nota BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS mensagens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    troca_id BIGINT NOT NULL,
    remetente_id BIGINT NOT NULL,
    mensagem VARCHAR(1000) NOT NULL,
    enviada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_mensagens_troca (troca_id),
    KEY idx_mensagens_remetente (remetente_id),
    KEY idx_mensagens_enviada_em (enviada_em),
    CONSTRAINT fk_mensagens_troca
        FOREIGN KEY (troca_id)
        REFERENCES trocas(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_mensagens_remetente
        FOREIGN KEY (remetente_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS notificacoes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    url VARCHAR(500),
    lida TINYINT(1) NOT NULL DEFAULT 0,
    criada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_notificacoes_usuario (usuario_id),
    KEY idx_notificacoes_lida (usuario_id, lida),
    KEY idx_notificacoes_criada_em (criada_em),
    CONSTRAINT fk_notificacoes_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO usuarios (nome, email, senha)
VALUES (
    'Admin',
    'admin@admin.com',
    SHA2('123456', 256)
);