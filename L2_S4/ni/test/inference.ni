ecrire_ln(indefini)

f = fn -> Entier {

   non_fonction = 1
   non_fonction(4)

   x = 4 + 5
   x = 4 + 5.0
   x = 4 + "5"
   x = 4.0 + 5.0


   id_entier = fn(a: Entier) -> Entier {
      retour a
   }

   id_entier(4.)

   ecrire_ln(4, 6)

   si lire_e("Entier >") {
      retour 1
   }
}

si lire_e("Entier >") < 1 {
   x = 4
}
sinon {
   x = 4.0
}

ecrire_ln(x)
x = 4 * 5 + 5 * (5 - x + 4)

f2 = fn(b: Booleen) -> Entier {
   si b {
      retour 10
   }

   retour "Chaine"
}
