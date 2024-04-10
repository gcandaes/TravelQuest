# Test Technique FullStack - TravelQuest

Salut et bienvenue ici. Auchan te propose de consacrer un peu de temps pour nous exposer tes talents.

Tu trouveras ici le squelette d'un projet Spring Reactive (JAVA) / MongoDB (technical-test-api) et la partie front sera en Thymeleaf (technical-test-renderer).

TravelQuest est une application de réservation de voyages qui offre une expérience utilisateur fluide. Les utilisateurs peuvent rechercher, sélectionner et réserver des voyages tout en bénéficiant de fonctionnalités avancées telles que des filtres de recherche personnalisés et une présentation claire des détails du voyage.

# Partie Back
## Partie 1: Chasse aux Bugs

1. **Impossible de lancer l'API**
   - **Description :** Un développeur frauduleux a cassé l'API sans aucun scrupule.
   - **Tâche :** Trouver le problème et le réparer.
   - **Solution :** 

j'ai copié l'erreur sur google : 
java.lang.NoClassDefFoundError: jakarta/servlet/ServletException
je suis allé sur stackoverflow : 
https://stackoverflow.com/questions/74710946/java-lang-noclassdeffounderror-jakarta-servlet-http-httpservletrequest
premier resultat : ajouter la dépendence manquante. 
Je suis donc allé voir dans le pom.xml et en effet, cette dépendance manquait.

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

2. **Impossible de récupérer les résultats de nos vols**
   - **Description :** Un ami du premier développeur frauduleux n'a pas testé son code, nous n'arrivons à récupérer les informations de vol
   - **Tâche :** Réparer la récupération des vols
   - **Solution :**

com.mongodb.MongoQueryException: Command failed with error 2 (BadValue): 'Field 'locale' is invalid

en regardant sur google https://www.mongodb.com/community/forums/t/getting-this-error-while-accessing-collection/234538/9

j'ai pu remarqué qu'il y avait une erreur de syntaxe dans la classe AirportRecord.java 
@Document(collection = "airport")
et non "collation"

Grace à Postman, je peux donc communiquer avec le endpoint deja créé
operation GET : http://localhost:8086/flight

cela permet de récuperer les informations des vols

## Partie 2: Évolutions Éclair

1. **Créer des vols**
   - **Description :** Permettre de créer des vols dans un espace d'administration (pas besoin de la sécuriser)
   - **Tâche :** Avoir un endpoint qui permet l'insertion des vols en base
   - **Solution :**
  
Depuis postman, j'ai créé une request POST http://localhost:8086/flight. Avec comme body un document json de type : 
{
    "id":"e9205fed-edfa-49e8-96d9-5a835c0a91ee",
   "departure":"2024-11-09T17:00:00",
   "arrival":"2024-11-10T05:00:00",
   "price":140.5,
   "origin":"LAX",
   "destination":"PEK",
   "image":"https://www.hdwallpapers.in/download/high_resolution_flying_eagle_4k_8k_hd-3840x2160789456.jpg"
}
Ceci comprend toutes les propriétés d'un Flight puisque c'est ceci qu'on veut enregistrer.

Cela permet d'appelé le endpoint préalablement créé. 
FlightEndpoint.java

    @PostMapping(
            path ="",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<FlightRecord> createFlight(@RequestBody FlightRecord flightRecord){
        return flightFacade.createFlight(flightRecord);
    }

Pour suivre la même logique que l'endpoint qui permet de récupérer les vols, on passe par FlightFacade.java : 

      public Mono<FlightRecord> createFlight(FlightRecord flightRecord) {
        return flightService.createFlight(flightRecord);
    }

Enfin, dans FlightService.java

    public Mono<FlightRecord> createFlight(FlightRecord flightRecord){
        return flightRepository.save(flightRecord);
    }

Cela permet donc d'enregistrer notre vol en base de données.
Ceci peut être facilement verifiable lorsque l'on appelle l'endpoint permettant de récupérer tous les vols. Ou alors en passant par MongoCompass qui permet de visualiser les enregistrements de notre bdd.
Une fois le backend demarré, on y accede en se connectant avec l'url localhost:27017, auchan-tech-test est le nom de la base et dans la collection Flight nous pouvons voir tous nos documents dont ceux que l'on vient de créer.


2. **Rajouter des filtres**
   - **Description :** Permettre à l'utilisateur de trier les résultats par prix ou par localisation.
   - **Tâche :** Ajouter la notion de filtre.

3. **Bonus : Rajouter la pagination**
   - **Description :** Permettre de paginer les résultats.
   - **Tâche :** On aimerait avoir uniquement 6 résultats par page afin d'optimiser les performances de notre application.
   
# Partie Front

## Partie 1: Chasse aux Bugs

1. **Problème d'affichage des tarifs :**
    - **Description :** Certains tarifs ne s'affichent pas correctement sur la page de réservation.
    - **Tâche :** Corrigez le problème d'affichage pour assurer la clarté et la précision des tarifs.
    - **Solution :**

Premierement dans index.html, à la ligne 93 j'ai du supprimer flight.photo puisque cette propriete n'existe pas. Ainsi j'ai pu lancer http://localhost:8087/

2. **Lenteur de chargement des images :**
    - **Description :** Les images des destinations mettent trop de temps à charger, affectant l'expérience utilisateur.
    - **Tâche :** Optimisez le chargement des images pour améliorer les temps de réponse de la page.

## Partie 2: Évolutions Éclair

1. **Page d'administration pour créer des vols :**
   - **Description :** Utilise le endpoint précédemment créé pour permettre de créer des vols dans une page front.
   - **Tâche :** Pas besoin de la sécuriser, ni de créer un compte admin pour y accéder.
   - 
2. **Filtrage des résultats de recherche :**
    - **Description :** Ajoutez une fonctionnalité de filtrage basique pour permettre aux utilisateurs de trier les résultats par prix, par localisation et/ou par dates.
    - **Tâche :** Implémentez un système de filtres pour permettre à l'utilisateur de retrouver facilement son vol

3. **Filtrage avancé des résultats de recherche :**
    - **Description :** Permettre la sélection d'un vol et, lors du retour à la page de recherche des vols, conserver le filtre précédemment appliqué.
    - **Tâche :** Tu peux très bien le faire en QueryParams ou en Javascript, à toi de voir.

4. **Bonus : Rajouter la pagination**
   - **Description :** Permettre de paginer les résultats.
   - **Tâche :** On aimerait avoir uniquement 6 résultats par page afin d'optimiser les performances de notre application.
   
## Consignes
- N'hésite pas à fouiller sur Google, StackOverflow, la documentation Spring pour trouver les réponses à tes questions.
- Sois prêt à discuter de tes choix et de ton approche lors de l'entretien.
- Have fun !


## Si tu rencontres un problème n'hésite pas à créer une issue sur le repo que tu as fork et on répondra aussi vite que possible !
