# IBM Security Verify SDK pour Android

Ce dépôt est destiné au développement actif du kit de développement logiciel IBM Security Verify pour Android.

## Mise en route
Chaque kit de développement logiciel (SDK) est séparé et vous pouvez choisir celui qui vous convient le mieux, au lieu d'un seul et unique kit SDK IBM Security Verify. Pour démarrer avec un SDK spécifique, consultez le fichier README.md situé dans le dossier du projet concerné.

## Prérequis
* Le SDK est écrit en Kotlin.
* Pour utiliser le SDK adaptatif ou multi-facteurs, un locataire [IBM Security Verify](https://www.ibm.com/products/verify-for-consumer-iam) ou [IBM Security Verify Access](https://www.ibm.com/au-en/products/verify-access) est nécessaire.

## Kits de développement logiciel
Les SDK suivants sont actuellement proposés dans le paquet :

| kit de développement de logiciels | Description |
|---|---|
| [Cœur](/sdk/core) | Le SDK IBM Security Verify Core fournit des fonctionnalités d'appui aux autres SDK, telles que la journalisation, les services Keystore, les structures d'erreur abstraites et les fonctions utilitaires. |
| [Authentification](/sdk/authentication) | Le SDK IBM Security Verify Authentication est une implémentation d'OAuth 2.0 et d'OIDC ciblant les cas d'utilisation mobiles. |
| [Adaptative](/sdk/adaptive) | Le SDK adaptatif IBM Security Verify permet d'évaluer les dispositifs. Les défis en matière d'authentification et d'autorisation peuvent être évalués sur la base des politiques de risque du nuage. |


## Télécharger
Les artefacts du SDK sont disponibles [ici.](/releases)

### Maven
Stockez les fichiers dans votre dépôt maven local. Il est généralement situé à l'adresse `~/.m2/repository/`.

Ajoutez `mavenLocal` en haut de votre liste de dépôts :

```
buildscript {
    repositories {
        mavenLocal()
    }
}
```

Ajoutez cette ligne pour chaque SDK à votre fichier `build.gradle` (au niveau de l'application):

    implementation 'com.github.ibm-security-verify:verify-sdk-android:<module>:<version>'


### Jitpack
Dans le fichier build.gradle de votre projet, ajoutez JitPack comme dépôt :

    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
            }
        }

Ensuite, pour chaque SDK, ajoutez la ligne suivante dans votre fichier build.gradle au niveau du module :

    implementation 'com.github.ibm-security-verify:verify-sdk-android:core:v3.0.0'

Remplacez le SDK particulier - `core` dans cet exemple - par le nom des SDK que vous souhaitez intégrer.

Vous pouvez ensuite utiliser le site web de JitPack https://jitpack.io/ pour générer une déclaration de compilation, basée sur l'url GitHub du SDK.

### Manuellement à partir de la version GitHub
Téléchargez les fichiers SDK requis à partir de https://github.com/ibm-security-verify/verify-sdk-android/releases/latest stockez-les dans le dossier lib et synchronisez votre projet.
<!-- v2.3.7 : caits-prod-app-gp_webui_20241231T140322-15_en_fr -->