------------
------------
-- SUPPORT FUNCTIONS
------------

-- Funtion to get OPERACAO max id
create or replace function getOperacoesMaxId return number
is
    maxId number;
begin
    select max(idoperacao) into maxId
    from operacoes;
    return maxId;
exception
    when others then
    dbms_output.put_line('Error: ' || SQLERRM);
    return 0;
end getOperacoesMaxId;
/

-- Function to check unidade medida
create or replace function func_Check_UnMedida(p_unMedida Unidades_Medida.idUnidadeMedida%Type) return boolean is
    result_Count int;
BEGIN
    select count(*) into result_Count
    from Unidades_Medida where idUnidadeMedida=p_unMedida;
    return result_Count>0;
END func_Check_UnMedida;
/

-- Function to check quinta
create or replace function func_check_Quinta(p_id_Quinta number) return boolean is
    result_Count int;
BEGIN
select count(*)
into result_Count
from Quintas where IDQUINTA=p_id_Quinta;
return result_Count>0;
END func_check_Quinta;
/

-- Function to check parcela
create or replace function func_Check_Parcela(p_id_Parcela number, p_area_Parcela number) return boolean
is
result_Count int;
begin
select count(*)
into result_Count
from Parcelas where IDPARCELA=p_id_Parcela and area>=p_area_Parcela;
return result_Count>0;
End func_Check_Parcela;
/

-- Function to check operador
create or replace function func_Check_Operador(p_id_operador number) return boolean
is
result_Count int;
begin
select count(*)
into result_Count
from Operadores where IDOPERADOR=p_id_operador;
return result_Count>0;
End func_Check_Operador;
/

-- Função para obter a lista de Receitas disponíveis.
CREATE OR REPLACE FUNCTION getListaReceitas RETURN SYS_REFCURSOR
IS
    receitas_cursor SYS_REFCURSOR;
BEGIN
    OPEN receitas_cursor FOR
        SELECT *
        FROM receitas;
    RETURN receitas_cursor;
END getListaReceitas;
/

-- Função para obter a lista de Fatores de Produção disponíveis.
CREATE OR REPLACE FUNCTION getListaFatProd RETURN SYS_REFCURSOR
IS
    fatprod_cursor SYS_REFCURSOR;
BEGIN
    OPEN fatprod_cursor FOR
        SELECT
            fatores_producao.idfatorproducao,
            fatores_producao.fabricante,
            formulacoes.formato,
            formulacoes.tipo,
            formulacoes.aplicacao
        FROM fatores_producao
        LEFT JOIN formulacoes ON fatores_producao.formulacaoid = formulacoes.idformulacao;
    RETURN fatprod_cursor;
END getListaFatProd;
/

-- Função para verificar a existência do par ReceitaID e FatProd
CREATE OR REPLACE FUNCTION checkExistingMixIngredient (
    app_idReceita NUMBER,
    app_fatorProdId VARCHAR
) RETURN BOOLEAN
IS
    temp_id NUMBER;
BEGIN
    SELECT receitas.receitaId INTO temp_id
        FROM receitas
        WHERE receitas.receitaId = app_idReceita AND receitas.fatorprodid = app_fatorProdId;
    RETURN true;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
        RETURN false;
END checkExistingMixIngredient;
/

-- Função para verificar a existência do ReceitaID
CREATE OR REPLACE FUNCTION checkExistingMixId (
    app_idReceita NUMBER
) RETURN BOOLEAN
IS
    temp_id NUMBER;
BEGIN
    SELECT idReceita INTO temp_id
        FROM mix
        WHERE mix.idReceita = app_idReceita;
    RETURN true;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
        RETURN false;
END checkExistingMixId;
/

-- Função para obter lista de aplicações
create or replace NONEDITIONABLE FUNCTION getAplicacoes
RETURN SYS_REFCURSOR AS 
    aplicacoesTable SYS_REFCURSOR;
BEGIN
    OPEN aplicacoesTable FOR
        SELECT * FROM aplicacoes_fatprod;
        RETURN aplicacoesTable;
 
END getAplicacoes;
/
 
-- Função para obter a lista de logs
create or replace NONEDITIONABLE FUNCTION getLogs
RETURN SYS_REFCURSOR AS 
    logsTable SYS_REFCURSOR;
