# TP Pizzeria

Prérequis : JDK 17 ou plus récent, Maven.

```
cd java
mvn test                                                      # lancer les tests
mvn compile exec:java -Dexec.mainClass=fr.tp.pizzeria.Main    # lancer le programme
```

Organisation du code :

- `model` : les objets métier
- `service` : la logique de la pizzeria
- `repository` : le stockage des données (en mémoire)

Les tests décrivent le comportement actuel de l'application. Ils doivent rester verts pendant tout le TP.
