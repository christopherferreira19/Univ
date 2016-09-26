boucle {
   x = lire_e("Entrer une valeur entiere (> 0) > ")
} tq x < 1

repete = fn -> Fn(Entier, Fn) {
   repete_it = fn(n: Entier, f: Fn) {
      tq n > 0 {
         f()
         n--
      }
   }

   repete_rec = fn(n: Entier, f: Fn) {
      si n > 0 {
         f()
         repete_rec(n - 1, f)
      }
   }

   # Définition de repete
   # Cette fonction appel les deux versions (itérative et recursive)
   # chacune avec une moitié de l'argument n
   retour fn(n: Entier, f: Fn) {
      n_it = n
      n_it /= 2
      n_rec = n
      n_rec %= 2
      n_rec += n / 2
      ecrire_ln("* It")
      repete_it(n_it, f)
      ecrire_ln("* Rec")
      repete_rec(n_rec, f)
   }

# On appel directement la fonction anonyme qui vient d'être crée
# Cela permet de simuler l'encapsulation
# i.e : les fonctions repete_rec et repete_it ne sont pas visibles
# en dehors de cette fonction.
}()

repete(x, fn(c: Chaine) -> Fn {
   retour fn {
      ecrire_ln(c)
   }
}("Ni !"))
