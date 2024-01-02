-- SPRINT 3

/* USBD28 Como Gestor Agrícola, pretendo que não seja possível apagar operações,
mas deve ser possível anular uma operação, ficando isso registado na BD. */

CREATE OR REPLACE TRIGGER Negar_Delete_Operacoes
  BEFORE DELETE ON Operacoes
BEGIN
    raise_application_error(-20000,'A seguinte ação não é permitida!');
END;




