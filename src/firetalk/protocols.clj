(ns firetalk.protocols
  (:require [clojure.walk :refer [postwalk keywordize-keys]])
  (:import  [com.google.cloud Timestamp]))

(def ^:private transform
  "Transforms a Firestore Document (Map<String, Object>) into a Clojure data structure"
  (partial
    postwalk
    (fn [x]
      (cond (map? x)                          (keywordize-keys x)
            (isa? java.util.HashMap (type x)) (into {} x)
            (isa? Timestamp (type x))         (.toDate x)
            :else                             x))))

(defprotocol AsMap
  "Implementing objects can be converted to a Clojure HashMap"
  (^clojure.lang.PersistentHashMap map-> [self]))

(extend-type QuerySnapshot
  AsMap
  (map-> [self]
    {:documents (mapv map-> (.getDocuments self))
     :read-time (.toDate (.getReadTime self))}))

(extend-type QueryDocumentSnapshot
  AsMap
  (map-> [self]
    (transform (into {} (.getData self)))))

