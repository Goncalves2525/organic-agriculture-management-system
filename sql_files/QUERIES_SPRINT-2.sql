-- SPRINT 2

/* US BD11
* Como Gestor Agrícola,
* quero registar uma operação de semeadura */

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
end;


create or replace function func_check_Quinta(p_id_Quinta number) return boolean
is
result_Count int;
begin
select count(*)
into result_Count
from Quintas where IDQUINTA=p_id_Quinta;
return result_Count>0;
End;

create or replace function func_Check_Parcela(p_id_Parcela number, p_area_Parcela number) return boolean
is
result_Count int;
begin
select count(*)
into result_Count
from Parcelas where IDPARCELA=p_id_Parcela and area>=p_area_Parcela;
return result_Count>0;
End;

create or replace function func_Check_Operador(p_id_operador number) return boolean
is
result_Count int;
begin
select count(*)
into result_Count
from Operadores where IDOPERADOR=p_id_operador;
return result_Count>0;
End;

create or replace function func_Check_UnMedida(p_unMedida Unidades_Medida.idUnidadeMedida%Type) return boolean
is
result_Count int;
begin
select count(*)
into result_Count
from Unidades_Medida where idUnidadeMedida=p_unMedida;
return result_Count>0;
End;

/** ***************************************************************************************************************** */

/* USBD12 */
* Como Gestor Agrícola,
* quero registar uma operação de monda.
+/

-- Function to get the list of Mondas
create or replace FUNCTION getMondas RETURN SYS_REFCURSOR IS
    mondas_cursor SYS_REFCURSOR;
BEGIN
    OPEN mondas_cursor FOR
        SELECT * FROM MONDAS;
    RETURN mondas_cursor;
END getMondas;

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

