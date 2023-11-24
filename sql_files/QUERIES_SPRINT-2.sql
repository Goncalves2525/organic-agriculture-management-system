-- SPRINT 2

/*
* USBD18
* Como Gestor Agrícula,
* pretendo obter a lista de operações realizadas numa dada parcela,
* para cada tipo de operações,
* num dado intervalo de tempo.
*/

SELECT
    parcelas.idparcela AS ID_Parcela,
    parcelas.nome AS Parcela,
    operacoes.idoperacao AS ID_Operacao
FROM OPERACOES
JOIN parcelas ON operacoes.parcelaid = parcelas.idparcela
WHERE operacoes.dataInicio BETWEEN TO_DATE('2010-12-10', 'YYYY-MM-DD') AND TO_DATE('2030-01-20', 'YYYY-MM-DD')
GROUP BY parcelas.idparcela, parcelas.nome, operacoes.idoperacao
ORDER BY parcelas.idparcela;

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



