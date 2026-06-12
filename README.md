# MiniGames App

## Rendu

- Nom : Thomas Dosne
- Branche a tester : `main`

## Fonctionnalites operationnelles

- Ecran d'accueil avec saisie du pseudo et blocage des jeux tant que le pseudo est vide.
- Navigation multi-ecrans avec `NavHost` : accueil, jeu de reaction, mot cache et leaderboard.
- Jeu de reaction avec valeur de depart aleatoire, cible aleatoire, sens aleatoire, vitesse variable, timer aveugle proche de la cible, score et sauvegarde Room.
- Jeu de mot cache avec grille 3 x 3, timer de 60 secondes, selection de lettres, effacement, validation, passage, indice et sauvegarde Room.
- Leaderboard Room affichant le top 10, avec filtre par jeu.

## Points connus

- La date des scores est stockee en base, mais elle n'est pas encore affichee dans le leaderboard.
- Les statistiques personnelles et la reinitialisation des scores ne sont pas implementees.
