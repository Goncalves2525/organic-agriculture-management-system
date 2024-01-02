-- SPRINT 3

/* USBD30 Como Gestor Agrícola, pretendo anular uma operação que estava prevista
 e não se realizou ou que foi criada por engano,
 sabendo que isso só é possível até aos 3 dias seguintes à sua data prevista de execução */

Create or replace function Anular_Operacao(p_id_Operacao in number)
return boolean
is
    outResult boolean := false;
    existeLinha number;
    dataOperacao date;
    excecaoForaData EXCEPTION;
    excecaoNExisteLinha EXCEPTION;
begin
    select count(*) into existeLinha from OPERACOES where idoperacao = p_id_Operacao and Anulado=0;

    if existeLinha = 1 then
        SELECT DataInicio INTO dataOperacao FROM OPERACOES WHERE idoperacao = p_id_Operacao;
        if dataOperacao+3>=sysdate then
            update Operacoes set Anulado=1 where IDOperacao=p_id_Operacao;
            insert into OPERACOES_LOGS(idlog,operacaoid,timestamp, descricao)
            select max(idlog)+1,p_id_Operacao,CURRENT_TIMESTAMP,'Operação anulada' from OPERACOES_LOGS;
            Commit;
            outResult := true;
        else
            raise excecaoForaData;
        end if;
    else
        raise excecaoNExisteLinha;
    end if;
    return outResult;
exception
    when excecaoForaData then
        raise_application_error(-20000,'Data fora da janela de anulação!');
        return outResult;
    when excecaoNExisteLinha then
        raise_application_error(-20000,'Essa operação não existe!');
        return outResult;
    when others then
        raise_application_error(-20000,' '|| SQLERRM);
        rollback;
end;