BEGIN
    OPEN logsTable FOR
        SELECT * FROM operacoes_logs;
        RETURN logsTable;
 
END getLogs;
/

------------
------------
-- SPRINT 2
------------

/* US BD11 Como Gestor Agrícola, quero registar uma operação de semeadura */
create or replace function FUNC_REGISTAR_SEMEADURA(p_id_Quinta in number,parcela in varchar2,cultura in varchar2,p_id_Operador in number, p_dataInicio in date,p_dataFim in date,p_qtd in number, p_unMedidaSemeadura Sementeiras.UNIDADEMEDIDA%Type, p_AreaParcela in number, p_unidadeMedidaParcela Parcelas.UnidadeMedida%Type)
return boolean
is
   outResult boolean := false;
   resultCheck boolean := true;
   quintaException EXCEPTION;
   parcelaException EXCEPTION;
   operadorException EXCEPTION;
   unMedidaException EXCEPTION;
   p_id_Parcela number;
   p_id_Cultura number;
begin
    SELECT idParcela INTO p_id_Parcela FROM Parcelas where nome=parcela;
    SELECT idCultura into p_id_Cultura from Culturas where nomecompleto=cultura;
    if not func_check_Quinta(p_id_Quinta) then
        resultCheck := false;
        raise quintaException;
    end if;
    if not func_Check_Parcela(p_id_Parcela,p_AreaParcela) then
        resultCheck := false;
        raise parcelaException;
    end if;
    if not func_Check_Operador(p_id_Operador) then
        resultCheck := false;
        raise operadorException;
    end if;
    if not func_Check_UnMedida(p_unMedidaSemeadura) then
        resultCheck := false;
        raise unMedidaException;
    end if;
    if resultCheck then
        insert into Cultivos(quintaID,parcelaid,culturaid,datainicio,quantidade,unidademedida)
        values(p_id_Quinta,p_id_Parcela,p_id_Cultura,p_dataInicio,p_qtd,p_unMedidaSemeadura);
        insert into OPERACOES (idOperacao,QUINTAID,CULTuraID,PARCELAID,OPERADORID,DATAINICIO,DATAFIM)
        select max(idOperacao)+1, p_id_Quinta,p_id_Cultura,p_id_Parcela,p_id_Operador,p_dataInicio,p_dataFim from OPERACOES;
        insert into Sementeiras(OperacaoID,quantidade,UnidadeMedida)
        select max(idOperacao), p_qtd,p_unMedidaSemeadura from OPERACOES;
        COMMIT;
        outResult := true;
    end if;
    if outResult then
      dbms_output.put_line('Operação registada com sucesso!');
      else
      dbms_output.put_line('Operação não registada!');
    end if;
    return outResult;
exception
    when quintaException then
        dbms_output.put_line('Quinta não existente');
        rollback;
        return outResult;
    when operadorException then
        dbms_output.put_line('Operador inválido');
        rollback;
        return outResult;
    when parcelaException then
        dbms_output.put_line('Parcela inválida ou com área superior à atual');
        rollback;
        return outResult;
    when unMedidaException then
        dbms_output.put_line('Unidade de medida inválida');
        rollback;
        return outResult;
    when others then
        rollback;
        dbms_output.put_line('Erro:' || sqlerrm);
        return outResult;
END FUNC_REGISTAR_SEMEADURA;
/

/* USBD12 Como Gestor Agrícola, quero registar uma operação de monda. */

-- Function to get the list of Mondas
create or replace FUNCTION getMondas RETURN SYS_REFCURSOR IS
    mondas_cursor SYS_REFCURSOR;
BEGIN
    OPEN mondas_cursor FOR
        SELECT * FROM MONDAS;
    RETURN mondas_cursor;
END getMondas;
/

-- Function to get the list of Cultivos for Mondas
create or replace NONEDITIONABLE function getCultivosForMondasData return SYS_REFCURSOR
is
    v_cursor SYS_REFCURSOR;
begin
    open v_cursor for
        select
            cultivos.parcelaid,
            parcelas.nome,
            cultivos.culturaid,
            culturas.nomecompleto,
            produtos.produto
        from cultivos
        left join parcelas on cultivos.parcelaid = parcelas.idparcela
        left join culturas on cultivos.culturaid = culturas.idcultura
        left join produtos on culturas.idcultura = produtos.culturaid;
    return v_cursor;
