flocon_rec = fn(n: Entier, l: Flottant) {
   si n == 1 {
      avancer(l)
   }
   sinon {
      c = l / 3.2

      couleur_suivante()
      flocon_rec(n - 1, c)
      tourner_g(45)
      couleur_suivante()
      flocon_rec(n - 1, c)
      tourner_d(90)
      couleur_suivante()
      flocon_rec(n - 1, c)
      tourner_g(45)
      couleur_suivante()
      flocon_rec(n - 1, c)
   }
}

flocon = fn {
   baisser()
   couleur_debut()

   n = 6
   l = 500.0

   flocon_cote = fn {
      flocon_rec(n, l)
      tourner_d(90)
   }

   debut_dessin()
   flocon_cote()
   flocon_cote()
   flocon_cote()
   flocon_cote()
   fin_dessin()
}

flocons = fn(n: Entier, offset: Fn) {
   si n > 0 {
      flocon()
      offset()
      flocons(n - 1, offset)
   }
}

ouvrir()

avancer(120)
tourner_d(90)
avancer(120)
tourner_g(90)

flocons(60, fn {
   lever()
   avancer(2)
   tourner_d(90)
   avancer(2)
   tourner_g(90)
   baisser()
})

attendre()
clore()
