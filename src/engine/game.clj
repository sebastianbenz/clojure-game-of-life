(ns engine.game)

(defn game [cells] 
  (set (map #(vector (inc (first %1)) (inc (nth %1 1))) cells)))