end getCultivosForMondasData;
/

-- Function to insert data into MONDAS AND OPERACOES
CREATE OR REPLACE FUNCTION insertMondas (
    quinta_id NUMBER,
    parcela_id NUMBER,
    cultura_id NUMBER,
    operador_id NUMBER,
    data_inicio VARCHAR2,
    quantidade NUMBER,
    unidade VARCHAR2,
    produto_atual VARCHAR2
) RETURN NUMBER
IS
    opid NUMBER;
    parcelaID NUMBER;
    parcela_valid BOOLEAN;
    area_parcela NUMBER;
BEGIN
    -- Check if the Parcela is valid using the func_Check_Parcela function
    SELECT area INTO area_parcela FROM Parcelas WHERE idParcela = parcela_id;
    parcela_valid := func_Check_Parcela(parcela_id, area_parcela);

    IF NOT parcela_valid THEN
        -- If the Parcela is not valid, return failure status
        DBMS_OUTPUT.PUT_LINE('Error: Parcela is not valid.');
        RETURN 0;
    END IF;

    -- Get the next operation ID
    opid := (getOperacoesMaxId + 1);

    BEGIN
        -- Insert data into the table
        INSERT INTO OPERACOES(idOperacao, QUINTAID, PARCELAID, CULTURAID, OPERADORID, dataInicio)
            VALUES (opid, quinta_id, parcela_id, cultura_id, 0, TO_DATE(data_inicio, 'YYYY-MM-DD'));

        INSERT INTO MONDAS(OPERACAOID, quantidade, UNIDADEMEDIDA)
            VALUES (opid, quantidade, unidade);

        COMMIT;
        RETURN 1; -- Return success status
    EXCEPTION
        -- Rollback the transaction if an exception occurs
        WHEN OTHERS THEN
            ROLLBACK;
            -- Log or handle the exception
            DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
            RETURN 0; -- Return failure status
    END;
END insertMondas;
/

/* USBD13 Como Gestor Agrícola, quero registar uma operação de colheita. */

-- Function to get the list of CULTIVOS
create or replace function getCultivosData return SYS_REFCURSOR
is
    v_cursor SYS_REFCURSOR;
begin
    open v_cursor for
        select
            cultivos.parcelaid,
            parcelas.nome,
            cultivos.culturaid,
            culturas.nomecompleto,
            produtos.produto
        from cultivos
        left join parcelas on cultivos.parcelaid = parcelas.idparcela
        left join culturas on cultivos.culturaid = culturas.idcultura
        left join produtos on culturas.idcultura = produtos.culturaid
        where cultivos.datafim is null
            and cultivos.culturaid <> 0;
    return v_cursor;
end getCultivosData;
/

-- Function to get PRODUTO quantidade
create or replace function getProdutoQuantidade (
    idcultura number
)
return number
is
    quantidade_ver number := 0;
begin
    select produtos.quantidade into quantidade_ver
    from produtos
    where produtos.culturaid = idcultura;
    if quantidade_ver is null then quantidade_ver := 0; end if;
    return quantidade_ver;
exception
    when others then
    dbms_output.put_line('Error: ' || SQLERRM);
    return 0;
end getProdutoQuantidade;
/

-- Function to insert data into COLHEITAS, OPERACOES and update PRODUTOS
CREATE OR REPLACE FUNCTION registerColheita (
    quinta_id number,
    parcela_id number,
    cultura_id number,
    operador_id number,
    data_inicio varchar2,
    quantidade_atual number,
    unidade varchar2,
    produto_atual varchar2
) RETURN NUMBER
IS
    opid number;
    quantidade_nova number;
begin
    opid := (getOperacoesMaxId + 1);
    quantidade_nova := (getProdutoQuantidade(cultura_id) + quantidade_atual);
    begin
        -- Insert data into the table
        INSERT INTO OPERACOES(idOperacao, QUINTAID, PARCELAID, CULTURAID, OPERADORID, dataInicio)
            VALUES (opid, quinta_id, parcela_id, cultura_id, 0, TO_DATE(data_inicio, 'YYYY-MM-DD'));
        INSERT INTO COLHEITAS(OPERACAOID, quantidade, UNIDADEMEDIDA)
            VALUES (opid, quantidade_atual, unidade);
        UPDATE PRODUTOS
            SET quantidade = quantidade_nova, UNIDADEMEDIDA = unidade
            WHERE CULTURAID = cultura_id AND produto = produto_atual;
        COMMIT; -- Commit the transaction if successful
        RETURN 1; -- Return success status
    EXCEPTION
        -- Rollback the transaction if an exception occurs
        WHEN OTHERS THEN
            ROLLBACK;
            -- Log or handle the exception
            DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
            RETURN 0; -- Return failure status
    END;
