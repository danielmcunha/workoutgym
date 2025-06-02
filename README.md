# WorkoutApp

**WorkoutApp** é um aplicativo projetado para personal trainers e alunos, visando o acompanhamento e gerenciamento de treinos. Com funcionalidades de criação e organização de exercícios, agendamento de treinos e rastreamento de progresso, este app proporciona uma experiência completa para a gestão de rotinas de treino.

## Tecnologias Utilizadas

### 🛠 **Kotlin Multiplatform Mobile (KMM)**
O **KMM** é utilizado para garantir a compatibilidade entre as plataformas **Android** e **iOS**. A maior parte da lógica de negócio é compartilhada entre as duas plataformas, proporcionando consistência e eficiência no desenvolvimento.

### 📱 **Jetpack Compose**
O **Jetpack Compose** foi utilizado para o desenvolvimento da interface de usuário no Android, oferecendo uma maneira moderna e declarativa de criar UIs, com menos código e maior flexibilidade.

### ☁️ **Firebase**
O **Firebase** é usado para o armazenamento em tempo real dos dados dos usuários, treinos e exercícios. Ele também é responsável pela autenticação e pela sincronização de dados entre dispositivos.

### 💻 **Kotlin**
A linguagem **Kotlin** é a base de todo o código do projeto, proporcionando concisão, segurança e interoperabilidade entre as plataformas Android e iOS.

## Funcionalidades

- **Cadastro e Login de Usuários**
  - Permite que usuários se cadastrem e façam login para acessar suas informações e treinos personalizados.

- **Criação e Gerenciamento de Exercícios**
  - O usuário pode criar, editar e organizar exercícios, configurando séries, repetições e pesos.

- **Listagem e Edição de Treinos**
  - Tela de listagem de treinos com funcionalidades de visualização, edição e reorganização dos exercícios.

- **Agendamento de Treinos**
  - Permite que personal trainers agendem treinos para seus alunos, com opções de definir a frequência e localização do compromisso.

- **Recorrência de Treinos**
  - Definição de recorrência para treinos, como treinos diários ou semanais, otimizando o planejamento a longo prazo.

- **Persistência de Dados**
  - Todos os dados do treino e histórico de progressos são salvos no **Firebase**, permitindo a sincronização em tempo real entre dispositivos.
