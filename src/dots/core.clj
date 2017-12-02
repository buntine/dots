(ns dots.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn build [state]
  [[[100, 656, 8], [100, 646, 4], [100, 634, 12]]
   [[122, 658, 4], [122, 650, 4], [122, 640, 8]]])

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  {:table (build 1)})

(defn update-state [s] s)

(defn draw-state [state]
  (q/no-loop)
  (q/no-stroke)
  (q/background 255)
  (q/fill 100 100 100)

  (doseq [col (:table state)
          cell col]
    (let [[x y rad] cell]
      (q/ellipse x y rad rad))))

(q/defsketch dots
  :title "Dots"
  :size [820 820]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
