# Mineger
Sistema de gerenciamento de modificações de Minecraft, desenvolvido como Trabalho Final Anual do segundo ano do curso de informática do IFPR - Campus Paranaguá; da turma INFO19

# Dependencias
1. JDK 11 ou superior
2. SDK JavaFX (Opcional)
3. MariaDB / MySQL
4. Maven

# Instruções
1. Clone este repositorio
2. Use o Maven para baixar as dependências descritas em pom.xml
3. Crie um schema no bando de dados MariaDB / MySQL
4. Execute o script mineger.sql no schema criado
5. Crie um usuário user@localhost com permissões no schema criado (necessário pelo definer dos triggers inclusos no script)
6. Crie um arquivo config.properties na pasta resources
7. Usando o exemplo incluso, preencha os dados de acordo com o seu ambiente
8. Compile o programa
    - Caso esteja usando maven, use o plugin javafx para compilar o programa com os modulos necessários, ou;
    - Caso esteja usando o compilador do JDK, inclua os modulos do SDK JavaFX nos argumentos da JVM