-- Function to insert data into MONDAS AND OPERACOES
CREATE OR REPLACE FUNCTION insertMondas (
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
    begin
        -- Insert data into the table
        INSERT INTO OPERACOES(idOperacao, QUINTAID, PARCELAID, CULTURAID, OPERADORID, dataInicio)
            VALUES (opid, quinta_id, parcela_id, cultura_id, 0, TO_DATE(data_inicio, 'YYYY-MM-DD'));
        INSERT INTO MONDAS(OPERACAOID, quantidade, UNIDADEMEDIDA)
            VALUES (opid, quantidade_atual, unidade);
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
END insertMondas;

/** ***************************************************************************************************************** */

/* USBD13
* Como Gestor Agrícola,
* quero registar uma operação de colheita.
*/

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

-- to test getOperacoesMaxId
declare
    maxId number;
begin
    maxId := getOperacoesMaxId;
    if maxId is not null then
        DBMS_OUTPUT.PUT_LINE('maxId: ' || maxId);
    end if;
end;

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

-- to test getCultivosData
set serveroutput on;
declare
    v_cursor SYS_REFCURSOR;
    v_parcelaid NUMBER;
    v_nome VARCHAR(50);
    v_culturaid NUMBER;
    v_nomecompleto VARCHAR(50);
    v_produto VARCHAR(50);
BEGIN
    v_cursor := getCultivosData;
    LOOP
        FETCH v_cursor INTO
            v_parcelaid,
            v_nome,
            v_culturaid,
            v_nomecompleto,
            v_produto;
        EXIT WHEN v_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('ParcelaID: ' || v_parcelaid ||
                             ', Nome: ' || v_nome ||
                             ', CulturaID: ' || v_culturaid ||
                             ', Nome Completo: ' || v_nomecompleto ||
                             ', Produto: ' || v_produto);
    END LOOP;
    CLOSE v_cursor;
END;

-- Function to get PRODUTO quantidade
create or replace function getProdutoQuantidade (
    idcultura number
)
return number
is
    quantidade_ver number;
begin
    select produtos.quantidade into quantidade_ver
    from produtos
    where produtos.culturaid = idcultura;
    return quantidade_ver;
exception
    when others then
    dbms_output.put_line('Error: ' || SQLERRM);
    return 0;
end getProdutoQuantidade;

-- to test getProdutoQuantidade
declare
    idcultura number := 30;
    quantidade_ver number;
begin
    quantidade_ver := getProdutoQuantidade(idcultura);
    if quantidade_ver is not null then
        DBMS_OUTPUT.PUT_LINE('quantidade: ' || quantidade_ver);
    end if;
end;

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

/** ***************************************************************************************************************** */

/* USBD14 */

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

/** ***************************************************************************************************************** */

/**
* US BD15
* Como Gestor Agrícola,
* quero registar uma operação de poda
*/

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

-- chamar a funcao
DECLARE
    result_message VARCHAR2(200);
BEGIN
    result_message := insert_poda(:idoperacao, :quantidade, 'un');
    DBMS_OUTPUT.PUT_LINE(result_message);
END;
/

/** ***************************************************************************************************************** */

/* US BD16
* Como Gestor Agrícola, 
* pretendo obter a lista dos produtos colhidos numa dada parcela,
* para cada espécie, num dado intervalo de tempo.
*/

--Função get_ListaProdColhidos
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
INNER JOIN cultivos ON o.parcelaid = cultivos.parcelaid AND o.culturaid = cultivos.culturaid
where o.dataInicio between p_dataInicio and p_dataFim and par.Nome=p_nome_Parcela
group by e.especie, c.NomeCompleto,par.Nome;

dbms_output.put_line('Parcela = ' || p_nome_Parcela);
dbms_output.put_line('Intervalo = entre ' || TO_CHAR(p_dataInicio, 'DD/MM/YYYY') || ' e ' || TO_CHAR(p_dataFim, 'DD/MM/YYYY'));
dbms_output.put_line('Resultado:');
LOOP
        FETCH listaProdColhidos INTO especie, produto;
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

--Chamada da função get_ListaProdColhidos
set serveroutput on
declare
   resultado sys_refcursor;
begin
   -- Call the function (example)
   resultado := get_ListaProdColhidos('CAMPO NOVO','20-MAY-23','06-NOV-23');
   exception
   when others then
   dbms_output.put_line('Erro: ' || sqlerrm);
end;

/** ***************************************************************************************************************** */

/*
* USBD17
* Como Gestor Agrícola,
* pretendo obter a lista dos fatores de produção aplicados numa dada parcela,
* e respetivas quantidades,
* para cada tipo de substância componente,
* num dado intervalo de tempo.
*/

with substancias_cte as (
    select
        componentes.fichatecnicaid,
        substancias.substancia,
        substancias.percentagem
    from componentes
    left join substancias on componentes.substanciaid = substancias.idsubstancia
    ),
fatprod_cte as (
    select
        fatores_producao.idfatorproducao as idfat,
        fatores_producao.fichatecnicaid as ft_ft,
        substancias_cte.fichatecnicaid as sub_ft,
        substancias_cte.substancia as sub_sub,
        substancias_cte.percentagem as sub_perc
    from fatores_producao
    left join substancias_cte on fatores_producao.fichatecnicaid = substancias_cte.fichatecnicaid
    )
select
    parcelas.nome as "Parcela",
    aplicacoes_fatprod.fatorprodid as "Fator Produção",
    sum(aplicacoes_fatprod.quantidade) as "Qt FP Aplicado",
    fatprod_cte.sub_sub as "Substancia",
    fatprod_cte.sub_perc "%subst",
    (sum(aplicacoes_fatprod.quantidade)*fatprod_cte.sub_perc) as "Qtd Substancia Aplicada",
    aplicacoes_fatprod.unidademedida as "Unidade"
from aplicacoes_fatprod
left join operacoes on aplicacoes_fatprod.operacaoid = operacoes.idoperacao
left join parcelas on operacoes.parcelaid = parcelas.idparcela
left join fatprod_cte on aplicacoes_fatprod.fatorprodid = fatprod_cte.idfat
where parcelas.nome like 'CAMPO GRANDE'
    and operacoes.dataInicio between to_date('01-01-2022', 'DD-MM-YYYY')
        and to_date('31-12-2022', 'DD-MM-YYYY')
group by parcelas.nome, aplicacoes_fatprod.fatorprodid, fatprod_cte.sub_sub, fatprod_cte.sub_perc, aplicacoes_fatprod.unidademedida
order by aplicacoes_fatprod.fatorprodid



/** ***************************************************************************************************************** */

/*
* USBD18
* Como Gestor Agrícula,
* pretendo obter a lista de operações realizadas numa dada parcela,
* para cada tipo de operações,
* num dado intervalo de tempo.
*/

WITH OperationData AS (
    SELECT
        O.idOperacao,
        O.dataInicio,
        O.dataFim,
        O.PARCELAID,
        AF.quantidade,
        AF.UNIDADEMEDIDA,
        'APLICACAO_FATPROD' AS OperationType
    FROM
        OPERACOES O
        JOIN APLICACOES_FATPROD AF ON O.idOperacao = AF.OPERACAOID
    UNION
    SELECT
        O.idOperacao,
        O.dataInicio,
        O.dataFim,
        O.PARCELAID,
        S.quantidade,
        S.UNIDADEMEDIDA,
        'SEMENTEIRA' AS OperationType
    FROM
        OPERACOES O
        JOIN SEMENTEIRAS S ON O.idOperacao = S.OPERACAOID
    UNION
    SELECT
        O.idOperacao,
        O.dataInicio,
        O.dataFim,
        O.PARCELAID,
        S.quantidade,
        S.UNIDADEMEDIDA,
        'PLANTACOES' AS OperationType
    FROM
        OPERACOES O
        JOIN PLANTACOES S ON O.idOperacao = S.OPERACAOID
    UNION
    SELECT
        O.idOperacao,
        O.dataInicio,
        O.dataFim,
        O.PARCELAID,
        S.quantidade,
        S.UNIDADEMEDIDA,
        'REGAS' AS OperationType
    FROM
        OPERACOES O
        JOIN REGAS S ON O.idOperacao = S.OPERACAOID
    UNION
    SELECT
        O.idOperacao,
        O.dataInicio,
        O.dataFim,
        O.PARCELAID,
        S.quantidade,
        S.UNIDADEMEDIDA,
        'PODAS' AS OperationType
    FROM
        OPERACOES O
        JOIN PODAS S ON O.idOperacao = S.OPERACAOID
    UNION
    SELECT
        O.idOperacao,
        O.dataInicio,
        O.dataFim,
        O.PARCELAID,
        S.quantidade,
        S.UNIDADEMEDIDA,
        'INCORPS_SOLO' AS OperationType
    FROM
        OPERACOES O
        JOIN INCORPS_SOLO S ON O.idOperacao = S.OPERACAOID
    UNION
    SELECT
        O.idOperacao,
        O.dataInicio,
        O.dataFim,
        O.PARCELAID,
        S.quantidade,
        S.UNIDADEMEDIDA,
        'COLHEITAS' AS OperationType
    FROM
        OPERACOES O
        JOIN COLHEITAS S ON O.idOperacao = S.OPERACAOID
    UNION
    SELECT
        O.idOperacao,
        O.dataInicio,
        O.dataFim,
        O.PARCELAID,
        S.quantidade,
        S.UNIDADEMEDIDA,
        'MONDAS' AS OperationType
    FROM
        OPERACOES O
        JOIN MONDAS S ON O.idOperacao = S.OPERACAOID
    UNION
    SELECT
        O.idOperacao,
        O.dataInicio,
        O.dataFim,
        O.PARCELAID,
        S.quantidade,
        S.UNIDADEMEDIDA,
        'MOBILIZACOES_SOLO' AS OperationType
    FROM
        OPERACOES O
        JOIN MOBILIZACOES_SOLO S ON O.idOperacao = S.OPERACAOID
)
SELECT
    OD.PARCELAID,
    P.nome AS PARCELANAME,
    C.CULTURAID,
    CU.nome AS CULTURANAME,
    OD.dataInicio,
    OD.OperationType,
    OD.quantidade,
    OD.UNIDADEMEDIDA
FROM
    OperationData OD
    JOIN PARCELAS P ON OD.PARCELAID = P.idParcela
    JOIN CULTIVOS C ON OD.PARCELAID = C.PARCELAID
    JOIN CULTURAS CU ON C.CULTURAID = CU.idCultura
WHERE
    OD.dataInicio BETWEEN TO_DATE('2023-07-01', 'YYYY-MM-DD') AND TO_DATE('2023-10-02', 'YYYY-MM-DD')
    AND P.nome = 'CAMPO NOVO'
ORDER BY
    OD.OperationType, OD.datainicio;
	

/** ***************************************************************************************************************** */

/*
* USBD19
* Como Gestor Agrícola,
* pretendo obter a lista de aplicações de fator de produção aplicados na instalação agrícola,
* incluindo a parcela e cultura (se aplicável), por tipo de fator de produção, num dado intervalo de tempo.
*/

SELECT
    operacoes.idoperacao as ID_Operacao,
    operacoes.dataInicio as Data_Operacao,
    aplicacoes_fatprod.fatorprodid as Fator_de_Producao,
    formulacoes.tipo as Tipo,
    parcelas.idparcela as ID_Parcela,
    parcelas.nome as Parcela,
    culturas.idcultura as ID_Cultura,
    culturas.nome as Cultura
FROM aplicacoes_fatprod
         INNER JOIN fatores_producao ON aplicacoes_fatprod.fatorprodid = fatores_producao.idfatorproducao
         INNER JOIN formulacoes ON fatores_producao.formulacaoid = formulacoes.idformulacao
         INNER JOIN operacoes ON aplicacoes_fatprod.operacaoid = operacoes.idoperacao
         INNER JOIN cultivos ON operacoes.parcelaid = cultivos.parcelaid AND operacoes.culturaid = cultivos.culturaid
         INNER JOIN parcelas ON cultivos.parcelaid = parcelas.idparcela
         INNER JOIN culturas ON cultivos.culturaid = culturas.idcultura
WHERE operacoes.dataInicio BETWEEN TO_DATE('2019-01-01', 'YYYY-MM-DD') AND TO_DATE('2023-07-06', 'YYYY-MM-DD')
GROUP BY operacoes.idoperacao, operacoes.dataInicio, aplicacoes_fatprod.fatorprodid, formulacoes.tipo, parcelas.idparcela, parcelas.nome, culturas.idcultura, culturas.nome
ORDER BY operacoes.dataInicio;

/*
 Mostrar apenas LAMEIRO DO MOINHO
 */
SELECT
    operacoes.idoperacao as ID_Operacao,
    operacoes.dataInicio as Data_Operacao,
    aplicacoes_fatprod.fatorprodid as Fator_de_Producao,
    formulacoes.tipo as Tipo,
    parcelas.idparcela as ID_Parcela,
    parcelas.nome as Parcela,
    culturas.idcultura as ID_Cultura,
    culturas.nome as Cultura
FROM aplicacoes_fatprod
         INNER JOIN fatores_producao ON aplicacoes_fatprod.fatorprodid = fatores_producao.idfatorproducao
         INNER JOIN formulacoes ON fatores_producao.formulacaoid = formulacoes.idformulacao
         INNER JOIN operacoes ON aplicacoes_fatprod.operacaoid = operacoes.idoperacao
         INNER JOIN cultivos ON operacoes.parcelaid = cultivos.parcelaid AND operacoes.culturaid = cultivos.culturaid
         INNER JOIN parcelas ON cultivos.parcelaid = parcelas.idparcela
         INNER JOIN culturas ON cultivos.culturaid = culturas.idcultura
WHERE operacoes.dataInicio BETWEEN TO_DATE('2019-01-01', 'YYYY-MM-DD') AND TO_DATE('2023-07-06', 'YYYY-MM-DD')
  AND parcelas.nome LIKE 'LAMEIRO DO MOINHO'
GROUP BY operacoes.idoperacao, operacoes.dataInicio, aplicacoes_fatprod.fatorprodid, formulacoes.tipo, parcelas.idparcela, parcelas.nome, culturas.idcultura, culturas.nome
ORDER BY operacoes.dataInicio;

/** ***************************************************************************************************************** */

/**
* US BD20
*Como Gestor Agrícola, pretendo obter os totais de rega mensal de cada parcela, 
*num dado intervalo de tempo.
*/
SELECT parcelas.idparcela, parcelas.nome, parcelas.area, parcelas.unidademedida,
    EXTRACT(MONTH FROM operacoes.datainicio) AS mes,
    EXTRACT(YEAR FROM operacoes.datainicio) AS ano,
    SUM(REGAS.QUANTIDADE) AS TOTALDEREGAS 
FROM QUINTAS
JOIN PARCELAS ON (quintas.idquinta = parcelas.quintaid)
JOIN OPERACOES ON (parcelas.idparcela = operacoes.parcelaid)
JOIN REGAS ON (operacoes.idoperacao = regas.operacaoid)
WHERE operacoes.datainicio BETWEEN TO_DATE('2010-12-10  ', 'YYYY-MM-DD') AND TO_DATE('2030-01-20', 'YYYY-MM-DD')
GROUP BY parcelas.idparcela, parcelas.nome, parcelas.area, parcelas.unidademedida, 
    EXTRACT(MONTH FROM operacoes.datainicio),
    EXTRACT(YEAR FROM operacoes.datainicio)


CREATE OR REPLACE PROCEDURE GetParcelasDataProc(
    p_datainicio VARCHAR2,
    p_datafim VARCHAR2
)
IS
BEGIN
    FOR result IN (
        SELECT 
            parcelas.idparcela, 
            parcelas.nome, 
            parcelas.area, 
            parcelas.unidademedida,
            EXTRACT(MONTH FROM operacoes.datainicio) AS mes,
            EXTRACT(YEAR FROM operacoes.datainicio) AS ano,
            SUM(REGAS.QUANTIDADE) AS TOTALDEREGAS 
        FROM 
            QUINTAS
        JOIN 
            PARCELAS ON quintas.idquinta = parcelas.quintaid
        JOIN 
            OPERACOES ON parcelas.idparcela = operacoes.parcelaid
        JOIN 
            REGAS ON operacoes.idoperacao = regas.operacaoid
        WHERE 
            operacoes.datainicio BETWEEN TO_DATE('2010-12-10', 'YYYY-MM-DD') AND TO_DATE('2030-01-20', 'YYYY-MM-DD')
        GROUP BY 
            parcelas.idparcela, 
            parcelas.nome, 
            parcelas.area, 
            parcelas.unidademedida, 
            EXTRACT(MONTH FROM operacoes.datainicio),
            EXTRACT(YEAR FROM operacoes.datainicio)
    ) LOOP
        -- Process the result
        DBMS_OUTPUT.PUT_LINE(
            'ID Parcela: ' || result.idparcela ||
            ', Nome: ' || result.nome ||
            ', �?rea: ' || result.area ||
            ', Unidade de Medida: ' || result.unidademedida ||
            ', Mês: ' || result.mes ||
            ', Ano: ' || result.ano ||
            ', Total de Regas: ' || result.TOTALDEREGAS
        );
    END LOOP;
END GetParcelasDataProc;
/

SET SERVEROUTPUT ON;
DECLARE
    datainicio VARCHAR2(10) := ('2010-12-10  ', 'YYYY-MM-DD');
    datafim VARCHAR2(10) := TO_DATE('2030-01-20', 'YYYY-MM-DD');
BEGIN
    GetParcelasData(datainicio, datafim);
END;
/

