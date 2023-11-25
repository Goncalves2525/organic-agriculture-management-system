-- SPRINT 2

/* USBD11 */

/** ***************************************************************************************************************** */

/* USBD12 */

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

/** ***************************************************************************************************************** */

/**
* US BD15
*Como Gestor Agrícola, quero registar uma operação de poda
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
/

-- chamar a funcao
DECLARE
    result_message VARCHAR2(200);
BEGIN
    result_message := insert_poda(:idoperacao, :quantidade, 'un');
    DBMS_OUTPUT.PUT_LINE(result_message);
END;
/

/** ***************************************************************************************************************** */

/* USBD16 */

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
        'MOBILIZACOES_SOLO' AS OperationType
    FROM
        OPERACOES O
        JOIN MOBILIZACOES_SOLO S ON O.idOperacao = S.OPERACAOID

)
SELECT
    OD.PARCELAID,
    OD.dataInicio,
    OD.OperationType,
    COUNT(*) AS OperationCount
FROM
    OperationData OD
WHERE
    OD.dataInicio BETWEEN TO_DATE('2010-12-10  ', 'YYYY-MM-DD') AND TO_DATE('2030-01-20', 'YYYY-MM-DD')
GROUP BY
    OD.PARCELAID, OD.DATAiNICIO, OD.OperationType
ORDER BY
    OD.PARCELAID, OD.OperationType;
	

/** ***************************************************************************************************************** */

/*
* USBD19
* Como Gestor Agrícola,
* pretendo obter a lista de aplicações de fator de produção aplicados na instalação agrícola,
* incluindo a parcela e cultura (se aplicável), por tipo de fator de produção, num dado intervalo de tempo.
*/

SELECT
    operacoes.idoperacao as ID_Operacao,
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
WHERE operacoes.dataInicio BETWEEN TO_DATE('2010-12-10  ', 'YYYY-MM-DD') AND TO_DATE('2030-01-20', 'YYYY-MM-DD')
GROUP BY operacoes.idoperacao, aplicacoes_fatprod.fatorprodid, formulacoes.tipo, parcelas.idparcela, parcelas.nome, culturas.idcultura, culturas.nome
ORDER BY operacoes.idoperacao;

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


CREATE OR REPLACE PROCEDURE GetParcelasData(
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
            operacoes.datainicio BETWEEN TO_DATE('2010-12-10  ', 'YYYY-MM-DD') AND TO_DATE('2030-01-20', 'YYYY-MM-DD') AND TO_DATE('2010-12-10  ', 'YYYY-MM-DD') AND TO_DATE('2030-01-20', 'YYYY-MM-DD')
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
            ', Área: ' || result.area ||
            ', Unidade de Medida: ' || result.unidademedida ||
            ', Mês: ' || result.mes ||
            ', Ano: ' || result.ano ||
            ', Total de Regas: ' || result.TOTALDEREGAS
        );
    END LOOP;
END GetParcelasData;
/

SET SERVEROUTPUT ON;
DECLARE
    datainicio VARCHAR2(10) := ('2010-12-10  ', 'YYYY-MM-DD');
    datafim VARCHAR2(10) := TO_DATE('2030-01-20', 'YYYY-MM-DD');
BEGIN
    GetParcelasData(datainicio, datafim);
END;
/
