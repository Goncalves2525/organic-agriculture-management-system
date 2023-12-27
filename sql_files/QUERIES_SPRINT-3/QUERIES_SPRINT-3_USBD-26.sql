-- SPRINT 3

/* USBD26 Como Gestor Agrícola,
* pretendo que a criação ou alteração de um registo de uma operação deva ser registado num log que indique
* o instante, tipo de operação e todos os dados relacionados com a operação (e.g. data da operação, parcela, etc.). */

-- Log de Aplicações de Fatores de Produção
create or replace NONEDITIONABLE TRIGGER "LOGAPLICACAOTRIGGER"
AFTER INSERT OR UPDATE ON aplicacoes_fatprod
                           FOR EACH ROW
DECLARE
operacaoID NUMBER;
    log_timestamp TIMESTAMP(0) WITH TIME ZONE;
    tipoTrigger VARCHAR2(255);
    data_operacao DATE;
    tipoOperacao VARCHAR2(255);
    parcelaID NUMBER(10);
    parcelaNome VARCHAR2(255);
    unidade VARCHAR2(255);

    quantidade FLOAT(10);
    fatorProducao VARCHAR2(255);

    descricao VARCHAR2(255);
BEGIN

     if inserting then
        tipoTrigger := 'INSERT';
else
        tipoTrigger := 'UPDATE';
end if;

    operacaoId := :new.operacaoid;
SELECT CURRENT_TIMESTAMP INTO log_timestamp FROM DUAL;
select datainicio into data_operacao from operacoes where operacoes.idoperacao = operacaoID;
tipoOperacao := 'Aplicação de Fator de Produção';
select parcelaid into parcelaID from operacoes where operacoes.idoperacao = operacaoID;
select nome into parcelaNome from parcelas where idParcela = parcelaid;
unidade := :new.unidademedida;

    quantidade := :new.quantidade;
    fatorProducao := :new.fatorprodid;

    descricao := 'Timestamp: ' || log_timestamp || '; Trigger: ' || tipoTrigger || '; Tipo de Operação: ' || tipoOperacao ||
    '; Data: ' || data_operacao || '; Parcela: ' || parcelaNome || '; Fator de Produção: ' || fatorProducao ||
    '; Quantidade: ' || quantidade || unidade;

INSERT INTO operacoes_logs(operacaoId, timestamp, descricao)
VALUES (operacaoID, log_timestamp, descricao);


END;




-- Log de Colheitas

create or replace NONEDITIONABLE TRIGGER "LOGCOLHEITATRIGGER"
AFTER INSERT OR UPDATE ON colheitas
                           FOR EACH ROW
DECLARE
operacaoID NUMBER;
    log_timestamp TIMESTAMP(0) WITH TIME ZONE;
    tipoTrigger VARCHAR2(255);
    data_operacao DATE;
    tipoOperacao VARCHAR2(255);
    parcelaID NUMBER(10);
    parcelaNome VARCHAR2(255);
    unidade VARCHAR2(255);
    quantidade FLOAT(10);

    planta_colhida VARCHAR2(255);

    descricao VARCHAR2(255);
BEGIN

     if inserting then
        tipoTrigger := 'INSERT';
else
        tipoTrigger := 'UPDATE';
end if;

    operacaoId := :new.operacaoid;
SELECT CURRENT_TIMESTAMP INTO log_timestamp FROM DUAL;
select datainicio into data_operacao from operacoes where operacoes.idoperacao = operacaoID;
tipoOperacao := 'Colheita';
select parcelaid into parcelaID from operacoes where operacoes.idoperacao = operacaoID;
select nome into parcelaNome from parcelas where idParcela = parcelaid;
unidade := :new.unidademedida;
    quantidade := :new.quantidade;

select nomeCompleto into planta_colhida from culturas
                                                 inner join cultivos on culturas.idcultura = cultivos.culturaid
                                                 inner join operacoes on cultivos.culturaid = operacoes.culturaid and cultivos.parcelaid = operacoes.parcelaid
where operacoes.idoperacao = operacaoID;

