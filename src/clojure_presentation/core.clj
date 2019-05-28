(ns clojure-presentation.core
  (:require [clojure.core.async :as async]))











;; REPL

(- 3 5 6 7)


(def x 8)
(def y 11)
(+ x y)


;; Dynamic Compilation































;; Functional Programming

(def hello (fn [x] (str "Hello, " x "!")))
(hello "World")

(defn hello-world []
  (println "Hello, World!"))
(hello-world)

(defn arg-count
  ([] 0)
  ([x] 1)
  ([x & more]
   (println more)
   (+ 1 (count more))))
(arg-count 1 1 1 1 1)




















;; First-class functions

(defn make-adder [x]
  (let [y x]
    (fn [z] (+ y z))))
(def add2 (make-adder 2))
(add2 4)

(even? 2)
(even? 3)
(filter odd? [1 2 3 4 5 6])


























;; Immutable Data Structures

(def person {:first-name "Tormod"
             :last-name  "Mathiesen"
             :birth-date "1996-07-15"})
(:first-name person)

(defn add-eye-color [value] (assoc person :eye-color value))

(-> (add-eye-color "blue")
    (dissoc :birth-date)
    (assoc :talent "lots"))
person
(dissoc (assoc person :eye-color "blue") :birth-date)
(-> person
    (assoc :eye-color "blue")
    (dissoc :birth-date))


(def got-names #{"Daenarys" "Sansa" "Tyrion" "Bran" "Aegon"})
(conj got-names "Sam")
(disj got-names "Bran")
got-names

(-> got-names
    (conj "Sam")
    (conj "Sam")
    (conj "Sam"))


(def favorite-animals ["Hawk" "Bear" "Wolf"])
(nth favorite-animals 0)
(subvec favorite-animals 0 2)
(conj favorite-animals "Giraffe")
favorite-animals


(def todo-list (list "Read more books"
                     "Exercise"
                     "Get money"
                     "Become happy"))
(cons "Become happy" todo-list)
(sort todo-list)


(let [my-vector [1 2 3 4]
      my-map {:fred "ethel"}
      my-set #{:a :b :c}
      my-list (list 4 3 2 1)]
  (list
    (conj my-vector 5)
    (assoc my-map :ricky "lucy")
    (conj my-list 5)
    ;the originals are intact
    my-vector
    my-map
    my-list))







;; Extensible abstractions

(let [my-vector [1 2 3 4]
      my-map    {:fred "ethel" :ricky "lucy"}
      my-list (list 4 3 2 1)]
  [(first my-vector)
   (rest my-vector)
   (first my-map)
   (rest my-map)
   (first my-list)
   (rest my-list)])

(filter even? [1 2 3 4])
(sort [4 3 2 1])
(map inc [1 2 3 4])
(reduce max [1 2 3 2 1])

(take 10 (cycle [1 2 3 4]))
(take 10 (repeat 1))









;; Recursive looping

(defn fib
  [n m counter]
  (if (> counter 1)
    (cons n (fib m (+ n m) (dec counter)))
    [n]))
(fib 0 1 10)

(defn my-zipmap [keys vals]
  (loop [my-map {}
         my-keys (seq keys)
         my-vals (seq vals)]
    (if (and (not-empty my-keys) (not-empty my-vals))
      (recur (assoc my-map (first my-keys) (first my-vals))
             (rest my-keys)
             (rest my-vals))
      my-map)))
(my-zipmap [:a :b :c] [1 2 3])




















;; Lisp!

;; Truthyness

(if -1
  1
  2)

(def thing {:thing "stuff"})

(if (:thing thing)
  "ja"
  "nei")

;; Code is Data

(read-string "(+ 1 2 3)")

(eval (read-string "(+ 1 2 3)"))

(comment
  (defmacro and
    ([] true)
    ([x] x)
    ([x & rest]
     `(let [and# ~x]
        (if and# (and ~@rest) and#)))))


(macroexpand '(and 1 false (get-stuff-from-db)))




















;; Runtime Polymorphism

(defmulti encounter (fn [x y] [(:Species x) (:Species y)]) )
(defmethod encounter [:Bunny :Lion] [b l] :run-away)
(defmethod encounter [:Lion :Bunny] [l b] :eat)
(defmethod encounter [:Lion :Lion] [l1 l2] :fight)
(defmethod encounter [:Bunny :Bunny] [b1 b2] :mate)
(defmethod encounter :default [x y] :nothing)
(def b1 {:Species :Bunny :other :stuff})
(def b2 {:Species :Bunny :other :stuff})
(def l1 {:Species :Lion :other :stuff})
(def l2 {:Species :Lion :other :stuff})
(encounter b1 b2)
(encounter b1 l1)
(encounter l1 b1)
(encounter l1 l2)
(encounter {} {})


























;; Concurrency

(comment
  mutable-data
  [ref agent atom])
(comment
  concurrent-actions
  [future deliver promise delay])

;; Channels!
'clojure.core.async


























;; JVM Hosted

(import '[java.time LocalDate])

(defn add-week ^LocalDate [^LocalDate date]
  (.plusDays date 7))

(defn day->str [^LocalDate date]
  (format "%s %s, %s" (.getMonth date) (.getDayOfMonth date) (.getYear date)))

;; infinite sequence of weekly dates, starting from today
(def future-weeks (iterate add-week (LocalDate/now)))
(take 3 future-weeks)

(map day->str (take 4 future-weeks))

;; Swing

(import '(javax.swing JFrame JLabel JTextField JButton))
(import '(java.awt.event ActionListener))
(import '(java.awt GridLayout))
(defn celsius []
  (let [frame (JFrame. "Celsius Converter")
        temp-text (JTextField.)
        celsius-label (JLabel. "Celsius")
        convert-button (JButton. "Convert")
        fahrenheit-label (JLabel. "Fahrenheit")]
    (.addActionListener
      convert-button
      (reify ActionListener
        (actionPerformed
          [_ evt]
          (let [c (Double/parseDouble (.getText temp-text))]
            (.setText fahrenheit-label
                      (str (+ 32 (* 1.8 c)) " Fahrenheit"))))))
    (doto frame
      (.setLayout (GridLayout. 2 2 3 3))
      (.add temp-text)
      (.add celsius-label)
      (.add convert-button)
      (.add fahrenheit-label)
      (.setSize 300 80)
      (.setVisible true))))

(comment

  (def frame (celsius))

  (doto frame
    (.setSize 500 300)))

;; Clojurescript figwheel

