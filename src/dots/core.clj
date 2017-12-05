(ns dots.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn build-cell [x y prev-radius]
  (let [radius (max 6 (* (rand-int 8) 2))]
    [x
     (- y 4
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

(defn build [state]
  (for [col (range (:cols state))]
    (let [offset (* col (:col-size state))
          padding (:padding state)
          threshold (+ (:min-y state)
                       (rand (:y-variance state)))]
      (build-col [[(+ padding offset)
                   (- (:height state)
                      padding
                      (rand (:y-variance state)))
                   0]]
                 threshold))))

(defn color-for [rad]
  (condp = rad
    6 [156 212 238]
    8 [134 138 170]
    10 [177 73 65]
    12 [38 30 17]
    14 [166 181 187]
    [150 150 200]))

(defn setup []
  (q/frame-rate 29)
  {:table
    (build
      {:cols 29
       :min-y 70
       :y-variance 250
       :height 1000
       :padding 100
       :col-size 22})})

(defn draw-state [state]
  (q/no-loop)
  (q/no-stroke)
  (q/background 240)

  (doseq [col (:table state)
          cell col]
    (let [[x y rad] cell]
      (apply q/fill (color-for rad))
      (q/ellipse x y rad rad)))

  (let [epoch  (int (/ (System/currentTimeMillis) 1000))]
    (q/save (str "./out/" epoch ".tif"))))

(q/defsketch dots
  :title "Dots"
  :size [820 950]
  :setup setup
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
