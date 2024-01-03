/* USBD32 Como Gestor Agrícola,
* pretendo registar uma operação de rega, incluindo a componente de fertirrega (se aplicável). */

(...)

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