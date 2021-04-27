(ns firetalk.read
  (:require [firetalk.protocols :refer [map->]]))

(defn & [x y] (y x))
(defn $ [x y] (x y))
(defn flip [f] (fn [x y & rest] (apply f (conj [y x] rest))))

#?(:cljs (require '["firebase" :refer [firestore]]))

(def client
 #?(:clj  (-> (com.google.cloud.firestore.FirestoreOptions/getDefaultInstance)
              .getService)
    :cljs (firestore)))

(defn field-path [strs]
  #?(:clj  (com.google.cloud.firestore.FieldPath/of strs)
     :cljs (firestore.FieldPath. strs)))

{:collection
 ["cuds" "id=1010101" "upcCode" "10101010"]}

(defn parse-collection [coll]
  (fn [client]
    (if (not (coll? coll))
      (doseq [[coll id]])
      (.collection client coll))))

(defn parse-where [[oper k v]]
  (let [k (if (coll? k) (field-path k) k)]
    (case oper
      =  #(.whereEqualTo % k v)
      in #(.whereIn % k v)
      >  #(.whereGreaterThan % k v)
      >= #(.whereGreaterThanOrEqualTo % k v)
      <  #(.whereLessThan % k v)
      <= #(.whereLessThanOrEqualTo % k v))))

(defn parse-query [query]
  (fn [client]
    (let [where (map parse-where (:where query))
          query (.collection client (:collection query))]
      (reduce & query where))))

(defn execute! [client query]
  (let [query-object (parse-query query)]
    (delay
      (-> (deref (.get (query-object client)))
          (map->)))))
