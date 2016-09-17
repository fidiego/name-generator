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
    "morning"
    "nameless"
    "patient"
    "pernicious"
    "polished"
    "proprietary"
    "proud"
    "pruden"
    "quiet"
    "restless"
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
    "wild"
    "winter"
    "whistling"
    "whispering"
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

  [:div.clearfix.xs-p1.md-mt2.xs-mt1.xs-border.container [content]])


(defn name-generator []
  [:div.col.xs-col-12.xs-text-center.xs-p1
    [:div.xs-text-1 "Name Generator"]
    [:div.xs-m1
      "This is a name generator. There are many like it, but this one is mine."]
    [:hr.xs-m2]
    [:input.button.input--centered
     {:align "middle" type "button" :value "Generate" :on-click #(swap! cool-name generate-name)}]
    [:div.xs-p2.xs-text-2 @cool-name]
    ])

;; -------------------------
;; Views

(defn home-page []
  [home-component name-generator])

(defn about-page []
  [:div [:h2 "About name-generator"]
   [:div [:a {:href "/"} "go to the home page"]]])

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