END registerColheita;
/

/* USBD14 Registar aplicação de fator produção. */
create or replace NONEDITIONABLE FUNCTION registarAplicacao(quintaID number, parcelaNome varchar2, culturaID number, operadorID number, dataInicio date, fatorProdID varchar2, quantidade number, unidadeMedida varchar2, area float)
RETURN NUMBER IS
    worked number := 1;
    idOperacao number;
    parcelaID number;
    correctArea boolean := true;
BEGIN
    idOperacao := getOperacoesMaxId + 1;
    SELECT idparcela INTO parcelaID FROM Parcelas WHERE parcelaNome = parcelas.nome;
    correctArea := func_check_parcela(parcelaID, area);
    IF correctArea THEN
        INSERT INTO OPERACOES(IDOPERACAO, QUINTAID, PARCELAID, CULTURAID, OPERADORID, DATAINICIO, DATAFIM) values (idOperacao, quintaID, parcelaID, culturaID, operadorID, dataInicio, null);
        INSERT INTO APLICACOES_FATPROD(OPERACAOID, FATORPRODID, QUANTIDADE, UNIDADEMEDIDA) values (idOperacao, fatorProdID, quantidade, unidadeMedida);
        COMMIT;
    ELSE
        worked := -2;
    END IF;
    RETURN worked;
EXCEPTION
    when others then
        rollback;
        worked := 0;
    return worked;
END registarAplicacao;
/

/** US BD15 Como Gestor Agrícola, quero registar uma operação de poda */
CREATE OR REPLACE FUNCTION insert_poda(
    p_idoperacao operacoes.idoperacao%TYPE,
    p_quantidade NUMBER,
    p_unidade VARCHAR2
) RETURN VARCHAR2
AS
BEGIN
    -- Insert into PODAS table
    INSERT INTO PODAS VALUES(p_idoperacao, p_quantidade, p_unidade);
    -- Return success message
    RETURN 'Insert successful';
EXCEPTION
    WHEN OTHERS THEN
        -- Handle exceptions
        RETURN 'Error: ' || SQLCODE || ' - ' || SQLERRM;
END insert_poda;
/

/* US BD16 Como Gestor Agrícola, pretendo obter a lista dos produtos colhidos numa dada parcela, para cada espécie, num dado intervalo de tempo. */
Create or replace function get_ListaProdColhidos(p_nome_Parcela in varchar2,p_dataInicio in date,p_dataFim in date)
return sys_refcursor
as listaProdColhidos sys_refcursor;
idOperacao number;
   produto  varchar2(255);
   especie varchar2(255);
   parcela  varchar2(255);
   dataOperacao date;
   produto_anterior VARCHAR2(255) := NULL;
   especie_anterior VARCHAR2(255) := NULL;
   primeira_vez BOOLEAN := TRUE;
begin
open listaProdColhidos for
select  e.especie, c.NomeCompleto,par.Nome as Parcela
from produtos p
left join culturas c on p.culturaid=c.idcultura
left join especies_vegetais e on c.especieVegetalID=e.idespecievegetal
left join operacoes o on c.IdCultura=o.culturaID
left join colheitas colhe on o.idoperacao=colhe.operacaoid
left join parcelas par on o.parcelaid=par.idparcela
left join cultivos ON o.parcelaid = cultivos.parcelaid AND o.culturaid = cultivos.culturaid
where o.dataInicio between p_dataInicio and p_dataFim and par.Nome=p_nome_Parcela
group by e.especie, c.NomeCompleto,par.Nome;

dbms_output.put_line('Parcela = ' || p_nome_Parcela);
dbms_output.put_line('Intervalo = entre ' || TO_CHAR(p_dataInicio, 'DD/MM/YYYY') || ' e ' || TO_CHAR(p_dataFim, 'DD/MM/YYYY'));
dbms_output.put_line('Resultado:');
LOOP
        FETCH listaProdColhidos INTO especie, produto, parcela;
        EXIT WHEN listaProdColhidos%NOTFOUND;

        IF primeira_vez OR especie_anterior != especie THEN
            dbms_output.put_line('    ' || especie);
            especie_anterior := especie;
            primeira_vez := FALSE;
        END IF;

        dbms_output.put_line('        ' || produto );
    END LOOP;
