(ns engine.test.game
  (:use [lazytest.describe :only (describe it)])
  (:use engine.game)
  )

(def fixture [1 1])
(def cell-with-no-neighbour #{fixture})
(def cell-with-one-neighbour #{fixture [0 1]})
(def cell-with-two-neighbours #{fixture [0 1] [1 2]})
(def cell-with-three-neighbours #{fixture [0 1] [1 2] [1 0]})
(def cell-with-four-neighbours #{fixture [0 1] [1 2] [1 0] [0 0]})

(defn keeps
  [cell field]
   (contains? (set field) cell))

(defn removes
  [cell field]
  (not (keeps cell field)))

(println "result" (under-population cell-with-four-neighbours))
;Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

(println "result: " (under-population cell-with-one-neighbour))

(describe "Any live cell with fewer than two live neighbours dies, as if caused by under-population."
  (it "removes cells with no neighbour"
    (removes fixture (under-population cell-with-no-neighbour)))
  (it "removes cells with one neighbour"
    (removes fixture (under-population cell-with-one-neighbour)))
  (it "keeps cells with two neighbours"
    (keeps fixture (under-population cell-with-two-neighbours)))
  (it "keeps cells with three neighbours"
    (keeps fixture (under-population cell-with-three-neighbours)))
  (it "removes cells with four neighbours"
    (removes fixture (under-population cell-with-four-neighbours))))

(def fixture2 [1 1])

(def cell-with-left-neighbour #{fixture2 [0 1]})
(def cell-with-right-neighbour #{fixture2 [2 1]})
(def cell-with-upper-neighbour #{fixture2 [1 0]})
(def cell-with-lower-neighbour #{fixture2 [1 2]})
(def cell-with-upper-left-neighbour #{fixture2 [0 0]})
(def cell-with-upper-right-neighbour #{fixture2 [2 0]})
(def cell-with-lower-left-neighbour #{fixture2 [0 2]})
(def cell-with-lower-right-neighbour #{fixture2 [2 2]})

(describe "Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent."
  (it "has left neighbours"
    (= 1 (neighbours cell-with-left-neighbour fixture2)))
  (it "has right neighbours"
    (= 1 (neighbours cell-with-right-neighbour fixture2)))
  (it "has upper neighbours"
    (= 1 (neighbours cell-with-upper-neighbour fixture2)))
  (it "has lower neighbours"
    (= 1 (neighbours cell-with-lower-neighbour fixture2)))
  (it "has upper-left neighbours"
    (= 1 (neighbours cell-with-upper-left-neighbour fixture2)))
  (it "has upper-right neighbours"
    (= 1 (neighbours cell-with-upper-right-neighbour fixture2)))
  (it "has lower-left neighbours"
    (= 1 (neighbours cell-with-lower-left-neighbour fixture2)))
  (it "has lower-right neighbours"
    (= 1 (neighbours cell-with-lower-right-neighbour fixture2)))
  (it "has two neighbours"
    (= 2 (neighbours cell-with-two-neighbours fixture)))
  (it "has three neighbours"
    (= 3 (neighbours cell-with-three-neighbours fixture)))
   (it "has four neighbours"
    (= 4 (neighbours cell-with-four-neighbours fixture))))



