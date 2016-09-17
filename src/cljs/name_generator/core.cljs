(ns name-generator.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))


;; -------------------------
;; Atoms

(def cool-name (reagent/atom ""))

;; -------------------------
;; The showboat

(def adjectives
  (list
    "aged"
    "ancient"
    "apprehensive"
    "autumn"
    "billowing"
    "bitter"
    "black"
    "blue"
    "bold"
    "broken"
    "delicate"
    "dichotomous"
    "divine"
    "empty"
    "ephemeral"
    "falling"
    "fragrant"
    "hidden"
    "holy"
    "judicious"
    "lingering"
    "little"
    "lively"
    "loyal"
    "morning"
    "nameless"
    "patient"
    "pernicious"
    "polished"
    "pontificating"
    "proprietary"
    "proud"
    "proverbial"
    "prudent"
    "quiet"
    "recalcitrant"
    "restless"
    "sacrosanct"
    "sanctimonious"
    "silent"
    "singular"
    "solitary"
    "sparkling"
    "spring"
    "still"
    "summer"
    "throbbing"
    "twilight"
    "vehement"
    "wandering"
    "weathered"
    "whispering"
    "whistling"
    "wild"
    "winter"
    "wise"
    "wispy"
    "young"
    ))


(def nouns
  (list
    "bistec"
    "burrito"
    "buñuelo"
    "chilaquiles"
    "chile"
    "chocolate"
    "churro"
    "cucumber"
    "empanada"
    "enchilada"
    "flauta"
    "jalapeño"
    "mango"
    "mole"
    "pastel"
    "persimmon"
    "quesadilla"
    "squash"
    "strawberry"
    "tamal"
    "vanilla"
    ))


(defn generate-name []
  (clojure.string/join
    (list
      (nth adjectives (Math/floor (rand (count adjectives))))
      "-"
      (nth nouns (Math/floor (rand (count nouns)))))))


;; -------------------------
;; Components

(defn home-component [content]
  [:div.homme-container [content]])


(defn social-footer [about]
  [:div.footer
   [:div.footer-inner
   [:a {:href "https://twitter.com/7idiego"} [:div.icon.icon--twitter]]
   [:a {:href "https://github.com/fidiego/name-generator"} [:div.icon.icon--github]]
   (if about
    [:a {:href "/about"} "About"])
   ]
   ])


(defn name-generator []
  [:div.text-center
    [:h1 "Name Generator"]
    [:p
      "This is a name generator. There are many like it, but this one is mine."]
    [:p.name @cool-name]
    [:button {:align "middle" type "button" :value "Generate" :on-click #(swap! cool-name generate-name)}
      "Generate"]
    [social-footer true]
    ])

;; -------------------------
;; Views

(defn home-page []
  [home-component name-generator])

(defn about-page []
  [:div
   [:h2.text-center "About This Thing"]
   [:div {:style {:maxWidth 400 :margin "0 auto" :textAlign "justify"}}
    [:p "I just needed an excuse to mess around with clojurescript.
      This could have easily been achieved with vanilla js, but where's the 'fun' in that?
      Also, who doesn't like lisps?"
    ]
    [:p "The real reason, tough, is that I tried this with elm but the fact I'd need to use
      a generator for the random numbers really killed my vibe."]]
   [:div.text-center [:a {:href "/"} "go back to the thing"]]
   [social-footer]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
