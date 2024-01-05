-- SPRINT 3

/* USBD34 Como Gestor Agrícola, pretendo obter a lista das substâncias de fatores de produção 
usadas noutros anos civis,mas não usadas no ano civil indicado. */

-- Criar a stored procedure
create or replace PROCEDURE ObterSubstanciasNaoUsadasNoAno(dataInicioParam IN DATE, dataFimParam IN DATE) IS
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
            WHERE o2.datainicio BETWEEN dataInicioParam AND dataFimParam
        )
        ORDER BY s.SUBSTANCIA
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(substancia_rec.SUBSTANCIA);
    END LOOP;
END ObterSubstanciasNaoUsadasNoAno;



SET SERVEROUTPUT ON;
BEGIN
    ObterSubstanciasNaoUsadasNoAno(TO_DATE('2023-01-01', 'YYYY-MM-DD'), TO_DATE('2023-12-31', 'YYYY-MM-DD'));
END;
/




-- Criar a function
CREATE OR REPLACE FUNCTION ObterSubstanciasNaoUsadasNoIntervaloDatas(dataInicioParam IN DATE, dataFimParam IN DATE)
RETURN VARCHAR2 IS
   listaSubstancias VARCHAR2(4000); -- Ajuste o tamanho conforme necessário
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
            WHERE o2.datainicio BETWEEN dataInicioParam AND dataFimParam
        )
        ORDER BY s.SUBSTANCIA
    ) LOOP
        listaSubstancias := listaSubstancias || substancia_rec.SUBSTANCIA || ', ';
    END LOOP;

    -- Remover a vírgula final, se houver
    IF LENGTH(listaSubstancias) > 0 THEN
        listaSubstancias := RTRIM(listaSubstancias, ', ');
    END IF;

    RETURN listaSubstancias;
END ObterSubstanciasNaoUsadasNoIntervaloDatas;
/


-- TESTE
DECLARE
    resultado VARCHAR2(4000); -- Ajuste o tamanho conforme necessário
BEGIN
    resultado := ObterSubstanciasNaoUsadasNoIntervaloDatas(TO_DATE('2023-01-01', 'YYYY-MM-DD'), TO_DATE('2023-12-01', 'YYYY-MM-DD'));
    
    IF resultado IS NOT NULL THEN
        DBMS_OUTPUT.PUT_LINE('Substâncias não usadas: ' || resultado);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Nenhuma substância encontrada.');
    END IF;
END;
/