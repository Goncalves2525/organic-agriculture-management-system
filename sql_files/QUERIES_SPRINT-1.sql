-- SPRINT 1

/**
* US BD05
* Como Gestor Agrícola,
* pretendo saber a quantidade de produtos colhidos numa dada parcela,
* para cada produto, num dado intervalo de tempo.
*/
SELECT CULTURAS.nomeCompleto as Produto, SUM(OPERACOES.quantidade) as quantidade, PARCELAS.nome as Parcela
FROM COLHEITAS
INNER JOIN OPERACOES ON COLHEITAS.OPERACAOID = OPERACOES.idOperacao
INNER JOIN CULTIVOS ON OPERACOES.CULTIVOSID = CULTIVOS.idCultivo
INNER JOIN PARCELAS ON CULTIVOS.PARCELAID = PARCELAS.idParcela
INNER JOIN CULTURAS ON CULTIVOS.CULTURAID = CULTURAS.idCultura
WHERE OPERACOES.data BETWEEN TO_DATE('2020-01-01', 'YYYY-MM-DD') AND TO_DATE('2022-12-31', 'YYYY-MM-DD')
GROUP BY CULTURAS.nomeCompleto, PARCELAS.nome;

/**
* US BD06
* Como Gestor Agrícola,
* pretendo saber o número de fatores de produção aplicados numa dada parcela,
* para cada tipo de fator, num dado intervalo de tempo para obter dados filtrados.
*/
SELECT APLICACOES_FATORES_PRODUCAO.FATORPRODUCAOID, FATORES_PRODUCAO.nomeComercial AS Fator, PARCELAS.nome as Parcela, COUNT(*) AS NumeroDeAplicacoes
FROM APLICACOES_FATORES_PRODUCAO
INNER JOIN FATORES_PRODUCAO ON APLICACOES_FATORES_PRODUCAO.FATORPRODUCAOID = FATORES_PRODUCAO.idFatorProducao
INNER JOIN OPERACOES ON APLICACOES_FATORES_PRODUCAO.OPERACAOID = OPERACOES.idOperacao
INNER JOIN CULTIVOS ON OPERACOES.CULTIVOSID = CULTIVOS.idCultivo
INNER JOIN PARCELAS ON CULTIVOS.PARCELAID = PARCELAS.idParcela
WHERE OPERACOES.data BETWEEN TO_DATE('2017-12-10', 'YYYY-MM-DD') AND TO_DATE('2023-01-20', 'YYYY-MM-DD')
GROUP BY APLICACOES_FATORES_PRODUCAO.FATORPRODUCAOID, FATORES_PRODUCAO.nomeComercial, PARCELAS.nome
ORDER BY APLICACOES_FATORES_PRODUCAO.FATORPRODUCAOID;

/**
* US BD07
* Como Gestor Agrícola,
* pretendo saber o número de operações realizadas numa dada parcela, para cada tipo de operação, num dado intervalo de tempo.
*/
select t.nome as "Nome Operação", p.nome as "Nome Parcela" , count(*) as "Nº de Operações" from OPERACOES o
    left join TIPOS_OPERACOES t on t.idTipoOperacao=o.tipooperacaoid
    left join cultivos c on c.idcultivo=o.cultivosID
    left join parcelas p on p.Idparcela=c.parcelaID
where o.data between to_date('2016-10-06', 'YYYY-MM-DD') and to_date('2023-01-12', 'YYYY-MM-DD') and p.Idparcela=102
group by  o.TipoOperacaoid,t.nome, p.nome

/**
* US BD08
* Como Gestor Agrícola,
* pretendo saber o fator de produção com mais aplicações na instalação agrícola num dado intervalo de tempo,
* a fim de gerir as interações entre os componentes dos fatores.
*/
with maxFatoresProducao as (
    select nomecomercial, count(fatorproducaoid) as max
	from aplicacoes_fatores_producao
	inner join fatores_producao
    		on aplicacoes_fatores_producao.fatorproducaoid = fatores_producao.idfatorproducao
	inner join operacoes
		on aplicacoes_fatores_producao.operacaoid = operacoes.idoperacao
	where operacoes.data between to_date('01-12-2020', 'DD-MM-YYYY') and to_date('20-12-2020', 'DD-MM-YYYY')
	group by nomecomercial)    
select nomecomercial, max as totalfatorproducaousado
from maxFatoresProducao 
where max = (select max(max) from maxFatoresProducao)

/**
* US BD09 
* Como Gestor Agrícola,
* pretendo saber o número de aplicações de cada tipo de fator de produção aplicados na instalação agrícola num dado intervalo de tempo.
*/
SELECT
    aplicacoes_fatores_producao.tipooperacaoid AS TipoFatorProducao,
    fatores_producao.nomeComercial AS NomeFatorProducao,
    COUNT(aplicacoes_fatores_producao.operacaoid) AS NumeroAplicacoes
FROM aplicacoes_fatores_producao
INNER JOIN fatores_producao ON aplicacoes_fatores_producao.fatorproducaoid = fatores_producao.idFatorProducao
INNER JOIN operacoes ON aplicacoes_fatores_producao.operacaoid = operacoes.idOperacao
WHERE operacoes.data >= TO_DATE('2019-01-01', 'YYYY-MM-DD') AND operacoes.data <= TO_DATE('2023-12-31', 'YYYY-MM-DD')
GROUP BY aplicacoes_fatores_producao.tipooperacaoid, fatores_producao.nomeComercial;
