package repository;

import tables.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TablesRepository {

    // Repository Lists
    List<TiposCultura> lstTiposCultura = new ArrayList<>();
    List<TiposConsumo> lstTiposConsumo = new ArrayList<>();
    List<EspeciesVegetais> lstEspeciesVegetais = new ArrayList<>();
    List<Culturas> lstCulturas = new ArrayList<>();
    List<Produtos> lstProdutos = new ArrayList<>();
    List<Ciclos> lstCiclos = new ArrayList<>();
    List<UnidadesMedida> lstUnidadesMedida = new ArrayList<>();
    List<Quintas> lstQuintas = new ArrayList<>();
    List<Operadores> lstOperadores = new ArrayList<>();
    List<Formulacoes> lstFormulacoes = new ArrayList<>();
    List<FatoresProducao> lstFatorProd = new ArrayList<>();
    List<FichasTecnicas> lstFichasTecnicas = new ArrayList<>();
    List<Substancias> lstSubstancias = new ArrayList<>();
    List<ComponentesFT> lstComponentesFT = new ArrayList<>();
    List<FatoresProducao> lstFatoresProducao = new ArrayList<>();
    List<Edificios> lstEdificios = new ArrayList<>();
    List<Parcelas> lstParcelas = new ArrayList<>();
    List<Armazens> lstArmazens = new ArrayList<>();
    List<Garagens> lstGaragens = new ArrayList<>();
    List<Moinhos> lstMoinhos = new ArrayList<>();
    List<SistemasRega> lstSistemasRega = new ArrayList<>();
    List<Cultivos> lstCultivos = new ArrayList<>();
    List<Operacoes> lstOperacoes = new ArrayList<>();
    List<AplicacoesFatProd> lstAplicacoes = new ArrayList<>();
    List<Sementeiras> lstSementeiras = new ArrayList<>();
    List<Plantacoes> lstPlantacoes = new ArrayList<>();
    List<Regas> lstRegas = new ArrayList<>();
    List<Podas> lstPodas = new ArrayList<>();
    List<IncorpsSolo> lstIncorpsSolo = new ArrayList<>();
    List<Colheitas> lstColheitas = new ArrayList<>();
    List<SetoresRega> lstSetoresRega = new ArrayList<>();
    List<Mondas> lstMondas = new ArrayList<>();
    List<MobilizacoesSolo> lstMobilizacoesSolo = new ArrayList<>();

    // Incrementable IDs
    int especieId = 1;
    int tipoPlantacaoId = 1;
    int culturaId = 1;
    int tipoConsumoId = 1;
    int formulacaoId = 1;
    int fichaTecnicaId = 1;
    int operacaoId = 1;
    int substanciaId = 1;

    public TablesRepository() {
    }

    ///// ADD TO TABLES defaults ///////////////////////////////////////////////////////////////////////////////////////

    public boolean addToTableTiposCulturaDefault(TiposCultura tipoCultura) {
        if (tipoCultura.getIdTipoCultura() == 0) {
            lstTiposCultura.add(tipoCultura);
            return true;
        }
        return false;
    }

    public boolean addToTableEspeciesVegetaisDefault(EspeciesVegetais especieVegetal) {
        if (especieVegetal.getIdEspecieVegetal() == 0) {
            lstEspeciesVegetais.add(especieVegetal);
            return true;
        }
        return false;
    }

    public boolean addToTableCulturasDefault(Culturas cultura) {
        if (cultura.getIdCultura() == 0) {
            lstCulturas.add(cultura);
            return true;
        }
        return false;
    }

    private boolean addToTableCultivosDefaultByParcela(int idParcela, double area, String unidadeMedida) {
        boolean foundCultivo = false;

        // Add default Cultivo for this Parcela (Cultivo record that represents the whole Parcela)
        for (Cultivos obj : lstCultivos) {
            if (obj.getParcelaId() == idParcela && obj.getCulturaId() == 0) {
                foundCultivo = true;
                break;
            }
        }

        if (!foundCultivo) {
            Cultivos cultivos = new Cultivos(1, idParcela, 0, null, null, area, unidadeMedida, 0);
            lstCultivos.add(cultivos);
        }

        return foundCultivo;
    }

    ///// ADD TO TABLES ///////////////////////////////////////////////////////////////////////////////////////////////

    public boolean addToTableQuintas(int idQuinta, String nome) {
        boolean found = false;

        // Checks if entry exists
        for (Quintas obj : lstQuintas) {
            if (obj.getIdQuinta() == idQuinta) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Quintas newEntry = new Quintas(idQuinta, nome);
            lstQuintas.add(newEntry);
        }
        return !found;
    }

    public boolean addToTableOperadores(int idOperador, String nome) {
        boolean found = false;

        // Checks if entry exists
        for (Operadores obj : lstOperadores) {
            if (obj.getIdOperador() == idOperador) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Operadores newEntry = new Operadores(idOperador, nome);
            lstOperadores.add(newEntry);
        }
        return !found;
    }

    public boolean addToTableUnidadesMedida(String idUnidadeMedida, String nomeCompleto) {
        boolean found = false;

        // Checks if entry exists
        for (UnidadesMedida obj : lstUnidadesMedida) {
            if (obj.getIdUnidadeMedida().equals(idUnidadeMedida)
                    && obj.getNomeCompleto().equals(nomeCompleto)) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            UnidadesMedida newEntry = new UnidadesMedida(idUnidadeMedida, nomeCompleto);
            lstUnidadesMedida.add(newEntry);
        }
        return !found;
    }

    public boolean addToTableTiposCultura(String tipoPlantacao) {
        TiposCultura newEntry = new TiposCultura(tipoPlantacaoId, tipoPlantacao);
        boolean found = false;
        // Checks if entry exists
        for (TiposCultura obj : lstTiposCultura) {
            if (obj.getNome().equals(newEntry.getNome())) {
                found = true;
                break;
            }
        }
        // Adds if it doesn't exist already
        if (!found) {
            lstTiposCultura.add(newEntry);
            ++this.tipoPlantacaoId;
        }
        return !found;
    }

    public boolean addToTableEspeciesVegetais(String especie, String nomeComum) {
        ProdutoDaEspecie produtoDaEspecie = ProdutoDaEspecie.valueOf(nomeComum);
        EspeciesVegetais newEntry = new EspeciesVegetais(especieId, especie, produtoDaEspecie.getEspecie());
        boolean found = false;
        // Checks if entry exists
        for (EspeciesVegetais obj : lstEspeciesVegetais) {
            if (obj.getEspecie().equals(newEntry.getEspecie()) && obj.getNomeComum().equals(nomeComum)) {
                found = true;
                break;
            }
        }
        // Adds if it doesn't exist already
        if (!found) {
            lstEspeciesVegetais.add(newEntry);
            ++especieId;
        }
        return !found;
    }

    public boolean addToTableCulturas(String tipoCultura, String especie, String nome, String nomeCompleto, String nomeComum) {
        boolean found = false;

        // Checks if entry exists
        for (Culturas obj : lstCulturas) {
            if (obj.getNomeCompleto().equals(nomeCompleto)) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {

            // Get relevant id's from other "tables"
            int localTipoCulturaId = 0;
            for (TiposCultura obj : lstTiposCultura) {
                if (obj.getNome().equals(tipoCultura)) {
                    localTipoCulturaId = obj.getIdTipoCultura();
                    break;
                }
            }
            int localEspecieId = 0;
            for (EspeciesVegetais obj : lstEspeciesVegetais) {
                if (obj.getEspecie().equals(especie)) {
                    localEspecieId = obj.getIdEspecieVegetal();
                    break;
                }
            }

            Culturas newEntry = new Culturas(culturaId, localTipoCulturaId, localEspecieId, nome, nomeCompleto, nomeComum);
            lstCulturas.add(newEntry);
            ++culturaId;
        }
        return !found;
    }

    public boolean addToTableProdutos(String nomeComum, String nomeCompleto) {
        boolean found = false;

        int localCulturaId = 0;
        for (Culturas obj : lstCulturas) {
            if (obj.getNomeCompleto().equals(nomeCompleto)) {
                localCulturaId = obj.getIdCultura();
                break;
            }
        }

        // Checks if entry exists
        for (Produtos obj : lstProdutos) {
            if (obj.getCulturaId() == localCulturaId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            ProdutoDaEspecie produtoDaEspecie = ProdutoDaEspecie.valueOf(nomeComum);
            Produtos newEntry = new Produtos(localCulturaId, produtoDaEspecie.getProduto());
            lstProdutos.add(newEntry);
        }
        return !found;
    }

    public boolean addToTableCiclos(String nomeComum, String tempo, String tipoOperacao) {
        boolean found = false;

        int localEspecieVegetalId = 0;
        for (EspeciesVegetais obj : lstEspeciesVegetais) {
            if (obj.getNomeComum().equals(nomeComum)) {
                localEspecieVegetalId = obj.getIdEspecieVegetal();
                break;
            }
        }

        // Checks if entry exists
        for (Ciclos obj : lstCiclos) {
            if (obj.getNomeCiclo().equals(tipoOperacao) && obj.getEspecieVegetalId() == localEspecieVegetalId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Ciclos newEntry = new Ciclos(tipoOperacao, localEspecieVegetalId, tempo);
            lstCiclos.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableTiposConsumo(String tipoConsumo) {
        TiposConsumo newEntry = new TiposConsumo(tipoConsumoId, tipoConsumo);
        boolean found = false;
        // Checks if entry exists
        for (TiposConsumo obj : lstTiposConsumo) {
            if (obj.getDescricao().equals(newEntry.getDescricao())) {
                found = true;
                break;
            }
        }
        // Adds if it doesn't exist already
        if (!found) {
            lstTiposConsumo.add(newEntry);
            ++tipoConsumoId;
        }
        return !found;
    }

    public boolean addToTableFormulacoes(String formato, String tipo, String aplicacao) {
        boolean found = false;

        // Checks if entry exists
        for (Formulacoes obj : lstFormulacoes) {
            if (obj.getFormato().equals(formato) && obj.getTipo().equals(tipo) && obj.getAplicacao().equals(aplicacao)) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Formulacoes newEntry = new Formulacoes(formulacaoId, formato, tipo, aplicacao);
            lstFormulacoes.add(newEntry);
            ++formulacaoId;
        }
        return !found;
    }

    public int addToTableFichasTecnicas(String designacao) {
        boolean found = false;

        // Checks if entry exists
        for (FatoresProducao obj : lstFatorProd) {
            if (obj.getNomeComercial().equals(designacao)) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            FichasTecnicas newEntry = new FichasTecnicas(fichaTecnicaId);
            lstFichasTecnicas.add(newEntry);
            ++fichaTecnicaId;
            return (fichaTecnicaId - 1);
        }

        return 0; // returning 0 when Ficha Tecnica already exists
    }

    public int addToTableSubstancias(String cx, double cxPerc, String unidade) {
        boolean found = false;
        int subsId = 0;

        // Checks if entry exists
        for (Substancias obj : lstSubstancias) {
            if (obj.getSubstancia().equals(cx) && obj.getPercentagem() == cxPerc) {
                found = true;
                subsId = obj.getIdSubstancia();
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Substancias newEntry = new Substancias(substanciaId, cx, cxPerc, unidade);
            lstSubstancias.add(newEntry);
            subsId = substanciaId;
            ++substanciaId;
        }

        return subsId;
    }

    public boolean addToTableComponentesFT(int idFichaTecnica, int idx, int substanciaId) {
        boolean found = false;

        // Checks if entry exists
        for (ComponentesFT obj : lstComponentesFT) {
            if (obj.getFichaTecnicaId() == idFichaTecnica
                    && obj.getIdComponente() == idx
                    && obj.getSubstancia() == substanciaId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            ComponentesFT newEntry = new ComponentesFT(idFichaTecnica, idx, substanciaId);
            lstComponentesFT.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableFatoresProducao(String nomeComercial, String fabricante, int idFichaTecnica, String formato, String tipo, String aplicacao) {
        boolean found = false;

        // Checks if entry exists
        for (FatoresProducao obj : lstFatoresProducao) {
            if (obj.getNomeComercial().equals(nomeComercial)) {
                found = true;
                break;
            }
        }

        int idFormulacao = 0;
        for (Formulacoes f : lstFormulacoes) {
            if (f.getFormato().equals(formato)
                    && f.getTipo().equals(tipo)
                    && f.getAplicacao().equals(aplicacao)) {
                idFormulacao = f.getIdFormulacao();
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            FatoresProducao newEntry = new FatoresProducao(nomeComercial, fabricante, idFormulacao, idFichaTecnica);
            lstFatoresProducao.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableEdificios(int quintaId, int idEdificio) {
        boolean found = false;

        // Checks if entry exists
        for (Edificios obj : lstEdificios) {
            if (obj.getQuintaId() == quintaId && obj.getIdEdificio() == idEdificio) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Edificios newEntry = new Edificios(quintaId, idEdificio);
            lstEdificios.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableParcelas(int quintaId, int idParcela, String nome, double area, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (Parcelas obj : lstParcelas) {
            if (obj.getQuintaId() == quintaId && obj.getIdParcela() == idParcela) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Parcelas newEntry = new Parcelas(quintaId, idParcela, nome, area, unidadeMedida);
            lstParcelas.add(newEntry);
            addToTableCultivosDefaultByParcela(idParcela, area, unidadeMedida);
        }

        return !found;
    }

    public boolean addToTableArmazens(int quintaId, int edificioId, String nome, double area, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (Armazens obj : lstArmazens) {
            if (obj.getQuintaId() == quintaId && obj.getEdificioId() == edificioId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Armazens newEntry = new Armazens(quintaId, edificioId, nome, area, unidadeMedida);
            lstArmazens.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableGaragens(int quintaId, int edificioId, String nome, double area, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (Garagens obj : lstGaragens) {
            if (obj.getQuintaId() == quintaId && obj.getEdificioId() == edificioId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Garagens newEntry = new Garagens(quintaId, edificioId, nome, area, unidadeMedida);
            lstGaragens.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableMoinhos(int quintaId, int edificioId, String nome, double area, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (Moinhos obj : lstMoinhos) {
            if (obj.getQuintaId() == quintaId && obj.getEdificioId() == edificioId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Moinhos newEntry = new Moinhos(quintaId, edificioId, nome, area, unidadeMedida);
            lstMoinhos.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableSistemasRega(int quintaId, int edificioId, String nome, double area, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (SistemasRega obj : lstSistemasRega) {
            if (obj.getQuintaId() == quintaId && obj.getEdificioId() == edificioId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            SistemasRega newEntry = new SistemasRega(quintaId, edificioId, nome, area, unidadeMedida);
            lstSistemasRega.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableCultivos(int quintaId, int parcelaId, String cultura, String dataInicio, String dataFim, double quantidade, String unidadeMedida, int setor) {
        boolean found = false;

        // Get ID Cultura
        int culturaId = 0;
        for (Culturas cult : lstCulturas) {
            if (cult.getNomeCompleto().equals(cultura)) {
                culturaId = cult.getIdCultura();
            }
        }

        // Checks if entry exists
        for (Cultivos obj : lstCultivos) {
            if (obj.getParcelaId() == parcelaId && obj.getCulturaId() == culturaId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Cultivos newEntry = new Cultivos(quintaId, parcelaId, culturaId, dataInicio, dataFim, quantidade, unidadeMedida, setor);
            lstCultivos.add(newEntry);
        }

        return !found;
    }

    public int addToTableOperacoes(int quintaId, int parcelaId, String cultura, int operadorId, String dataInicio) {
        boolean found = false;

        // Get ID Cultura
        int culturaId = 0;
        for (Culturas cult : lstCulturas) {
            if (cult.getNomeCompleto().equals(cultura)) {
                culturaId = cult.getIdCultura();
            }
            // if not found, will keep 0, and consider "operacao" to the "parcela"
        }

        /*
        // Checks if entry exists
        for (Operacoes obj : lstOperacoes) {
            if (obj.getParcelaId() == parcelaId && obj.getCulturaId() == culturaId
                    && obj.getDataInicio().equals(dataInicio)) {
                found = true;
                break;
            }
        }
        */


            Operacoes newEntry = new Operacoes(operacaoId, quintaId, parcelaId, culturaId, 0, dataInicio, null);
            lstOperacoes.add(newEntry);
            ++operacaoId;


        return (operacaoId - 1);
    }

    public boolean addToTableAplicacoesFatProd(int operacaoId, String fatorProd, double quantidade, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (AplicacoesFatProd obj : lstAplicacoes) {
            if (obj.getOperacaoId() == operacaoId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            AplicacoesFatProd newEntry = new AplicacoesFatProd(operacaoId, fatorProd, quantidade, unidadeMedida);
            lstAplicacoes.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableSementeiras(int operacaoId, double quantidade, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (Sementeiras obj : lstSementeiras) {
            if (obj.getOperacaoId() == operacaoId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Sementeiras newEntry = new Sementeiras(operacaoId, quantidade, unidadeMedida);
            lstSementeiras.add(newEntry);
        }

        return !found;
    }

    public boolean addToTablePlantacoes(int operacaoId, double quantidade, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (Plantacoes obj : lstPlantacoes) {
            if (obj.getOperacaoId() == operacaoId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Plantacoes newEntry = new Plantacoes(operacaoId, quantidade, unidadeMedida);
            lstPlantacoes.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableRegas(int operacaoId, double quantidade, String unidadeMedida, int setor) {
        boolean found = false;

        // Checks if entry exists
        for (Regas obj : lstRegas) {
            if (obj.getOperacaoId() == operacaoId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Regas newEntry = new Regas(operacaoId, quantidade, unidadeMedida, setor);
            lstRegas.add(newEntry);
        }

        return !found;
    }

    public boolean addToTablePodas(int operacaoId, double quantidade, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (Podas obj : lstPodas) {
            if (obj.getOperacaoId() == operacaoId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Podas newEntry = new Podas(operacaoId, quantidade, unidadeMedida);
            lstPodas.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableIncorpsSolo(int operacaoId, double quantidade, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (IncorpsSolo obj : lstIncorpsSolo) {
            if (obj.getOperacaoId() == operacaoId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            IncorpsSolo newEntry = new IncorpsSolo(operacaoId, quantidade, unidadeMedida);
            lstIncorpsSolo.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableColheitas(int operacaoId, double quantidade, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (Colheitas obj : lstColheitas) {
            if (obj.getOperacaoId() == operacaoId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Colheitas newEntry = new Colheitas(operacaoId, quantidade, unidadeMedida);
            lstColheitas.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableMondas(int operacaoId, double quantidade, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (Mondas obj : lstMondas) {
            if (obj.getOperacaoId() == operacaoId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            Mondas newEntry = new Mondas(operacaoId, quantidade, unidadeMedida);
            lstMondas.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableMobilizacesSolo(int operacaoId, double quantidade, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (MobilizacoesSolo obj : lstMobilizacoesSolo) {
            if (obj.getOperacaoId() == operacaoId) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            MobilizacoesSolo newEntry = new MobilizacoesSolo(operacaoId, quantidade, unidadeMedida);
            lstMobilizacoesSolo.add(newEntry);
        }

        return !found;
    }

    public boolean addToTableSetoresRega(int idSetor, int quintaId, int edificioId,
                                         String dataInicio, String dataFim, double caudalMaximo, String unidadeMedida) {
        boolean found = false;

        // Checks if entry exists
        for (SetoresRega obj : lstSetoresRega) {
            if (obj.getIdSetor() == idSetor) {
                found = true;
                break;
            }
        }

        // Adds if it doesn't exist already
        if (!found) {
            SetoresRega newEntry = new SetoresRega(idSetor, quintaId, edificioId, dataInicio,
                    dataFim, caudalMaximo, unidadeMedida);
            lstSetoresRega.add(newEntry);
        }

        return !found;
    }

    ///// SET DATA TO TABLE ////////////////////////////////////////////////////////////////////////////////////////////

    public boolean setSetorRegaToCultivo(int setorId, String nomeParcela, String nomeCompletoCultura) {

        boolean set = false;
        int parcelaId = 0;
        int culturaId = 0;

        for (Parcelas obj : lstParcelas) {
            if (obj.getNome().equals(nomeParcela)) {
                parcelaId = obj.getIdParcela();
            }
        }

        for (Culturas obj : lstCulturas) {
            if (obj.getNomeCompleto().equals(nomeCompletoCultura)) {
                culturaId = obj.getIdCultura();
            }
        }

        // Checks if entry exists
        for (Cultivos obj : lstCultivos) {
            if (obj.getParcelaId() == parcelaId && obj.getCulturaId() == culturaId) {
                set = true;

                obj.setSetor(setorId);

                break;
            }
        }

        return set;
    }

    ///// GET LIST ////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<TiposCultura> getLstTiposCultura() {
        return lstTiposCultura;
    }

    public List<TiposConsumo> getLstTiposConsumo() {
        return lstTiposConsumo;
    }

    public List<EspeciesVegetais> getLstEspeciesVegetais() {
        return lstEspeciesVegetais;
    }

    public List<Culturas> getLstCulturas() {
        return lstCulturas;
    }

    public List<Produtos> getLstProdutos() {
        return lstProdutos;
    }

    public List<Ciclos> getLstCiclos() {
        return lstCiclos;
    }

    public List<UnidadesMedida> getLstUnidadesMedida() {
        return lstUnidadesMedida;
    }

    public List<Quintas> getLstQuintas() {
        return lstQuintas;
    }

    public List<Operadores> getLstOperadores() {
        return lstOperadores;
    }

    public List<Formulacoes> getLstFormulacoes() {
        return lstFormulacoes;
    }

    public List<FatoresProducao> getLstFatorProd() {
        return lstFatorProd;
    }

    public List<FichasTecnicas> getLstFichasTecnicas() {
        return lstFichasTecnicas;
    }

    public List<Substancias> getLstSubstancias() {
        return lstSubstancias;
    }

    public List<ComponentesFT> getLstComponentesFT() {
        return lstComponentesFT;
    }

    public List<FatoresProducao> getLstFatoresProducao() {
        return lstFatoresProducao;
    }

    public List<Edificios> getLstEdificios() {
        return lstEdificios;
    }

    public List<Parcelas> getLstParcelas() {
        return lstParcelas;
    }

    public List<Armazens> getLstArmazens() {
        return lstArmazens;
    }

    public List<Garagens> getLstGaragens() {
        return lstGaragens;
    }

    public List<Moinhos> getLstMoinhos() {
        return lstMoinhos;
    }

    public List<SistemasRega> getLstSistemasRega() {
        return lstSistemasRega;
    }

    public List<Cultivos> getLstCultivos() {
        return lstCultivos;
    }

    public List<Operacoes> getLstOperacoes() {
        return lstOperacoes;
    }

    public Collection<Operacoes> getObjFromList(int id) {
        LinkedList<Operacoes> list = new LinkedList<>();
        for (Operacoes obj : getLstOperacoes()) {
            if (obj.getCulturaId() == id) {
                list.push(obj);
            }
        }
        return list;
    }

    public List<AplicacoesFatProd> getLstAplicacoes() {
        return lstAplicacoes;
    }

    public List<Sementeiras> getLstSementeiras() {
        return lstSementeiras;
    }

    public List<Plantacoes> getLstPlantacoes() {
        return lstPlantacoes;
    }

    public List<Regas> getLstRegas() {
        return lstRegas;
    }

    public List<Podas> getLstPodas() {
        return lstPodas;
    }

    public List<IncorpsSolo> getLstIncorpsSolo() {
        return lstIncorpsSolo;
    }

    public List<Colheitas> getLstColheitas() {
        return lstColheitas;
    }

    public List<SetoresRega> getLstSetoresRega() {
        return lstSetoresRega;
    }

    public List<Mondas> getLstMondas() {
        return lstMondas;
    }

    public List<MobilizacoesSolo> getLstMobilizacoesSolo() {
        return lstMobilizacoesSolo;
    }
}
