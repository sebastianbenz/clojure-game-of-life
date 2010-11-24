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
    (.setColor g Color/GRAY)
    (doall (map #(.drawLine g  (position %1) 0 (position %1) (position rows))(range 0 columns)))
    (doall (map #(.drawLine g  0 (position %1) (position columns) (position %1))(range 0 rows)))))

(defn draw-box 
  [g position x y color]
  (.setColor g color)
  (let [size (position 1)]
    (.fillRect g (position x) (position y) size size)))

(defn x
  [cell]
  (nth cell 0))

(defn y
  [cell]
  (nth cell 1))

(defn draw-cells
  [g position cells color]
  (doall (map #(draw-box g position (x %1 ) (y %1) color) cells) ))

(defn visible?
  [cell rows columns]
  (and (<= columns (x cell)) (<= rows (y cell))))

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
  ([engine seed]  
    (run-game engine seed  
      {:columns 50 :rows 50 :speed 500 :cellsize 10}))
  ([engine seed options] 
    (let [panel (field-panel engine seed options)
          frame (field-frame panel)
          timer (Timer. (options :speed) panel)]
      (.start timer))))


