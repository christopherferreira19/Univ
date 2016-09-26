fact_borne = 1
fact_defaut = 1
fact_test = 5

fact = fn(n: Entier) -> Entier {
   si n <= fact_borne { retour fact_defaut }
   retour n * fact(n - 1)
}

ecrire_ln(fact(fact_test))
