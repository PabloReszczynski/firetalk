(ns firetalk.core
  (:require [firetalk.protocols :refer [map->]])
  (:import  [com.google.cloud.firestore FirestoreOptions
                                        Firestore
                                        Query
                                        QuerySnapshot
                                        QueryDocumentSnapshot
                                        FieldPath]
            [com.google.cloud Timestamp]))


(def ^Firestore client (-> (FirestoreOptions/getDefaultInstance)
                           (.getService)))

(def query (-> client
               (.collection "cuds")
               (.whereEqualTo "id" "100003711188241603683")))

(def myq '{:collection "cuds"
           :where      [[= "id" "100003711188241603683"]]})
;; ---------------------------------------------------------------------------
;; Utility Functions
;; ---------------------------------------------------------------------------



(def dsquery '[:find ?e
               :in $
               :where [?e :cud/id "100003711188241603683"]])
