# Contribuer

Consultez les lignes directrices suivantes pour soumettre des questions, des problèmes ou des modifications à ce référentiel.

## Style de codage
Ce SDK suit un style de codage basé sur les [conventions de codage](https://kotlinlang.org/docs/coding-conventions.html/) pour le code source dans le langage de programmation Kotlin.

## Enjeux et questions

Si vous rencontrez un problème, si vous avez une question ou si vous souhaitez suggérer une amélioration du SDK IBM Security Verify, n'hésitez pas à soumettre une [demande](https://github.com/ibm-security-verify/verify-sdk-android/issues).
Avant cela, veuillez rechercher des problèmes similaires. Il est possible que quelqu'un ait déjà rencontré ce problème.

## Demandes d'extraction

Si vous souhaitez contribuer au dépôt, voici un guide rapide :

1. Mettre le référentiel en fourche
2. Développez et testez vos modifications de code :
   * Suivre le style de codage tel que documenté ci-dessus
   * Veuillez ajouter un ou plusieurs tests pour valider vos changements.
3. Assurez-vous que tout est construit/testé proprement.
4. Validez vos modifications
5. Poussez vers votre fork et soumettez une pull request à la branche `main`


## Générer de la documentation

Pour générer les documents HTML d'un composant SDK, exécutez `./gradlew <component>:dokkaJavadoc` dans le répertoire `sdk` du projet. Consultez ensuite les documents dans le répertoire `./<component>/build/dokka/javadoc` :

```
open index.html
```


## Ressources supplémentaires

* [Documentation générale de GitHub](https://help.github.com/)
* [Documentation sur les demandes d'extraction GitHub](https://help.github.com/send-pull-requests/)


# Certificat d'origine du développeur 1.1

En apportant une contribution à ce projet, je certifie que :

(a) La contribution a été créée en tout ou en partie par moi et j'ai le droit de la soumettre sous une licence open source
j'ai le droit de la soumettre sous la licence open source
indiquée dans le fichier ; ou

(b) La contribution est basée sur des travaux antérieurs qui, à ma connaissance, sont couverts par une licence open source appropriée
à ma connaissance, est couvert par une licence open source appropriée et j'ai le droit, en vertu de cette licence, de soumettre ce travail
et j'ai le droit, en vertu de cette licence, de soumettre ce travail avec des modifications, qu'elles soient créées en totalité ou en partie
travail avec des modifications, qu'elles aient été créées en tout ou en partie par moi, sous la même licence de source ouverte (à moins que je n'aie pas
sous la même licence open source (à moins que je ne sois autorisé à le faire sous une autre licence)
sous une autre licence), comme indiqué dans le fichier ; ou
dans le fichier ; ou

(c) La contribution m'a été fournie directement par une autre personne qui a certifié (a), (b) ou (c) et je n'ai pas modifié la contribution
personne qui a certifié (a), (b) ou (c) et je ne l'ai pas modifiée
et je ne l'ai pas modifiée.

(d) Je comprends et j'accepte que ce projet et la contribution
sont publics et qu'un enregistrement de la contribution (y compris toutes les
informations personnelles que je soumets avec elle, y compris ma signature) est
indéfiniment et qu'elle peut être redistribuée conformément à ce projet ou à la (aux) licence(s) open source concernée(s)
ce projet ou de la (des) licence(s) open source concernée(s).

<!-- v2.3.7 : caits-prod-app-gp_webui_20241231T140336-21_en_fr -->