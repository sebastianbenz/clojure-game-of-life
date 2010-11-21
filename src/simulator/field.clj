(ns simulator.field
  (:require [clojure.set :as set])
  (:import 
    (javax.swing JFrame JPanel Timer)
    (java.awt Color)
    (java.awt.event ActionListener )
    (java.awt Dimension)))




(defn draw-grid 
  [g columns rows cellsize]
  (do
    (doall (map #(.drawLine g  (* %1 cellsize) 0 (* %1 cellsize) (* rows cellsize) )(range 0 columns)))
    (doall (map #(.drawLine g  0 (* %1 cellsize) (* columns cellsize) (* %1 cellsize) )(range 0 rows)))))

(defn draw-box 
  [g cellsize x y color]
  (let [x (int (* x cellsize))
        y (int (* y cellsize))]
    (.setColor g color)
    (.fillRect g x y cellsize cellsize)))

(defn draw-cells
  [g cellsize cells color]
  (doall (map #(draw-box g cellsize (nth %1 0) (nth %1 1) color) cells) ))

(defn field-panel 
  [engine cells options]
  (let [columns (options :columns)
        rows (options :rows)
        cellsize (options :cellsize)
        new-cells (atom cells)
        previous-cells (atom ())
        panel (proxy [JPanel ActionListener] []
                
                (paintComponent [g]
                  (let [removed (set/difference @previous-cells @new-cells)
                        added (set/difference @new-cells @previous-cells)]
                        
                  (draw-cells g cellsize removed Color/WHITE)
                  (draw-cells g cellsize added Color/BLACK)
                  (draw-grid g columns rows cellsize)))
                
                (actionPerformed [e]
                  (swap! previous-cells (fn [old] (deref new-cells)))
                  (swap! new-cells engine)
                  (.repaint this)))]
    (doto panel
      (.setPreferredSize (Dimension. (* columns cellsize)  (* rows cellsize))))))



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


