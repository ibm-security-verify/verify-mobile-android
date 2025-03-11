# IBM Security Verify FIDO2™ Exemple d'application pour Android

L'exemple d'application FIDO2 est une application mobile de bout en bout qui utilise les fonctionnalités du serveur FIDO2 dans IBM Security Verify Access (sur site) et IBM Security Verify (dans le nuage).


## Mise en route
Cette démo fonctionne avec une instance IBM Security Verify Access, hébergée sur https://fidointerop.securitypoc.com. Un IBMid est nécessaire pour y accéder.

1. Clonez ce dépôt et ouvrez-le dans Android Studio
1. Créer une "configuration Run/Debug" pour le module `fido2_demo`
1. Démarrer le module `fido2_demo`
1. Connectez-vous à https://fidointerop.securitypoc.com allez à `Account settings` et copiez votre `Access Token`
1. Dans l'application de démonstration, cliquez sur `Get Started` : <br/><img src="./fido2demo-1-start.png" width="30%">
1. Collez votre jeton d'accès, attribuez un surnom à votre enregistrement et cliquez sur `Register` : <br/><img src="./fido2demo-2-register.png" width="30%">
1. Une boîte de dialogue d'authentification biométrique similaire à [celle-ci](https://developer.android.com/training/sign-in/biometric-auth#display-login-prompt) s'affiche
1. Compléter l'authentification
1. Une fois l'enregistrement réussi, l'application redirige vers l'écran `Authenticate` :
   <br/><img src="./fido2demo-3-authenticate.png" width="30%">
1. Modifiez le site `Transaction message` à votre guise. L'activation ou la désactivation de cette fonction permet de pré-remplir automatiquement un texte aléatoire.
1. Cliquez sur `Authenticate`
1. Une boîte de dialogue d'authentification biométrique s'affiche à nouveau. Cette fois, le message de transaction de l'étape précédente sera affiché dans l'en-tête de la boîte de dialogue.
1. Compléter l'authentification
1. Lorsque la vérification est réussie, l'application affiche le résultat : <br/><img src="./fido2demo-4-close.png" width="30%">

## Licence
Ce paquet contient du code sous licence MIT (la "Licence"). Vous pouvez consulter la licence dans le fichier [LICENSE](../../LICENSE) de ce paquet.
<br/><br/>
FIDO™ et FIDO2™ sont des marques (déposées dans de nombreux pays) de FIDO Alliance, Inc.

<!-- v2.3.7 : caits-prod-app-gp_webui_20241231T140332-9_en_fr -->