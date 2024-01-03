create or replace NONEDITIONABLE function FUNC_REGISTAR_REGA(p_id_Quinta in number,p_id_Parcela in varchar2,p_id_Operador in number, p_dataInicio in date,p_qtd in number, p_unMedida Regas.UNIDADEMEDIDA%Type, p_Setor in number)
return boolean
is
   outResult boolean := false;
   resultCheck boolean := true;
   idParcela number;
begin
    SELECT idParcela INTO idParcela FROM Parcelas where nome=p_id_Parcela;
    if resultCheck then
        insert into OPERACOES (idOperacao,QUINTAID,PARCELAID,OPERADORID,DATAINICIO)
        values ((Select max(idOperacao)+1 from OPERACOES),p_id_Quinta,idParcela,p_id_Operador,p_dataInicio);
        insert into Regas(OPERACAOID,quantidade,UNIDADEMEDIDA,setor)
        values((Select max(idOperacao) from OPERACOES),p_qtd,p_unMedida,0);
        outResult := true;
    end if;
    if outResult then
        dbms_output.put_line('Operação registada com sucesso!');
    end if;
    return outResult;
exception
    when others then
        dbms_output.put_line('Erro:' || sqlerrm);
    return outResult;
end;