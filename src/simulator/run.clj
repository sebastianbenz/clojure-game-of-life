(ns simulator.run)
(use 'simulator.field )
(use 'engine.game )

(def cells #{[0 0][1 1][2 2]})
(def options  {:columns 192 :rows 100 :speed 100 :cellsize 10})

(run-game game cells options)

