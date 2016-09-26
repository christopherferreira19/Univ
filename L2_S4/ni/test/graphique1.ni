rect = fn(l: Entier) {
   avancer(l)
   tourner_d(90)

   avancer(l)
   tourner_d(90)

   avancer(l)
   tourner_d(90)

   avancer(l)
   tourner_d(90)
}

remplir = fn(l: Entier, o: Entier) {
   tq l > 0 {
      lever()
      avancer(o)
      tourner_d(90)
      avancer(o)
      tourner_g(90)
      couleur_suivante()
      baisser()

      rect(l)
      l = l - o * 2
   }
}

ouvrir()
remplir(900, 1)
attendre()
clore()
