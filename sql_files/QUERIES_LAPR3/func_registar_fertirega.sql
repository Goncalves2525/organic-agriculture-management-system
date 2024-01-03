create or replace NONEDITIONABLE function FUNC_REGISTAR_FERTIREGA(p_id_Quinta in number,p_id_Parcela in number, p_id_Operador in number,p_quantidade in number,p_unMedida in number,p_Setor in number, p_receitaID in number, p_horaInicio in Fertirregas.HORAINICIO%Type, p_dataInicio in date)
return varchar2
is
   outResult boolean := false;
   parcela number(10);
   parcelaName varchar2(255);
begin
        select distinct parcelaid into parcela from cultivos where cultivos.setorregaid = p_Setor;
        select nome into parcelaName from parcelas where parcelas.idparcela = parcela;

        insert into OPERACOES (idOperacao,QUINTAID,PARCELAID,OPERADORID,DATAINICIO)
        values ((Select max(idOperacao)+1 from OPERACOES),p_id_Quinta,parcela,p_id_Operador,p_dataInicio);

        insert into fertirregas(OperacaoID,quantidade,unidadeMedida,Setor,ReceitaId,HoraInicio)
        values((Select max(idOperacao) from OPERACOES),p_quantidade,p_unMedida,p_Setor,p_receitaID,p_horaInicio);

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