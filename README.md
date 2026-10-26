# SMS — Steve IA

Prototype Android léger : trois suggestions de réponses, style doux par défaut, champ de message, et copie manuelle.

## Fonctionnalités présentes
- Interface sombre avec logo SS.
- Coller un message reçu.
- Trois propositions locales.
- Styles doux, amical, drôle et drague légère.
- Copie dans le presse-papiers.
- Aucun envoi automatique.

## Limites actuelles
Cette version n'est pas encore connectée à Gemini ou ChatGPT et ne lit pas les notifications WhatsApp. Les suggestions sont des exemples locaux. L'icône flottante et l'intégration IA seront ajoutées ensuite.

## Compilation
Avec Gradle et un SDK Android compatible :
`gradle assembleDebug`

APK attendu :
`app/build/outputs/apk/debug/app-debug.apk`

Identifiant Android : `com.steve.sms`
SDK cible : Android API 34.
