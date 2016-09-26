# Operations booléenes
assert(vrai)
assert(non(faux))
assert(vrai ou vrai)
assert(vrai ou faux)
assert(vrai et vrai)
assert(non(vrai et faux))
assert(non(faux et vrai))

# Evaluation paresseuse
assert(vrai ou lire_e("Jamais affiché") <> 1)
assert(non(faux et lire_e("Jamais affiché") <> 1))

# Operations arithmétiques
assert(1 < 2)
assert(2 > 1)
assert(1 <= 2)
assert(2 >= 1)
assert(1 <= 1)
assert(1 >= 1)
assert(3 == 1 + 2)
assert(3 <> 1 + 3)
assert(non(3 == 1 + 3))
assert(1_000 == 999 + 1)

# '.' Est équivalent à '0.0'
assert(0.0 == .)

test_1 = flottant(1) + 2.3
assert(test_1 == 3.3)
test_2 = .4 * -.5
assert(test_2 == -.2)
assert(test_1 - test_2 == 3.5)
test_3 = 1 + -+-3
assert(test_3 == 4)
test_5 = 1.0 / 4.0
assert(test_5 == .25)
test_6 = 5 % 4
assert(test_6 == 1)

ecrire_ln("Everything checks !")
ecrire_ln("Or does it ?")
assert(faux)
