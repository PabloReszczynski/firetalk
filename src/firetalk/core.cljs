(ns firetalk.core
  (:require [firetalk.protocols :refer [map->]]
            ["firebase" :refer [firestore]]))

(def client (firestore.))

(def query (-> client
               (.collection "cuds")
               (.whereEqualTo "id" "1010101")))

(defn execute! [client query]
  (let [query-object]))
