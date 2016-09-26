BLEVIN Daniel
FERREIRA Christopher

APNEE 1
==========================

Méta-Grammaire
--------------------------

Plutôt que de considerer un lexème 'symb' pouvant representer à la fois
un symbole terminal et un symbole non terminal, on choisit de laisser
le lexer faire la discrimination.  Un avantage immédiat de cette méthode
est de pouvoir imposer dans la grammaire que la partie gauche d'une règle
soit un non terminal.

On a donc l'alphabet :
 { '-eps-', '->', '|', ';', terminal, nonterminal }

On propose la grammaire suivante (en utilisant la syntaxe même qu'elle décrit) :
1.  <grammaire> -> <regle> <grammaire> ;
2.  <grammaire> -> -eps- ;
3.  <regle>     -> nonterminal '->' <droite> ';' ;
4.  <droite>    -> <symboles> <droite*> ;
5.  <droite*>   -> '|' <symboles> <droite*> ;
6.  <droite*>   -> -eps- ;
7.  <symbole>   -> nonterminal ;
8.  <symbole>   -> terminal ;
9.  <symbole>   -> '-eps-' ;
10. <symboles>  -> <symbole> <symboles*> ;
11. <symboles*> -> <symbole> <symboles*> ;
12. <symboles*> -> -eps- ;

Non terminaux s'annulant : { <grammaire>, <droite*>, <symboles*> }

Premiers :
  <grammaire>: { nonterminal }
  <regle>:     { nonterminal }
  <droite>:    { nonterminal, terminal, '-eps-' }
  <droite*>:   { '|' }
  <symbole>:   { nonterminal, terminal, '-eps-' }
  <symboles>:  { nonterminal, terminal, '-eps-' }
  <symboles*>: { nonterminal, terminal, '-eps-' }

Suivants :
  <grammaire>: { $ }
  <regle>:     { nonterminal, $ }
  <droite>:    { ';' }
  <symboles>:  { '|', ';'}
  <symbole>:   { nonterminal, terminal, '-eps-', '|', ';'}
  <droite*>:   { ';' }
  <symboles*>: { '|', ';'}

Table LL1 : Sous la forme (nonterminal, terminal): N° de la règle
  (<grammaire>, nonterminal): 1
  (<grammaire>, $):           2
  (<regle>, nonterminal):     3
  (<droite>, nonterminal):    4
  (<droite>, terminal):       4
  (<droite>, '-eps-'):        4
  (<droite*>, '|'):           5
  (<droite*>, ';'):           6
  (<symbole>, nonterminal):   7
  (<symbole>, terminal):      8
  (<symbole>, '-eps-'):       9
  (<symboles>, nonterminal):  10
  (<symboles>, terminal):     10
  (<symboles>, '-eps-'):      10
  (<symboles*>, nonterminal): 11
  (<symboles*>, terminal):    11
  (<symboles*>, '-eps-'):     11
  (<symboles*>, '|'):         12
  (<symboles*>, ';'):         12

La grammaire est donc bien LL(1).


Implémentation
--------------------------

Quelques considérations :

 - On utilise la hierarchie de type suivante pour representer les léxèmes :
    Lexeme
        ReservedLexeme
        Symbol
            NonTerminal
            Terminal
    et on ajoute une classe MetaLexer enveloppant la classe fourni SequenceDeSymboles
    et traduisant les chaines de caractères produit par SequenceDeSymboles en
    l'instance adéquate de l'interface Lexeme.
    Ceci permet d'évacuer rapidement le problème de distinguer les terminaux
    des non-terminaux et les lexemes reservés de leur version symbole 'quotée'
    en encodant ces distinctions dans le système de type.

 - En terme de structure de données, une règle produisant epsilon est une règle
   dont la liste des symboles de la partie droite est vide.

 - On utilise la librairie Google Guava notamment pour son type SetMultimap
   très approprié aux algorithmes utilisés pour construire la table LL1.
   L'ajout d'une librairie externe pose problème si l'on veut executer directement
   le jar produit par Maven, pour contourner ce problème il est possible d'utiliser
   la commande suivante :
   mvn exec:java -Dexec.mainClass=org.getalp.metagrammar.MetaGrammarLL1Parser < grammaires/meta.txt

 - Pour implementer la condition d'arrêt des algorithmes de forme "point fixe",
   plutôt que de comparer l'ensemble construit à sa version avant l'étape
   d'itération, on determine durant chacune de ces étapes si un changement à été
   réalisé. Ceci est simplifié par la convention des méthodes de type
   add/addAll/put/putAll de l'API Collection de Java qui retourne un booléen
   indiquant si un changement concret est survenu dans la collection.


APNEE 2
==========================

Une instance d'un parser peut être créé à partir d'une grammaire et de sa table LL1,
on ajoute aussi un paramètre Lexer, ce qui permet de supporter des grammaires avec des
phases d'analyses lexicales différentes.
Un certain nombre d'exemples du fonctionnement de la classe Parser peuvent être trouvés
dans la classe ParserTest et les fichiers src/test/resources/*.txt.
