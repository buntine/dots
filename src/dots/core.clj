(ns dots.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  {:color 0
   :cells 30
   :rows 30
   :min-rad 4
   :max-rad 16
   :cell-size 22
   :angle 0})

(defn update-state [state]
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)})

(defn draw-state [state]
  (q/no-loop)
  (q/background 240)
  (q/fill (:color state) 255 255)
      (q/ellipse 100 100 100 100))

(q/defsketch dots
  :title "You spin my circle right round"
  :size [820 820]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
