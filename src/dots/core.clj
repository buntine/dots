(ns dots.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn build-cell [x y prev-radius]
  (let [radius 4]
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
    (let [offset (* col (:col-size state))]
      (build-col [[(+ 100 offset) 698 4]]
                 (:min-y state)))))
  ;[[[100, 658, 4], [100, 648, 8], [100, 634, 12]]
  ; [[122, 658, 4], [122, 650, 4], [122, 640, 8]]])

(defn setup []
  (q/frame-rate 29)
  (q/color-mode :hsb)
  {:table
    (build
      {:cols 29
       :min-y 100
       :col-size 22})})

(defn update-state [s] s)

(defn draw-state [state]
  (q/no-loop)
  (q/no-stroke)
  (q/background 255)
  (q/fill 0 0 50)

  (doseq [col (:table state)
          cell col]
    (let [[x y rad] cell]
      (q/ellipse x y rad rad))))

(q/defsketch dots
  :title "Dots"
  :size [820 800]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
