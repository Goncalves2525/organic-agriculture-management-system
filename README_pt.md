# Sistema de Gestão para Agricultura Biológica

Este projeto consiste num sistema integrado para apoiar a gestão de uma instalação agrícola em modo de produção biológico (MPB). O sistema permite gerir parcelas, culturas, operações agrícolas, regas, sensores, comercialização de produtos e outros componentes essenciais para a agricultura biológica.

## Visão Geral

O sistema é composto por diferentes módulos interligados:

1. **Gestão de Operações Agrícolas** - Registo e consulta de operações como semeadura, monda, poda, colheita e aplicação de fatores de produção
2. **Sistema de Rega e Fertirrega** - Controlo e gestão de regas e aplicação de fertilizantes
3. **Gestão de Sensores** - Monitorização de dados meteorológicos e de solo
4. **Gestão de Distribuição** - Rotas e logística para distribuição de cabazes agrícolas
5. **Gestão de Armazém** - Controlo de temperatura e condições de armazenamento

## Componentes do Sistema

O sistema é desenvolvido com uma arquitetura modular:

### 1. Sensores Management
Implementado em C e Assembly para aquisição e processamento de dados de sensores meteorológicos e de solo.

### 2. Farm Operations Management
Componente principal baseado em Oracle PL/SQL para gestão de todas as operações agrícolas.

### 3. Basket Management
Módulo em Java para gestão de cabazes e rotas de distribuição.

### 4. Farm Coordination
Aplicação principal em Java que integra todos os componentes e fornece interface para utilizadores.

## Tecnologias Utilizadas

- **Java** - Aplicação principal e interface de utilizador
- **Oracle PL/SQL** - Base de dados e procedimentos
- **C/Assembly** - Gestão de sensores e hardware
- **Estruturas de Dados Avançadas** - Grafos e algoritmos para otimização de rotas

## Funcionalidades Principais

### Caderno de Campo
- Registo de operações agrícolas
- Monitorização de aplicação de fatores de produção
- Registo de colheitas e produtividade
- Planeamento de operações

### Sistema de Rega
- Controlo de setores de rega
- Programação de ciclos de rega
- Aplicação de fertirrega
- Monitorização de consumo de água

### Sensores
- Leitura de temperatura, humidade, velocidade do vento
- Análise de dados meteorológicos
- Monitorização de humidade e pH do solo
- Alertas para condições extremas

### Gestão de Cabazes
- Determinação de rotas otimizadas
- Identificação de hubs ideais
- Cálculo de capacidade de transporte
- Agendamento de entregas

## Instalação e Configuração

### Pré-requisitos
- Java JDK 11 ou superior
- Oracle Database 19c ou superior
- Maven 3.6 ou superior
- Kit de desenvolvimento para Raspberry Pi Pico W
- Ambiente de desenvolvimento C (GCC, Make)
- IDE (recomendado: IntelliJ IDEA ou NetBeans)

### Tutorial de Instalação Passo a Passo

#### 1. Configuração do Ambiente Java

##### Linux (Ubuntu/Debian)
```bash
# Instalar o Java JDK 11
sudo apt install openjdk-11-jdk

# Verificar a instalação
java -version
javac -version

# Instalar o Maven
sudo apt install maven

# Verificar a instalação do Maven
mvn -version
```

##### macOS
```bash
# Usando Homebrew para instalar o Java JDK 11
brew tap adoptopenjdk/openjdk
brew install --cask adoptopenjdk11

# Verificar a instalação
java -version
javac -version

# Instalar o Maven
brew install maven

# Verificar a instalação do Maven
mvn -version
```

##### Windows
1. Faça o download do Java JDK 11 em: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
2. Execute o instalador e siga as instruções
3. Configure as variáveis de ambiente:
    - Adicione `JAVA_HOME` às variáveis de ambiente do sistema: `C:\Program Files\Java\jdk-11.0.X`
    - Adicione `%JAVA_HOME%\bin` à variável PATH

4. Faça o download do Maven em: https://maven.apache.org/download.cgi
5. Extraia para um diretório, como `C:\Program Files\Apache\maven`
6. Configure as variáveis de ambiente:
    - Adicione `M2_HOME` às variáveis de ambiente do sistema: `C:\Program Files\Apache\maven`
    - Adicione `%M2_HOME%\bin` à variável PATH

7. Verifique a instalação:
```cmd
java -version
javac -version
mvn -version
```

#### 2. Configuração da Base de Dados Oracle