return listaProdColhidos;
end;
/


------------
------------
-- SPRINT 3
------------

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
/

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
/

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
/

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
/

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
/

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
/

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
/

--log de sementeiras
create or replace NONEDITIONABLE TRIGGER "LOGSEMENTEIRATRIGGER" AFTER INSERT OR UPDATE ON sementeiras
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
/

--log das fertirregas
create or replace NONEDITIONABLE TRIGGER LOGFERTIRREGATRIGGER
    AFTER INSERT OR UPDATE ON Fertirregas
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
    quantidade NUMBER(10);

    setor NUMBER(10);
    receita number(10);

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
tipoOperacao := 'Fertirrega';
select parcelaid into parcelaID from operacoes where operacoes.idoperacao = operacaoID;
select nome into parcelaNome from parcelas where idParcela = parcelaid;
unidade := :new.unidademedida;
    quantidade := :new.quantidade;

    setor := :new.setor;
    receita := :new.receitaID;

    descricao := 'Timestamp: ' || log_timestamp || '; Trigger: ' || tipoTrigger || '; Tipo de Operação: ' || tipoOperacao ||
    '; Data: ' || data_operacao || '; Parcela: ' || parcelaNome || '; Nº Setor: ' || setor || '; Receita ID: ' || receita ||
    '; Quantidade: ' || quantidade || unidade;


INSERT INTO operacoes_logs(operacaoId, timestamp, descricao)
VALUES (operacaoID, log_timestamp, descricao);


END;
/

/* USBD27 Como Gestor Agrícola, pretendo que não seja possível alterar ou apagar os logs. */
CREATE OR REPLACE TRIGGER Negar_Alteracoes_Logs
  BEFORE DELETE OR UPDATE ON OPERACOES_LOGS
BEGIN
    raise_application_error(-20000,'A seguinte ação não é permitida!');
END;
/

/* USBD28 Como Gestor Agrícola, pretendo que não seja possível apagar operações, mas deve ser possível anular uma operação, ficando isso registado na BD. */
CREATE OR REPLACE TRIGGER Negar_Delete_Operacoes
  BEFORE DELETE ON Operacoes
BEGIN
    raise_application_error(-20000,'A seguinte ação não é permitida!');
END;
/

/* USBD30 Como Gestor Agrícola, pretendo anular uma operação que estava prevista e não se realizou ou que foi criada por engano, sabendo que isso só é possível até aos 3 dias seguintes à sua data prevista de execução */
Create or replace function Anular_Operacao(p_id_Operacao in number)
return boolean
is
    outResult boolean := false;
    existeLinha number;
    dataOperacao date;
    excecaoForaData EXCEPTION;
    excecaoNExisteLinha EXCEPTION;
begin
    select count(*) into existeLinha from OPERACOES where idoperacao = p_id_Operacao and Anulado=0;

    if existeLinha = 1 then
        SELECT DataInicio INTO dataOperacao FROM OPERACOES WHERE idoperacao = p_id_Operacao;
        if dataOperacao+3>=sysdate then
            update Operacoes set Anulado=1 where IDOperacao=p_id_Operacao;
            insert into OPERACOES_LOGS(idlog,operacaoid,timestamp, descricao)
            select max(idlog)+1,p_id_Operacao,CURRENT_TIMESTAMP,'Operação anulada' from OPERACOES_LOGS;
            Commit;
            outResult := true;
        else
            raise excecaoForaData;
        end if;
    else
        raise excecaoNExisteLinha;
    end if;
    return outResult;
exception
    when excecaoForaData then
        raise_application_error(-20000,'Data fora da janela de anulação!');
        return outResult;
    when excecaoNExisteLinha then
        raise_application_error(-20000,'Essa operação não existe!');
        return outResult;
    when others then
        raise_application_error(-20000,' '|| SQLERRM);
        rollback;
end;
/

