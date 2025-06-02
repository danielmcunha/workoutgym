WorkoutApp é um aplicativo desenvolvido para acompanhar e gerenciar treinos de usuários, ideal para personal trainers e seus alunos. O app permite o registro, gerenciamento e acompanhamento de rotinas de treino, com funcionalidades para agendamento, customização de exercícios e rastreamento de progresso.

Tecnologias Utilizadas
Kotlin Multiplatform Mobile (KMM): Utilizado para garantir a compatibilidade e compartilhamento de código entre plataformas Android e iOS. Com KMM, a maior parte da lógica de negócio é compartilhada, garantindo consistência entre as versões do aplicativo para ambas as plataformas.

Jetpack Compose: Framework moderno de UI para Android, utilizado para criar interfaces de usuário nativas de forma declarativa e eficiente. Compose facilita o desenvolvimento de interfaces responsivas e intuitivas com menos código e mais flexibilidade.

Firebase: Para armazenamento de dados em tempo real, autenticação e sincronização de dados entre dispositivos. O app usa Firestore para armazenar informações dos treinos, usuários e histórico de atividades.

Kotlin: Linguagem de programação moderna que proporciona concisão e segurança, sendo a base para o desenvolvimento do projeto tanto para Android quanto para o código compartilhado em KMM.

Funcionalidades
Cadastro e Login de Usuários: Os usuários podem se cadastrar e fazer login para acessar suas informações personalizadas.

Criação e Gerenciamento de Exercícios: O usuário pode criar, editar e organizar exercícios, com possibilidade de customizar séries, repetições e pesos.

Listagem e Edição de Treinos: O app oferece uma tela de listagem dos treinos, onde o usuário pode visualizar detalhes, reordenar e editar exercícios.

Relatórios de treino: O app tem a possibilidade de visualizar relatórios de treinos passados, com gráficos e histórico das execuções

Agendamento de Treinos: Personal trainers podem agendar treinos para seus alunos, definindo a frequência e a localização dos compromissos.

Recorrência de Treinos: O app permite definir a recorrência dos treinos (diária, semanal, etc.), otimizando o planejamento de longo prazo.

Persistência de Dados: Toda a informação do treino, progresso do aluno e configurações do usuário são salvas no Firebase, permitindo sincronização em tempo real e armazenamento seguro.

Funcionalidades em Desenvolvimento
Funcionalidade de Notificações: Alertas e lembretes para os treinos agendados.

Análises e Relatórios: Visualização do progresso de treinos e comparação de desempenho.

Integração com Wearables: Futuras integrações com dispositivos vestíveis para monitoramento em tempo real (ex: frequência cardíaca, calorias queimadas).
