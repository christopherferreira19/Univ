x = faux

boucle {
   ecrire_ln(4)
# L'inference est capable de déterminer que x ne peut qu'être faux
# dans ce cas, il va donc supprimer la boucle et 'inliner' son corps
} tq x


si x {
   # Pas d'inference ici étant donné que x est toujours faux
   garbage(pas, infere, ni, compile)
}

retour

# Pas d'inference ici étant donné que le programme aura
# été arreté par le retour ci-dessus
garbage(pas, infere, ni, compile)