descricao := 'Timestamp: ' || log_timestamp || '; Trigger: ' || tipoTrigger || '; Tipo de Operação: ' || tipoOperacao ||
    '; Data: ' || data_operacao || '; Parcela: ' || parcelaNome || '; Planta Colhida: ' || planta_colhida ||
    '; Quantidade: ' || quantidade || unidade;

INSERT INTO operacoes_logs(operacaoId, timestamp, descricao)
VALUES (operacaoID, log_timestamp, descricao);


END;




-- Log de incorporações no solo

create or replace NONEDITIONABLE TRIGGER "LOGINCORP_SOLOTRIGGER"
AFTER INSERT OR UPDATE ON incorps_solo
                           FOR EACH ROW
DECLARE
operacaoID NUMBER;
    log_timestamp TIMESTAMP(0) WITH TIME ZONE;
    tipoTrigger VARCHAR2(255);
    data_operacao DATE;
    tipoOperacao VARCHAR2(255);
    parcelaID NUMBER(10);
    parcelaNome VARCHAR2(255);
    unidade VARCHAR2(255);
    quantidade FLOAT(10);

    planta_incorporada VARCHAR2(255);

    descricao VARCHAR2(255);
BEGIN

     if inserting then
        tipoTrigger := 'INSERT';
else
        tipoTrigger := 'UPDATE';
end if;

    operacaoId := :new.operacaoid;
SELECT CURRENT_TIMESTAMP INTO log_timestamp FROM DUAL;
select datainicio into data_operacao from operacoes where operacoes.idoperacao = operacaoID;
tipoOperacao := 'Incorporação no Solo';
select parcelaid into parcelaID from operacoes where operacoes.idoperacao = operacaoID;
select nome into parcelaNome from parcelas where idParcela = parcelaid;
unidade := :new.unidademedida;
    quantidade := :new.quantidade;

select nomeCompleto into planta_incorporada from culturas
                                                     inner join cultivos on culturas.idcultura = cultivos.culturaid
                                                     inner join operacoes on cultivos.culturaid = operacoes.culturaid and cultivos.parcelaid = operacoes.parcelaid
where operacoes.idoperacao = operacaoID;

descricao := 'Timestamp: ' || log_timestamp || '; Trigger: ' || tipoTrigger || '; Tipo de Operação: ' || tipoOperacao ||
    '; Data: ' || data_operacao || '; Parcela: ' || parcelaNome || '; Planta Incorporada: ' || planta_incorporada ||
    '; Quantidade: ' || quantidade || unidade;

INSERT INTO operacoes_logs(operacaoId, timestamp, descricao)
VALUES (operacaoID, log_timestamp, descricao);


END;



-- log de mobilizações de solo

create or replace NONEDITIONABLE TRIGGER "LOGMOBILIZACAO_SOLOTRIGGER"
AFTER INSERT OR UPDATE ON mobilizacoes_solo
                           FOR EACH ROW
DECLARE
operacaoID NUMBER;
    log_timestamp TIMESTAMP(0) WITH TIME ZONE;
    tipoTrigger VARCHAR2(255);
    data_operacao DATE;
    tipoOperacao VARCHAR2(255);
    parcelaID NUMBER(10);
    parcelaNome VARCHAR2(255);
    unidade VARCHAR2(255);
    quantidade FLOAT(10);

    descricao VARCHAR2(255);
BEGIN

     if inserting then
        tipoTrigger := 'INSERT';
else
        tipoTrigger := 'UPDATE';
end if;

    operacaoId := :new.operacaoid;
SELECT CURRENT_TIMESTAMP INTO log_timestamp FROM DUAL;
select datainicio into data_operacao from operacoes where operacoes.idoperacao = operacaoID;
tipoOperacao := 'Mobilização no solo';
select parcelaid into parcelaID from operacoes where operacoes.idoperacao = operacaoID;
select nome into parcelaNome from parcelas where idParcela = parcelaid;
unidade := :new.unidademedida;
    quantidade := :new.quantidade;

    descricao := 'Timestamp: ' || log_timestamp || '; Trigger: ' || tipoTrigger || '; Tipo de Operação: ' || tipoOperacao ||
    '; Data: ' || data_operacao || '; Parcela: ' || parcelaNome ||
    '; Quantidade: ' || quantidade || unidade;

