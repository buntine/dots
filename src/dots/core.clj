(ns dots.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn build [state]
  160)

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  {:table (build 1)})

(defn update-state [s] s)

(defn draw-state [state]
  (q/no-loop)
  (q/background 255)
  (q/fill 100 255 255)
    (q/ellipse 100 (:table state) 100 100))

(q/defsketch dots
  :title "Dots"
  :size [820 820]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
