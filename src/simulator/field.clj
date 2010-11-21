(ns simulator.field
  (:import 
    (javax.swing JFrame JPanel Timer)
    (java.awt Color)
    (java.awt.event ActionListener )
    (java.awt Dimension)))

(defn draw-box 
  [g cellsize x y color]
  (let [x (int (* x cellsize))
        y (int (* y cellsize))]
    (.setColor g color)
    (.fillRect g x y cellsize cellsize)
    (.setColor g Color/BLACK)
    (.drawRect g x y cellsize cellsize)))


(defn field-panel 
  [engine cells options]
  (let [columns (options :columns)
        rows (options :rows)
        cellsize (options :cellsize)
        aCells (atom cells)
        panel (proxy [JPanel ActionListener] []
                (paintComponent [g] 
                  (let [state @aCells]
                    (loop [x 0]
                      (if (= x columns)
                        nil
                        (do
                          (loop [y 0]
                            (if (= y rows)
                              nil
                              (do
                                (if (contains? state [x y])
                                  (draw-box g cellsize x y Color/BLACK)
                                  (draw-box g cellsize x y Color/WHITE))
                                (recur (inc y)))) )
            (recur (inc x)))) )))
                (actionPerformed [e]
                  (swap! aCells engine)
                  (.repaint this)))]
    (doto panel
      (.setPreferredSize (Dimension. (* columns cellsize)  (* rows cellsize))))))


(defn field-frame 
  [panel]
  (doto (JFrame. "Game of Life")
    (.setDefaultCloseOperation (JFrame/EXIT_ON_CLOSE))
    (.add panel)
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


