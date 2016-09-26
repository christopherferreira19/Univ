curry = fn(
      f: Fn(Entier, Entier) -> Entier,
      g: Entier)
      -> Fn(Entier) -> Entier {
   retour fn(d: Entier) -> Entier {
      retour f(g, d)
   }
}

plus1 = curry(plus, 1)
assert(plus1(2) == 3)
