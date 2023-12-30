-- SPRINT 3

/* USBD34 Como Gestor Agrícola, pretendo obter a lista das substâncias de fatores de produção 
usadas noutros anos civis,mas não usadas no ano civil indicado. */

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


-- Exemplo 1: Chamar a procedure para o ano 2022
SET SERVEROUTPUT ON;
BEGIN
    ObterSubstanciasNaoUsadasNoAno(2024);
END;
/

-- Exemplo 2: Chamar a procedure para o ano 2021
SET SERVEROUTPUT ON;
BEGIN
    ObterSubstanciasNaoUsadasNoAno(2023);
END;
/
