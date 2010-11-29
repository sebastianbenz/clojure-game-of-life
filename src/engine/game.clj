(ns engine.game
   (:require [clojure.set :as set]))

(def under-population 1)
(def over-population 4)
(def parent-count 3)

(def box #{[2 1] [2 2] [1 1] [1 2]}) 
(def boat #{[1 1] [2 1] [1 2] [3 2] [2 3]})
(def blinker #{[3 1] [3 2] [3 3]}) 
(def glider #{[2 0] [0 1] [2 1] [1 2] [2 2]}) 
(def gosper-glider-gun #{[1 5] [2 5] [1 6] [2 6]  
                         [11 5] [11 6] [11 7] [12 4] [12 8] [13 3] [13 9] [14 3] [14 9]
                         [15 6] [16 4] [16 8] [17 5] [17 6] [17 7] [18 6]
                         [21 3] [21 4] [21 5] [22 3] [22 4] [22 5] [23 2] [23 6] [25 1] [25 2] [25 6] [25 7] 
                         [35 3] [35 4] [36 3] [36 4]})

;([-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1])
(def neighbour-offsets 
  (let [digits (range -1 2)] 
  (for [x digits y digits 
        :let [value [x y]]
        :when (not (= value [0 0])) ] value)))

(defn all-neighbours
  [cell]
  (for [offset neighbour-offsets] (map + offset cell)))

(defn alive-neighbours
  [cells cell]
  (filter #(contains? cells %) (all-neighbours cell)))

(defn dead-neighbours
  [cells cell]
  (filter #(not( contains? cells %)) (all-neighbours cell)))

(defn regulate  
  [cells]
 (filter #(let [alive-neighbour-count (count (alive-neighbours cells %))]
            (and 
              (> alive-neighbour-count under-population ) 
              (<  alive-neighbour-count over-population) ))cells))

(defn dead-neighbour-cells
  [cells]
  (reduce set/union (for [cell cells]  (dead-neighbours cells cell))))

(defn reproduce
  [cells]
  (filter #(= parent-count (count (alive-neighbours cells %))) (dead-neighbour-cells cells)))

(defn tick [cells] 
  (set/union (set (reproduce cells)) (set (regulate cells))))