/* USBD31 Como Gestor Agrícola, pretendo registar uma receita de fertirrega para usar em operações de rega. */
-- Função para adicionar um novo mixId à tabela MIX
CREATE OR REPLACE FUNCTION createMix(
    app_idReceita NUMBER
) RETURN BOOLEAN
IS
BEGIN
    INSERT INTO mix(idReceita)
        VALUES (app_idReceita);
    RETURN true;
    EXCEPTION
        WHEN OTHERS THEN
            RETURN false;
END createMix;
/

-- Função para registar uma Receita.
CREATE OR REPLACE FUNCTION registarReceita (
    app_idReceita NUMBER,
    app_fatorProdId VARCHAR,
    app_quantidadePorHectar NUMBER,
    app_unidadeMedida VARCHAR
) RETURN NUMBER
IS
    resultCheck BOOLEAN := true;
    invalidoCodigoReceitaException EXCEPTION;
    codigoReceitaExcetion EXCEPTION;
BEGIN
    BEGIN
        -- Validation phase:
        IF (app_idReceita <= 0) THEN
            RAISE invalidoCodigoReceitaException;
        END IF;
        resultCheck := checkExistingMixIngredient(app_idReceita, app_fatorProdId);
        IF (resultCheck) THEN
            RAISE codigoReceitaExcetion;
        END IF;
        -- Insertion phase:
        resultCheck := checkExistingMixId(app_idReceita);
        IF (NOT resultCheck) THEN
            resultCheck := createMix(app_idReceita);
        END IF;
        INSERT INTO receitas(RECEITAID, FATORPRODID, quantidadePorHectar, UNIDADEMEDIDA)
            VALUES (app_idReceita, app_fatorProdId, app_quantidadePorHectar, app_unidadeMedida);
        COMMIT; -- Commit the transaction if successful
        RETURN 1; -- Return success status
        --END IF;
    EXCEPTION
        WHEN invalidoCodigoReceitaException THEN
            ROLLBACK; -- Rollback the transaction if an exception occurs
            DBMS_OUTPUT.PUT_LINE('Error: O ID de Receita introduzido é inválido.'); -- Print the exception
            RETURN 0; -- Return failure status for invalid idReceita
        WHEN codigoReceitaExcetion THEN
            ROLLBACK; -- Rollback the transaction if an exception occurs
            DBMS_OUTPUT.PUT_LINE('Error: O ID de Receita introduzido já existe.'); -- Print the exception
            RETURN 0; -- Return failure status for existing idReceita
        WHEN OTHERS THEN
            ROLLBACK; -- Rollback the transaction if an exception occurs
            DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM); -- Print the exception
            RETURN 0; -- Return failure status for DB error
    END;
END registarReceita;
/

/* USBD32 Como Gestor Agrícola, pretendo registar uma operação de rega, incluindo a componente de fertirrega (se aplicável). */
-- Insert Fertirregas
create or replace NONEDITIONABLE FUNCTION insertFertirregas (
    quinta_id NUMBER,
    parcela_id NUMBER,
    cultura_id NUMBER,
    operador_id NUMBER,
    data_inicio VARCHAR2,
    quantidade_atual NUMBER,
    unidade VARCHAR2,
    SETORID NUMBER,
    RECEITAID NUMBER,
    hora_inicio VARCHAR
) RETURN NUMBER
IS
    opid NUMBER;
    quantidade_nova NUMBER;
    parcelaID NUMBER;
    parcela_valid BOOLEAN;
    area_parcela NUMBER;
BEGIN
    -- Get the next operation ID
    opid := (getOperacoesMaxId + 1);

    BEGIN
        -- Insert data into the table
        INSERT INTO OPERACOES(idOperacao, QUINTAID, PARCELAID, CULTURAID, OPERADORID, dataInicio)
            VALUES (opid, quinta_id, parcela_id, cultura_id, 0, TO_DATE(data_inicio, 'YYYY-MM-DD'));

        INSERT INTO FERTIRREGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, RECEITAID, horainicio)
            VALUES (opid, quantidade_atual, unidade, SETORID, RECEITAID, hora_inicio);


        COMMIT; -- Commit the transaction if successful
        RETURN 1; -- Return success status
    EXCEPTION
        -- Rollback the transaction if an exception occurs
        WHEN OTHERS THEN
            ROLLBACK;
            -- Log or handle the exception
            DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
            RETURN 0; -- Return failure status
    END;
END insertFertirregas;
/

