# Projet en collaboration avec Quentin Vignan https://github.com/QuentinVignan

PrimeFlixPlus est un projet expérimental de plateforme de streaming, son but étant de
réussir à développer une Application web servie par ReactJS et alimentée par Spring
framework. Le projet devra mettre en place une API REST manageant des utilisateurs,
profils, vidéos, tickets de support, notifications, abonnements, ainsi qu’un historique des
saisies. L’accès aux routes de streaming utilisera Spring Webflux, l’activation des comptes
utilisateur se fera via Spring Mail et les routes REST prendront des DTO en paramètre dont
les paramètres validés seront traités par l’usage de Spring Validation. Un encodeur vidéo
utilisant ffmpeg devra servir à l’upload de vidéos.

## Fonctionnalités PrimeflixPlus:
- Variabilisation des adresses backend et encodeur via des variables d’environnements
(application.properties, META-INF et Variables d’environnement Docker)

## Vidéo:
- Upload de vidéo [Admin, affilié]
- Suppression de vidéo [Admin seulement]
- Modification de vidéo [Admin, affilié]
- Passage public d'une vidéo [Admin]
- Consultation vidéo [User/abonné minimum]

## Compte utilisateur:
- Inscription utilisateur [Tout rôle]
- Connexion Utilisateur [Tout utilisateur]
- Modifier son propre utilisateur, rôle exclus [User]
- Modifier tout utilisateur [Support, Admin]
- Supprimer son propre utilisateur [Tout rôle]
- Supprimer un utilisateur [Admin, Support]

## Profils utilisateurs:
- Création profil principal première connexion [Tout utilisateur]
- Ajout profil [Tout utilisateur]
- Ajout profil enfant [Tout utilisateur]
- Sécurisation des profils non-enfant [Tout utilisateur]
- Modifier son propre profil [Tout utilisateur]
- Suppression de son propre profil [Tout utilisateur]
- Modifier un profil d’un autre utilisateur [Admin, Support]
- Supprimer un profil d’un autre utilisateur [Admin, Support]

## Support:
- Accès panneau support [Tout utilisateur]
- Création ticket [Tout utilisateur]
- Modification ticket [Admin, support]
- Suppression ticket [Admin, support]
- Modification propre message ticket [Tout utilisateur]
- Cloture de son propre ticket [Tout utilisateur]
- Cloture d’un ticket quelconque [Admin]
- Cloture d’un ticket quelconque sur lequel on est assigné [Support]
- Visualiser ses propres tickets [Tout utilisateur]
- Visualiser tous les tickets [Admin]
- Visualiser ses propres tickets assignés [Support]

## Notifications:
- Push de notification [Admin]
- Reception de notifications selon les préférences [Tout utilisateur]

## Mails:
- Envoi mail à l'inscription [Tout utilisateur]
- Activation de compte via mail [Tout utilisateur]

## Abonnements:
- Inscription à un abonnement [Tout utilisateur]
- Changement abonnement [Tout utilisateur]

## Panneau admin [Admin]:
- Listing utilisateurs [Admin]
- Modification utilisateurs [Admin]
- Modification abonnement [Admin]
- Gestion des vidéos [Admin]

## Panneau affilié:
(Panneau admin ayant seulement accès aux vidéos uploadées soient même)
- Gestion de ses propres vidéos [Admin]

## Système d'historique
- Historisation des actions
- Accéder à l’historique [Support, Admin]

## [Futures améliorations ?]
- Templates mail
- Système de paiement
- Intégration des timestamps
- Personnalisation loader vidéo
- Synchronisation de la lecture à plusieurs
- Utilisation de plusieurs encodeurs sollicités par actuator
- Test de persistence et de tolérance de fault de la stack via chaosmonkeys

![Sans titre](https://github.com/user-attachments/assets/a67ee71c-7910-4ee5-96b2-c22f4ea6a5c3)
