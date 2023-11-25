-- SPRINT 2

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



