-- SPRINT 2

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



