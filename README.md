# Application Mashup CRM

Ce projet est une application "Mashup" qui combine différents services web (REST et RPC).
L'application offre une vue unifiée des données clients provenant de deux sources

1. InternalCRM : Un CRM interne géré par un service RPC (Apache Thrift).
2. Salesforce : Un CRM externe accessible via API REST.

# Prérequis

- Java SE 21
- Apache Thrift 0.22.0
- Un compte développeur Salesforce (pour l'accès API).

# Configuration

Avant de compiler et lancer l'application, vous devez configurer l'accès à Salesforce.
1. Accédez au dossier des ressources du module virtualcrm `virtualcrm/src/main/resources/`
2. Renommez ou copiez le fichier `example.salesforce.properties` en `salesforce.properties`.
3. Ouvrez ce fichier et renseignez vos identifiants
```properties
CLIENT_ID=votre_client_id
CLIENT_SECRET=votre_client_secret
USERNAME=votre_email_salesforce
PASSWORD=votre_mot_de_passe
API_VERSION=v52.0
```

# Compilation

Le projet utilise Gradle pour la compilation. Utilisez ./gradlew (sous Linux/Mac) ou gradlew.bat (sous Windows).

Vous devez tout d'abord compiler le Client et l'outil LeadMerger.
```bash
./gradlew :client:shadowJar :leadmerger:shadowJar
```

# Exécution des Services

Le projet utilise le plugin Gretty pour lancer les applications web (internalcrm et virtualcrm) dans un conteneur Tomcat embarqué.
Pour lancer les services (InternalCRM + VirtualCRM) :
```bash
./gradlew farmRun
```
Laissez ce terminal ouvert pour maintenir le serveur en marche.

# Exécution du Client et de l'outil LeadMerger

Ouvrez un nouveau terminal pour exécuter les commandes suivantes.

### Client

Afficher l’aide :

```bash
java -jar client/build/libs/client-all.jar
```

Exemple de commandes :

```bash
java -jar client-all.jar findLeads 300000000.0 400000000.0 NC
java -jar client-all.jar findLeadsByDate 2025-10-01 2025-10-05
```

### Outil LeadMerger

```bash
java -jar leadmerger/build/libs/leadmerger-all.jar
```