-- Insert Regas
create or replace NONEDITIONABLE FUNCTION insertRegas (
    quinta_id NUMBER,
    parcela_id NUMBER,
    cultura_id NUMBER,
    operador_id NUMBER,
    data_inicio VARCHAR2,
    quantidade_atual NUMBER,
    unidade VARCHAR2,
    SETORID NUMBER,
    hora_inicio VARCHAR
) RETURN NUMBER
IS
    opid NUMBER;
    quantidade_nova NUMBER;
    parcelaID NUMBER;
    parcela_valid BOOLEAN;
    area_parcela NUMBER;
BEGIN
    -- Get the next operation ID
    opid := (getOperacoesMaxId + 1);

    BEGIN
        -- Insert data into the table
        INSERT INTO OPERACOES(idOperacao, QUINTAID, PARCELAID, CULTURAID, OPERADORID, dataInicio)
            VALUES (opid, quinta_id, parcela_id, cultura_id, 0, TO_DATE(data_inicio, 'YYYY-MM-DD'));

        INSERT INTO REGAS(OPERACAOID, quantidade, UNIDADEMEDIDA, SETOR, horainicio)
            VALUES (opid, quantidade_atual, unidade, SETORID, hora_inicio);

        COMMIT; -- Commit the transaction if successful
        RETURN 1; -- Return success status
    EXCEPTION
        -- Rollback the transaction if an exception occurs
        WHEN OTHERS THEN
            ROLLBACK;
            -- Log or handle the exception
            DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
            RETURN 0; -- Return failure status
    END;
END insertRegas;
/

/* USBD33 Como Gestor Agrícola, pretendo obter a lista das culturas com maior consumo de água (rega) para um dado ano civil. O consumo é em minutos e, em caso de empate, devem ser dadas todas essas culturas. */
create or replace NONEDITIONABLE FUNCTION GetTopRankedOperations(
    start_date DATE,
    end_date DATE,
    unit_of_measure VARCHAR2
) RETURN SYS_REFCURSOR IS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
    SELECT
        PARCELAID,
        PARCELANAME,
        CULTURAID,
        CULTURANAME,
        dataInicio,
        OperationType,
        quantidade,
        UNIDADEMEDIDA
    FROM
        (
            SELECT
                OD.*,
                P.nome AS PARCELANAME,
                C.CULTURAID,
                CU.nome AS CULTURANAME,
                DENSE_RANK() OVER (ORDER BY OD.quantidade DESC) AS rnk
            FROM
                (
                    SELECT
                        O.idOperacao,
                        O.dataInicio,
                        O.dataFim,
                        O.PARCELAID,
                        R.quantidade,
                        R.UNIDADEMEDIDA,
                        'REGAS' AS OperationType
                    FROM
                        OPERACOES O
                        JOIN REGAS R ON O.idOperacao = R.OPERACAOID
                    UNION
                    SELECT
                        O.idOperacao,
                        O.dataInicio,
                        O.dataFim,
                        O.PARCELAID,
                        F.quantidade,
                        F.UNIDADEMEDIDA,
                        'FERTIRREGAS' AS OperationType
                    FROM
                        OPERACOES O
                        JOIN FERTIRREGAS F ON O.idOperacao = F.OPERACAOID
                ) OD
                JOIN PARCELAS P ON OD.PARCELAID = P.idParcela
                JOIN CULTIVOS C ON OD.PARCELAID = C.PARCELAID
                JOIN CULTURAS CU ON C.CULTURAID = CU.idCultura
            WHERE
                OD.dataInicio BETWEEN start_date AND end_date
                AND OD.UNIDADEMEDIDA = unit_of_measure
        )
    WHERE
        rnk = 1
    ORDER BY
        quantidade DESC;

    RETURN result_cursor;
END;
/