##### Linux (Ubuntu/Debian)
1. Faça o download do Oracle Database Express Edition em: https://www.oracle.com/database/technologies/xe-downloads.html
```bash
# Instalar as dependências necessárias
sudo apt-get install alien libaio1

# Converter o RPM para DEB e instalar (supondo que baixou a versão RPM)
sudo alien -i oracle-database-xe-21c-1.0-1.x86_64.rpm

# Configurar a base de dados
sudo /etc/init.d/oracle-xe-21c configure
```

##### macOS
Oracle Database não é oficialmente suportado no macOS. Opções alternativas:
1. Usar Docker:
```bash
# Baixar e executar a imagem Docker do Oracle Database
docker pull container-registry.oracle.com/database/express:latest
docker run -d -p 1521:1521 -p 5500:5500 -e ORACLE_PWD=password container-registry.oracle.com/database/express:latest
```

2. Usar uma máquina virtual com Linux ou Windows para executar o Oracle Database

##### Windows
1. Faça o download do Oracle Database Express Edition em: https://www.oracle.com/database/technologies/xe-downloads.html
2. Execute o instalador e siga as instruções
3. Durante a instalação, defina uma senha para o utilizador SYSTEM

Para todos os sistemas operativos, após a instalação, crie um utilizador para a aplicação:
```sql
-- Conecte-se como SYSTEM ou SYS
sqlplus SYSTEM/sua_senha@localhost:1521/XE

-- Crie um novo utilizador
CREATE USER farm_manager IDENTIFIED BY password;
GRANT CONNECT, RESOURCE, DBA TO farm_manager;
```

Execute os scripts de criação da base de dados:

##### Linux/macOS
```bash
cd database/scripts
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_schema.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_tables.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_procedures.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_triggers.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @insert_initial_data.sql
```

##### Windows
```cmd
cd database\scripts
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_schema.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_tables.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_procedures.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @create_triggers.sql
sqlplus farm_manager/password@localhost:1521/XEPDB1 @insert_initial_data.sql
```

#### 3. Configuração do Componente de Sensores

##### Linux (Ubuntu/Debian)
```bash
# Instalar ferramentas de desenvolvimento
sudo apt install build-essential cmake gcc-arm-none-eabi libnewlib-arm-none-eabi

# Instalar SDK do Raspberry Pi Pico
git clone https://github.com/raspberrypi/pico-sdk.git
cd pico-sdk
git submodule update --init
export PICO_SDK_PATH=$(pwd)
```

##### macOS
```bash
# Usando Homebrew
brew install cmake
brew install --cask gcc-arm-embedded

# Instalar SDK do Raspberry Pi Pico
git clone https://github.com/raspberrypi/pico-sdk.git
cd pico-sdk
git submodule update --init
export PICO_SDK_PATH=$(pwd)
```

##### Windows
1. Instale o CMake: https://cmake.org/download/
2. Instale o ARM GCC Toolchain: https://developer.arm.com/tools-and-software/open-source-software/developer-tools/gnu-toolchain/gnu-rm/downloads
3. Instale o Visual Studio Build Tools (ou Visual Studio completo)
4. Clone o repositório do SDK do Raspberry Pi Pico:
```cmd
git clone https://github.com/raspberrypi/pico-sdk.git
cd pico-sdk
git submodule update --init
set PICO_SDK_PATH=%cd%
```

Para todos os sistemas operativos, compile o código para o microcontrolador:

##### Linux/macOS
```bash
cd SensorsManagement/ColetorDeDados
mkdir build && cd build
cmake ..
make
```

##### Windows
```cmd
cd SensorsManagement\ColetorDeDados
mkdir build
cd build
cmake -G "NMake Makefiles" ..
nmake
```

Carregue o firmware para o Raspberry Pi Pico W:
1. Mantenha pressionado o botão BOOTSEL ao ligar o Pico W ao computador
2. Copie o ficheiro .uf2 gerado para o volume que aparece

Compile os componentes de processamento de dados:

##### Linux/macOS
```bash
cd SensorsManagement/ProcessadorDeDados
make
   
cd SensorsManagement/SaidaDeDados
make
```

##### Windows
```cmd
cd SensorsManagement\ProcessadorDeDados
nmake -f Makefile.win
   
cd SensorsManagement\SaidaDeDados
nmake -f Makefile.win
```

#### 4. Configuração do Projeto Principal

##### Linux/macOS/Windows
1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/farm-management.git
cd farm-management
```

2. Configure o ficheiro de propriedades:
```bash
# Linux/macOS
cp config/application.properties.template config/application.properties

