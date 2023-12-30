-- SPRINT 3

/*  USBD24 Como Gestor Agrícola,
* pretendo que todos os registos relacionados com operações tenham registado o instante em que foram criados,
* gerado pelo SGBD. */

Adicionou-se a coluna "timestamp" à tabela "operacoes" para guardar o instante em que a operação foi criada.

/* USBD25 Como Gestor Agrícola,
* pretendo que a identificação da operação seja um número sequencial,
* não gerado automaticamente pelo SGBD,
* que deve ser gerado no contexto da transação de registo da operação.
* Se este registo falhar, não deve haver consequências,
* nomeadamente a existência de ”buracos” na numeração. */

-- Funcionalidade já implementada no SPRINT2, por opção da equipa de trabalho.

/* USBD26 Como Gestor Agrícola,
* pretendo que a criação ou alteração de um registo de uma operação deva ser registado num log que indique
* o instante, tipo de operação e todos os dados relacionados com a operação (e.g. data da operação, parcela, etc.). */



/*  USBD27 Como Gestor Agrícola,
pretendo que não seja possível alterar ou apagar os logs. */

(...)

/* USBD28 Como Gestor Agrícola,
* pretendo que não seja possível apagar operações,
* mas deve ser possível anular uma operação, ficando isso registado na BD. */

(...)

/* USBD30 Como Gestor Agrícola,
* pretendo anular uma operação que estava prevista e não se realizou ou que foi criada por engano,
* sabendo que isso só é possível até aos 3 dias seguintes `a sua data prevista de execução,
* se não houver operações posteriores dependentes desta. */

(...)

/* USBD31 Como Gestor Agrícola,
* pretendo registar uma receita de fertirrega para usar em operações de rega. */

(...)

/* USBD32 Como Gestor Agrícola,
* pretendo registar uma operação de rega, incluindo a componente de fertirrega (se aplicável). */

(...)

/* USBD33 Como Gestor Agrícola,
* pretendo obter a lista das culturas com maior consumo de água (rega) para um dado ano civil.
* O consumo é em minutos e, em caso de empate, devem ser dadas todas essas culturas. */

(...)

/* USBD34 Como Gestor Agrícola,
* pretendo obter a lista das substâncias de fatores de produção usadas noutros anos civis,
* mas não usadas no ano civil indicado. */

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