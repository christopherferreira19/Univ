Ni
===============

### Préparation

Dans le dossier `ext/lib_graphique/graphsimple`, lancer la commande :

```
make
```

### Compilation

A la racine du projet :

```
gnat make -Pni.gpr
```

### Execution

A la racine du projet :

```
./ni lex chemin/dun/fichier.ni
./ni ast chemin/dun/fichier.ni
./ni cpl chemin/dun/fichier.ni
./ni run chemin/dun/fichier.ni
./ni run chemin/dun/fichier.nic
```

### Debug

Compilation avec les informations pour la trace d'appels Ada :

```
gnat make -Pni.gpr -g -bargs -E
```

Récuperation des informations de la trace d'appels Ada :

```
addr2line -e ni --functions --demangle <liste d'addresses indiquées par l'erreur>
```
