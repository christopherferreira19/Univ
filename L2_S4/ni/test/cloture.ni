x = 2

f = fn(y: Entier) -> Fn -> Entier {
   retour fn -> Entier {
      # x et y sont enfermées dans la fonction
      retour (y * x) + (y - x)
   }
}

assert(f(5)() == 13)
