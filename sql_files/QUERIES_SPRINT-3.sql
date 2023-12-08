-- SPRINT 3

/*  USBD24 Como Gestor Agrícola,
* pretendo que todos os registos relacionados com operações tenham registado o instante em que foram criados,
* gerado pelo SGBD. */

(...)

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

(...)

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

(...)