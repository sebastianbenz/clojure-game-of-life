(ns simulator.field
  (:require [clojure.set :as set])
  (:import 
    (javax.swing JFrame JPanel Timer)
    (java.awt Color)
    (java.awt.event ActionListener )
    (java.awt Dimension)))

(defn draw-box 
  [g position x y color]
  (.setColor g color)
  (let [size (position 1)]
    (.fillRect g (position x) (position y) size size)))

(defn draw-cells
  [g position cells color]
  (doall (map #(draw-box g position (nth %1 0) (nth %1 1) color) cells) ))

(defn field-panel 
  [engine seed options]
  (let [columns (options :columns)
        rows (options :rows)
        position  #(* %1 (options :cellsize))
        new-cells (atom seed)
        previous-cells (atom ())
        panel (proxy [JPanel ActionListener] []
                
                (paintComponent [g]
                  (let [removed (set/difference @previous-cells @new-cells)
                        added (set/difference @new-cells @previous-cells)]
                    (draw-cells g position removed Color/WHITE)
                    (draw-cells g position added Color/BLACK)
                
                (actionPerformed [e]
                  (swap! previous-cells (fn [_] (deref new-cells)))
                  (swap! new-cells engine)
                  (.repaint this)))]
    (doto panel
      (.setPreferredSize (Dimension. (position columns)  (position rows))))))

(defn field-frame 
  [panel]
  (doto (JFrame. "Game of Life")
    (.setDefaultCloseOperation (JFrame/EXIT_ON_CLOSE))
    (.add panel)
    (.setBackground Color/WHITE)
    .pack
    .show))

(defn run-game 
  ([engine seed]  
    (run-game engine seed  
      {:columns 50 :rows 50 :speed 500 :cellsize 10}))
  
  ([engine seed options] 
    (let [panel (field-panel engine seed options)
          frame (field-frame panel)
          timer (Timer. (options :speed) panel)]
      (.start timer))))