INSERT INTO operacoes_logs(operacaoId, timestamp, descricao)
VALUES (operacaoID, log_timestamp, descricao);


END;



-- log de mondas

create or replace NONEDITIONABLE TRIGGER "LOGMONDATRIGGER"
AFTER INSERT OR UPDATE ON mondas
                           FOR EACH ROW
DECLARE
operacaoID NUMBER;
    log_timestamp TIMESTAMP(0) WITH TIME ZONE;
    tipoTrigger VARCHAR2(255);
    data_operacao DATE;
    tipoOperacao VARCHAR2(255);
    parcelaID NUMBER(10);
    parcelaNome VARCHAR2(255);
    unidade VARCHAR2(255);
    quantidade FLOAT(10);

    descricao VARCHAR2(255);
BEGIN

     if inserting then
        tipoTrigger := 'INSERT';
else
        tipoTrigger := 'UPDATE';
end if;

    operacaoId := :new.operacaoid;
SELECT CURRENT_TIMESTAMP INTO log_timestamp FROM DUAL;
select datainicio into data_operacao from operacoes where operacoes.idoperacao = operacaoID;
tipoOperacao := 'Monda';
select parcelaid into parcelaID from operacoes where operacoes.idoperacao = operacaoID;
select nome into parcelaNome from parcelas where idParcela = parcelaid;
unidade := :new.unidademedida;
    quantidade := :new.quantidade;

    descricao := 'Timestamp: ' || log_timestamp || '; Trigger: ' || tipoTrigger || '; Tipo de Operação: ' || tipoOperacao ||
    '; Data: ' || data_operacao || '; Parcela: ' || parcelaNome ||
    '; Quantidade: ' || quantidade || unidade;

INSERT INTO operacoes_logs(operacaoId, timestamp, descricao)
VALUES (operacaoID, log_timestamp, descricao);


END;




--log de plantações

create or replace NONEDITIONABLE TRIGGER "LOGPLANTACAOTRIGGER"
AFTER INSERT OR UPDATE ON plantacoes
                           FOR EACH ROW
DECLARE
operacaoID NUMBER;
    log_timestamp TIMESTAMP(0) WITH TIME ZONE;
    tipoTrigger VARCHAR2(255);
    data_operacao DATE;
    tipoOperacao VARCHAR2(255);
    parcelaID NUMBER(10);
    parcelaNome VARCHAR2(255);
    unidade VARCHAR2(255);
    quantidade FLOAT(10);

    planta VARCHAR2(255);

    descricao VARCHAR2(255);
BEGIN

     if inserting then
        tipoTrigger := 'INSERT';
else
        tipoTrigger := 'UPDATE';
end if;

    operacaoId := :new.operacaoid;
SELECT CURRENT_TIMESTAMP INTO log_timestamp FROM DUAL;
select datainicio into data_operacao from operacoes where operacoes.idoperacao = operacaoID;
tipoOperacao := 'Plantação';
select parcelaid into parcelaID from operacoes where operacoes.idoperacao = operacaoID;
select nome into parcelaNome from parcelas where idParcela = parcelaid;
unidade := :new.unidademedida;
    quantidade := :new.quantidade;

select nomeCompleto into planta from culturas
                                         inner join cultivos on culturas.idcultura = cultivos.culturaid
                                         inner join operacoes on cultivos.culturaid = operacoes.culturaid and cultivos.parcelaid = operacoes.parcelaid
where operacoes.idoperacao = operacaoID;

descricao := 'Timestamp: ' || log_timestamp || '; Trigger: ' || tipoTrigger || '; Tipo de Operação: ' || tipoOperacao ||
    '; Data: ' || data_operacao || '; Parcela: ' || parcelaNome || '; Planta: ' || planta ||
    '; Quantidade: ' || quantidade || unidade;