# Windows
copy config\application.properties.template config\application.properties
```

3. Edite o ficheiro `config/application.properties` com as informações de conexão à base de dados:
```properties
# Database Connection
database.inet=localhost
database.port=1521
database.sid=XEPDB1
database.user=farm_manager
database.password=password
   
# Sensors Configuration
sensors.input.directory=/path/to/sensor/data
sensors.output.directory=/path/to/processed/data
```

4. Compile o projeto com Maven:
```bash
# Linux/macOS/Windows
mvn clean install
```

#### 5. Execução do Sistema

##### Linux
```bash
# Iniciar a base de dados Oracle
sudo systemctl start oracle-xe

# Iniciar o componente de sensores
cd SensorsManagement/ProcessadorDeDados
./processadordedados /dev/ttyUSB0 config output 50

# Iniciar o componente de saída de dados
cd SensorsManagement/SaidaDeDados
./saidadedados output farm_data 60000

# Executar a aplicação principal
cd <diretório-do-projeto>
java -jar target/farm-management.jar
```

##### macOS
```bash
# Iniciar a base de dados Oracle (se estiver usando Docker)
docker start <container-id>

# Iniciar o componente de sensores
cd SensorsManagement/ProcessadorDeDados
./processadordedados /dev/tty.usbserial config output 50

# Iniciar o componente de saída de dados
cd SensorsManagement/SaidaDeDados
./saidadedados output farm_data 60000

# Executar a aplicação principal
cd <diretório-do-projeto>
java -jar target/farm-management.jar
```

##### Windows
```cmd
# Iniciar a base de dados Oracle
# Use o Oracle Database Configuration Assistant ou serviços do Windows

# Iniciar o componente de sensores
cd SensorsManagement\ProcessadorDeDados
processadordedados.exe COM3 config output 50

# Iniciar o componente de saída de dados
cd SensorsManagement\SaidaDeDados
saidadedados.exe output farm_data 60000

# Executar a aplicação principal
cd <diretório-do-projeto>
java -jar target\farm-management.jar
```

#### 6. Verificação da Instalação

1. Teste a conexão com a base de dados através da opção "Database Connection Test" no menu principal
2. Verifique a receção de dados dos sensores através da opção "Dados dos Sensores" no menu principal
3. Importe os dados de exemplo através da opção "Import legacy data" se necessário

#### 7. Resolução de Problemas Comuns

- **Erro de conexão com a base de dados**: Verifique as configurações no ficheiro application.properties e se o serviço Oracle está em execução
- **Sensores não detetados**:
    - **Linux/macOS**: Verifique as permissões da porta serial (`sudo chmod 666 /dev/ttyUSB0` ou equivalente)
    - **Windows**: Verifique o número correto da porta COM no Gestor de Dispositivos
- **Erros de compilação**: Verifique a versão do JDK e se todas as dependências estão instaladas corretamente
- **Problemas com Oracle no macOS**: Considere usar uma máquina virtual ou Docker como alternativa

## Estrutura do Projeto

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── repository/       # Acesso a dados
│   │   │   ├── tables/           # Classes de entidades
│   │   │   ├── ui/               # Interface de utilizador
│   │   │   ├── utils/            # Utilitários
│   │   │   └── App.java          # Ponto de entrada
│   │   └── resources/
│   │       └── config/           # Ficheiros de configuração
│   └── test/                     # Testes unitários
├── SensorsManagement/            # Componente de sensores (C/Assembly)
│   ├── ColetorDeDados/
│   ├── ProcessadorDeDados/
│   └── SaidaDeDados/
└── database/                     # Scripts e modelos de BD
    └── scripts/
```

## Menu Principal da Aplicação

O menu principal permite acesso às seguintes funcionalidades:
- Teste de Conexão à Base de Dados
- Importação de dados legados
- Gestão de Sementeiras
- Gestão de Mondas
- Aplicações de Fatores de Produção
- Registo de Colheitas
- Registo de Regas e Fertirrega
- Consulta de Logs de Operações
- Gestão de Distribuição (GFH Manager)
- Visualização de Dados de Sensores
- Anulação de Operações

## Contribuição

O projeto segue uma metodologia ágil baseada em SCRUM, com sprints de quatro semanas. O desenvolvimento é feito segundo uma abordagem de Test-Driven Development (TDD).

## Licença

Este projeto foi desenvolvido como parte do curso de Licenciatura em Engenharia Informática (LEI) do Instituto Superior de Engenharia do Porto (ISEP).
