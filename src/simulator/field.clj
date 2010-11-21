(ns simulator.field
  (:require [clojure.set :as set])
  (:import 
    (javax.swing JFrame JPanel Timer)
    (java.awt Color)
    (java.awt.event ActionListener )
    (java.awt Dimension)))

(defn draw-grid 
  [g columns rows position]
  (do
    (doall (map #(.drawLine g  (position %1) 0 (position %1) (position rows))(range 0 columns)))
    (doall (map #(.drawLine g  0 (position %1) (position columns) (position %1))(range 0 rows)))))

(defn draw-box 
  [g position x y color]
  (.setColor g color)
  (let [size (position 1)]
    (.fillRect g (position x) (position y) size size)))

(defn draw-cells
  [g position cells color]
  (doall (map #(draw-box g position (nth %1 0) (nth %1 1) color) cells) ))

(defn field-panel 
  [engine cells options]
  (let [columns (options :columns)
        rows (options :rows)
        position  #(* %1 (options :cellsize))
        new-cells (atom cells)
        previous-cells (atom ())
        panel (proxy [JPanel ActionListener] []
                
                (paintComponent [g]
                  (let [removed (set/difference @previous-cells @new-cells)
                        added (set/difference @new-cells @previous-cells)]
                    (draw-cells g position removed Color/WHITE)
                    (draw-cells g position added Color/BLACK)
                    (draw-grid g columns rows position)))
                
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
  ([engine cells]  
    (run-game engine cells  
      {:columns 50 :rows 50 :speed 500 :cellsize 10}))
  
  ([engine cells options] 
    (let [panel (field-panel engine cells options)
          frame (field-frame panel)
          timer (Timer. (options :speed) panel)]
      (.start timer))))


