-- SPRINT 3

/* USBD31 Como Gestor Agrícola,
* pretendo registar uma receita de fertirrega para usar em operações de rega. */

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


-- Bloco anónimo para testar registo de receita
DECLARE
    app_idReceita NUMBER := ?;
    app_fatorProdId VARCHAR(255) := '?';
    app_quantidadePorHectar NUMBER := ?;
    app_unidadeMedida VARCHAR(255) := '?';
    outcome NUMBER := -1;
BEGIN
    outcome := registarReceita(app_idReceita, app_fatorProdId, app_quantidadePorHectar, app_unidadeMedida);
    DBMS_OUTPUT.PUT_LINE('resultado: ' || outcome);
END;
