# SMS - Steve IA — mise à jour v2

Cette mise à jour ajoute :
- une bulle SS flottante et déplaçable au-dessus des autres applications ;
- une lecture locale du texte des notifications autorisées ;
- un panneau affichant le dernier message lisible et trois réponses à copier ;
- aucun envoi automatique de message.

## Installer le patch dans Termux
Dans le dossier du projet `~/SMS-Steve-IA`, décompresse ce ZIP en remplaçant les fichiers existants. Puis commit/push :
```sh
cd ~/SMS-Steve-IA
git add app
git commit -m "Add floating Steve IA overlay and notification capture"
git push
```
GitHub Actions lancera la compilation. Télécharge ensuite `SMS-Steve-IA-debug-APK` depuis l'exécution verte.

## Première configuration Android
1. Ouvre Steve IA.
2. Appuie sur « 1. Autoriser les notifications » et active Steve IA dans l'accès aux notifications.
3. Appuie sur « 2. Autoriser la superposition » et autorise l'affichage par-dessus les autres applications.
4. Appuie sur « 3. Afficher la bulle Steve IA ».
5. Ouvre WhatsApp ou une autre application. Quand une notification contient du texte, Steve IA l'enregistre localement. Appuie sur la bulle SS pour voir les suggestions, puis sur Copier.
6. Pour arrêter la bulle, rouvre Steve IA et choisis « Arrêter la bulle ».

Limites : Android ne permet de récupérer que le texte exposé dans les notifications. Les messages masqués, médias, notifications groupées ou notifications désactivées ne seront pas forcément capturés. L'accès aux notifications est sensible : n'autorise que les applications nécessaires. Cette version garde le dernier message localement et ne l'envoie pas à un serveur.
