# Semana 01
Foram criadas documentações que vão auxiliar no desenvolvimento do projeto:
- Documento de visão
- Diagrama de classe
- Diagrama relacional

Essa semana foi resumida em planejamento, para facilitar o desenvolvimento futuro.
Foi gerado também um arquivo SQL com quase 450.000 registros que servirá como repositório de leitura para implementar algumas funcionalidades do sistema; o sistema deve ser capaz de lidar com o volume grande de registros no banco de dados.

# Semana 02
Foram adicionadas os modelos que representam as entidades do banco de dados que acompanha aplicação. Logo essas mudanças estarão prontas para uma revisão e por fim para integração no codigo fonte.

O desenvolvimento ainda está um pouco lento, pois essa semana foi reservada para concluir atividades referentes à disciplina de banco de dados, então o banco de dados foi concluido, e então foi produzido um relatório contento a descrição da atividade, bem como a descrição completa do banco de dados e mais outros requisitos da disciplina.

# Semana 03
Foi implementada a camada DAO como prevista no diagrama de classe. Algumas porções da camada de entidades foi refatorada para melhor representar a necessidade do sistema.

# Semana 04
- Foi adicionada uma tela principal para a aplicação, usando javafx e FXML;
- A cadamada de entidades foi refatorada para evitar avisos do compilador;
- Foi implementada inserção de ModModules pela interface de usuario;
- Foi implementada inserção da relação entre ModModules e ModFile pela interface de usuario;
- Foi implementada a instalação de ModFiles;
- Boa parte do código foi refatorado para aumentar a legibilidade, seguindo os principios do Clean Code;
- Foi criado um utilitario como wrapper para o JDBC chamado Clarice;
- Foi implementado cadastro de login de usuario;
