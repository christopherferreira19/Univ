fn1 = fn -> Fn -> Entier {
   x = 0

   retour fn -> Entier {
      retour x
   }
}

fn2 = fn -> Entier {
   x = 1
   retour fn1()()
}

# Portée léxicale x est la valeur à la définition de la fonction
assert(fn2() == 0)

y = 5

fn3 = fn {
   assert(y == 5)
   y = 9
   assert(y == 9)
}

fn3()

# Fermeture par copie donc y n'a pas été modifié par l'appel à fn3
assert(y == 5)
