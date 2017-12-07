(ns dots.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.string :as s]
            [clojure.java.io :as io]))

(def delta 1)
(def min-rad 6)
(def max-rad 8)
(def padding 4)
(def cols 26)
(def min-y 70)
(def y-variance 250)
(def height 1000)
(def xy-padding 75)
(def checksum-padding (* delta 30))
(def col-size 22)
(def window-width 703)
(def window-height 994)
(def bg 233)
(def checksum-fg  [166 181 187])
(def checksum-size (* delta 14))

(defn build-cell [x y prev-radius]
  (let [radius (max min-rad (* (rand-int max-rad) 2))]
    [x
     (- y padding
          (/ radius 2)
          (/ prev-radius 2))
     radius]))

(defn build-col [cells threshold]
  (let [last-cell (last cells)]
    (if (< (second last-cell) threshold)
      cells
      (let [cell (apply build-cell last-cell)]
        (recur
          (conj cells cell)
          threshold)))))

(defn build []
  (for [col (range cols)]
    (let [offset (* col col-size)
          threshold (+ min-y
                       (rand y-variance))]
      (build-col [[(+ xy-padding offset)
                   (- height
                      xy-padding
                      (rand y-variance))
                   0]]
                 threshold))))

(defn color-for [rad]
  (condp = rad
    6 [156 212 238]
    8 [134 138 170]
    10 [177 73 65]
    12 [61,102,61]
    14 [166 181 187]
    [150 150 200]))

(defn checksum [data]
  (* 1200
    (reduce
      (fn [acc x]
        (reduce
          (fn [acc y]
            (reduce + acc y)) acc x)) 0 data)))

(defn draw-dots [data]
    (doseq [col data
            cell col]
      (let [[x y rad] (map #(* % delta) cell)]
        (apply q/fill (color-for (last cell)))
        (q/ellipse x y rad rad))))

(defn draw-checksum [cs]
  (let [w (* delta window-width)
        h (* delta window-height)]
    (apply q/fill checksum-fg)
    (q/text-font
      (q/create-font "Simplifica" checksum-size true))
    (q/text-align :center :bottom)
    (q/text
      (s/upper-case (format "%x" cs))
      0 0 w (- h checksum-padding))))

(defn persist [data]
  (let [epoch  (int (/ (System/currentTimeMillis) 1000))
        path (str "./out/" epoch)]
    (spit (str path ".txt")
          (with-out-str (pr data)))
    (q/save (str path ".tif"))))

(defn setup []
  (q/frame-rate 29)

  {:table
    (let [path "./state.txt"
          exists (.exists (io/as-file path))]
      (if exists
        (read-string (slurp path))
        (build)))})

(defn draw-state [state]
  (q/no-loop)
  (q/no-stroke)
  (q/background bg)

  (let [data (:table state)
        cs (int (checksum data))]
    (draw-dots data)
    (draw-checksum cs)
    (persist data)))

(q/defsketch dots
  :title "Dots"
  :size [(* delta window-width) (* delta window-height)]
  :setup setup
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
