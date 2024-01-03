create or replace  function FUNC_REGISTAR_REGA(p_id_Quinta in number,p_id_Parcela in varchar2,p_id_Operador in number, p_dataInicio in date,p_qtd in number, p_unMedida Regas.UNIDADEMEDIDA%Type, p_Setor in number, p_horaInicio in Regas.HoraInicio%Type)
return varchar2
is
   outResult boolean := false;
   resultCheck boolean := true;
   idParcela number;
   parcela number(10);
   parcelaName varchar2(255);
begin

        select distinct parcelaid into parcela from cultivos where cultivos.setorregaid = p_Setor;
        select nome into parcelaName from parcelas where parcelas.idparcela = parcela;

        insert into OPERACOES (idOperacao,QUINTAID,PARCELAID,OPERADORID,DATAINICIO)
        values ((Select max(idOperacao)+1 from OPERACOES),p_id_Quinta,parcela,p_id_Operador,p_dataInicio);

        insert into regas(OperacaoID,quantidade,unidadeMedida,Setor,HoraInicio)
        values((Select max(idOperacao) from OPERACOES),p_qtd,p_unMedida,p_Setor,p_horaInicio);

    outResult := true;

    if outResult then
        dbms_output.put_line('Operação registada com sucesso!');
    else
        dbms_output.put_line('Operação não registada!');
    end if;
    return parcelaName;
exception
    when others then
        dbms_output.put_line('Erro:' || sqlerrm);
end;