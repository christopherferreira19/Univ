-- Implemente une "machine virtuelle" simple.
--
-- Elle est dotée d'une pile d'appel servant à
-- passé les arguments entre appel de fonctions
-- et d'une mémoire fonctionnant sous forme de
-- pile de plage mémoire pour les variables
-- locales des fonctions.
-- Ces deux piles sont representés dans un
-- seul tableau, chacune commencant à l'un
-- des deux bouts.
--
-- Elle gére aussi une trace des appels
-- successifs de fonctions

with PStrings;
with Valeurs;
with Programmes;

package Vm is

   use PStrings;
   use Valeurs;
   use Programmes;

   Stack_Overflow: exception;

   procedure executer(pgm_arg: Programme);

   procedure afficher_trace;

   -- Instruction EPL
   -- Met la valeur litteral sur le sommet de la
   -- pile d'appel
   procedure empiler(litteral: Valeur);

   -- Instruction DPL
   -- Depile le sommet de la pile d'appel
   procedure depiler;

   -- Comme depiler ci-dessus mais retourne la
   -- valeur en question
   function depiler return Valeur;

   -- Pour les deux instructions suivantes
   -- `addr`, l'addresse represente l'indice
   -- de la variable locale dans la plage
   -- alloué à la fonction en cours

   -- Instruction CHR
   -- Prends la valeur à l'adresse donné
   -- dans la pile mémoire et la met au sommet
   -- de la pile d'appel
   procedure charger(addr: Natural);

   -- Instruction STK
   -- Dépile la valeur au sommet de la pile
   -- d'appel et l'enregistre à l'adresse
   -- donné dans la pile mémoire
   procedure stocker(addr: Natural);

   -- Instruction PRM
   -- Met au sommet de la pile d'appel la
   -- primitive correspondant au nom donné.
   procedure primitive(nom: PString);

   -- Instruction CLT
   -- Créé une fonction placé au sommet de la pile d'appel
   -- `nom` correspond au nom de la routine qui doit être
   -- défini dans le programme en cours d'éxécution
   -- `clotures` indique le nombre de variables à enfermer
   -- avec la fonction, ces variables doivent se trouver
   -- au sommets de la pile d'appel au moment de l'execution
   -- de cette instruction.
   procedure cloture(nom: PString; rec: Boolean; clotures: Natural);

   -- Pour les trois instructions de saut suivantes
   -- `decalage` est exprimé en nombre d'instructions
   -- à partir de l'instruction de saut en elle-même
   -- Peut être positif comme négatif

   -- Instruction STI
   -- Réalise un saut d'instruction inconditionnel
   procedure saut(decalage: Integer);

   -- Instruction STV
   -- Réalise un saut d'instruction si la valeur
   -- depiler sur la pile d'appel est vrai
   procedure saut_vrai(decalage: Integer);

   -- Instruction STF
   -- Réalise un saut d'instruction si la valeur
   -- depiler sur la pile d'appel est faux
   procedure saut_faux(decalage: Integer);

   -- Instruction APP
   -- Appel la fonction se trouvant au sommet de la pile
   -- d'appel, ses arguments doivent se trouver à la
   -- suite sur cette même pile.
   -- Une plage mémoire est alloué étant donné la taille
   -- demandé par la routine correspondante à la fonction
   procedure appel;

   -- Instruction RET
   -- Arrête l'execution de la fonction courante et
   -- recommence à l'instruction suivant l'instruction
   -- d'appel de la fonction appelante
   -- La plage mémoire de la fonction est libérée
   procedure retour;

   procedure pile;

end;