INSERT INTO operacoes_logs(operacaoId, timestamp, descricao)
VALUES (operacaoID, log_timestamp, descricao);


END;




--log de regas

create or replace NONEDITIONABLE TRIGGER "LOGREGATRIGGER"
AFTER INSERT OR UPDATE ON regas
                           FOR EACH ROW
DECLARE
operacaoID NUMBER;
    log_timestamp TIMESTAMP(0) WITH TIME ZONE;
    tipoTrigger VARCHAR2(255);
    data_operacao DATE;
    tipoOperacao VARCHAR2(255);
    parcelaID NUMBER(10);
    parcelaNome VARCHAR2(255);
    unidade VARCHAR2(255);
    quantidade FLOAT(10);

    setor NUMBER(10);

    descricao VARCHAR2(255);
BEGIN

     if inserting then
        tipoTrigger := 'INSERT';
else
        tipoTrigger := 'UPDATE';
end if;

    operacaoId := :new.operacaoid;
SELECT CURRENT_TIMESTAMP INTO log_timestamp FROM DUAL;
select datainicio into data_operacao from operacoes where operacoes.idoperacao = operacaoID;
tipoOperacao := 'Rega';
select parcelaid into parcelaID from operacoes where operacoes.idoperacao = operacaoID;
select nome into parcelaNome from parcelas where idParcela = parcelaid;
unidade := :new.unidademedida;
    quantidade := :new.quantidade;

    setor := :new.setor;

    descricao := 'Timestamp: ' || log_timestamp || '; Trigger: ' || tipoTrigger || '; Tipo de Operação: ' || tipoOperacao ||
    '; Data: ' || data_operacao || '; Parcela: ' || parcelaNome || '; Nº Setor: ' || setor ||
    '; Quantidade: ' || quantidade || unidade;

INSERT INTO operacoes_logs(operacaoId, timestamp, descricao)
VALUES (operacaoID, log_timestamp, descricao);


END;




--log de sementeiras

create or replace NONEDITIONABLE TRIGGER "LOGSEMENTEIRATRIGGER"
AFTER INSERT OR UPDATE ON sementeiras
                           FOR EACH ROW
DECLARE
operacaoID NUMBER;
    log_timestamp TIMESTAMP(0) WITH TIME ZONE;
    tipoTrigger VARCHAR2(255);
    data_operacao DATE;
    tipoOperacao VARCHAR2(255);
    parcelaID NUMBER(10);
    parcelaNome VARCHAR2(255);
    unidade VARCHAR2(255);
    quantidade FLOAT(10);

    planta VARCHAR2(255);

    descricao VARCHAR2(255);
BEGIN

     if inserting then
        tipoTrigger := 'INSERT';
else
        tipoTrigger := 'UPDATE';
end if;

    operacaoId := :new.operacaoid;
SELECT CURRENT_TIMESTAMP INTO log_timestamp FROM DUAL;
select datainicio into data_operacao from operacoes where operacoes.idoperacao = operacaoID;
tipoOperacao := 'Sementeira';
select parcelaid into parcelaID from operacoes where operacoes.idoperacao = operacaoID;
select nome into parcelaNome from parcelas where idParcela = parcelaid;
unidade := :new.unidademedida;
    quantidade := :new.quantidade;

select nomeCompleto into planta from culturas
                                         inner join cultivos on culturas.idcultura = cultivos.culturaid
                                         inner join operacoes on cultivos.culturaid = operacoes.culturaid and cultivos.parcelaid = operacoes.parcelaid
where operacoes.idoperacao = operacaoID;

descricao := 'Timestamp: ' || log_timestamp || '; Trigger: ' || tipoTrigger || '; Tipo de Operação: ' || tipoOperacao ||
    '; Data: ' || data_operacao || '; Parcela: ' || parcelaNome || '; Planta: ' || planta ||
    '; Quantidade: ' || quantidade || unidade;

INSERT INTO operacoes_logs(operacaoId, timestamp, descricao)
VALUES (operacaoID, log_timestamp, descricao);


END;

