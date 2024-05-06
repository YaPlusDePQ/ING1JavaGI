<!--








 /!\ Faire Ctrl-Maj-V /!\ 
 
 
 
 
 
 
 
 
 
 
 -->
# Le projet

## Info
Pour l'instant tout est sur teams

## Convention de code du projet

* On ne va pas bidouiller sur le travail des autres sans leurs autorisations.
* Le langage du code est **l'anglais**.
* Nomenclature:
    * On utilise le camelCase pour tous les noms (variables, fonctions, types, fichier....).
    ```java
    public void myFonction(...){}; 
    String myVariable;
    ```
    * Les constantes sont strictement en majuscule.
    * Faites des noms claire pour vos fonction et variables, ça coute rien et c'est claire
    * **TABULATION À CHAQUE OUVERTURE DE {.**
* Nomenclature (HTML et CSS):
    * pas de majuscule
    * les noms composés de plusieurs mot sont séparé par des '-'
    * Tabulation a chaque ouverture de div
* Commentaires
    * Commenter régulièrement le code (je sais c'est chiant mais c'est moins pire que le faire tout d'un coup).
    * Toutes les fonctions doivent posséder une description de la forme suivante:
    ```java
    /**
    * description
    *
    * @param  param1 description
    * @param  param2 description
    * @return      description
    */
    ```
    * Tout édit fait par quelqu'un sur la partie d'un autre devra être précisé par un commentaire avec l'auteur, la raison et la date+h.
    ```java
    // crash si -1 dans le param1 -- Razu 11/03 01:09
    ```
## Astuce pour coder
* **ChatGPT c'est très bof**, dans 95% des cas sa réponse est a chier, priviligé Google et n'utilisé ChatGPT que pour des tache d'esclave répéptitive.
* **Si tu sais pas, [Google](https://www.google.com/) sait**, donc quand tu te pose une question tu tape "[ta question en anglais] in java" dans Google et voila. LE site de référence en générale c'est [Stack Overflow](https://stackoverflow.com/) privilégié les liens qui mènent dessus.
* Si dans vos recherche vous tombé sur un site qui est un blog random avec un enorme pavé et plein de pubs, passez votre chemin c'est de la merde.
* **OUI lire la doc c'est chiant, OUI ça prend 30-40min, MAIS c'est bien plus rapide** que chercher tout et n'importe quoi (croyez en mon experience et celle de Gio vous allez perdre 4h et en plus vous aurez apprit unqiement comment régler votre problème actuel) donc notamment pour [javaFX](https://openjfx.io/openjfx-docs/) mais aussi pour [java](https://dev.java/learn/) **on lit les documentations**.

# SET UP
## Installation:
Voici la liste de tout ce qui doit être installé sur votre machine. Pour vérifié si ce n'est pas déjà installé vous pourrez taper la commande qui se trouve au bout de la ligne:
* [Git](https://github.com/git-for-windows/git/releases/download/v2.45.0.windows.1/Git-2.45.0-64-bit.exe) pour vérifier l'installation: ``` git --version ```
* [Make](https://gnuwin32.sourceforge.net/downlinks/make.php) pour vérifier l'installation: ``` make --version ```
* [Java JDK](https://download.oracle.com/java/22/latest/jdk-22_windows-x64_bin.exe) pour vérifier l'installation: ``` javac -version ```
* [JavaFX](https://download2.gluonhq.com/openjfx/22.0.1/openjfx-22.0.1_windows-x64_bin-sdk.zip) pas vérifiable simplement.

## GitHub
Le lien de la repositorie git est https://github.com/YaPlusDePQ/ING1JavaGI.<br />
Une fois placé dans le dossier qui va accuillir le projet, éxécutez les commandes suivantes:
```sh
git init
```
Utilisez la commande suivante pour créé un mot de référence à la repositorie pour les futures commandes. Remplacez [nom] par ce que vous voulez.
```sh
git remote add [nom] https://github.com/YaPlusDePQ/ING1JavaGI.git
```
Et enfin:
```sh
git pull [nom] master
```
(Il ce peut qu'il vous soit demander de vous login avec github)

## Makefile
Dans le dossier du projet, créez un fichier qui s'appelle "makefile" (ATTENTION il ne doit avoir aucune extension) et copier-coller ce code:
```makefile
# Compiler
JC = javac

# Dossiers source et de destination
SRCDIR = src
BINDIR = bin

# Flags de compilation
PATH_TO_FX := "C:/Program Files/Java/javafx-sdk-22.0.1/lib"
JCFLAGS := -d $(BINDIR)/ -cp $(SRCDIR)/ --module-path $(PATH_TO_FX) --add-modules javafx.controls,javafx.fxml

# Liste des fichiers source
SOURCES := $(wildcard $(SRCDIR)/*.java)

# Liste des fichiers class
CLASSES := $(SOURCES:$(SRCDIR)/%.java=$(BINDIR)/%.class)

# Commande pour construire les fichiers .class
$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JC) $(JCFLAGS) $<

# Règle par défaut pour construire tous les fichiers .class
all: $(CLASSES)

# Nettoyer les fichiers générés
clean:
	$(RM) $(BINDIR)/*.class
```
Vous pouvez modifier ce fichier à votre guise pour qu'il suivent vos besoins (ce fichier est ignoré par Git).

### Java
Une fois téléchargés éxécutez les trois premiers (les seules éxécutables) et complétez l'installation, 
puis éxécutez les commandes plus haut pour bien vérifiez que tout fonctionne. Si "javac" n'est pas reconnue après l'installation réalisez les étpaes suivantes:
* Tapez "Modifier les variables d'environnement système" dans la barre de recherche Windows
* Ouvrez l'interface et cliquez sur "Variables d'envirronement..." en bas a droite
* Dans le tableau du bas cherchez "path" et double-cliquez
* Cliquez sur "Nouveau" et copier le chemin absolue vers le dossier bin de la JDK (par exemple chez moi il se trouve à C:\Program Files\Java\jdk-22\bin)
* Fermez le terminal de commande et rééxécutez "javac -version" (il ce peux qu'un redemarrage soit requis)

### JavaFX
Decompressez le fichier ZIP ou vous voulez que ça soit enregistrez (pour tout regrouper je l'ai decompressez dans le dossier "C:\Program Files\java"). Le plus important sera d'avoir le bon chemin dans le makefile, pour cela il suffira de mettre le chemin vers le dossier "lib" au niveau de la variable "PATH_TO_FX" (Si vous avez installez comme moi et que vous n'est pas sur les PC de l'école normalement c'est le même que celuis déjà present. Pour le PC de l'école il faudrait changer le C: en H:)

*si tu as lu jusqu'ici écrit "banane" dans le groupe télégram comme ça je sais que t'es pas un bouffon si tu demande de l'aide*

## Vérification
Pour vérifier que tout va bien vous pouvez normalement éxécutez "make" dans le terminal VScode, normalement la compilation réussi sans erreur, puis vous pouvez éxécutez le programme en faisant:
```sh
cd ./bin
java --module-path "[Le chemin que vous avez mit pour PATH_TO_FX]" --add-modules javafx.controls,javafx.fxml HelloFX
```

# Aide commandes

## GIT 
| Commande  | Action |
| ------------- |:-------------:|
| git add [fichier(s) ou . (pour inclure toutes les modifications)] |ajoute les fichiers indiqués au prochain commit |
| git commit -m "[message]" | crée le commit prêt à être push sur la repositorie |
| git reset HEAD~ | annule le commit le plus recent |
| git push [remote nom] master | publie le commit |
| git pull [remote nom] master | mets à jours les fichiers locaux |
| git clone [url] | télécharge la repositorie |
| git remote -v | donne le nom de référence |

### Si deux personnes travaillent en même sur le même fichier (ce qui devrait normalement JAMAIS arriver) **NE PAS** forcer avec les pull et push et aller manuellment sur la repositorie pour constater les differents changements. 
