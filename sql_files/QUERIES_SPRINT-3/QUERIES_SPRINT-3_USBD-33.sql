/* USBD33 Como Gestor Agrícola,
* pretendo obter a lista das culturas com maior consumo de água (rega) para um dado ano civil.
* O consumo é em minutos e, em caso de empate, devem ser dadas todas essas culturas. */


-- Query
WITH OperationData AS (
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
)
, RankedOperations AS (
    SELECT
        OD.*,
        DENSE_RANK() OVER (ORDER BY OD.quantidade DESC) AS rnk
    FROM
        OperationData OD
)
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
            RO.*,
            P.nome AS PARCELANAME,
            C.CULTURAID,
            CU.nome AS CULTURANAME
        FROM
            RankedOperations RO
            JOIN PARCELAS P ON RO.PARCELAID = P.idParcela
            JOIN CULTIVOS C ON RO.PARCELAID = C.PARCELAID
            JOIN CULTURAS CU ON C.CULTURAID = CU.idCultura
        WHERE
            RO.dataInicio BETWEEN TO_DATE('2023-07-01', 'YYYY-MM-DD') AND TO_DATE('2023-10-02', 'YYYY-MM-DD')
            AND P.nome = 'CAMPO NOVO'
            AND RO.UNIDADEMEDIDA = 'min'
    )
WHERE
    rnk = 1
ORDER BY
    quantidade DESC;
(...)

-- Func
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