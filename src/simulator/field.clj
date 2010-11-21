(ns simulator.field
  (:import 
    (javax.swing JFrame JPanel Timer)
    (java.awt Color)
    (java.awt.event ActionListener )
    (java.awt Dimension)))

(defn draw-box [g x y color]
 (let [x (int (* x 10))
       y (int (* y 10))]
  (.setColor g color)
  (.fillRect g x y 10 10)
  (.setColor g Color/BLACK)
  (.drawRect g x y 10 10)))


(defn field-panel [game-engine cells width height]
  (let [aCells (atom cells)
        panel (proxy [JPanel ActionListener] []
    (paintComponent [g] 
      (let [oldState @aCells
            newState (game-engine oldState)]
        
      (loop [x 0]
        (if (= x 50)
          nil
          (do
            (loop [y 0]
              (if (= y 50)
                nil
                (do
                   (if (contains? @aCells [x y])
                     (draw-box g x y Color/BLACK)
                     (draw-box g x y Color/WHITE))
                  (recur (inc y)))) )
            (recur (inc x)))) )))
    (actionPerformed [e]
      (swap! aCells game-engine)
      (.repaint this)))]
    (doto panel
      (.setPreferredSize (Dimension. width height)))))


(defn field-frame [panel]
  (doto (JFrame. "Game of Life")
;    (.setDefaultCloseOperation (JFrame/EXIT_ON_CLOSE))
    (.add panel)
    .pack
    .show))

(defn run-game [game-engine cells]
  (let [width 500 
        height 500
        panel (field-panel game-engine cells width height)
        frame (field-frame panel)
        timer (Timer. 500 panel)]
  (.start timer)))


