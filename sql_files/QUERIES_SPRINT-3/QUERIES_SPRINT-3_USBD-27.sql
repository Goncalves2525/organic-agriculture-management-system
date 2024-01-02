-- SPRINT 3

/* USBD27 Como Gestor Agrícola, pretendo que não seja possível alterar ou apagar os logs. */

CREATE OR REPLACE TRIGGER Negar_Alteracoes_Logs
  BEFORE DELETE OR UPDATE ON OPERACOES_LOGS
BEGIN
    raise_application_error(-20000,'A seguinte ação não é permitida!');
END;