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
          padding (:padding state)]
      (build-col [[(+ padding offset) (- (:height state) padding) 0]]
                 (:min-y state)))))

(defn setup []
  (q/frame-rate 29)
  (q/color-mode :hsb)
  {:table
    (build
      {:cols 29
       :min-y 100
       :height 800
       :padding 100
       :col-size 22})})

(defn draw-state [state]
  (q/no-loop)
  (q/no-stroke)
  (q/background 240)

  (doseq [col (:table state)
          cell col]
    (let [[x y rad] cell]
      (q/fill 137 118 (- 255 (* rad 9)))
      (q/ellipse x y rad rad))))

(q/defsketch dots
  :title "Dots"
  :size [820 800]
  :setup setup
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
