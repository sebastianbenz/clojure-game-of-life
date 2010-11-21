(ns engine.game)

(defn game [cells] 
  (set (map #(vector (inc (first %1)) (inc (nth %1 1))) cells)))

;([-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1])"
(def offsets 
  (let [digits (range -1 2)] 
  (for [x digits y digits 
        :let [value [x y]]
        :when (not (= value [0 0])) ] value)))

(defn neighbours 
  [cells cell]
 (reduce + (doall (map #(if (contains? cells (map + cell %1)) 1 0) offsets))))

(defn under-population  
  [cells]
 (filter #(let [neighbour-count (neighbours cells %)]
            (and (< 1 neighbour-count) (> 4 neighbour-count) ))
            cells)) 

