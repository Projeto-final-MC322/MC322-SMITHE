# SMITHE - Study Management in Tech Engineering
Projeto da disciplina MC322 1s2026 dos alunos Guilherme Arthur, Mariana Fonsechi, Victória Leite e Vitor Ribeiro Lima.

O **SMITHE** é um aplicativo desktop de gerenciamento de estudos desenvolvido em Java com interface gráfica em JavaFX. Criado com foco nas necessidades e na pesada rotina de disciplinas práticas e teóricas de estudantes de Engenharia de Computação da Unicamp, o projeto visa otimizar o aprendizado e a retenção de conteúdo através de técnicas de estudo consolidadas e gamificação.

## 🚀 Funcionalidades

* **Flashcards & Revisão Espaçada:** Crie cartões de estudo (frente e verso) e deixe o algoritmo integrado calcular automaticamente o melhor momento para revisá-los, maximizando a memorização a longo prazo.
* **Mapas Mentais:** Estruture visualmente o conhecimento por meio de nós e conexões para entender sistemas complexos.
* **Resumos:** Organize suas anotações e materiais de estudo de forma estruturada e centralizada.
* **Timer Pomodoro:** Gerencie seu tempo de foco e pausas para manter a produtividade sem fadiga.
* **Desempenho:** Ganhe pontos (BZ) e suba de nível no aplicativo conforme conclui revisões e atinge metas de estudo.
* **Modo Claro / Escuro:** Interface totalmente adaptável com troca de tema dinâmica para maior conforto visual.

## 🛠️ Tecnologias Utilizadas

* **Linguagem Base:** Java
* **Interface Gráfica:** JavaFX (Estruturação com FXML e estilização nativa com CSS)
* **Build Tool & Gerenciamento de Dependências:** Gradle
* **Testes:** JUnit para testes unitários e de integração
* **Armazenamento:** Serialização local de dados utilizando JSON

## 📂 Estrutura do Projeto

O código-fonte segue uma arquitetura orientada a componentes que separa as responsabilidades de negócio e interface:

* `GUI/`: Controladores das telas JavaFX e captura de interações do usuário.
* `logica/`: Sistemas centrais do aplicativo, como `GerenciadorDeRevisao`, cálculo de `Revisao_Espacada` e motor do `TimerPomodoro`.
* `modelo/`: Definições das entidades principais (`Flashcard`, `Resumo`, `MentalMap`, `EstatisticaDesempenho`).
* `save/`: Classes dedicadas à persistência e manipulação de arquivos (JSON) para salvar o estado e progresso do usuário de forma contínua.

## ⚙️ Como Executar

Certifique-se de ter o **JDK** adequado configurado e o **Gradle** instalado na sua máquina.

1. Navegue até a pasta raiz do projeto:
   ```bash
   cd ~/Estudos/SMITHE
   ```
2. Limpe os builds antigos e recompile o projeto:
   ```bash
   gradle clean build
   ```
3. Execute o aplicativo:
   ```bash
   gradle run
   ```

## 🧪 Testes

Para visualizar e executar a suíte de testes unitários do projeto e garantir que as entidades do domínio e controladores estão funcionando corretamente:
```bash
gradle test
```
Os relatórios detalhados dos testes serão gerados em `app/build/reports/tests/test/index.html`.


