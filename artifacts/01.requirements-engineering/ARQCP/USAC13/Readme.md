# USAC09 - Função que lê os últimos ficheiros, transforma valores em números reais e cria um ficheiro no diretório adequado.

## 1. Requirements Engineering
### 1.1. User Story Description
_"Como Gestor Agrícola, pretendo que o sistema mantenha um ficheiro atualizado com os últimos dados recolhidos de cada sensor."_

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**
> N/A

**From the client clarifications:**

> **Q:** N/A
>  
> **A:** N/A

### 1.3. Acceptance Criteria

* **AC1:** Deverá ser possível receber dados de vários tipos de sensores.
* **AC2:** Deverá ser possível receber dados de um número variável de sensores.
* **AC3:** Apenas deverá aceitar valores de sensores existentes.
* **AC4:** Identificar último ficheiro de dados recolhidos.

### 1.4. Found out Dependencies

* Depende da existência de dados criados pelo ProcessadorDeDados.

### 1.5 Input and Output Data

**Input Data:**
* N/A

**Output Data:**
* Retorna 0 se executado com sucesso e um qualquer outro número em caso de insucesso.
* Ficheiro CSV com dados da última leitura do ProcessadorDeDados registada.

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](SSD.svg "System Sequence Diagram")

### 1.7 Other Relevant Remarks

*  N/A.