/* USBD34 Como Gestor Agrícola, pretendo obter a lista das substâncias de fatores de produção usadas noutros anos civis,mas não usadas no ano civil indicado. */
-- Criar a stored procedure
CREATE OR REPLACE PROCEDURE ObterSubstanciasNaoUsadasNoAno(anoParam IN NUMBER) IS
BEGIN
    FOR substancia_rec IN (
        SELECT DISTINCT s.SUBSTANCIA
        FROM SUBSTANCIAS s
        JOIN COMPONENTES c ON c.substanciaid = s.idsubstancia
        JOIN FICHAS_TECNICAS ft ON ft.idfichatecnica = c.fichatecnicaid
        JOIN fatores_producao fp ON fp.fichatecnicaid = ft.idfichatecnica
        JOIN aplicacoes_fatprod afp ON fp.idfatorproducao = afp.fatorprodid
        JOIN operacoes o ON o.idoperacao = afp.operacaoid
        WHERE s.SUBSTANCIA NOT IN (
            SELECT DISTINCT s2.SUBSTANCIA
            FROM SUBSTANCIAS s2
            JOIN COMPONENTES c2 ON c2.substanciaid = s2.idsubstancia
            JOIN FICHAS_TECNICAS ft2 ON ft2.idfichatecnica = c2.fichatecnicaid
            JOIN fatores_producao fp2 ON fp2.fichatecnicaid = ft2.idfichatecnica
            JOIN aplicacoes_fatprod afp2 ON fp2.idfatorproducao = afp2.fatorprodid
            JOIN operacoes o2 ON o2.idoperacao = afp2.operacaoid
            WHERE EXTRACT(YEAR FROM o2.datainicio) = anoParam
        )
        ORDER BY s.SUBSTANCIA
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(substancia_rec.SUBSTANCIA);
    END LOOP;
END ObterSubstanciasNaoUsadasNoAno;
/


------------
------------
-- LAPR 3
------------

create or replace  function FUNC_REGISTAR_FERTIREGA(p_id_Quinta in number,p_id_Parcela in number, p_id_Operador in number,p_quantidade in number,p_unMedida in fertirregas.unidadeMedida%Type,p_Setor in number, p_receitaID in number, p_horaInicio in Fertirregas.HORAINICIO%Type, p_dataInicio in date)
return varchar2
is
   outResult boolean := false;
   parcela number(10);
   parcelaName varchar2(255);
begin
        select distinct parcelaid into parcela from cultivos where cultivos.setorregaid = p_Setor;
        select nome into parcelaName from parcelas where parcelas.idparcela = parcela;

        insert into OPERACOES (idOperacao,QUINTAID,PARCELAID,OPERADORID,DATAINICIO)
        values ((Select max(idOperacao)+1 from OPERACOES),p_id_Quinta,parcela,p_id_Operador,p_dataInicio);

        insert into fertirregas(OperacaoID,quantidade,unidadeMedida,Setor,ReceitaId,HoraInicio)
        values((Select max(idOperacao) from OPERACOES),p_quantidade,p_unMedida,p_Setor,p_receitaID,p_horaInicio);

        outResult := true;

    if outResult then
        dbms_output.put_line('Operação registada com sucesso!');
    else
        dbms_output.put_line('Operação não registada!');
    end if;
    return parcelaName;
exception
    when others then
        dbms_output.put_line('Erro:' || sqlerrm);
        return '';
end;
/

create or replace  function FUNC_REGISTAR_REGA(p_id_Quinta in number,p_id_Parcela in varchar2,p_id_Operador in number, p_dataInicio in date,p_qtd in number, p_unMedida Regas.UNIDADEMEDIDA%Type, p_Setor in number, p_horaInicio in Regas.HoraInicio%Type)
return varchar2
is
   outResult boolean := false;
   resultCheck boolean := true;
   idParcela number;
   parcela number(10);
   parcelaName varchar2(255);
begin

        select distinct parcelaid into parcela from cultivos where cultivos.setorregaid = p_Setor;
        select nome into parcelaName from parcelas where parcelas.idparcela = parcela;

        insert into OPERACOES (idOperacao,QUINTAID,PARCELAID,OPERADORID,DATAINICIO)
        values ((Select max(idOperacao)+1 from OPERACOES),p_id_Quinta,parcela,p_id_Operador,p_dataInicio);

        insert into regas(OperacaoID,quantidade,unidadeMedida,Setor,HoraInicio)
        values((Select max(idOperacao) from OPERACOES),p_qtd,p_unMedida,p_Setor,p_horaInicio);

    outResult := true;

    if outResult then
        dbms_output.put_line('Operação registada com sucesso!');
    else
        dbms_output.put_line('Operação não registada!');
    end if;
    return parcelaName;
exception
    when others then
        dbms_output.put_line('Erro:' || sqlerrm);
        return '';
end;
/

